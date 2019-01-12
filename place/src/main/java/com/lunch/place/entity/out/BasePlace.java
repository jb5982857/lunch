package com.lunch.place.entity.out;

/**
 * 添加地点的实体类
 */
public class BasePlace {

    private String place;

    private String username;

    //检查是否参数有空
    public boolean isEmpty(String... excepts) {
        return username == null || place == null;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
