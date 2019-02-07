package com.java110.property.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
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
@Java110Listener("savePropertyFeeListener")
@Transactional
public class SavePropertyFeeListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SavePropertyFeeListener.class);

    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_PROPERTY_FEE;
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

        //物业费用节点处理
        if(data.containsKey("businessPropertyFee")){
            JSONArray businessPropertyFees = data.getJSONArray("businessPropertyFee");
            doBusinessPropertyFee(business,businessPropertyFees);
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


        //物业费用
        List<Map> businessPropertyFees = propertyServiceDaoImpl.getBusinessPropertyFee(info);
        if(businessPropertyFees != null && businessPropertyFees.size() >0){
            propertyServiceDaoImpl.savePropertyFeeInstance(info);
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
        
        //物业费用
        List<Map> propertyFees = propertyServiceDaoImpl.getPropertyFee(info);
        if(propertyFees != null && propertyFees.size()>0){
            propertyServiceDaoImpl.updatePropertyFeeInstance(paramIn);
        }
    }

    /**
     * 保存物业费用
     * @param business 业务对象
     * @param businessPropertyFees 物业费用
     */
    private void doBusinessPropertyFee(Business business, JSONArray businessPropertyFees) {

        for(int businessPropertyFeeIndex = 0 ;businessPropertyFeeIndex < businessPropertyFees.size();businessPropertyFeeIndex++) {
            JSONObject businessPropertyFee = businessPropertyFees.getJSONObject(businessPropertyFeeIndex);
            Assert.jsonObjectHaveKey(businessPropertyFee, "propertyId", "businessPropertyFee 节点下没有包含 propertyId 节点");

            if (businessPropertyFee.getString("feeId").startsWith("-")) {
                String propertyFeeId = GenerateCodeFactory.getPropertyFeeId();
                businessPropertyFee.put("feeId", propertyFeeId);
            }
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
