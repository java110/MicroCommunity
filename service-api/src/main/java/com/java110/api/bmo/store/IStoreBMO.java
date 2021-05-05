package com.java110.api.bmo.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;

/**
 * @ClassName IStoreBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:48
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IStoreBMO extends IApiBaseBMO {

    public JSONObject addStaffOrg(JSONObject paramInJson);

    /**
     * 添加总部办公室
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrgHeadPart(JSONObject paramInJson);

    /**
     * 添加公司总部
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrgHeadCompany(JSONObject paramInJson);

    /**
     * 添加一级组织信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrg(JSONObject paramInJson);

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    public JSONObject addStaff(JSONObject paramInJson);

    /**
     * 添加商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject addStore(JSONObject paramInJson);

    public JSONObject updateStore(JSONObject paramInJson);

    public JSONObject addPurchase(JSONObject paramInJson);

    public JSONObject addCollection(JSONObject paramInJson);

     JSONObject contractApply(JSONObject paramInJson);


    /**
     * 合同变更
     *
     * @param paramInJson
     * @return
     */
     JSONObject contractChange(JSONObject paramInJson);

    /**
     * 调拨审核
     *
     * @param paramInJson
     * @return
     */
    JSONObject allocationStorehouse(JSONObject paramInJson);

    JSONObject addAccount(JSONObject paramInJson,String acctType);

}
