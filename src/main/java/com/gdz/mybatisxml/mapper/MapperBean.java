package com.gdz.mybatisxml.mapper;

import lombok.Data;

import java.util.List;

/**
 * @Author: guandezhi
 * @Date: 2019/1/28 10:48
 */
@Data
public class MapperBean {

    private String interfaceName;

    private List<InterfaceMethodInfo> interfaceMethodInfoList;


}
