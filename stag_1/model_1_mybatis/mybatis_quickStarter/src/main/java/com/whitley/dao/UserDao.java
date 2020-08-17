package com.whitley.dao;

import com.whitley.pojo.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
//    List<User> findAll() throws IOException;

    @Select("select * from user where id = #{id}")
    User findUserById(int id);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "products", column = "id", javaType = List.class,
                    many=@Many(select = "com.whitley.dao.ProductDao.findProductByUid")),
    })
    @Select("select * from user")
    List<User> findUserAndProduct();
}
