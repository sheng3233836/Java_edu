package com.whitley.sqlSession;

import com.whitley.pojo.Configuration;
import com.whitley.xml.XMLConfigBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream in) throws PropertyVetoException, DocumentException {
        // 1. 使用dom4j解析配置文件，封装在Configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);

        // 2. 创建SqlSessionFactory， 生产sqlSession:会话对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
