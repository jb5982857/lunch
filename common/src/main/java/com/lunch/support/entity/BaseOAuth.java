package com.lunch.support.entity;

import com.alibaba.fastjson.JSON;

public abstract class BaseOAuth {
    private long time;

    public BaseOAuth() {
        this.time = System.currentTimeMillis();
    }

    public BaseOAuth(long time) {
        this.time = time;
    }

    protected abstract int registerTime();

    protected abstract String getResult();

    public boolean compare(String str) {
        return getResult().equals(str) && !timeOut();
    }

    protected boolean timeOut() {
        return System.currentTimeMillis() - time > registerTime();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
