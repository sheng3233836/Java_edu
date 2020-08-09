package com.whitley.dao;

import java.util.List;

import com.whitley.pojo.User;

/**
 * @author yuanxin
 * @date 2020-08-09
 */
public interface UserDao {
    List<User> findAll() throws Exception;

    User findByCondition(User user) throws Exception;
}
