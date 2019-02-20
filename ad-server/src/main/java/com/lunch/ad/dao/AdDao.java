package com.lunch.ad.dao;

import com.lunch.ad.entity.AdEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdDao {

    List<AdEntity> queryById(@Param("id") int id);
}
