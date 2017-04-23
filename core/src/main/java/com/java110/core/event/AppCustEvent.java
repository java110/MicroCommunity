package com.java110.core.event;

import com.alibaba.fastjson.JSONArray;
import com.java110.App;
import com.java110.core.context.AppContext;

/**
 * 客户事件
 * Created by wuxw on 2017/4/14.
 */
public class AppCustEvent extends AppEvent {

    JSONArray custData = null;

    /**
     * 初始化客户事件
     * @param source
     * @param context
     * @param custData
     */
    public AppCustEvent(Object source,AppContext context,JSONArray custData){
        super(source,context);
        this.custData = custData;
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

    public JSONArray getCustData() {
        return custData;
    }
}
