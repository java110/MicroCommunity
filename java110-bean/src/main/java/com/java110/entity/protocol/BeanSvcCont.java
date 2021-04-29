package com.java110.entity.protocol;

import java.io.Serializable;

/**
 * 服务之间交互基类，包含一个表明类的beanName
 * Created by wuxw on 2017/2/28.
 */
public class BeanSvcCont implements Serializable{



    //对应子类的class
    private String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
