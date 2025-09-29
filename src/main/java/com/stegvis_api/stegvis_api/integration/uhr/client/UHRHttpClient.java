package com.stegvis_api.stegvis_api.integration.uhr.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;

public interface UHRHttpClient {

    @GetExchange("/searchTotal")
    ProgramResponse searchTotal(
            @RequestParam("searchfor") String searchFor,
            @RequestParam("searchterm") String searchTerm,
            @RequestParam(value = "searchkategori", required = false) String searchCategory,
            @RequestParam(value = "pagesize", required = false) Integer pageSize,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam("tillfalle") String tillfalle);
}