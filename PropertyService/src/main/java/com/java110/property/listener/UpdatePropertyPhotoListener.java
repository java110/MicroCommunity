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
 * 1、businessPropertyPhoto:{} 物业照片节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updatePropertyPhotoListener")
@Transactional
public class UpdatePropertyPhotoListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdatePropertyPhotoListener.class);
    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_PROPERTY_PHOTO;
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

        if(data.containsKey("businessPropertyPhoto")){
            JSONArray businessPropertyPhotos = data.getJSONArray("businessPropertyPhoto");
            doBusinessPropertyPhoto(business,businessPropertyPhotos);
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

        //物业照片
        List<Map> businessPropertyPhotos = propertyServiceDaoImpl.getBusinessPropertyPhoto(info);
        if(businessPropertyPhotos != null && businessPropertyPhotos.size() >0){
            for(Map businessPropertyPhoto : businessPropertyPhotos) {
                flushBusinessPropertyPhoto(businessPropertyPhoto,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyPhotoInstance(businessPropertyPhoto);
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

            //自动保存DEL信息
            autoSaveDelBusinessPropertyPhoto(business,businessPropertyPhoto);

            businessPropertyPhoto.put("bId", business.getbId());
            businessPropertyPhoto.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            propertyServiceDaoImpl.saveBusinessPropertyPhoto(businessPropertyPhoto);
        }
    }


    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }



}
