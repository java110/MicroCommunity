package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.property.dao.IPropertyServiceDao;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除物业信息 侦听
 *
 * 处理节点
 * 1、businessProperty:{} 物业基本信息节点
 * 2、businessPropertyAttr:[{}] 物业属性信息节点
 * 3、businessPropertyPhoto:[{}] 物业照片信息节点
 * 4、businessPropertyCerdentials:[{}] 物业证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deletePropertyInfoListener")
@Transactional
public class DeletePropertyInfoListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeletePropertyInfoListener.class);
    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DELETE_STORE_INFO;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessProperty 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if(data.containsKey("businessProperty")){
            JSONObject businessProperty = data.getJSONObject("businessProperty");
            doBusinessProperty(business,businessProperty);
            dataFlowContext.addParamOut("propertyId",businessProperty.getString("propertyId"));
        }

        if(data.containsKey("businessPropertyAttr")){
            JSONArray businessPropertyAttrs = data.getJSONArray("businessPropertyAttr");
            doSaveBusinessPropertyAttrs(business,businessPropertyAttrs);
        }

        if(data.containsKey("businessPropertyPhoto")){
            JSONArray businessPropertyPhotos = data.getJSONArray("businessPropertyPhoto");
            doBusinessPropertyPhoto(business,businessPropertyPhotos);
        }

        if(data.containsKey("businessPropertyCerdentials")){
            JSONArray businessPropertyCerdentialses = data.getJSONArray("businessPropertyCerdentials");
            doBusinessPropertyCerdentials(business,businessPropertyCerdentialses);
        }
    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //物业信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //物业信息
        Map businessPropertyInfo = propertyServiceDaoImpl.getBusinessPropertyInfo(info);
        if( businessPropertyInfo != null && !businessPropertyInfo.isEmpty()) {
            flushBusinessPropertyInfo(businessPropertyInfo,StatusConstant.STATUS_CD_INVALID);
            propertyServiceDaoImpl.updatePropertyInfoInstance(businessPropertyInfo);
            dataFlowContext.addParamOut("propertyId",businessPropertyInfo.get("property_id"));
        }
        //物业属性
        List<Map> businessPropertyAttrs = propertyServiceDaoImpl.getBusinessPropertyAttrs(info);
        if(businessPropertyAttrs != null && businessPropertyAttrs.size() > 0) {
            for(Map businessPropertyAttr : businessPropertyAttrs) {
                flushBusinessPropertyAttr(businessPropertyAttr,StatusConstant.STATUS_CD_INVALID);
                propertyServiceDaoImpl.updatePropertyAttrInstance(businessPropertyAttr);
            }
        }
        //物业照片
        List<Map> businessPropertyPhotos = propertyServiceDaoImpl.getBusinessPropertyPhoto(info);
        if(businessPropertyPhotos != null && businessPropertyPhotos.size() >0){
            for(Map businessPropertyPhoto : businessPropertyPhotos) {
                flushBusinessPropertyPhoto(businessPropertyPhoto,StatusConstant.STATUS_CD_INVALID);
                propertyServiceDaoImpl.updatePropertyPhotoInstance(businessPropertyPhoto);
            }
        }
        //物业证件
        List<Map> businessPropertyCerdentialses = propertyServiceDaoImpl.getBusinessPropertyCerdentials(info);
        if(businessPropertyCerdentialses != null && businessPropertyCerdentialses.size()>0){
            for(Map businessPropertyCerdentials : businessPropertyCerdentialses) {
                flushBusinessPropertyCredentials(businessPropertyCerdentials,StatusConstant.STATUS_CD_INVALID);
                propertyServiceDaoImpl.updatePropertyCerdentailsInstance(businessPropertyCerdentials);
            }
        }
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //物业信息
        Map propertyInfo = propertyServiceDaoImpl.getPropertyInfo(info);
        if(propertyInfo != null && !propertyInfo.isEmpty()){

            //物业信息
            Map businessPropertyInfo = propertyServiceDaoImpl.getBusinessPropertyInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyInfo == null || businessPropertyInfo.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（property），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessPropertyInfo(businessPropertyInfo,StatusConstant.STATUS_CD_VALID);
            propertyServiceDaoImpl.updatePropertyInfoInstance(businessPropertyInfo);
            dataFlowContext.addParamOut("propertyId",propertyInfo.get("property_id"));
        }

        //物业属性
        List<Map> propertyAttrs = propertyServiceDaoImpl.getPropertyAttrs(info);
        if(propertyAttrs != null && propertyAttrs.size()>0){

            List<Map> businessPropertyAttrs = propertyServiceDaoImpl.getBusinessPropertyAttrs(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyAttrs == null || businessPropertyAttrs.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(property_attr)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessPropertyAttr : businessPropertyAttrs) {
                flushBusinessPropertyAttr(businessPropertyAttr,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyAttrInstance(businessPropertyAttr);
            }
        }

        //物业照片
        List<Map> propertyPhotos = propertyServiceDaoImpl.getPropertyPhoto(info);
        if(propertyPhotos != null && propertyPhotos.size()>0){
            List<Map> businessPropertyPhotos = propertyServiceDaoImpl.getBusinessPropertyPhoto(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyPhotos == null || businessPropertyPhotos.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(property_photo)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessPropertyPhoto : businessPropertyPhotos) {
                flushBusinessPropertyPhoto(businessPropertyPhoto,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyPhotoInstance(businessPropertyPhoto);
            }
        }

        //物业属性
        List<Map> propertyCerdentialses = propertyServiceDaoImpl.getPropertyCerdentials(info);
        if(propertyCerdentialses != null && propertyCerdentialses.size()>0){
            List<Map> businessPropertyCerdentialses = propertyServiceDaoImpl.getBusinessPropertyCerdentials(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyCerdentialses == null || businessPropertyCerdentialses.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(property_cerdentials)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessPropertyCerdentials : businessPropertyCerdentialses) {
                flushBusinessPropertyCredentials(businessPropertyCerdentials,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyCerdentailsInstance(businessPropertyCerdentials);
            }
        }
    }

    /**
     * 保存物业照片
     * @param business 业务对象
     * @param businessPropertyPhotos 物业照片
     */
    private void doBusinessPropertyPhoto(Business business, JSONArray businessPropertyPhotos) {

        for(int businessPropertyPhotoIndex = 0 ;businessPropertyPhotoIndex < businessPropertyPhotos.size();businessPropertyPhotoIndex++) {
            JSONObject businessPropertyPhoto = businessPropertyPhotos.getJSONObject(businessPropertyPhotoIndex);
            Assert.jsonObjectHaveKey(businessPropertyPhoto, "propertyId", "businessPropertyPhoto 节点下没有包含 propertyId 节点");

            if (businessPropertyPhoto.getString("propertyPhotoId").startsWith("-")) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"propertyPhotoId 错误，不能自动生成（必须已经存在的propertyPhotoId）"+businessPropertyPhoto);
            }

            autoSaveDelBusinessPropertyPhoto(business,businessPropertyPhoto);
        }
    }

    /**
     * 处理 businessProperty 节点
     * @param business 总的数据节点
     * @param businessProperty 物业节点
     */
    private void doBusinessProperty(Business business,JSONObject businessProperty){

        Assert.jsonObjectHaveKey(businessProperty,"propertyId","businessProperty 节点下没有包含 propertyId 节点");

        if(businessProperty.getString("propertyId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"propertyId 错误，不能自动生成（必须已经存在的propertyId）"+businessProperty);
        }
        //自动插入DEL
        autoSaveDelBusinessProperty(business,businessProperty);
    }



    /**
     * 保存物业属性信息
     * @param business 当前业务
     * @param businessPropertyAttrs 物业属性
     */
    private void doSaveBusinessPropertyAttrs(Business business,JSONArray businessPropertyAttrs){
        JSONObject data = business.getDatas();

        for(int propertyAttrIndex = 0 ; propertyAttrIndex < businessPropertyAttrs.size();propertyAttrIndex ++){
            JSONObject propertyAttr = businessPropertyAttrs.getJSONObject(propertyAttrIndex);
            Assert.jsonObjectHaveKey(propertyAttr,"attrId","businessPropertyAttr 节点下没有包含 attrId 节点");
            if(propertyAttr.getString("attrId").startsWith("-")){
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+propertyAttr);
            }

            autoSaveDelBusinessPropertyAttr(business,propertyAttr);
        }
    }


    /**
     * 保存 物业证件 信息
     * @param business 当前业务
     * @param businessPropertyCerdentialses 物业证件
     */
    private void doBusinessPropertyCerdentials(Business business, JSONArray businessPropertyCerdentialses) {

        Map info = null;
        Map currentPropertyCerdentials = null;
        for(int businessPropertyCerdentialsIndex = 0 ; businessPropertyCerdentialsIndex < businessPropertyCerdentialses.size() ; businessPropertyCerdentialsIndex ++) {
            JSONObject businessPropertyCerdentials = businessPropertyCerdentialses.getJSONObject(businessPropertyCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessPropertyCerdentials, "propertyId", "businessPropertyPhoto 节点下没有包含 propertyId 节点");

            if (businessPropertyCerdentials.getString("propertyCerdentialsId").startsWith("-")) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"propertyPhotoId 错误，不能自动生成（必须已经存在的propertyPhotoId）"+businessPropertyCerdentials);
            }

            autoSaveDelBusinessPropertyCerdentials(business,businessPropertyCerdentials);
        }
    }

    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }
}
