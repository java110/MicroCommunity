package com.java110.core.event;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.AppContext;

/**
 * 作废订单事件
 * Created by wuxw on 2017/4/25.
 */
public class AppDeleteCustEvent extends AppEvent {

    JSONArray custData = null;

    /**
     * 初始化客户事件
     * @param source
     * @param context
     * @param custData
     */
    public AppDeleteCustEvent(Object source,AppContext context,JSONArray custData){
        super(source,context);
        this.custData = custData;
    }


    /**
     * Constructs a prototypical Event.
     *
     * @param source  The object on which the Event initially occurred.
     * @param context
     * @throws IllegalArgumentException if source is null.
     */
    public AppDeleteCustEvent(Object source, AppContext context) {
        super(source, context);
    }

    public JSONArray getCustData() {
        return custData;
    }
}
