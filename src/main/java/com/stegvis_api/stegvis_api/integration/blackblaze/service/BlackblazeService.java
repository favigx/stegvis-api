package com.stegvis_api.stegvis_api.integration.blackblaze.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class BlackblazeService {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public BlackblazeService(
            @Value("${stegvis.b2.s3.endpoint}") String endpoint,
            @Value("${stegvis.b2.s3.access-key}") String accessKey,
            @Value("${stegvis.b2.s3.secret-key}") String secretKey,
            @Value("${stegvis.b2.s3.bucket-name}") String bucketName) {

        this.bucketName = bucketName;
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(endpoint, ""))
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)))
                .build();

    }

    public String uploadFile(String fileName, Resource file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.contentLength());

        s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public String getFileUrl(String fileName, long durationSec) {
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(durationSec));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}