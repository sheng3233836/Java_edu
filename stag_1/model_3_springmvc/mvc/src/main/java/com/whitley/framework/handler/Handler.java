package com.whitley.framework.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author yuanxin
 * @date 2021-01-02
 */
public class Handler {
    private Object object;
    private Method method;
    private Pattern pattern; //Spring 的 url ,它可以支持正则
    private Map<String, Integer> paramIndexMapping; // 参数顺序

    public Handler(Object object, Method method, Pattern pattern) {
        this.object = object;
        this.method = method;
        this.pattern = pattern;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }
}
