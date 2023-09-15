package com.czdx.parkingcharge.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生对象
 */
@Data
public class Student implements Serializable {

    private Integer id;
    private String name;

    private String no;

    private String address;

}
