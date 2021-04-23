package com.java110.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ElectricPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 10:02
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class ElectricPo implements Serializable {

    private String id;

    private Date startTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
