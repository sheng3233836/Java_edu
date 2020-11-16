package com.whitely.utils;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author yuanxin
 * @date 2020-11-05
 */
public class ConnectionUtils {

//    private ConnectionUtils(){
//    }
//
//    private static ConnectionUtils connectionUtils = new ConnectionUtils();
//
//    public static ConnectionUtils getInstance() {
//        return connectionUtils;
//    }

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    public Connection getCurrentConnection() throws SQLException {
        Connection connection = null;
        if (threadLocal.get() == null) {
            connection = DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return threadLocal.get();
    }
}
