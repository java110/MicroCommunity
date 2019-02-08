package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
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
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("savePropertyPhotoListener")
@Transactional
public class SavePropertyPhotoListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SavePropertyPhotoListener.class);

    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PROPERTY_PHOTO;
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

        if(!data.containsKey("businessPropertyPhoto")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"没有businessPropertyPhoto节点");
        }

        JSONArray businessPropertyPhotos = data.getJSONArray("businessPropertyPhoto");
        doBusinessPropertyPhoto(business,businessPropertyPhotos);
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

        //物业照片
        List<Map> businessPropertyPhotos = propertyServiceDaoImpl.getBusinessPropertyPhoto(info);
        if(businessPropertyPhotos != null && businessPropertyPhotos.size() >0){
            propertyServiceDaoImpl.savePropertyPhotoInstance(info);
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
        List<Map> propertyPhotos = propertyServiceDaoImpl.getPropertyPhoto(info);
        if(propertyPhotos != null && propertyPhotos.size()>0){
            propertyServiceDaoImpl.updatePropertyPhotoInstance(paramIn);
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
                String propertyPhotoId = GenerateCodeFactory.getPropertyPhotoId();
                businessPropertyPhoto.put("propertyPhotoId", propertyPhotoId);
            }
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
