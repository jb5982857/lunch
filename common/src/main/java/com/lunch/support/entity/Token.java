package com.lunch.support.entity;

import com.alibaba.fastjson.JSON;

public class Token extends BaseOAuth {
    private static final int TIME_OUT = 10 * 60 * 1000;
    private String signature;

    public Token() {

    }

    public Token(String signature, long timestamp) {
        super(timestamp);
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.signature = signature;
    }

    public Token(String signature) {
        this.signature = signature;
    }

    @Override
    public String getResult() {
        return signature;
    }

    public void setResult(String signature) {
        this.signature = signature;
    }

    @Override
    protected int registerTime() {
        return TIME_OUT;
    }
}