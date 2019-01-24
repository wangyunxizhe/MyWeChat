package com.yuan.dao;

import com.yuan.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wangy on 2018/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void queryAreas() {
        List<Area> areas = areaDao.queryAreas();
        assertEquals(2, areas.size());
    }

    @Test
    public void queryAreaById() throws Exception {
        Area area = areaDao.queryAreaById(1);
        assertEquals("东苑", area.getAreaName());
    }

    @Test
    public void insertArea() throws Exception {
        Area area = new Area();
        area.setAreaName("南苑");
        area.setPriority(1);
        int num = areaDao.insertArea(area);
        assertEquals(1, num);
    }

    @Test
    public void updateArea() throws Exception {
        Area area = new Area();
        area.setAreaName("西苑");
        area.setAreaId(3);
        area.setLastEditTime(new Date());
        int num = areaDao.updateArea(area);
        assertEquals(1, num);
    }

    @Test
    public void delArea() throws Exception {
        int num = areaDao.delArea(3);
        assertEquals(1, num);
    }

}