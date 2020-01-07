package com.gdz.mybatisxml;


import com.gdz.mybatisxml.entity.User;
import com.gdz.mybatisxml.mapper.UserMapper;
import com.gdz.mybatisxml.utils.JDBCUtils;

/**
 * @Author: guandezhi
 * @Date: 2019/1/29 10:51
 */
public class Main {


    public static void main(String[] args) {
        JDBCUtils.load("/application.properties");
        String path = "mapper/UserMapper.xml";
        UserMapper userMapper = SqlSession.getUserMapper(UserMapper.class,  path);
        User user = userMapper.queryUser(1L);
        System.out.println(user);
    }



}
