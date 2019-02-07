package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.property.dao.IPropertyServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改物业信息 侦听
 *
 * 处理节点
 * 1、businessPropertyHouse:{} 物业基本信息节点
 * 2、businessPropertyHouseAttr:[{}] 物业属性信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updatePropertyHouseHouseListener")
@Transactional
public class UpdatePropertyHouseListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdatePropertyHouseListener.class);
    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_PROPERTY_HOUSE;
    }

    /**
     * business过程
     * @param dataFlowContext
     * @param business
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessPropertyHouse 节点
        if(data.containsKey("businessPropertyHouse")){
            JSONObject businessPropertyHouse = data.getJSONObject("businessPropertyHouse");
            doBusinessPropertyHouse(business,businessPropertyHouse);
            dataFlowContext.addParamOut("propertyId",businessPropertyHouse.getString("propertyId"));
        }

        if(data.containsKey("businessPropertyHouseAttr")){
            JSONArray businessPropertyHouseAttrs = data.getJSONArray("businessPropertyHouseAttr");
            doSaveBusinessPropertyHouseAttrs(business,businessPropertyHouseAttrs);
        }
    }


    /**
     * business to instance 过程
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
        Map businessPropertyHouseInfo = propertyServiceDaoImpl.getBusinessPropertyHouse(info);
        if( businessPropertyHouseInfo != null && !businessPropertyHouseInfo.isEmpty()) {
            flushBusinessPropertyHouse(businessPropertyHouseInfo,StatusConstant.STATUS_CD_VALID);
            propertyServiceDaoImpl.updatePropertyHouseInstance(businessPropertyHouseInfo);
            dataFlowContext.addParamOut("houseId",businessPropertyHouseInfo.get("house_id"));
        }
        //物业属性
        List<Map> businessPropertyHouseAttrs = propertyServiceDaoImpl.getBusinessPropertyHouseAttrs(info);
        if(businessPropertyHouseAttrs != null && businessPropertyHouseAttrs.size() > 0) {
            for(Map businessPropertyHouseAttr : businessPropertyHouseAttrs) {
                flushBusinessPropertyHouseAttr(businessPropertyHouseAttr,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyHouseAttrInstance(businessPropertyHouseAttr);
            }
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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //物业信息
        Map propertyInfo = propertyServiceDaoImpl.getPropertyHouse(info);
        if(propertyInfo != null && !propertyInfo.isEmpty()){

            //物业信息
            Map businessPropertyHouseInfo = propertyServiceDaoImpl.getBusinessPropertyHouse(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyHouseInfo == null || businessPropertyHouseInfo.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（property），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessPropertyHouse(businessPropertyHouseInfo,StatusConstant.STATUS_CD_VALID);
            propertyServiceDaoImpl.updatePropertyHouseInstance(businessPropertyHouseInfo);
            dataFlowContext.addParamOut("propertyId",propertyInfo.get("property_id"));
        }

        //物业属性
        List<Map> propertyAttrs = propertyServiceDaoImpl.getPropertyHouseAttrs(info);
        if(propertyAttrs != null && propertyAttrs.size()>0){

            List<Map> businessPropertyHouseAttrs = propertyServiceDaoImpl.getBusinessPropertyHouseAttrs(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyHouseAttrs == null || businessPropertyHouseAttrs.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(property_attr)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessPropertyHouseAttr : businessPropertyHouseAttrs) {
                flushBusinessPropertyHouseAttr(businessPropertyHouseAttr,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyHouseAttrInstance(businessPropertyHouseAttr);
            }
        }
    }


    /**
     * 处理 businessPropertyHouse 节点
     * @param business 总的数据节点
     * @param businessPropertyHouse 物业节点
     */
    private void doBusinessPropertyHouse(Business business,JSONObject businessPropertyHouse){

        Assert.jsonObjectHaveKey(businessPropertyHouse,"propertyId","businessPropertyHouse 节点下没有包含 propertyId 节点");
        Assert.jsonObjectHaveKey(businessPropertyHouse,"houseId","businessPropertyHouse 节点下没有包含 houseId 节点");

        if(businessPropertyHouse.getString("propertyId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"propertyId 错误，不能自动生成（必须已经存在的propertyId）"+businessPropertyHouse);
        }

        if(businessPropertyHouse.getString("houseId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"houseId 错误，不能自动生成（必须已经存在的houseId）"+businessPropertyHouse);
        }
        //自动保存DEL
        autoSaveDelBusinessPropertyHouse(business,businessPropertyHouse);

        businessPropertyHouse.put("bId",business.getbId());
        businessPropertyHouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存物业信息
        propertyServiceDaoImpl.saveBusinessPropertyHouse(businessPropertyHouse);

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
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+propertyAttr);
            }
            //自动保存DEL数据
            autoSaveDelBusinessPropertyHouseAttr(business,propertyAttr);

            propertyAttr.put("bId",business.getbId());
            propertyAttr.put("propertyId",propertyAttr.getString("propertyId"));
            propertyAttr.put("operate", StatusConstant.OPERATE_ADD);

            propertyServiceDaoImpl.saveBusinessPropertyHouseAttr(propertyAttr);
        }
    }
    

    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }



}
