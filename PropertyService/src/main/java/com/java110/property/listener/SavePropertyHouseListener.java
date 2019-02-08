package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
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
@Java110Listener("savePropertyHouseListener")
@Transactional
public class SavePropertyHouseListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SavePropertyHouseListener.class);

    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PROPERTY_HOUSE;
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
        if(data.containsKey("businessPropertyHouse")){
            JSONObject businessProperty = data.getJSONObject("businessPropertyHouse");
            doBusinessPropertyHouse(business,businessProperty);
            dataFlowContext.addParamOut("houseId",businessProperty.getString("houseId"));
        }

        if(data.containsKey("businessPropertyHouseAttr")){
            JSONArray businessPropertyHouseAttrs = data.getJSONArray("businessPropertyHouseAttr");
            doSaveBusinessPropertyHouseAttrs(business,businessPropertyHouseAttrs);
        }
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

        //物业信息
        Map businessPropertyHouse = propertyServiceDaoImpl.getBusinessPropertyHouse(info);
        if( businessPropertyHouse != null && !businessPropertyHouse.isEmpty()) {
            propertyServiceDaoImpl.savePropertyHouseInstance(info);
            dataFlowContext.addParamOut("houseId",businessPropertyHouse.get("house_id"));
        }
        //物业属性
        List<Map> businessPropertyHouseAttrs = propertyServiceDaoImpl.getBusinessPropertyHouseAttrs(info);
        if(businessPropertyHouseAttrs != null && businessPropertyHouseAttrs.size() > 0) {
            propertyServiceDaoImpl.savePropertyHouseAttrsInstance(info);
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
        //物业信息
        Map propertyInfo = propertyServiceDaoImpl.getPropertyHouse(info);
        if(propertyInfo != null && !propertyInfo.isEmpty()){
            paramIn.put("houseId",propertyInfo.get("house_id").toString());
            propertyServiceDaoImpl.updatePropertyHouseInstance(paramIn);
            dataFlowContext.addParamOut("houseId",propertyInfo.get("house_id"));
        }

        //物业属性
        List<Map> propertyAttrs = propertyServiceDaoImpl.getPropertyHouseAttrs(info);
        if(propertyAttrs != null && propertyAttrs.size()>0){
            propertyServiceDaoImpl.updatePropertyHouseAttrInstance(paramIn);
        }
    }

    /**
     * 处理 businessProperty 节点
     * @param business 总的数据节点
     * @param businessProperty 物业节点
     */
    private void doBusinessPropertyHouse(Business business,JSONObject businessProperty){

        Assert.jsonObjectHaveKey(businessProperty,"houseId","businessPropertyHouse 节点下没有包含 houseId 节点");
        Assert.jsonObjectHaveKey(businessProperty,"propertyId","businessPropertyHouse 节点下没有包含 propertyId 节点");

        if(businessProperty.getString("propertyId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"businessPropertyHouse 节点下 propertyId 必须为已有物业ID 不能以 - 开头");
        }
        if(businessProperty.getString("houseId").startsWith("-")){
            //刷新缓存
            flushHouseId(business.getDatas());
        }

        businessProperty.put("bId",business.getbId());
        businessProperty.put("operate", StatusConstant.OPERATE_ADD);
        //保存物业信息
        propertyServiceDaoImpl.saveBusinessPropertyHouse(businessProperty);

    }



    /**
     * 保存物业属性信息
     * @param business 当前业务
     * @param businessPropertyHouseAttrs 物业属性
     */
    private void doSaveBusinessPropertyHouseAttrs(Business business,JSONArray businessPropertyHouseAttrs){
        JSONObject data = business.getDatas();
        for(int propertyAttrIndex = 0 ; propertyAttrIndex < businessPropertyHouseAttrs.size();propertyAttrIndex ++){
            JSONObject propertyAttr = businessPropertyHouseAttrs.getJSONObject(propertyAttrIndex);
            Assert.jsonObjectHaveKey(propertyAttr,"attrId","businessPropertyHouseAttr 节点下没有包含 attrId 节点");

            if(propertyAttr.getString("attrId").startsWith("-")){
                String attrId = GenerateCodeFactory.getAttrId();
                propertyAttr.put("attrId",attrId);
            }

            propertyAttr.put("bId",business.getbId());
            propertyAttr.put("operate", StatusConstant.OPERATE_ADD);

            propertyServiceDaoImpl.saveBusinessPropertyHouseAttr(propertyAttr);
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
     * 刷新 商户ID
     * @param data
     */
    private void flushHouseId(JSONObject data) {

        String houseId = GenerateCodeFactory.getHouseId();
        JSONObject businessProperty = data.getJSONObject("businessPropertyHouse");
        businessProperty.put("houseId",houseId);
        //刷物业属性
        if(data.containsKey("businessPropertyHouseAttr")) {
            JSONArray businessPropertyHouseAttrs = data.getJSONArray("businessPropertyHouseAttr");
            for(int businessPropertyHouseAttrIndex = 0;businessPropertyHouseAttrIndex < businessPropertyHouseAttrs.size();businessPropertyHouseAttrIndex++) {
                JSONObject businessPropertyHouseAttr = businessPropertyHouseAttrs.getJSONObject(businessPropertyHouseAttrIndex);
                businessPropertyHouseAttr.put("houseId", houseId);
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
