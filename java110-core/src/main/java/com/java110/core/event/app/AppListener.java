package com.java110.core.event.app;

import com.alibaba.fastjson.JSONObject;

import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2017/4/14.
 */
public interface AppListener<E extends AppEvent> extends EventListener {

    /**
     * 受理
     * Handle an application event.
     * @param event the event to respond to
     */
    void soDataService(E event);


    /**
     * 查询订单信息
     * @param event
     * @return
     */
    JSONObject queryDataInfo(E event);


    /**
     * 查询需要作废的订单信息
     * @param event
     * @return
     */
    JSONObject queryNeedDeleteDataInfo(E event);

}
