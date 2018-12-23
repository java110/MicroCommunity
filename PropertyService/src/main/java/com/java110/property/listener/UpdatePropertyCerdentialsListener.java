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
 * 4、businessPropertyCerdentials:[{}] 物业证件信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updatePropertyCerdentialsListener")
@Transactional
public class UpdatePropertyCerdentialsListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdatePropertyCerdentialsListener.class);
    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_PROPERTY_CERDENTIALS;
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

        if(data.containsKey("businessPropertyCerdentials")){
            JSONArray businessPropertyCerdentialses = data.getJSONArray("businessPropertyCerdentials");
            doBusinessPropertyCerdentials(business,businessPropertyCerdentialses);
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
        //物业证件
        List<Map> businessPropertyCerdentialses = propertyServiceDaoImpl.getBusinessPropertyCerdentials(info);
        if(businessPropertyCerdentialses != null && businessPropertyCerdentialses.size()>0){
            for(Map businessPropertyCerdentials : businessPropertyCerdentialses) {
                flushBusinessPropertyCredentials(businessPropertyCerdentials,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyCerdentailsInstance(businessPropertyCerdentials);
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
     * 保存 物业证件 信息
     * @param business 当前业务
     * @param businessPropertyCerdentialses 物业证件
     */
    private void doBusinessPropertyCerdentials(Business business, JSONArray businessPropertyCerdentialses) {

        for(int businessPropertyCerdentialsIndex = 0 ; businessPropertyCerdentialsIndex < businessPropertyCerdentialses.size() ; businessPropertyCerdentialsIndex ++) {
            JSONObject businessPropertyCerdentials = businessPropertyCerdentialses.getJSONObject(businessPropertyCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessPropertyCerdentials, "propertyId", "businessPropertyPhoto 节点下没有包含 propertyId 节点");

            if (businessPropertyCerdentials.getString("propertyCerdentialsId").startsWith("-")) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"propertyPhotoId 错误，不能自动生成（必须已经存在的propertyCerdentialsId）"+businessPropertyCerdentials);
            }

            autoSaveDelBusinessPropertyCerdentials(business,businessPropertyCerdentials);

            businessPropertyCerdentials.put("bId", business.getbId());
            businessPropertyCerdentials.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            propertyServiceDaoImpl.saveBusinessPropertyCerdentials(businessPropertyCerdentials);
        }
    }

    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }



}
