package com.whitely.utils;

import java.sql.SQLException;

/**
 * @author yuanxin
 * @date 2020-11-05
 */
public class TransactionManager {

    public static void beginTransaction() throws SQLException {
        ConnectionUtils.getInstance().getCurrentConnection().setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        ConnectionUtils.getInstance().getCurrentConnection().commit();
    }

    public static void rollback() throws SQLException {
        ConnectionUtils.getInstance().getCurrentConnection().rollback();
    }
}
