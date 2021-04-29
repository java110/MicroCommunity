package com.java110.utils.util;

import java.util.Date;

/**
 * @ClassName PersonDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/28 17:39
 * @Version 1.0
 * add by wuxw 2020/1/28
 **/
public class PersonDto extends parentDto{

    private int id;

    private String name;

    private Date createTime;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
