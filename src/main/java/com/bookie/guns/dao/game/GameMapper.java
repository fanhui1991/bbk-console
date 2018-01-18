package com.bookie.guns.dao.game;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by FH on 2018/1/18.
 */
public interface GameMapper {

    List<Map<String, Object>> gameFh(Map<String, Object> map);
}
