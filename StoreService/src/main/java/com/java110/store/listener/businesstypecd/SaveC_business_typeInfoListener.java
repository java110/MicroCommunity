package com.java110.store.listener.businesstypecd;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.store.dao.IC_business_typeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 BusinessType信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveC_business_typeInfoListener")
@Transactional
public class SaveC_business_typeInfoListener extends AbstractC_business_typeBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveC_business_typeInfoListener.class);

    @Autowired
    private IC_business_typeServiceDao c_business_typeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO;
    }

    /**
     * 保存BusinessType信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessC_business_type 节点
        if(data.containsKey("businessC_business_type")){
            Object bObj = data.get("businessC_business_type");
            JSONArray businessC_business_types = null;
            if(bObj instanceof JSONObject){
                businessC_business_types = new JSONArray();
                businessC_business_types.add(bObj);
            }else {
                businessC_business_types = (JSONArray)bObj;
            }
            //JSONObject businessC_business_type = data.getJSONObject("businessC_business_type");
            for (int bC_business_typeIndex = 0; bC_business_typeIndex < businessC_business_types.size();bC_business_typeIndex++) {
                JSONObject businessC_business_type = businessC_business_types.getJSONObject(bC_business_typeIndex);
                doBusinessC_business_type(business, businessC_business_type);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("id", businessC_business_type.getString("id"));
                }
            }
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

        //BusinessType信息
        List<Map> businessC_business_typeInfo = c_business_typeServiceDaoImpl.getBusinessC_business_typeInfo(info);
        if( businessC_business_typeInfo != null && businessC_business_typeInfo.size() >0) {
            reFreshShareColumn(info, businessC_business_typeInfo.get(0));
            c_business_typeServiceDaoImpl.saveC_business_typeInfoInstance(info);
            if(businessC_business_typeInfo.size() == 1) {
                dataFlowContext.addParamOut("id", businessC_business_typeInfo.get(0).get("id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("id")) {
            return;
        }

        if (!businessInfo.containsKey("id")) {
            return;
        }

        info.put("id", businessInfo.get("id"));
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
        //BusinessType信息
        List<Map> c_business_typeInfo = c_business_typeServiceDaoImpl.getC_business_typeInfo(info);
        if(c_business_typeInfo != null && c_business_typeInfo.size() > 0){
            reFreshShareColumn(paramIn, c_business_typeInfo.get(0));
            c_business_typeServiceDaoImpl.updateC_business_typeInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessC_business_type 节点
     * @param business 总的数据节点
     * @param businessC_business_type BusinessType节点
     */
    private void doBusinessC_business_type(Business business,JSONObject businessC_business_type){

        Assert.jsonObjectHaveKey(businessC_business_type,"id","businessC_business_type 节点下没有包含 id 节点");

        if(businessC_business_type.getString("id").startsWith("-")){
            //刷新缓存
            //flushC_business_typeId(business.getDatas());

            businessC_business_type.put("id",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_id));

        }

        businessC_business_type.put("bId",business.getbId());
        businessC_business_type.put("operate", StatusConstant.OPERATE_ADD);
        //保存BusinessType信息
        c_business_typeServiceDaoImpl.saveBusinessC_business_typeInfo(businessC_business_type);

    }

    public IC_business_typeServiceDao getC_business_typeServiceDaoImpl() {
        return c_business_typeServiceDaoImpl;
    }

    public void setC_business_typeServiceDaoImpl(IC_business_typeServiceDao c_business_typeServiceDaoImpl) {
        this.c_business_typeServiceDaoImpl = c_business_typeServiceDaoImpl;
    }
}
