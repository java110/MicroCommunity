package com.java110.core.event.app.common;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.AppContext;
import com.java110.core.event.app.AppEvent;

/**
 * 通用处理事件，有些服务的处理方式是一模一样的
 * 没有必要 重复去创建类 做重复工作
 *
 * Created by wuxw on 2017/9/15.
 */
public class AppCommonEvent extends AppEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source  The object on which the Event initially occurred.
     * @param context
     * @throws IllegalArgumentException if source is null.
     */
    public AppCommonEvent(Object source, AppContext context) {
        this(source, context,null);
    }


    public AppCommonEvent(Object source, AppContext context, JSONArray data){
        super(source, context);
        this.setData(data);
    }
}
