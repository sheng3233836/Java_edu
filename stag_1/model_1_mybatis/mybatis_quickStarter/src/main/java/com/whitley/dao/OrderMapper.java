package com.whitley.dao;

import com.whitley.pojo.Order;
import com.whitley.pojo.User;

import java.util.List;

public interface OrderMapper {
    List<Order> findOrderAndUser();
    List<User> findUsers();

    List<User> findUserAndRole();
}
