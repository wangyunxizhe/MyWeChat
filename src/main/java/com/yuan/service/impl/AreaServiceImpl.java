package com.yuan.service.impl;

import com.yuan.dao.AreaDao;
import com.yuan.entity.Area;
import com.yuan.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wangy on 2018/11/26.
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> queryAreas() {
        return areaDao.queryAreas();
    }

    @Override
    public Area queryAreaById(int areaId) {
        return areaDao.queryAreaById(areaId);
    }

    @Transactional
    @Override
    public boolean insertArea(Area area) {
        if (area.getAreaName() != null && !area.getAreaName().equals("")) {
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int num = areaDao.insertArea(area);
                if (num > 0) {
                    return true;
                } else {
                    throw new RuntimeException("区域信息插入失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("区域信息插入失败：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("区域信息不能为空！");
        }
    }

    @Transactional
    @Override
    public boolean updateArea(Area area) {
        if (area.getAreaId() != null && area.getAreaId() > 0) {
            area.setLastEditTime(new Date());
            try {
                int num = areaDao.updateArea(area);
                if (num > 0) {
                    return true;
                } else {
                    throw new RuntimeException("区域信息更新失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("区域信息更新失败：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("区域信息不能为空！");
        }
    }

    @Transactional
    @Override
    public boolean delArea(int areaId) {
        if (areaId > 0) {
            try {
                int num = areaDao.delArea(areaId);
                if (num > 0) {
                    return true;
                } else {
                    throw new RuntimeException("区域信息删除失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("区域信息删除失败：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("区域Id不能为空！");
        }
    }

}
