package com.whitely.service.impl;

import java.sql.SQLException;

import com.whitely.beans.Account;
import com.whitely.dao.AccountDao;
import com.whitely.factory.BeanFactory;
import com.whitely.service.TransferService;
import com.whitely.utils.TransactionManager;

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

    @Override
    public void getName(){
        accountDao.getName();
    }

    @Override
    public void transfer() throws SQLException {
        try {
            TransactionManager.beginTransaction();
            Account account1 = accountDao.getAccount("zhangsan");
            Account account2 = accountDao.getAccount("lisi");

            int trans = 100;
            account1.setMoney(account1.getMoney() + trans);
            account1.setMoney(account1.getMoney() - trans);

            accountDao.update(account1.getCardNo(), account1.getMoney());
            accountDao.update(account2.getCardNo(), account2.getMoney());
            TransactionManager.commit();
        } catch (Exception e) {
            TransactionManager.rollback();
        }
    }

    public static void main(String[] args) throws SQLException {
        TransferService transferService = (TransferService) BeanFactory.getBean("transferService");
        transferService.getName();
        transferService.transfer();
    }
}
