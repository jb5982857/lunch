package com.lunch.ip.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface IpDao {
    Integer getStateByIp(@Param("ip") String ip);
}