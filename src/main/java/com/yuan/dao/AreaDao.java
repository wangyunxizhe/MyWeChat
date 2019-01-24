package com.yuan.dao;

import com.yuan.entity.Area;

import java.util.List;

/**
 * Created by wangy on 2018/11/23.
 */
public interface AreaDao {

    List<Area> queryAreas();

    Area queryAreaById(int areaId);

    int insertArea(Area area);

    int updateArea(Area area);

    int delArea(int areaId);

}
