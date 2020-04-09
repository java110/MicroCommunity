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
    public JSONObject addStaffOrg(JSONObject paramInJson);

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    public JSONObject addStaff(JSONObject paramInJson);

    /**
     * 添加用户
     *
     * @param paramObj
     */
    public JSONObject addUser(JSONObject paramObj, DataFlowContext dataFlowContext);

    public JSONObject modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext);

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject deleteStaff(JSONObject paramInJson);

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject deleteUser(JSONObject paramInJson);


    /**
     * 注册用户
     *
     * @param paramObj
     */
    public JSONObject registerUser(JSONObject paramObj, DataFlowContext dataFlowContext);
}
