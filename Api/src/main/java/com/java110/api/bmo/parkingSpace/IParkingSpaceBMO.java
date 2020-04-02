package com.java110.api.bmo.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IParkingSpaceBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:27
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IParkingSpaceBMO extends IApiBaseBMO {

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteParkingSpace(JSONObject paramInJson);
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject editParkingSpace(JSONObject paramInJson);
    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyParkingSpaceState(JSONObject paramInJson);
    /**
     * 删除物业费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addParkingSpace(JSONObject paramInJson);
    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifySellParkingSpaceState(JSONObject paramInJson);
    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 计算费用信息
     *
     * @param paramInJson 传入数据字段
     */
    public void computeFeeInfo(JSONObject paramInJson);
    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject sellParkingSpace(JSONObject paramInJson);
}
