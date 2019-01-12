package com.lunch.place.dao;


import com.lunch.place.entity.in.Place;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 保存地点的db类
 */
@Component
public interface PlaceDao {

    //根据account_id和地点去db里面找id
    Long queryIdByAccountIdAndPlace(@Param("accountId") long accountId, @Param("place") String place);

    //为某个用户添加一个地点
    long addPlace(Place place);

    //改变一个地方的状态为喜欢
    void likePlace(@Param("id") long id, @Param("desc") String desc, @Param("time") Date time);

    //更改一个地方的状态为拉黑
    void hatePlace(@Param("id") long id, @Param("desc") String desc, @Param("time") Date time);

    //通过account_id和状态来查找对应的地点,如果state 不传则是空
    List<Place> queryAllPlaceByAccountId(@Param("id") long id, @Param("state") Integer state);
}
