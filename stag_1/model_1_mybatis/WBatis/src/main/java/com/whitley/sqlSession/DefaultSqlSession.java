package com.whitley.sqlSession;

import com.whitley.io.Resources;
import com.whitley.pojo.Configuration;
import com.whitley.pojo.MappedStatement;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object... params) throws Exception {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return objects.get(0);
        } else {
            throw new RuntimeException("查询数据为空或者过多");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层方法执行JDBC代码，根据情况执行selectList或者selectOne;
                // 准备参数 1：statementId
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;
                System.out.println(statementId);
                // 准备参数 2：Object... params ：args

                // 获取被调用方法的返回类型

                Type genericReturnType = method.getGenericReturnType();
                // 是否有泛型
                if (genericReturnType instanceof ParameterizedType) {
                    return selectList(statementId, args);
                } else {
                    return selectOne(statementId, args);
                }
            }
        });

        return (T) proxyInstance;
    }
}
