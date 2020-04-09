package com.java110.api.bmo.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;

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
    public JSONObject deleteFloor(JSONObject paramInJson);
    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitCommunityMember(JSONObject paramInJson);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject editFloor(JSONObject paramInJson);

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFloor(JSONObject paramInJson);
    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public JSONObject addCommunityMember(JSONObject paramInJson);
}
