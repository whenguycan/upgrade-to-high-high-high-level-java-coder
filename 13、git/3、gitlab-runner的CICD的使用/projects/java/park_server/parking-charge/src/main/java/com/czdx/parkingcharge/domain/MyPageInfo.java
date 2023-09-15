package com.czdx.parkingcharge.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 轻量级PageInfo
 * @param <T>
 */
@Getter
@Setter
public class MyPageInfo<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 5214752514988056473L;

    private int pageNum;

    private int pageSize;

    private long total;

    private List<T> list;

    public MyPageInfo() {}

    public MyPageInfo(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
