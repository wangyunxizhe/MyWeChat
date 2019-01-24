package com.yuan.service;

import com.yuan.entity.Area;
import java.util.List;

/**
 * Created by wangy on 2018/11/26.
 */
public interface AreaService {

    List<Area> queryAreas();

    Area queryAreaById(int areaId);

    boolean insertArea(Area area);

    boolean updateArea(Area area);

    boolean delArea(int areaId);

}
