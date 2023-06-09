package com.lettrip.lettripbackend.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;

    private Object data;
    public ApiResponse(boolean success,String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success,String message,Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
