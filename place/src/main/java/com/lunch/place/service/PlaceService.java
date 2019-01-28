package com.lunch.place.service;

import com.lunch.place.dao.PlaceDao;
import com.lunch.place.entity.in.Place;
import com.lunch.place.entity.out.BasePlace;
import com.lunch.place.entity.out.PlaceState;
import com.lunch.place.feignService.IAccountService;
import com.lunch.support.constants.Code;
import com.lunch.support.result.VerifyResult;
import com.lunch.support.service.BaseService;
import com.lunch.support.tool.LogNewUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlaceService extends BaseService {
    @Autowired
    private PlaceDao placeDao;

    //添加地点到db
    public boolean addPlace(BasePlace place, long id) {
        if (checkPlace(id, place.getPlace()) > 0) {
            //该用户已拥有该地点
            LogNewUtils.warn("user " + place.getSession() + " already has place " + place.getPlace());
            return false;
        }

        return placeDao.addPlace(new Place(id, place.getPlace(), 0, new Date())) > 0;
    }

    @SuppressWarnings("all")
    public boolean like(PlaceState place, long accountId) {
        long placeId = checkPlace(accountId, place.getPlace());
        if (placeId <= 0) {
            //该用户没有该地点
            LogNewUtils.error("user " + place.getSession() + " did not have place " + place.getPlace());
            return false;
        }

        placeDao.likePlace(placeId, place.getDesc(), new Date());
        return true;
    }

    //拉黑某个地方
    public boolean hate(PlaceState place, long accountId) {
        long placeId = checkPlace(accountId, place.getPlace());
        if (placeId <= 0) {
            //该用户没有该地点
            LogNewUtils.error("user " + place.getSession() + " did not have place " + place.getPlace());
            return false;
        }

        placeDao.hatePlace(placeId, place.getDesc(), new Date());
        return true;
    }

    //获取所有的地点
    public List<Place> getAllList(long userId) {
        return placeDao.queryAllPlaceByAccountId(userId, null);
    }

    //查询喜欢的地点
    public List<Place> getLikePlace(long userId) {
        return getPlaceByState(userId, 1);
    }

    //查找讨厌的地方
    public List<Place> getHatePlace(long userId) {
        return getPlaceByState(userId, 2);
    }

    //查找添加过的地方,即还没有被列入喜欢或者讨厌的地方
    public List<Place> getAddPlace(long userId) {
        return getPlaceByState(userId, 0);
    }

    //用account_id去检查db中是否已经有了这个地点
    private long checkPlace(long accountId, String place) {
        Long result = placeDao.queryIdByAccountIdAndPlace(accountId, place);
        return result == null ? -1 : result;
    }

    //通过状态值去查找地点
    private List<Place> getPlaceByState(long userId, int state) {
        return placeDao.queryAllPlaceByAccountId(userId, state);
    }
}
