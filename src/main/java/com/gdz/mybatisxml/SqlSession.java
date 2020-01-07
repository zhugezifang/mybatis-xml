package com.gdz.mybatisxml;

import com.gdz.mybatisxml.handler.UserMapperInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @Author: guandezhi
 * @Date: 2019/1/29 10:17
 */
public class SqlSession {

    public static <T> T getUserMapper(Class clazz, String path) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz },
                new UserMapperInvocationHandler(path));
    }

}
