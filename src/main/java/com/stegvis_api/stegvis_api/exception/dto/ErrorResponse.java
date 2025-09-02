package com.stegvis_api.stegvis_api.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
}
