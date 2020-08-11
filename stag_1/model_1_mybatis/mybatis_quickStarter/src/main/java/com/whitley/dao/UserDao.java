package com.whitley.dao;

import com.whitley.pojo.User;

import java.io.IOException;
import java.util.List;

public interface UserDao {
    List<User> findAll() throws IOException;
}
