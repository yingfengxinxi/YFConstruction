package com.kakarote.build.home.service;

import com.kakarote.core.entity.BasePage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName VideoMonitorDataService
 * @Author wnj58
 * @Date 2021/9/24 18:06
 * @purpose
 * @Version 1.0
 **/
public interface VideoMonitorDataService {
    List<Map<String, Object>> videoEquiByPro(Map<String,Object> map);

    List<Map<String, Object>> videoEquiByProByCheckHox(Map<String,Object> map);

    List<Map<String, Object>> videoEquiByLocation(Map<String,Object> map);

    List<Map<String, Object>> videoStreamList(Map<String,Object> map);

    Map<String, Object> selectVideoNumber();

    List<Map<String, Object>> selectVideoList(Map<String, Object> params);

    BasePage<Map<String,Object>> selectVideoOnlineList(Map<String,Object> param);

    Map<String, Object> selectVideoNumberByPro();

    Map selectVideoById(Integer id);

    boolean videoOnline(Map<String,Object> map);
}
