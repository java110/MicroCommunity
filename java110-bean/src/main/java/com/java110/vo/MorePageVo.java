package com.java110.vo;

import java.io.Serializable;

/**
 * @ClassName MorePageVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/24 11:19
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MorePageVo extends Vo implements Serializable {


    // 分页页数
    private int page;
    // 行数
    private int rows;

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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
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
