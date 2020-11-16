package com.whitely.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.whitely.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author yuanxin
 * @date 2020-11-16
 */
public class ProxyFactory {

    private TransactionManager transactionManager;

    public Object getJdkProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object res;
                try {
                    transactionManager.beginTransaction();
                    res = method.invoke(obj, args);
                    transactionManager.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    transactionManager.rollback();
                    throw e;
                }
                return res;
            }
        });
    }

    public Object getCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("pre method invoke");
                Object res = method.invoke(obj, objects);
                System.out.println("after method invoke");
                return res;
            }
        });
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
