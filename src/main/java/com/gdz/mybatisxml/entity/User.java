package com.gdz.mybatisxml.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: guandezhi
 * @Date: 2019/1/28 10:32
 */
@Data
public class User {

    private Long id;
    private String name;
    private Long age;
    private Date time;

}
