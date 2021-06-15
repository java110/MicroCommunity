package com.java110.core.event.app.cust;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.AppContext;
import com.java110.core.event.app.AppEvent;

/**
 * 客户事件
 * Created by wuxw on 2017/4/14.
 */
public class AppCustEvent extends AppEvent {

    /**
     * 初始化客户事件
     * @param source
     * @param context
     * @param data
     */
    public AppCustEvent(Object source,AppContext context,JSONArray data){
        super(source,context);
        this.setData(data);
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppCustEvent(Object source, AppContext context) {
        this(source, context,null);
    }

}
