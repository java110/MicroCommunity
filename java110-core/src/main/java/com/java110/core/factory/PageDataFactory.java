package com.java110.core.factory;

import com.java110.core.context.PageData;

/**
 * Created by wuxw on 2018/5/2.
 */
public class PageDataFactory {

    public static PageData newInstance(){
        return new PageData();
    }
}
