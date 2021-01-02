package com.whitley.demo.service.impl;

import com.whitley.demo.service.DemoService;
import com.whitley.framework.annotations.MyService;

/**
 * @author yuanxin
 * @date 2021-01-02
 */
@MyService
public class DemoServiceImpl implements DemoService {
    @Override
    public String get(String name) {
        System.out.println("service 实现中的name参数：" + name);
        return name;
    }
}
