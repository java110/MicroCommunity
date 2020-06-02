package com.java110.api.bmo.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName INoticeBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:01
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface INoticeBMO extends IApiBaseBMO {

    /**
     * 添加通知信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteNotice(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加通知信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addNotice(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加公告信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateNotice(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
