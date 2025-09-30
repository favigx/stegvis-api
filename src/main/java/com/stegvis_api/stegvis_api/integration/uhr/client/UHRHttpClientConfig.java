package com.stegvis_api.stegvis_api.integration.uhr.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UHRHttpClientConfig {

    @Value("${stegvis.uhr.get.url}")
    private String uhrUrl;

    @Bean
    public UHRHttpClient uhrHttpClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(uhrUrl)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(UHRHttpClient.class);
    }
}