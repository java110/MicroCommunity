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
@Java110Listener("savePropertyCerdentialsListener")
@Transactional
public class SavePropertyCerdentialsListener extends AbstractPropertyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SavePropertyCerdentialsListener.class);

    @Autowired
    IPropertyServiceDao propertyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_PROPERTY_CERDENTIALS;
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

        if(!data.containsKey("businessPropertyCerdentials")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"没有businessPropertyCerdentials节点");
        }
        JSONArray businessPropertyCerdentialses = data.getJSONArray("businessPropertyCerdentials");
        doBusinessPropertyCerdentials(business,businessPropertyCerdentialses);
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

        //物业证件
        List<Map> businessPropertyCerdentialses = propertyServiceDaoImpl.getBusinessPropertyCerdentials(info);
        if(businessPropertyCerdentialses != null && businessPropertyCerdentialses.size()>0){
            propertyServiceDaoImpl.savePropertyCerdentialsInstance(info);
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
        //物业属性
        List<Map> propertyCerdentialses = propertyServiceDaoImpl.getPropertyCerdentials(info);
        if(propertyCerdentialses != null && propertyCerdentialses.size()>0){
            propertyServiceDaoImpl.updatePropertyCerdentailsInstance(paramIn);
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
                String propertyPropertyId = GenerateCodeFactory.getPropertyCerdentialsId();
                businessPropertyCerdentials.put("propertyCerdentialsId", propertyPropertyId);
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


    public IPropertyServiceDao getPropertyServiceDaoImpl() {
        return propertyServiceDaoImpl;
    }

    public void setPropertyServiceDaoImpl(IPropertyServiceDao propertyServiceDaoImpl) {
        this.propertyServiceDaoImpl = propertyServiceDaoImpl;
    }
}
