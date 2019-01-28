package com.lunch.zuul;

import org.springframework.stereotype.Component;

@Component
public class IPFallbackImpl implements IPService{
    @Override
    public boolean check(String ip) {
        return false;
    }
}
