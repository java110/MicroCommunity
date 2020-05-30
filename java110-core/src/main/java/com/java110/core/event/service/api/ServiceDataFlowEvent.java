package com.java110.core.event.service.api;

import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;

import java.util.EventObject;

/**
 *
 * 服务事件
 * Created by wuxw on 2018/5/18.
 */
public class ServiceDataFlowEvent extends EventObject {

    private DataFlowContext dataFlowContext;
    private AppService appService;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ServiceDataFlowEvent(Object source, DataFlowContext dataFlowContext,AppService appService) {
        super(source);
        this.dataFlowContext = dataFlowContext;
        this.appService = appService;
    }


    public DataFlowContext getDataFlowContext() {
        return dataFlowContext;
    }

    /*public void setDataFlowContext(DataFlowContext dataFlowContext) {
        this.dataFlowContext = dataFlowContext;
    }*/

    public AppService getAppService(){
        return appService;
    }
}
