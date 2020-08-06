package com.whitley.pojo;

import com.whitley.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class BoundSql {
    private String sql;
    private List<ParameterMapping> parameterMappingList = new ArrayList<>();

    public BoundSql(String parseSql, List<ParameterMapping> parameterMappings) {
        this.sql=parseSql;
        this.parameterMappingList=parameterMappings;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
