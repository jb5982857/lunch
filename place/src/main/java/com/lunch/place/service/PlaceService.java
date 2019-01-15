package com.lunch.place.service;

import com.lunch.place.dao.PlaceDao;
import com.lunch.place.entity.in.Place;
import com.lunch.place.entity.out.BasePlace;
import com.lunch.place.entity.out.PlaceState;
import com.lunch.place.feignService.IAccountService;
import com.lunch.redis.support.StringRedisService;
import com.lunch.support.entity.AccessUser;
import com.lunch.support.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlaceService extends BaseService {
    @Autowired
    private StringRedisService<AccessUser> userRedisService;
    @Autowired
    private PlaceDao placeDao;
    @SuppressWarnings("all")
    @Autowired
    private IAccountService accountService;

    //添加地点到db
    public boolean addPlace(BasePlace place) {

        long id = checkUsername(place.getUsername());
        if (id == -1) {
            logger.info("could not find in db or redis " + place.getUsername());
            return false;
        }

        if (checkPlace(id, place.getPlace()) > 0) {
            //该用户已拥有该地点
            logger.info("user " + place.getUsername() + " already has place " + place.getPlace());
            return false;
        }

        return placeDao.addPlace(new Place(id, place.getPlace(), 0, new Date())) > 0;
    }

    @SuppressWarnings("all")
    public boolean like(PlaceState place) {
        long accountId = checkUsername(place.getUsername());
        if (accountId == -1) {
            logger.info("could not find in db or redis " + place.getUsername());
            return false;
        }

        long placeId = checkPlace(accountId, place.getPlace());
        if (placeId <= 0) {
            //该用户没有该地点
            logger.info("user " + place.getUsername() + " did not have place " + place.getPlace());
            return false;
        }

        placeDao.likePlace(placeId, place.getDesc(), new Date());
        return true;
    }

    //拉黑某个地方
    public boolean hate(PlaceState place) {
        long accountId = checkUsername(place.getUsername());
        if (accountId == -1) {
            logger.info("could not find in db or redis " + place.getUsername());
            return false;
        }

        long placeId = checkPlace(accountId, place.getPlace());
        if (placeId <= 0) {
            //该用户没有该地点
            logger.info("user " + place.getUsername() + " did not have place " + place.getPlace());
            return false;
        }

        placeDao.hatePlace(placeId, place.getDesc(), new Date());
        return true;
    }

    /**
     * 检查username的有效性，先redis后db，并返回对应db的id,如果没有则返回-1
     *
     * @param username 用户名
     * @return db对应的id
     */
    private long checkUsername(String username) {
        return accountService.checkUsername(username);
    }

    //用account_id去检查db中是否已经有了这个地点
    private long checkPlace(long accountId, String place) {
        Long result = placeDao.queryIdByAccountIdAndPlace(accountId, place);
        return result == null ? -1 : result;
    }

    //获取所有的地点
    public List<Place> getAllList(String username) {
        long userId = checkUsername(username);
        if (userId == -1) {
            logger.info("could not find in db or redis " + username);
            return null;
        }

        return placeDao.queryAllPlaceByAccountId(userId, null);
    }

    //查询喜欢的地点
    public List<Place> getLikePlace(String username) {
        return getPlaceByState(username, 1);
    }

    //查找讨厌的地方
    public List<Place> getHatePlace(String username) {
        return getPlaceByState(username, 2);
    }

    //查找添加过的地方,即还没有被列入喜欢或者讨厌的地方
    public List<Place> getAddPlace(String username) {
        return getPlaceByState(username, 0);
    }

    //通过状态值去查找地点
    private List<Place> getPlaceByState(String username, int state) {
        long userId = checkUsername(username);
        if (userId == -1) {
            logger.info("could not find in db or redis " + username);
            return null;
        }

        return placeDao.queryAllPlaceByAccountId(userId, state);
    }
}
