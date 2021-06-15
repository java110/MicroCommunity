package com.java110.api.bmo.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IUserBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:59
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IUserBMO extends IApiBaseBMO {
     void addStaffOrg(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
     void addStaff(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加用户
     *
     * @param paramObj
     */
    void addUser(JSONObject paramObj, DataFlowContext dataFlowContext);

    void modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext);

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    void deleteStaff(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    void deleteUser(JSONObject paramInJson, DataFlowContext dataFlowContext);


    /**
     * 注册用户
     *
     * @param paramObj
     */
    void registerUser(JSONObject paramObj, DataFlowContext dataFlowContext);
}
