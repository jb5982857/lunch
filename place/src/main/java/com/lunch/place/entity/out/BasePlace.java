package com.lunch.place.entity.out;

/**
 * 添加地点的实体类
 */
public class BasePlace {

    private String place;

    private String session;

    //检查是否参数有空
    public boolean isEmpty(String... excepts) {
        return session == null || place == null;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String username) {
        this.session = username;
    }


}
