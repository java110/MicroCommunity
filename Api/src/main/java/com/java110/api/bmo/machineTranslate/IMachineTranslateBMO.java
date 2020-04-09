package com.java110.api.bmo.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.hardwareAdapation.CarInoutDto;
import com.java110.dto.hardwareAdapation.MachineDto;

/**
 * @ClassName IMachineTranslateBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:53
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IMachineTranslateBMO extends IApiBaseBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addCarInoutDetail(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId, MachineDto machineDto);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId);
    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addCarInoutFee(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId);
    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addCarInoutFee(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId, String startTime);

    public JSONObject modifyCarInout(JSONObject reqJson, DataFlowContext context, CarInoutDto carInoutDto);
    public JSONObject modifyCarInout(JSONObject reqJson, DataFlowContext context, CarInoutDto carInoutDto, String state, String endTime);
    /**
     * 保存照片
     *
     * @param reqJson
     * @param context
     */
    public JSONObject savePhoto(JSONObject reqJson, DataFlowContext context);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加设备同步信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
