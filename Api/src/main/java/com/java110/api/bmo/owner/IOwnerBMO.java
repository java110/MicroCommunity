package com.java110.api.bmo.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerDto;

/**
 * @ClassName IOwner
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:14
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IOwnerBMO extends IApiBaseBMO {
    /**
     * 添加业主应用用户关系
     *
     * @param paramInJson
     * @return
     */
    public void addOwnerAppUser(JSONObject paramInJson, CommunityDto communityDto, OwnerDto ownerDto, DataFlowContext dataFlowContext);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAuditAppUserBindingOwner(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteOwner(JSONObject paramInJson, DataFlowContext dataFlowContext);


    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public void exitCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editOwner(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void editOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext);
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
    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext);
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
    public void addOwner(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加审核业主绑定信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAuditAppUserBindingOwner(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editOwnerPhotoPass(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
