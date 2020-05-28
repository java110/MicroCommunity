package com.java110.api.bmo.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IFloorBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:35
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IFloorBMO extends IApiBaseBMO {

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteFloor(JSONObject paramInJson, DataFlowContext context);

    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public void exitCommunityMember(JSONObject paramInJson, DataFlowContext context);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editFloor(JSONObject paramInJson, DataFlowContext context);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addFloor(JSONObject paramInJson, DataFlowContext context);

    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMember(JSONObject paramInJson, DataFlowContext context);
}
