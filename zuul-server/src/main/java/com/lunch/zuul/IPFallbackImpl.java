package com.lunch.zuul;

import com.lunch.support.tool.LogNewUtils;
import org.springframework.stereotype.Component;

@Component
public class IPFallbackImpl implements IPService {
    @Override
    public boolean check(String ip) {
        LogNewUtils.error("IPFallbackImpl");
        return false;
    }
}
