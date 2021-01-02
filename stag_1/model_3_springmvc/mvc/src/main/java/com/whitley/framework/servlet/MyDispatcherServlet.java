package com.whitley.framework.servlet;

import com.whitley.framework.annotations.MyAutowired;
import com.whitley.framework.annotations.MyController;
import com.whitley.framework.annotations.MyRequestMapping;
import com.whitley.framework.annotations.MyService;
import com.whitley.framework.handler.Handler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuanxin
 * @date 2021-01-02
 */
public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();
    // 缓存扫描到的全限定类名
    private List<String> classNames = new ArrayList<>();
    // ioc 容器
    private Map<String, Object> iocMap = new HashMap<>();
    // url mapping
//    private Map<String, Method> handlerMapping = new HashMap<>();
    private List<Handler> handlers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 加载配置文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        // 扫描相关的类
        doScan(properties.getProperty("scanPackage"));
        // 初始化bean对象，实现ioc容器
        doInstance();
        // 实现依赖注入
        doAutowired();
        // 构建一个HandlerMapping，将Url和Method映射
        initHandlerMapping();
        System.out.println("my mvc 初始化完成");
        // 处理请求
    }

    private void initHandlerMapping() {
        if (iocMap.isEmpty()) {
            return;
        }
        for (Object o : iocMap.values()) {
            try {
                Class<?> aClass = o.getClass();
                if (!aClass.isAnnotationPresent(MyController.class)) {
                    continue;
                }
                String baseUrl = "";
                if (aClass.isAnnotationPresent(MyRequestMapping.class)) {
                    baseUrl = aClass.getAnnotation(MyRequestMapping.class).value();
                }

                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                        continue;
                    }
                    String value = method.getAnnotation(MyRequestMapping.class).value();
                    String url = baseUrl + value;

                    // 建立和method的映射关系
                    Handler handler = new Handler(o, method, Pattern.compile(url));
                    Map<String, Integer> paramIndexMapping = new HashMap<>();
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        if (parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class) {
                            paramIndexMapping.put(parameter.getType().getSimpleName(), i);
                        } else {
                            paramIndexMapping.put(parameter.getName(), i);
                        }
                    }
                    handler.setParamIndexMapping(paramIndexMapping);
                    handlers.add(handler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doAutowired() {
        if (iocMap.isEmpty()) {
            return;
        }
        // 遍历容器中的注解
        for (Object o : iocMap.values()) {
            try {
                Class<?> aClass = o.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    if (!declaredField.isAnnotationPresent(MyAutowired.class)) {
                        continue;
                    }
                    String beanName = declaredField.getAnnotation(MyAutowired.class).value();
                    if ("".equals(beanName.trim())) {
                        beanName = declaredField.getType().getName();
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(o, iocMap.get(beanName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 基于classNames 缓存的类名，通过反射，完成对象的创建和管理
    private void doInstance() {
        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(MyController.class)) {
                    //controller id不做多处理, 直接小写处理
                    iocMap.put(lowerFirst(aClass.getSimpleName()), aClass.newInstance());
                } else if (aClass.isAnnotationPresent(MyService.class)) {
                    String name = aClass.getAnnotation(MyService.class).value();
                    if (!"".equals(name.trim())) {
                        iocMap.put(name, aClass.newInstance());
                    } else {
                        // service 子类放入IOC容器
                        iocMap.put(lowerFirst(aClass.getSimpleName()), aClass.newInstance());
                    }

                    // service 层往往是接口的，所以需要再放入一份接口的
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        // 以接口的类名作为ID放入IOC容器
                        iocMap.put(anInterface.getName(), aClass.newInstance());
                    }
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static String lowerFirst(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }


    private void doScan(String scanPackage) {
        String path = this.getClass().getClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(path);
        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Handler handler = getHandler(req);
        if (handler == null) {
            resp.getWriter().write("404 not found");
            return;
        }

        Object[] args = new Object[handler.getParamIndexMapping().size()];

        // 填充普通参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            if (!handler.getParamIndexMapping().containsKey(param.getKey())) {
                continue;
            }

            args[handler.getParamIndexMapping().get(param.getKey())] = String.join(",", param.getValue());
        }
        Integer reqIndex = handler.getParamIndexMapping().get(req.getClass().getSimpleName());
        if (reqIndex != null) {
            args[reqIndex] = req;
        }
        Integer respIndex = handler.getParamIndexMapping().get(resp.getClass().getSimpleName());
        if (respIndex != null) {
            args[respIndex] = resp;
        }

        // 执行
        try {
            handler.getMethod().invoke(handler.getObject(), args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Handler getHandler(HttpServletRequest req) {
        if (handlers.isEmpty()) {
            return null;
        }
        String requestURI = req.getRequestURI();
        for (Handler handler : handlers) {
            Matcher matcher = handler.getPattern().matcher(requestURI);
            if (matcher.matches()) {
                return handler;
            }
        }
        return null;
    }
}
