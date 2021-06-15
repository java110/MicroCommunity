package com.java110.core.event.app;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.AppContext;

import java.util.EventObject;

/**
 *
 * java110 事件
 * Created by wuxw on 2017/4/14.
 */
public class AppEvent extends EventObject {

    private AppContext context;

    //这个类每次都会创建，所以是线程安全的
    private JSONArray data ;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppEvent(Object source,AppContext context) {
        super(source);
        this.context= context;
    }

    public AppContext getContext() {
        return context;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }


    public JSONArray getData(){
        return data;
    }

    public void setData(JSONArray data){
        this.data = data;
    }
}
