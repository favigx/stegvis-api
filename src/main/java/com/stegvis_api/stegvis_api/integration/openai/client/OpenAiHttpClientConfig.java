package com.stegvis_api.stegvis_api.integration.openai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;

@Configuration
public class OpenAiHttpClientConfig {

        @Value("${stegvis.openai.apikey}")
        private String openAiApiKey;

        @Bean
        public WebClient openAiWebClient() {
                TcpClient tcpClient = TcpClient.create()
                                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)

                                .doOnConnected(conn -> conn
                                                .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(30))
                                                .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(30)));
                HttpClient httpClient = HttpClient.from(tcpClient)
                                .responseTimeout(Duration.ofSeconds(20));

                return WebClient.builder()
                                .baseUrl("https://api.openai.com/v1")
                                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                                .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .build();
        }
}