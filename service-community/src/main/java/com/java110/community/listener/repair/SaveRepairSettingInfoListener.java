package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairSettingServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.repair.RepairSettingPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 报修设置信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRepairSettingInfoListener")
@Transactional
public class SaveRepairSettingInfoListener extends AbstractRepairSettingBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveRepairSettingInfoListener.class);

    @Autowired
    private IRepairSettingServiceDao repairSettingServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_SETTING;
    }

    /**
     * 保存报修设置信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessRepairSetting 节点
        if (data.containsKey(RepairSettingPo.class.getSimpleName())) {
            Object bObj = data.get(RepairSettingPo.class.getSimpleName());
            JSONArray businessRepairSettings = null;
            if (bObj instanceof JSONObject) {
                businessRepairSettings = new JSONArray();
                businessRepairSettings.add(bObj);
            } else {
                businessRepairSettings = (JSONArray) bObj;
            }
            //JSONObject businessRepairSetting = data.getJSONObject(RepairSettingPo.class.getSimpleName());
            for (int bRepairSettingIndex = 0; bRepairSettingIndex < businessRepairSettings.size(); bRepairSettingIndex++) {
                JSONObject businessRepairSetting = businessRepairSettings.getJSONObject(bRepairSettingIndex);
                doBusinessRepairSetting(business, businessRepairSetting);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("settingId", businessRepairSetting.getString("settingId"));
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

        //报修设置信息
        List<Map> businessRepairSettingInfo = repairSettingServiceDaoImpl.getBusinessRepairSettingInfo(info);
        if (businessRepairSettingInfo != null && businessRepairSettingInfo.size() > 0) {
            reFreshShareColumn(info, businessRepairSettingInfo.get(0));
            repairSettingServiceDaoImpl.saveRepairSettingInfoInstance(info);
            if (businessRepairSettingInfo.size() == 1) {
                dataFlowContext.addParamOut("settingId", businessRepairSettingInfo.get(0).get("setting_id"));
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
        //报修设置信息
        List<Map> repairSettingInfo = repairSettingServiceDaoImpl.getRepairSettingInfo(info);
        if (repairSettingInfo != null && repairSettingInfo.size() > 0) {
            reFreshShareColumn(paramIn, repairSettingInfo.get(0));
            repairSettingServiceDaoImpl.updateRepairSettingInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessRepairSetting 节点
     *
     * @param business              总的数据节点
     * @param businessRepairSetting 报修设置节点
     */
    private void doBusinessRepairSetting(Business business, JSONObject businessRepairSetting) {

        Assert.jsonObjectHaveKey(businessRepairSetting, "settingId", "businessRepairSetting 节点下没有包含 settingId 节点");

        if (businessRepairSetting.getString("settingId").startsWith("-")) {
            //刷新缓存
            //flushRepairSettingId(business.getDatas());

            businessRepairSetting.put("settingId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));

        }

        businessRepairSetting.put("bId", business.getbId());
        businessRepairSetting.put("operate", StatusConstant.OPERATE_ADD);
        //保存报修设置信息
        repairSettingServiceDaoImpl.saveBusinessRepairSettingInfo(businessRepairSetting);

    }

    @Override
    public IRepairSettingServiceDao getRepairSettingServiceDaoImpl() {
        return repairSettingServiceDaoImpl;
    }

    public void setRepairSettingServiceDaoImpl(IRepairSettingServiceDao repairSettingServiceDaoImpl) {
        this.repairSettingServiceDaoImpl = repairSettingServiceDaoImpl;
    }
}
