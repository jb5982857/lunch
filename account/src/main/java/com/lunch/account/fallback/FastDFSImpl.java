package com.lunch.account.fallback;

import com.lunch.account.feignService.IFastDFSService;
import com.lunch.support.tool.LogNewUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FastDFSImpl implements IFastDFSService {
    @Override
    public String upload(MultipartFile file) {
        return null;
    }

    @Override
    public String hello() {
        return "fastdfs down";
    }

    @Override
    public void delete(String url) {
        LogNewUtils.warn("fastdfs delete request error!");
    }
}
