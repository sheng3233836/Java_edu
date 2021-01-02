package com.whitley.demo;

import com.whitley.demo.service.DemoService;
import com.whitley.framework.annotations.MyAutowired;
import com.whitley.framework.annotations.MyController;
import com.whitley.framework.annotations.MyRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuanxin
 * @date 2021-01-02
 */
@MyController
@MyRequestMapping("/demo")
public class DemoController {
    @MyAutowired
    private DemoService demoService;

    @MyRequestMapping("/get")
    public String get(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }
}
