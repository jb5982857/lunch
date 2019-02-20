package com.lunch.place.controller;

import com.lunch.place.config.GlobalExceptionHandler;
import com.lunch.place.entity.in.Place;
import com.lunch.place.entity.out.BasePlace;
import com.lunch.place.entity.out.PlaceState;
import com.lunch.place.service.PlaceService;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.LogNewUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController extends BaseController {

    @Autowired
    private PlaceService placeService;

    //添加地点
    //ApiIgnore swagger隐藏参数
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult addPlace(BasePlace place, @ApiIgnore @RequestAttribute("userId") long userId) {
        boolean result = placeService.addPlace(place, userId);
        return new BaseResult(result ? Code.SUCCESS : Code.ADD_PLACE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //地点转换为喜欢
    @ApiModelProperty(value = "userId", hidden = true)
    @RequestMapping(value = "/toLike", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult place2Like(PlaceState place, @ApiIgnore @RequestAttribute("userId") long userId) {
        boolean result = placeService.like(place, userId);
        return new BaseResult(result ? Code.SUCCESS : Code.CHANGE_PLACE_STATE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //地点转换为讨厌
    @ApiModelProperty(value = "userId", hidden = true)
    @RequestMapping(value = "/toHate", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult place2Hate(PlaceState place, @ApiIgnore @RequestAttribute("userId") long userId) {
        boolean result = placeService.hate(place, userId);
        return new BaseResult(result ? Code.SUCCESS : Code.CHANGE_PLACE_STATE_FAILED, result ? S.SUCCESS : S.ERROR_PLACE);
    }

    //返回所有的地点
    @GetMapping("/all")
    @ApiModelProperty(value = "userId", hidden = true)
    public BaseResult getPlaces(String session, @ApiIgnore @RequestAttribute("userId") long userId) {
        List<Place> places = placeService.getAllList(userId);
        if (places == null || places.size() == 0) {
            LogNewUtils.info("can not find place by session " + session);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return BaseResult.obtain(places);
    }

    //返回喜欢的地点
    @ApiModelProperty(value = "userId", hidden = true)
    @GetMapping("/like")
    public BaseResult getLikePlace(String session, @ApiIgnore @RequestAttribute("userId") long userId) {
        List<Place> places = placeService.getLikePlace(userId);
        if (places == null || places.size() == 0) {
            LogNewUtils.info("can not find place by session " + session);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return BaseResult.obtain(places);
    }

    //返回讨厌的地点
    @ApiModelProperty(value = "userId", hidden = true)
    @GetMapping("/hate")
    public BaseResult getHatePlace(String session, @ApiIgnore @RequestAttribute("userId") long userId) {
        List<Place> places = placeService.getHatePlace(userId);
        if (places == null || places.size() == 0) {
            LogNewUtils.info("can not find place by session " + session);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return BaseResult.obtain(places);
    }

    //查询被添加的地方，即还没有被列为喜欢或者讨厌的地方
    @ApiModelProperty(value = "userId", hidden = true)
    @RequestMapping(value = "/added", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult getAddedPlace(String session, @ApiIgnore @RequestAttribute("userId") long userId) {
        List<Place> places = placeService.getAddPlace(userId);
        if (places == null || places.size() == 0) {
            LogNewUtils.info("can not find place by session " + session);
            return new BaseResult(Code.QUERY_PLACE_NULL, S.QUERY_PLACE_EMPTY);
        }
        return BaseResult.obtain(places);
    }

    @GetMapping("/session_out")
    public BaseResult sessionOut() {
        return GlobalExceptionHandler.SESSION_OUT;
    }
}