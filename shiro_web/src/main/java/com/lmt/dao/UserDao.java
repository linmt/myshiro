package com.lmt.dao;

import com.lmt.vo.User;

import java.util.List;

/**
 * Created by 热带雨林 on 2019/1/27.
 */
public interface UserDao {
    public User getUserByUserName(String userName);

    List<String> getRolesByUserName(String userName);
}
