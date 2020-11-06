package com.whitely.dao;

import java.sql.SQLException;

import com.whitely.beans.Account;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public interface AccountDao {
    void getName();

    int update(String cardNo, int money) throws SQLException;

    Account getAccount(String cradNo) throws SQLException;
}
