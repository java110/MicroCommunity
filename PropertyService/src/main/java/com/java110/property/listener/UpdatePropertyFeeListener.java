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
 * 3、businessPropertyFee:[{}] 物业照片信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updatePropertyFeeListener")
@Transactional
public class UpdatePropertyFeeListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdatePropertyFeeListener.class);
    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_PROPERTY_FEE;
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


        if(data.containsKey("businessPropertyFee")){
            JSONArray businessPropertyFees = data.getJSONArray("businessPropertyFee");
            doBusinessPropertyFee(business,businessPropertyFees);
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
        List<Map> businessPropertyFees = propertyServiceDaoImpl.getBusinessPropertyFee(info);
        if(businessPropertyFees != null && businessPropertyFees.size() >0){
            for(Map businessPropertyFee : businessPropertyFees) {
                flushBusinessPropertyFee(businessPropertyFee,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyFeeInstance(businessPropertyFee);
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
        List<Map> propertyFees = propertyServiceDaoImpl.getPropertyFee(info);
        if(propertyFees != null && propertyFees.size()>0){
            List<Map> businessPropertyFees = propertyServiceDaoImpl.getBusinessPropertyFee(delInfo);
            //除非程序出错了，这里不会为空
            if(businessPropertyFees == null || businessPropertyFees.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(property_fee)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessPropertyFee : businessPropertyFees) {
                flushBusinessPropertyFee(businessPropertyFee,StatusConstant.STATUS_CD_VALID);
                propertyServiceDaoImpl.updatePropertyFeeInstance(businessPropertyFee);
            }
        }
    }

    /**
     * 保存物业照片
     * @param business 业务对象
     * @param businessPropertyFees 物业照片
     */
    private void doBusinessPropertyFee(Business business, JSONArray businessPropertyFees) {


        for(int businessPropertyFeeIndex = 0 ;businessPropertyFeeIndex < businessPropertyFees.size();businessPropertyFeeIndex++) {
            JSONObject businessPropertyFee = businessPropertyFees.getJSONObject(businessPropertyFeeIndex);
            Assert.jsonObjectHaveKey(businessPropertyFee, "propertyId", "businessPropertyFee 节点下没有包含 propertyId 节点");

            if (businessPropertyFee.getString("feeId").startsWith("-")) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"feeId 错误，不能自动生成（必须已经存在的feeId）"+businessPropertyFee);
            }

            //自动保存DEL信息
            autoSaveDelBusinessPropertyFee(business,businessPropertyFee);

            businessPropertyFee.put("bId", business.getbId());
            businessPropertyFee.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            propertyServiceDaoImpl.saveBusinessPropertyFee(businessPropertyFee);
        }
    }

    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }



}
