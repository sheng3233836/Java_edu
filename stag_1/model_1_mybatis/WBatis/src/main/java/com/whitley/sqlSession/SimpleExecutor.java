package com.whitley.sqlSession;

import com.whitley.pojo.BoundSql;
import com.whitley.pojo.Configuration;
import com.whitley.pojo.MappedStatement;
import com.whitley.utils.GenericTokenParser;
import com.whitley.utils.ParameterMapping;
import com.whitley.utils.ParameterMappingTokenHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException {
        // 1. 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. 获取SQL
            //转换#{}转义
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        // 3. 获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
        // 4. 设置参数
        // 5. 执行SQL
        // 6. 封装返回结果

        return null;
    }

    /**
     * 完成对#{}解析，1将#{}使用?代替，2.解析出#{}的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parseSql, parameterMappings);
    }
}
