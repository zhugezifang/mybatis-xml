package com.gdz.mybatisxml.mapper;

import lombok.Data;

/**
 * @Author: guandezhi
 * @Date: 2019/1/28 10:51
 */
@Data
public class InterfaceMethodInfo {

    private String sqlType;

    private String methodName;

    private String sql;

    private String paramterType;

    private Object resultType;
}
