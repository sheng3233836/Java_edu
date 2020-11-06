package com.whitely.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author yuanxin
 * @date 2020-11-05
 */
public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource = new DruidDataSource();


    static {
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/self_test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
    }

    public static DruidDataSource getInstance() {
        return druidDataSource;
    }
}
