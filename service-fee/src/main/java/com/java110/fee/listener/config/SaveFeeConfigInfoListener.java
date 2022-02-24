package com.java110.fee.listener.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.fee.dao.IFeeConfigServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 费用配置信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFeeConfigInfoListener")

public class SaveFeeConfigInfoListener extends AbstractFeeConfigBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveFeeConfigInfoListener.class);

    @Autowired
    private IFeeConfigServiceDao feeConfigServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG;
    }

    /**
     * 保存费用配置信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessFeeConfig 节点
        if(data.containsKey(PayFeeConfigPo.class.getSimpleName())){
            Object bObj = data.get(PayFeeConfigPo.class.getSimpleName());
            JSONArray businessFeeConfigs = null;
            if(bObj instanceof JSONObject){
                businessFeeConfigs = new JSONArray();
                businessFeeConfigs.add(bObj);
            }else {
                businessFeeConfigs = (JSONArray)bObj;
            }
            //JSONObject businessFeeConfig = data.getJSONObject("businessFeeConfig");
            for (int bFeeConfigIndex = 0; bFeeConfigIndex < businessFeeConfigs.size();bFeeConfigIndex++) {
                JSONObject businessFeeConfig = businessFeeConfigs.getJSONObject(bFeeConfigIndex);
                doBusinessFeeConfig(business, businessFeeConfig);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("configId", businessFeeConfig.getString("configId"));
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

        //费用配置信息
        List<Map> businessFeeConfigInfo = feeConfigServiceDaoImpl.getBusinessFeeConfigInfo(info);
        if( businessFeeConfigInfo != null && businessFeeConfigInfo.size() >0) {
            reFreshShareColumn(info, businessFeeConfigInfo.get(0));
            feeConfigServiceDaoImpl.saveFeeConfigInfoInstance(info);
            if(businessFeeConfigInfo.size() == 1) {
                dataFlowContext.addParamOut("configId", businessFeeConfigInfo.get(0).get("config_id"));
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

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
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
        //费用配置信息
        List<Map> feeConfigInfo = feeConfigServiceDaoImpl.getFeeConfigInfo(info);
        if(feeConfigInfo != null && feeConfigInfo.size() > 0){
            reFreshShareColumn(paramIn, feeConfigInfo.get(0));
            feeConfigServiceDaoImpl.updateFeeConfigInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessFeeConfig 节点
     * @param business 总的数据节点
     * @param businessFeeConfig 费用配置节点
     */
    private void doBusinessFeeConfig(Business business,JSONObject businessFeeConfig){

        Assert.jsonObjectHaveKey(businessFeeConfig,"configId","businessFeeConfig 节点下没有包含 configId 节点");

        if(businessFeeConfig.getString("configId").startsWith("-")){
            //刷新缓存
            //flushFeeConfigId(business.getDatas());

            businessFeeConfig.put("configId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));

        }

        businessFeeConfig.put("bId",business.getbId());
        businessFeeConfig.put("operate", StatusConstant.OPERATE_ADD);
        //保存费用配置信息
        feeConfigServiceDaoImpl.saveBusinessFeeConfigInfo(businessFeeConfig);

    }

    public IFeeConfigServiceDao getFeeConfigServiceDaoImpl() {
        return feeConfigServiceDaoImpl;
    }

    public void setFeeConfigServiceDaoImpl(IFeeConfigServiceDao feeConfigServiceDaoImpl) {
        this.feeConfigServiceDaoImpl = feeConfigServiceDaoImpl;
    }
}
