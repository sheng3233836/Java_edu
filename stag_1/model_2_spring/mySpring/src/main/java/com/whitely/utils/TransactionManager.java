package com.whitely.utils;

import java.sql.SQLException;

/**
 * @author yuanxin
 * @date 2020-11-05
 */
public class TransactionManager {
    private ConnectionUtils connectionUtils;

    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentConnection().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connectionUtils.getCurrentConnection().commit();
    }

    public void rollback() throws SQLException {
        connectionUtils.getCurrentConnection().rollback();
    }

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }
}
