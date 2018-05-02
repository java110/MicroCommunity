package com.java110.common.factory;

import com.java110.entity.service.PageData;

/**
 * Created by wuxw on 2018/5/2.
 */
public class PageDataFactory {

    public static PageData newInstance(){
        return new PageData();
    }
}
