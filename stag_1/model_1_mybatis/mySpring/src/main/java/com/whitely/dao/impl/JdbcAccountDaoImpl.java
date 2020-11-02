package com.whitely.dao.impl;

import com.whitely.dao.AccountDao;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public class JdbcAccountDaoImpl implements AccountDao {
    public void getName() {
        System.out.println("this is jdbc");
    }
}
