package com.lunch.ad.service;

import com.lunch.ad.config.AdConfig;
import com.lunch.ad.dao.AdDao;
import com.lunch.ad.entity.AdEntity;
import com.lunch.ad.feignService.IAccountService;
import com.lunch.support.constants.Code;
import com.lunch.support.result.BaseResult;
import com.lunch.support.result.VerifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService {
    //session过期
    public static final int SESSION_OUT = -1;
    //用户系统挂了
    public static final int ACCOUNT_DOWN = -2;
    @Autowired
    private AdDao adDao;

    @Autowired
    private IAccountService accountService;

    public List<AdEntity> getSplash() {
        return adDao.queryById(AdConfig.getShowId());
    }
}
