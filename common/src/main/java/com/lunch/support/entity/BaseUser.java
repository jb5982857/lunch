package com.lunch.support.entity;

public class BaseUser {

    private long id;

    private String username;

    private String password;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //检查是否参数有空
    public boolean isEmpty(String... excepts) {
        return username == null || password == null;
    }

    @Override
    public String toString() {
        return id + "|" + username + "|" + password;
    }
}
