package com.java110.fee.listener.tempCarFeeConfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 临时车收费标准信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveTempCarFeeConfigInfoListener")
@Transactional
public class SaveTempCarFeeConfigInfoListener extends AbstractTempCarFeeConfigBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveTempCarFeeConfigInfoListener.class);

    @Autowired
    private ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_INFO;
    }

    /**
     * 保存临时车收费标准信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessTempCarFeeConfig 节点
        if (data.containsKey(TempCarFeeConfigPo.class.getSimpleName())) {
            Object bObj = data.get(TempCarFeeConfigPo.class.getSimpleName());
            JSONArray businessTempCarFeeConfigs = null;
            if (bObj instanceof JSONObject) {
                businessTempCarFeeConfigs = new JSONArray();
                businessTempCarFeeConfigs.add(bObj);
            } else {
                businessTempCarFeeConfigs = (JSONArray) bObj;
            }
            //JSONObject businessTempCarFeeConfig = data.getJSONObject(TempCarFeeConfigPo.class.getSimpleName());
            for (int bTempCarFeeConfigIndex = 0; bTempCarFeeConfigIndex < businessTempCarFeeConfigs.size(); bTempCarFeeConfigIndex++) {
                JSONObject businessTempCarFeeConfig = businessTempCarFeeConfigs.getJSONObject(bTempCarFeeConfigIndex);
                doBusinessTempCarFeeConfig(business, businessTempCarFeeConfig);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("configId", businessTempCarFeeConfig.getString("configId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //临时车收费标准信息
        List<Map> businessTempCarFeeConfigInfo = tempCarFeeConfigServiceDaoImpl.getBusinessTempCarFeeConfigInfo(info);
        if (businessTempCarFeeConfigInfo != null && businessTempCarFeeConfigInfo.size() > 0) {
            reFreshShareColumn(info, businessTempCarFeeConfigInfo.get(0));
            tempCarFeeConfigServiceDaoImpl.saveTempCarFeeConfigInfoInstance(info);
            if (businessTempCarFeeConfigInfo.size() == 1) {
                dataFlowContext.addParamOut("configId", businessTempCarFeeConfigInfo.get(0).get("config_id"));
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

        if (info.containsKey("community_id")) {
            return;
        }

        if (!businessInfo.containsKey("communityId")) {
            return;
        }

        info.put("community_id", businessInfo.get("communityId"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //临时车收费标准信息
        List<Map> tempCarFeeConfigInfo = tempCarFeeConfigServiceDaoImpl.getTempCarFeeConfigInfo(info);
        if (tempCarFeeConfigInfo != null && tempCarFeeConfigInfo.size() > 0) {
            reFreshShareColumn(paramIn, tempCarFeeConfigInfo.get(0));
            tempCarFeeConfigServiceDaoImpl.updateTempCarFeeConfigInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessTempCarFeeConfig 节点
     *
     * @param business                 总的数据节点
     * @param businessTempCarFeeConfig 临时车收费标准节点
     */
    private void doBusinessTempCarFeeConfig(Business business, JSONObject businessTempCarFeeConfig) {

        Assert.jsonObjectHaveKey(businessTempCarFeeConfig, "configId", "businessTempCarFeeConfig 节点下没有包含 configId 节点");

        if (businessTempCarFeeConfig.getString("configId").startsWith("-")) {
            //刷新缓存
            //flushTempCarFeeConfigId(business.getDatas());

            businessTempCarFeeConfig.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));

        }

        businessTempCarFeeConfig.put("bId", business.getbId());
        businessTempCarFeeConfig.put("operate", StatusConstant.OPERATE_ADD);
        //保存临时车收费标准信息
        tempCarFeeConfigServiceDaoImpl.saveBusinessTempCarFeeConfigInfo(businessTempCarFeeConfig);

    }

    @Override
    public ITempCarFeeConfigServiceDao getTempCarFeeConfigServiceDaoImpl() {
        return tempCarFeeConfigServiceDaoImpl;
    }

    public void setTempCarFeeConfigServiceDaoImpl(ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl) {
        this.tempCarFeeConfigServiceDaoImpl = tempCarFeeConfigServiceDaoImpl;
    }
}
