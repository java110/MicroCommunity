package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.property.dao.IPropertyServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("savePropertyUserListener")
@Transactional
public class SavePropertyUserListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SavePropertyUserListener.class);

    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_PROPERTY_USER;
    }

    /**
     * 保存物业信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessProperty 节点
        if(!data.containsKey("businessPropertyUser")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"没有businessPropertyUser节点");
        }


        JSONArray businessPropertyUsers = data.getJSONArray("businessPropertyUser");
        doBusinessPropertyUser(business,businessPropertyUsers);
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //物业用户
        List<Map> businessPropertyUsers = propertyServiceDaoImpl.getBusinessPropertyUser(info);
        if(businessPropertyUsers != null && businessPropertyUsers.size() >0){
            propertyServiceDaoImpl.savePropertyUserInstance(info);
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //物业照片
        List<Map> propertyUsers = propertyServiceDaoImpl.getPropertyUser(info);
        if(propertyUsers != null && propertyUsers.size()>0){
            propertyServiceDaoImpl.updatePropertyUserInstance(paramIn);
        }
    }


    /**
     * 处理 businessProperty 节点
     * @param business 总的数据节点
     * @param businessPropertyUsers 物业用户节点
     */
    private void doBusinessPropertyUser(Business business,JSONArray businessPropertyUsers){


        for(int businessPropertyUserIndex = 0 ;businessPropertyUserIndex < businessPropertyUsers.size();businessPropertyUserIndex++) {
            JSONObject businessPropertyUser = businessPropertyUsers.getJSONObject(businessPropertyUserIndex);
            Assert.jsonObjectHaveKey(businessPropertyUser,"propertyId","businessPropertyUser 节点下没有包含 propertyId 节点");
            Assert.jsonObjectHaveKey(businessPropertyUser,"userId","businessPropertyUser 节点下没有包含 userId 节点");

            if(businessPropertyUser.getString("propertyUserId").startsWith("-")){
                String propertyUserId = GenerateCodeFactory.getPropertyUserId();
                businessPropertyUser.put("propertyUserId", propertyUserId);
            }
            businessPropertyUser.put("bId",business.getbId());
            businessPropertyUser.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            propertyServiceDaoImpl.saveBusinessPropertyUser(businessPropertyUser);
        }

    }



    /**
     * 保存物业属性信息
     * @param business 当前业务
     * @param businessPropertyAttrs 物业属性
     */
    private void doSaveBusinessPropertyAttrs(Business business,JSONArray businessPropertyAttrs){
        JSONObject data = business.getDatas();
        JSONObject businessProperty = data.getJSONObject("businessProperty");
        for(int propertyAttrIndex = 0 ; propertyAttrIndex < businessPropertyAttrs.size();propertyAttrIndex ++){
            JSONObject propertyAttr = businessPropertyAttrs.getJSONObject(propertyAttrIndex);
            Assert.jsonObjectHaveKey(propertyAttr,"attrId","businessPropertyAttr 节点下没有包含 attrId 节点");

            if(propertyAttr.getString("attrId").startsWith("-")){
                String attrId = GenerateCodeFactory.getAttrId();
                propertyAttr.put("attrId",attrId);
            }

            propertyAttr.put("bId",business.getbId());
            propertyAttr.put("propertyId",businessProperty.getString("propertyId"));
            propertyAttr.put("operate", StatusConstant.OPERATE_ADD);

            propertyServiceDaoImpl.saveBusinessPropertyAttr(propertyAttr);
        }
    }


    /**
     * 保存 物业证件 信息
     * @param business 当前业务
     * @param businessPropertyCerdentialses 物业证件
     */
    private void doBusinessPropertyCerdentials(Business business, JSONArray businessPropertyCerdentialses) {
        for(int businessPropertyCerdentialsIndex = 0 ; businessPropertyCerdentialsIndex < businessPropertyCerdentialses.size() ; businessPropertyCerdentialsIndex ++) {
            JSONObject businessPropertyCerdentials = businessPropertyCerdentialses.getJSONObject(businessPropertyCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessPropertyCerdentials, "propertyId", "businessPropertyPhoto 节点下没有包含 propertyId 节点");

            if (businessPropertyCerdentials.getString("propertyCerdentialsId").startsWith("-")) {
                String propertyPhotoId = GenerateCodeFactory.getPropertyCerdentialsId();
                businessPropertyCerdentials.put("propertyCerdentialsId", propertyPhotoId);
            }
            Date validityPeriod = null;
            try {
                if(StringUtil.isNullOrNone(businessPropertyCerdentials.getString("validityPeriod"))){
                    validityPeriod = DateUtil.getLastDate();
                }else {
                    validityPeriod = DateUtil.getDateFromString(businessPropertyCerdentials.getString("validityPeriod"), DateUtil.DATE_FORMATE_STRING_B);
                }
            } catch (ParseException e) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"传入参数 validityPeriod 格式不正确，请填写 "+DateUtil.DATE_FORMATE_STRING_B +" 格式，"+businessPropertyCerdentials);
            }
            businessPropertyCerdentials.put("validityPeriod",validityPeriod);
            businessPropertyCerdentials.put("bId", business.getbId());
            businessPropertyCerdentials.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            propertyServiceDaoImpl.saveBusinessPropertyCerdentials(businessPropertyCerdentials);
        }
    }



    /**
     * 刷新 物业ID
     * @param data
     */
    private void flushPropertyId(JSONObject data) {

        String propertyId = GenerateCodeFactory.getPropertyId();
        JSONObject businessProperty = data.getJSONObject("businessProperty");
        businessProperty.put("propertyId",propertyId);
        //刷物业属性
        if(data.containsKey("businessPropertyAttr")) {
            JSONArray businessPropertyAttrs = data.getJSONArray("businessPropertyAttr");
            for(int businessPropertyAttrIndex = 0;businessPropertyAttrIndex < businessPropertyAttrs.size();businessPropertyAttrIndex++) {
                JSONObject businessPropertyAttr = businessPropertyAttrs.getJSONObject(businessPropertyAttrIndex);
                businessPropertyAttr.put("propertyId", propertyId);
            }
        }
        //刷 是物业照片 的 propertyId
        if(data.containsKey("businessPropertyPhoto")) {
            JSONArray businessPropertyPhotos = data.getJSONArray("businessPropertyPhoto");
            for(int businessPropertyPhotoIndex = 0;businessPropertyPhotoIndex < businessPropertyPhotos.size();businessPropertyPhotoIndex++) {
                JSONObject businessPropertyPhoto = businessPropertyPhotos.getJSONObject(businessPropertyPhotoIndex);
                businessPropertyPhoto.put("propertyId", propertyId);
            }
        }
        //刷 物业证件 的propertyId
        if(data.containsKey("businessPropertyCerdentials")) {
            JSONArray businessPropertyCerdentialses = data.getJSONArray("businessPropertyCerdentials");
            for(int businessPropertyCerdentialsIndex = 0;businessPropertyCerdentialsIndex < businessPropertyCerdentialses.size();businessPropertyCerdentialsIndex++) {
                JSONObject businessPropertyCerdentials = businessPropertyCerdentialses.getJSONObject(businessPropertyCerdentialsIndex);
                businessPropertyCerdentials.put("propertyId", propertyId);
            }
        }
    }


    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }
}
