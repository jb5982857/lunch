package com.lunch.ad.controller;

import com.lunch.ad.entity.AdEntity;
import com.lunch.ad.result.SplashResult;
import com.lunch.ad.service.AdService;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.LogNewUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AdController extends BaseController {

    @Autowired
    private AdService adService;

    @GetMapping("/hello")
    public String hello() {
        return "ad hello!";
    }

    @GetMapping("/splash")
    public BaseResult getSplash() {
        LogNewUtils.info("splash");
        List<AdEntity> entities = adService.getSplash();
        if (entities == null || entities.size() == 0) {
            return OK;
        }

        List<SplashResult> results = new ArrayList<>();
        for (AdEntity entity : entities) {
            results.add(new SplashResult(entity.getId(), entity.getCompany(), entity.getUrl(), entity.getDuring(), entity.getTitle(), entity.getAction()));
        }

        return BaseResult.obtain(results);
    }
}
