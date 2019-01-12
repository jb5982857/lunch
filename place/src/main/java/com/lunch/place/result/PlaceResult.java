package com.lunch.place.result;

import com.lunch.place.entity.in.Place;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;

import java.util.List;

/**
 * 返回给客户端的地点集合
 */
public class PlaceResult extends BaseResult {

    private List<Place> places;

    public PlaceResult(int code, String desc, List<Place> places) {
        super(code, desc);
        this.places = places;
    }

    public PlaceResult(List<Place> places) {
        this(Code.SUCCESS, S.SUCCESS, places);
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
