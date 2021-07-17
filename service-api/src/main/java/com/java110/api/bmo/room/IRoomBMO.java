package com.java110.api.bmo.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;

/**
 * @ClassName IRoomBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:43
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IRoomBMO extends IApiBaseBMO {

    /**
     * 修改房屋
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void addBusinessRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void addBusinessUnit(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void addCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void addBusinessFloor(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除物业费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitPropertyFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addRoomAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateRoomAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateShellRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPropertyFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void sellRoom(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
