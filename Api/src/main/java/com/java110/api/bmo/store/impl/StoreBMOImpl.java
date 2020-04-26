package com.java110.api.bmo.store.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.store.IStoreBMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.store.IStoreInnerServiceSMO;
import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName StoreBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:48
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("storeBMOImpl")
public class StoreBMOImpl extends ApiBaseBMO implements IStoreBMO {
    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;
    /**
     * 添加商户
     *
     * @param paramInJson
     * @return
     */
    public JSONObject addStore(JSONObject paramInJson) {
        JSONObject business = JSONObject.parseObject("{}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        business.put(CommonConstant.HTTP_BUSINESS_DATAS, refreshParamIn(paramInJson));

        return business;

    }

    @Override
    public JSONObject updateStore(JSONObject paramInJson) {
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(paramInJson.getString("storeId"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
        Assert.listOnlyOne(storeDtos, "未找到需要修改的商户或多条数据");
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_STORE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessStore = new JSONObject();
        businessStore.putAll(BeanConvertUtil.beanCovertMap(storeDtos.get(0)));
        businessStore.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStore", businessStore);
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
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("storeUserId", "-1");
        businessStoreUser.put("userId", paramInJson.getJSONObject("businessStore").getString("userId"));
        businessStoreUser.put("relCd", StoreUserRelConstant.REL_ADMIN);
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser", businessStoreUsers);

        return business;
    }
    /**
     * 对请求报文处理
     *
     * @param paramObj
     * @return
     */
    private JSONObject refreshParamIn(JSONObject paramObj) {

        String storeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_storeId);
        paramObj.put("storeId", storeId);
        if (paramObj.containsKey("businessStore")) {
            JSONObject businessStoreObj = paramObj.getJSONObject("businessStore");
            businessStoreObj.put("storeId", storeId);
            if (!businessStoreObj.containsKey("password")) {
                String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                businessStoreObj.put("password", staffDefaultPassword);
            }

            if (!businessStoreObj.containsKey("mapX")) {
                businessStoreObj.put("mapX", "");
            }

            if (!businessStoreObj.containsKey("mapY")) {
                businessStoreObj.put("mapY", "");
            }
        }

        if (paramObj.containsKey("businessStoreAttr")) {
            JSONArray attrs = paramObj.getJSONArray("businessStoreAttr");

            for (int businessStoreAttrIndex = 0; businessStoreAttrIndex < attrs.size(); businessStoreAttrIndex++) {
                JSONObject attr = attrs.getJSONObject(businessStoreAttrIndex);
                attr.put("storeId", storeId);
                attr.put("attrId", "-" + (businessStoreAttrIndex + 1));
            }
        }

        if (paramObj.containsKey("businessStorePhoto")) {
            JSONArray photos = paramObj.getJSONArray("businessStorePhoto");

            for (int businessStorePhotoIndex = 0; businessStorePhotoIndex < photos.size(); businessStorePhotoIndex++) {
                JSONObject attr = photos.getJSONObject(businessStorePhotoIndex);
                attr.put("storeId", storeId);
                attr.put("storePhotoId", "-" + (businessStorePhotoIndex + 1));
            }
        }

        if (paramObj.containsKey("businessStoreCerdentials")) {
            JSONArray cerdentials = paramObj.getJSONArray("businessStoreCerdentials");

            for (int businessStoreCerdentialsIndex = 0; businessStoreCerdentialsIndex < cerdentials.size(); businessStoreCerdentialsIndex++) {
                JSONObject attr = cerdentials.getJSONObject(businessStoreCerdentialsIndex);
                attr.put("storeId", storeId);
                attr.put("storeCerdentialsId", "-" + (businessStoreCerdentialsIndex + 1));
            }
        }

        return paramObj;
    }

    /**
     * 添加一级组织信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrg(JSONObject paramInJson) {

        String orgId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId);
        paramInJson.put("levelOneOrgId", orgId);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOrg = new JSONObject();
        businessOrg.put("orgName", paramInJson.getJSONObject("businessStore").getString("name"));
        businessOrg.put("orgLevel", "1");
        businessOrg.put("parentOrgId", orgId);
        businessOrg.put("belongCommunityId", "9999");
        businessOrg.put("orgId", orgId);
        businessOrg.put("allowOperation", "F");
        businessOrg.put("storeId", paramInJson.getString("storeId"));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrg", businessOrg);
        return business;
    }

    /**
     * 添加公司总部
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrgHeadCompany(JSONObject paramInJson) {

        String orgId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId);
        paramInJson.put("levelTwoOrgId", orgId);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 4);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOrg = new JSONObject();
        businessOrg.put("orgName", "公司总部");
        businessOrg.put("orgLevel", "2");
        businessOrg.put("parentOrgId", paramInJson.getString("levelOneOrgId"));
        businessOrg.put("belongCommunityId", "9999");
        businessOrg.put("orgId", orgId);
        businessOrg.put("allowOperation", "F");
        businessOrg.put("storeId", paramInJson.getString("storeId"));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrg", businessOrg);
        return business;
    }

    /**
     * 添加总部办公室
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOrgHeadPart(JSONObject paramInJson) {

        String orgId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId);
        paramInJson.put("levelThreeOrgId", orgId);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 5);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOrg = new JSONObject();
        businessOrg.put("orgName", "总部办公室");
        businessOrg.put("orgLevel", "3");
        businessOrg.put("parentOrgId", paramInJson.getString("levelTwoOrgId"));
        businessOrg.put("belongCommunityId", "9999");
        businessOrg.put("orgId", orgId);
        businessOrg.put("storeId", paramInJson.getString("storeId"));
        businessOrg.put("allowOperation", "F");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrg", businessOrg);
        return business;
    }


    public JSONObject addStaffOrg(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_STAFF_REL);
        business.put(CommonConstant.HTTP_SEQ,  DEFAULT_SEQ + 6);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessOrgStaffRels = new JSONArray();
        JSONObject businessOrgStaffRel = new JSONObject();
        businessOrgStaffRel.put("relId", "-1");
        businessOrgStaffRel.put("storeId", paramInJson.getString("storeId"));
        businessOrgStaffRel.put("staffId", paramInJson.getJSONObject("businessStore").getString("userId"));
        businessOrgStaffRel.put("orgId", paramInJson.getString("levelThreeOrgId"));
        businessOrgStaffRel.put("relCd", StoreUserRelConstant.REL_ADMIN);
        businessOrgStaffRels.add(businessOrgStaffRel);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrgStaffRel", businessOrgStaffRels);

        return business;
    }
}
