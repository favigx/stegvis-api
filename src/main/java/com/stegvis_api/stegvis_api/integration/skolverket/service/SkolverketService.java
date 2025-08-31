package com.stegvis_api.stegvis_api.integration.skolverket.service;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.integration.skolverket.client.SkolverketHttpClient;

@Service
public class SkolverketService {

    private final SkolverketHttpClient skolverketHttpClient;

    public SkolverketService(SkolverketHttpClient skolverketHttpClient) {
        this.skolverketHttpClient = skolverketHttpClient;
    }

}
