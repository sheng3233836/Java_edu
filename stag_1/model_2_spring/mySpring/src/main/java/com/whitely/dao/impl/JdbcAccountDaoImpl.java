package com.whitely.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.whitely.beans.Account;
import com.whitely.dao.AccountDao;
import com.whitely.utils.ConnectionUtils;
import com.whitely.utils.DruidUtils;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public class JdbcAccountDaoImpl implements AccountDao {
    @Override
    public void getName() {
        System.out.println("this is jdbc");
    }

    @Override
    public int update(String cardNo, int money) throws SQLException {
        Connection connection = ConnectionUtils.getInstance().getCurrentConnection();
        String sql = "update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,money);
        preparedStatement.setString(2,cardNo);
        int i = preparedStatement.executeUpdate();
        System.out.println(sql);
        preparedStatement.close();
        return i;
    }

    @Override
    public Account getAccount(String cardNo) throws SQLException {
        Connection connection = ConnectionUtils.getInstance().getCurrentConnection();
        String sql = "select * from account where cardNo=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();

        Account account = new Account();
        while(resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setMoney(resultSet.getInt("money"));
        }

        resultSet.close();
        preparedStatement.close();
//        connection.close();

        return account;
    }
}
