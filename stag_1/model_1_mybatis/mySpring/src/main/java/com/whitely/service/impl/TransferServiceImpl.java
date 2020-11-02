package com.whitely.service.impl;

import com.whitely.dao.AccountDao;
import com.whitely.factory.BeanFactory;
import com.whitely.service.TransferService;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public class TransferServiceImpl implements TransferService {
//    private AccountDao accountDao = new JdbcAccountDaoImpl();

//    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    //最佳
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void getName(){
        accountDao.getName();
    }

    public static void main(String[] args) {
        TransferService transferService = (TransferService) BeanFactory.getBean("transferService");
        transferService.getName();
    }
}
