package com.java110.utils.util;

import java.util.Date;

/**
 * @ClassName PersonVo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/28 17:39
 * @Version 1.0
 * add by wuxw 2020/1/28
 **/
public class PersonVo extends parentDto{

    private String id;
    private String name;

    private String createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
