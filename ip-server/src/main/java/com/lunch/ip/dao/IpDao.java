package com.lunch.ip.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface IpDao {
    int getStateByIp(@Param("ip") String ip);
}
