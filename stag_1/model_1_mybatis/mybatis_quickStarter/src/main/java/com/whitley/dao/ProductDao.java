package com.whitley.dao;

import java.util.List;

import com.whitley.pojo.Product;
import com.whitley.pojo.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.impl.PerpetualCache;

/**
 * @author yuanxin
 * @date 2020-08-16
 */
@CacheNamespace(implementation = PerpetualCache.class)
public interface ProductDao {


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "user", column = "uid", javaType = User.class,
                    one=@One(select = "com.whitley.dao.UserDao.findUserById")),
    })
    @Select("select * from product")
    List<Product> findProductAndUser();

    @Select("select * from product where uid = #{uid}")
    List<Product> findProductByUid(int uid);
}
