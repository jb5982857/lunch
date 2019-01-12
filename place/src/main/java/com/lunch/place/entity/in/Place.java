package com.lunch.place.entity.in;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 地点
 */
public class Place {
    //唯一标示
    private long id;
    //对应account表中的id
    private long accountId;
    //地点名称
    private String placeName;
    //状态 0 去过 1 喜欢 2 拉黑
    private int state;
    //更新状态的时间
    private Date time;
    //喜欢或者拉黑的原因
    private String desc;

    public Place(Integer id, Long accountId, String placeName, Integer state, Timestamp time, String desc) {
        this.id = id;
        this.accountId = accountId;
        this.placeName = placeName;
        this.state = state;
        this.time = time;
        this.desc = desc;
    }

    public Place(long accountId, String placeName, int state, Date time) {
        this.accountId = accountId;
        this.placeName = placeName;
        this.state = state;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
