package com.fs.web.exception;

public class HttpClientRequestException extends RuntimeException {

    public HttpClientRequestException(String massage){
        super(massage);
    }
}
