package com.lunch.ad.entity;

/**
 * 闪屏数据
 */
public class AdEntity {
    //对应时间点
    private int id;
    //公司
    private String company;
    //图片url
    private String url;
    //显示时长，如果没有这个字段就一直显示
    private int during;
    //附带文字，长度100
    private String title;
    //该广告是否可以关闭，0可关闭，1不可关闭
    private Integer close;
    //点击之后的行为
    private String action;

    public AdEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuring() {
        return during;
    }

    public void setDuring(int during) {
        this.during = during;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
