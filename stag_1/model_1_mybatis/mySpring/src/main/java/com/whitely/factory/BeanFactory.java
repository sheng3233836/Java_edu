package com.whitely.factory;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author yuanxin
 * @date 2020-10-26
 */
public class BeanFactory {
    /**
     * 1. 读物解析XML，通过反射技术实例化对象并且存储待用
     * 2. 对外提供获取实例对象的接口
     */

    private static Map<String, Object> map = new HashMap<String, Object>();

    static {
        /**
         * 1. 读物解析XML，通过反射技术实例化对象并且存储待用
         */
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");

        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//bean");
            for (Element element : list) {
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                Class<?> aClass = Class.forName(className);
                map.put(id, aClass.newInstance());
            }

            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入
            List<Element> plist = rootElement.selectNodes("//property");
            for (Element element : plist) {
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");
                Element parent = element.getParent();
                String id = parent.attributeValue("id");

                Object parentObj = map.get(id);

                // 找到set+name方法
                Method[] methods = parentObj.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().equals("set"+name)) {
                        method.invoke(parentObj, map.get(ref));
                    }
                }
                map.put(id, parentObj);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String id) {
        return map.get(id);
    }
}
