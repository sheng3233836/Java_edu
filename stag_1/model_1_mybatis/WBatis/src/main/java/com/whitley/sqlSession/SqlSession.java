package com.whitley.sqlSession;

import java.util.List;

public interface SqlSession {
    <T> List<T> selectList(String statementId, Object... params) throws Exception;

    <T> T selectOne(String statementId, Object... params) throws Exception;

    // 为dao接口生成代理实现类

    <T> T getMapper(Class<?> mapperClass);
}
