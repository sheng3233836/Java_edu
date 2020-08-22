package com.whitley.dao;

import com.whitley.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper {
    List<User> findAll();

    List<User> findById(User user);

    List<User> findByIds(int[] array);

    @Insert("insert into user values(#{id},#{username})")
    void addUser(User user);

    @Update("update user set username = #{username} where id = #{id}")
    void updateUser(User user);

    @Select("select * from user")
    List<User> findUser();

}
