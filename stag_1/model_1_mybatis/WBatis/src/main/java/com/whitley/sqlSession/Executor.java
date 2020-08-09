package com.whitley.sqlSession;

import com.whitley.pojo.Configuration;
import com.whitley.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
