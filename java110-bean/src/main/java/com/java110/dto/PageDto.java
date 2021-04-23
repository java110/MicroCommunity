package com.java110.dto;

import java.io.Serializable;

/**
 * 分页数据封装对象
 */
public class PageDto extends Dto implements Serializable {

    public static final int DEFAULT_PAGE = -1;

    // 分页页数
    private int page = DEFAULT_PAGE;
    // 行数
    private int row;

    //页数
    private int records;

    // 总记录数
    private int total;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
}
