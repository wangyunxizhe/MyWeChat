package com.yuan.dao;

import com.yuan.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    String selectNicknameByOpenid(String openid);

    int updateOpenidByUser(User record);
}