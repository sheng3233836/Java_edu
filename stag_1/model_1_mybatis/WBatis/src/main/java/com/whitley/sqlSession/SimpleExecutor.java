package com.whitley.sqlSession;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.whitley.pojo.BoundSql;
import com.whitley.pojo.Configuration;
import com.whitley.pojo.MappedStatement;
import com.whitley.utils.GenericTokenParser;
import com.whitley.utils.ParameterMapping;
import com.whitley.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        // 1. 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. 获取SQL
            //转换#{}转义
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        // 3. 获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
        // 4. 设置参数

        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            // 暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i + 1, o);
        }
        // 5. 执行SQL
        ResultSet resultSet = preparedStatement.executeQuery();

        // 6. 封装返回结果
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        List<Object> res = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段值
                Object value = resultSet.getObject(columnName);

                // 反射
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            res.add(o);
        }


        return (List<T>) res;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null) {
            return Class.forName(parameterType);
        }
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
