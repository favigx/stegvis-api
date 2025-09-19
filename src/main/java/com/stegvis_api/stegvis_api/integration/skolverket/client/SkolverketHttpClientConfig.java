package com.stegvis_api.stegvis_api.integration.skolverket.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class SkolverketHttpClientConfig {

    @Bean
    public SkolverketHttpClient skolverketHttpClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.skolverket.se/syllabus/v1")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(SkolverketHttpClient.class);
    }
}
