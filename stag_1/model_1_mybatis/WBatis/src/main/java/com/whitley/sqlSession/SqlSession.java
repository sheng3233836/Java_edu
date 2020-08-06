package com.whitley.sqlSession;

import java.util.List;

public interface SqlSession {
    <T> List<T> selectList(String statementId, Object... params);

    <T> T selectOne(String statementId, Object... params);

}
