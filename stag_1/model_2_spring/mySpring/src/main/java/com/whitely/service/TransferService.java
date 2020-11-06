package com.whitely.service;

import java.sql.SQLException;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public interface TransferService {
    void getName();

    void transfer() throws SQLException;
}
