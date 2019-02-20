package com.lunch.ad.config;

/**
 * 展示配置，几点对应着db里面的id，把该点对应的id全部拉出来
 */
public class AdConfig {
    public static int getShowId() {
        return (int) (System.currentTimeMillis() / 1000 / 60 / 60 % 8);
    }
}
