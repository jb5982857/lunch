package com.lunch.place.controller;

import com.lunch.place.entity.in.Place;
import com.lunch.place.entity.out.BasePlace;
import com.lunch.place.entity.out.PlaceState;
import com.lunch.place.result.PlaceResult;
import com.lunch.place.service.PlaceService;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController extends BaseController {

    @Autowired
    private PlaceService placeService;

    //添加地点
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult addPlace(BasePlace place) {
        boolean result = placeService.addPlace(place);
        return new BaseResult(result ? Code.SUCCESS : Code.ADD_PLACE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //地点转换为喜欢
    @RequestMapping(value = "/toLike", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult place2Like(PlaceState place) {
        boolean result = placeService.like(place);
        return new BaseResult(result ? Code.SUCCESS : Code.CHANGE_PLACE_STATE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //地点转换为讨厌
    @RequestMapping(value = "/toHate", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult place2Hate(PlaceState place) {
        boolean result = placeService.hate(place);
        return new BaseResult(result ? Code.SUCCESS : Code.CHANGE_PLACE_STATE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //返回所有的地点
    @GetMapping("/all")
    public BaseResult getPlaces(String username) {
        List<Place> places = placeService.getAllList(username);
        if (places == null || places.size() == 0) {
            LogUtils.info("can not find place by username " + username);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return new PlaceResult(places);
    }

    //返回喜欢的地点
    @GetMapping("/like")
    public BaseResult getLikePlace(String username) {
        List<Place> places = placeService.getLikePlace(username);
        if (places == null || places.size() == 0) {
            LogUtils.info("can not find place by username " + username);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return new PlaceResult(places);
    }

    //返回讨厌的地点
    @GetMapping("/hate")
    public BaseResult getHatePlace(String username) {
        List<Place> places = placeService.getHatePlace(username);
        if (places == null || places.size() == 0) {
            LogUtils.info("can not find place by username " + username);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return new PlaceResult(places);
    }

    //查询被添加的地方，即还没有被列为喜欢或者讨厌的地方
    @RequestMapping(value = "/added", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult getAddedPlace(String username) {
        List<Place> places = placeService.getAddPlace(username);
        if (places == null || places.size() == 0) {
            LogUtils.info("can not find place by username " + username);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return new PlaceResult(places);
    }
}