package com.gdz.mybatisxml.handler;

import com.gdz.mybatisxml.mapper.InterfaceMethodInfo;
import com.gdz.mybatisxml.mapper.MapperBean;
import com.gdz.mybatisxml.utils.JDBCUtils;
import com.gdz.mybatisxml.utils.XmlParseUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: guandezhi
 * @Date: 2019/1/29 10:18
 */
@Slf4j
public class UserMapperInvocationHandler implements InvocationHandler {

    private String path;

    public UserMapperInvocationHandler(String path) {
        this.path = path;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //解析mapperXml配置文件
        MapperBean mapperBean = XmlParseUtils.loadXml(path);
        if (!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())) {
            return null;
        }

        List<InterfaceMethodInfo> interfaceMethodInfoList = mapperBean.getInterfaceMethodInfoList();

        InterfaceMethodInfo currentMethodInfo = getInterfaceMethodInfo(method, interfaceMethodInfoList);

        if (currentMethodInfo == null) {
            return null;
        }

        //返回值对象
        Object returnTypeObj = currentMethodInfo.getResultType();

        //查询入参
        List<Object> paramsList = new ArrayList<>();
        paramsList.add(args[0]);

        ResultSet resultSet = JDBCUtils.query(currentMethodInfo.getSql(), paramsList);

        // 结果集和对象映射
        setReturnTypeObj(returnTypeObj, resultSet);

        return returnTypeObj;
    }

    /**
     * 结果集和对象映射
     *
     * @param returnTypeObj
     * @param resultSet
     * @throws Exception
     */
    private void setReturnTypeObj(Object returnTypeObj, ResultSet resultSet) throws Exception {
        if (resultSet != null) {
            while (resultSet.next()) {
                Field[] fields = returnTypeObj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    field.setAccessible(true);
                    if (field.getType() == Integer.class) {
                        field.set(returnTypeObj, resultSet.getInt(fieldName));
                    }
                    if (field.getType() == Long.class) {
                        field.set(returnTypeObj, resultSet.getLong(fieldName));
                    }
                    if (field.getType() == String.class) {
                        field.set(returnTypeObj, resultSet.getString(fieldName));
                    }
                    if (field.getType() == Date.class) {
                        field.set(returnTypeObj, resultSet.getDate(fieldName));
                    }
                }
            }
        }
    }


    /**
     * 获取当前方法信息
     *
     * @param method
     * @param interfaceMethodInfoList
     * @return
     */
    private InterfaceMethodInfo getInterfaceMethodInfo(Method method, List<InterfaceMethodInfo> interfaceMethodInfoList) {
        InterfaceMethodInfo currentMethodInfo = null;
        if (interfaceMethodInfoList != null && interfaceMethodInfoList.size() > 0) {
            for (InterfaceMethodInfo interfaceMethodInfo : interfaceMethodInfoList) {
                if (method.getName().equals(interfaceMethodInfo.getMethodName())) {
                    currentMethodInfo = interfaceMethodInfo;
                    break;
                }
            }
        }
        return currentMethodInfo;
    }
}
