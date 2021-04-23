package com.java110.core.event.app.cust;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.AppContext;
import com.java110.core.event.app.AppEvent;

/**
 * 作废订单事件
 * Created by wuxw on 2017/4/25.
 */
public class AppDeleteCustEvent extends AppEvent {


    /**
     * 初始化客户事件
     * @param source
     * @param context 上下文对象
     * @param data 分装的数据
     */
    public AppDeleteCustEvent(Object source,AppContext context,JSONArray data){
        super(source,context);
        this.setData(data);
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

}
