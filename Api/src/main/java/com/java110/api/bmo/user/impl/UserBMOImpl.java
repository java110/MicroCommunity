package com.java110.api.bmo.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:59
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("userBMOImpl")
public class UserBMOImpl extends ApiBaseBMO implements IUserBMO {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    public JSONObject addStaffOrg(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_STAFF_REL);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessOrgStaffRels = new JSONArray();
        JSONObject businessOrgStaffRel = new JSONObject();
        businessOrgStaffRel.put("relId", "-1");
        businessOrgStaffRel.put("storeId", paramInJson.getString("storeId"));
        businessOrgStaffRel.put("staffId", paramInJson.getString("userId"));
        businessOrgStaffRel.put("orgId", paramInJson.getString("orgId"));
        businessOrgStaffRel.put("relCd", paramInJson.getString("relCd"));
        businessOrgStaffRels.add(businessOrgStaffRel);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrgStaffRel", businessOrgStaffRels);

        return business;
    }

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    public JSONObject addStaff(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("storeUserId", "-1");
        businessStoreUser.put("userId", paramInJson.getString("userId"));
        businessStoreUser.put("relCd", paramInJson.getString("relCd"));
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser", businessStoreUsers);

        return business;
    }

    /**
     * 添加用户
     *
     * @param paramObj
     */
    public JSONObject addUser(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramObj, "name", "请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "tel", "请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "orgId", "请求报文格式错误或未包含部门信息");
        Assert.jsonObjectHaveKey(paramObj, "address", "请求报文格式错误或未包含地址信息");
        Assert.jsonObjectHaveKey(paramObj, "sex", "请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(paramObj, "relCd", "请求报文格式错误或未包含员工角色");


        if (paramObj.containsKey("email") && !StringUtil.isEmpty(paramObj.getString("email"))) {
            Assert.isEmail(paramObj, "email", "不是有效的邮箱格式");
        }


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser", refreshParamIn(paramObj));

        return business;
    }

    /**
     * 注册用户
     *
     * @param paramObj
     */
    public JSONObject registerUser(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramObj, "name", "请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "tel", "请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "password", "请求参数中未包含password 节点，请确认");


        if (paramObj.containsKey("email") && !StringUtil.isEmpty(paramObj.getString("email"))) {
            Assert.isEmail(paramObj, "email", "不是有效的邮箱格式");
        }


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        //paramObj.put("userId","-1");
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_ORDINARY);
        //设置默认密码
        String userPassword = paramObj.getString("password");
        userPassword = AuthenticationFactory.passwdMd5(userPassword);
        paramObj.put("password", userPassword);

        if(paramObj.containsKey("openId") && !"-1".equals(paramObj.getString("openId"))){
            JSONArray userAttr = new JSONArray();
            JSONObject userAttrObj = new JSONObject();
            userAttrObj.put("attrId", "-1");
            userAttrObj.put("specCd", "100201911001");
            userAttrObj.put("value", paramObj.getString("openId"));
            userAttr.add(userAttrObj);
            paramObj.put("businessUserAttr", userAttr);
        }

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser", paramObj);

        return business;
    }


    /**
     * 对请求报文处理
     *
     * @param paramObj
     * @return
     */
    private JSONObject refreshParamIn(JSONObject paramObj) {
        //paramObj.put("userId","-1");
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_STAFF);
        //设置默认密码
        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);
        paramObj.put("password", staffDefaultPassword);
        return paramObj;
    }

    public JSONObject modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext) {
        //校验json 格式中是否包含 name,email,levelCd,tel

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser", builderStaffInfo(paramObj, dataFlowContext));

        return business;
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        UserDto userDto = new UserDto();
        userDto.setStatusCd("0");
        userDto.setUserId(paramObj.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUserHasPwd(userDto);

        Assert.listOnlyOne(userDtos, "数据错误查询到多条用户信息或单条");

        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDtos.get(0)));

        if (!paramObj.getString("oldPwd").equals(userDtos.get(0).getPassword())) {
            throw new IllegalArgumentException("原始密码错误");
        }
        userInfo.putAll(paramObj);
        userInfo.put("password", paramObj.getString("newPwd"));

        return userInfo;
    }

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject deleteStaff(JSONObject paramInJson) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_STORE_USER);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("userId", paramInJson.getString("userId"));
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser", businessStoreUsers);

        return business;

    }

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject deleteUser(JSONObject paramInJson) {
        //校验json 格式中是否包含 name,email,levelCd,tel


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_REMOVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("userId", paramInJson.getString("userId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser", businessStoreUser);

        return business;

    }


}
