package com.java110.user.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.event.service.BusinessServiceDataFlowEvent;
import com.java110.event.service.BusinessServiceDataFlowListener;
import com.java110.user.dao.IUserServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Service("saveUserInfo")
@Transactional
public class SaveUserTagListener extends LoggerEngine implements BusinessServiceDataFlowListener{

    @Autowired
    IUserServiceDao userServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_USER_INFO;
    }

    @Override
    public void soService(BusinessServiceDataFlowEvent event) {
        //这里处理业务逻辑数据
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        doSaveUserInfo(dataFlowContext);
    }

    private void doSaveUserInfo(DataFlowContext dataFlowContext){
        String businessType = dataFlowContext.getOrder().getBusinessType();
        Business business = dataFlowContext.getCurrentBusiness();
        //Assert.hasLength(business.getbId(),"bId 不能为空");
        // Instance 过程
        if(StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(businessType)){
            //doComplateUserInfo(business);
            doSaveInstanceUserInfo(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(businessType)){ // Business过程
            doSaveBusinessUserInfo(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_DELETE.equals(businessType)){ //撤单过程
            doDeleteInstanceUserInfo(dataFlowContext,business);
        }

        dataFlowContext.setResJson(DataTransactionFactory.createBusinessResponseJson(dataFlowContext,ResponseConstant.RESULT_CODE_SUCCESS,"成功",
                dataFlowContext.getParamOut()));
    }

    /**
     * 撤单
     * @param business
     */
    private void doDeleteInstanceUserInfo(DataFlowContext dataFlowContext,Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        Map userInfo = userServiceDaoImpl.queryUserInfo(info);
        if(userInfo != null && !userInfo.isEmpty()){
            info.put("bId",bId);
            info.put("userId",userInfo.get("user_id").toString());
            info.put("statusCd",StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserInfoInstance(userInfo);
            dataFlowContext.addParamOut("userId",userInfo.get("user_id"));
        }

        info.clear();
        info.put("bId",bId);

        List<Map> userAttrs = userServiceDaoImpl.queryUserInfoAttrs(info);

        if(userAttrs != null && userAttrs.size() >0){
            info.put("bId",bId);
            //info.put("userId",userInfo.get("user_id").toString());
            info.put("statusCd",StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserAttrInstance(info);
        }


    }

    /**
     * instance过程
     * @param business
     */
    private void doSaveInstanceUserInfo(DataFlowContext dataFlowContext,Business business) {

        JSONObject data = business.getDatas();

        //Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //Assert.jsonObjectHaveKey(data,"businessUser","datas 节点下没有包含 businessUser 节点");

        //JSONObject businessUser = data.getJSONObject("businessUser");
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);
        Map businessUser = userServiceDaoImpl.queryBusinessUserInfo(info);
        if( businessUser != null && !businessUser.isEmpty()) {
            userServiceDaoImpl.saveUserInfoInstance(businessUser);
            dataFlowContext.addParamOut("userId",businessUser.get("user_id"));
        }
        List<Map> businessUserAttrs = userServiceDaoImpl.queryBusinessUserInfoAttrs(info);
        if(businessUserAttrs != null && businessUserAttrs.size() > 0) {
            userServiceDaoImpl.saveUserAttrInstance(businessUser);
        }


    }

    /**
     * 保存数据至u_user 表中
     * @param business
     */
    private void doComplateUserInfo(DataFlowContext dataFlowContext,Business business) {
        String bId = business.getbId();
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_VALID);
        userServiceDaoImpl.updateUserInfoInstance(paramIn);
        userServiceDaoImpl.updateUserAttrInstance(paramIn);
    }

    /**
     * 处理用户信息
     * @param business
     */
    private void doSaveBusinessUserInfo(DataFlowContext dataFlowContext,Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        Assert.jsonObjectHaveKey(data,"businessUser","datas 节点下没有包含 businessUser 节点");

        JSONObject businessUser = data.getJSONObject("businessUser");

        Assert.jsonObjectHaveKey(businessUser,"userId","businessUser 节点下没有包含 userId 节点");

        if(businessUser.getInteger("userId") < 0){
            //生成userId
            String userId = GenerateCodeFactory.getUserId();
            businessUser.put("userId",userId);
        }
        dataFlowContext.addParamOut("userId",businessUser.getString("userId"));
        businessUser.put("bId",business.getbId());
        businessUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存用户信息
        userServiceDaoImpl.saveBusinessUserInfo(businessUser);

        if(businessUser.containsKey("businessUserAttr")){
            doSaveUserAttrs(business);
        }

        //userServiceDaoImpl.saveUserInfoInstance(businessUser);



    }

    private void doSaveUserAttrs(Business business){
        JSONObject data = business.getDatas();
        JSONObject businessUser = data.getJSONObject("businessUser");
        JSONArray businessUserAttrs = businessUser.getJSONArray("businessUserAttr");
        for(int userAttrIndex = 0 ; userAttrIndex < businessUserAttrs.size();userAttrIndex ++){
            JSONObject userAttr = businessUserAttrs.getJSONObject(userAttrIndex);
            Assert.jsonObjectHaveKey(userAttr,"attrId","businessUserAttr 节点下没有包含 attrId 节点");

            if(userAttr.getInteger("attrId") < 0){
                String attrId = GenerateCodeFactory.getAttrId();
                userAttr.put("attrId",attrId);
            }

            userAttr.put("bId",business.getbId());
            userAttr.put("userId",businessUser.getString("userId"));
            userAttr.put("operate", StatusConstant.OPERATE_ADD);

            userServiceDaoImpl.saveBusinessUserAttr(userAttr);
        }

        /*JSONObject attrInstance = new JSONObject();
        attrInstance.put("bId",business.getbId());
        userServiceDaoImpl.saveUserAttrInstance(attrInstance);*/
    }

    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
