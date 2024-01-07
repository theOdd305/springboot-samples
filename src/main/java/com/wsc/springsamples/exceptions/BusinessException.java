package com.wsc.springsamples.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}
