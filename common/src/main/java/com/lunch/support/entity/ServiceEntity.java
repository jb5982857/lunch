package com.lunch.support.entity;

import com.lunch.support.constants.Code;

/**
 * 用户service返回controller的实体类，code如果不等于success，那就直接返回code和desc给用户
 *
 * @param <T>
 */

public class ServiceEntity<T> {
    public int code = Code.SUCCESS;

    public String desc;

    public T data;

    public ServiceEntity() {
    }

    public ServiceEntity(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ServiceEntity(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSuccess() {
        return code == Code.SUCCESS;
    }

    public static <T> ServiceEntity obtain(T t) {
        ServiceEntity<T> entity = new ServiceEntity<>();
        entity.setData(t);
        return entity;
    }
}
