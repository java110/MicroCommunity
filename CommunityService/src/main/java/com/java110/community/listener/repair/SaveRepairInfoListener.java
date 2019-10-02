package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IRepairServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 报修信息信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRepairInfoListener")
@Transactional
public class SaveRepairInfoListener extends AbstractRepairBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveRepairInfoListener.class);

    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR;
    }

    /**
     * 保存报修信息信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessRepair 节点
        if(data.containsKey("businessRepair")){
            Object bObj = data.get("businessRepair");
            JSONArray businessRepairs = null;
            if(bObj instanceof JSONObject){
                businessRepairs = new JSONArray();
                businessRepairs.add(bObj);
            }else {
                businessRepairs = (JSONArray)bObj;
            }
            //JSONObject businessRepair = data.getJSONObject("businessRepair");
            for (int bRepairIndex = 0; bRepairIndex < businessRepairs.size();bRepairIndex++) {
                JSONObject businessRepair = businessRepairs.getJSONObject(bRepairIndex);
                doBusinessRepair(business, businessRepair);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("repairId", businessRepair.getString("repairId"));
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

        //报修信息信息
        List<Map> businessRepairInfo = repairServiceDaoImpl.getBusinessRepairInfo(info);
        if( businessRepairInfo != null && businessRepairInfo.size() >0) {
            reFreshShareColumn(info, businessRepairInfo.get(0));
            repairServiceDaoImpl.saveRepairInfoInstance(info);
            if(businessRepairInfo.size() == 1) {
                dataFlowContext.addParamOut("repairId", businessRepairInfo.get(0).get("repair_id"));
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
        //报修信息信息
        List<Map> repairInfo = repairServiceDaoImpl.getRepairInfo(info);
        if(repairInfo != null && repairInfo.size() > 0){
            reFreshShareColumn(paramIn, repairInfo.get(0));
            repairServiceDaoImpl.updateRepairInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessRepair 节点
     * @param business 总的数据节点
     * @param businessRepair 报修信息节点
     */
    private void doBusinessRepair(Business business,JSONObject businessRepair){

        Assert.jsonObjectHaveKey(businessRepair,"repairId","businessRepair 节点下没有包含 repairId 节点");

        if(businessRepair.getString("repairId").startsWith("-")){
            //刷新缓存
            //flushRepairId(business.getDatas());

            businessRepair.put("repairId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId));

        }

        businessRepair.put("bId",business.getbId());
        businessRepair.put("operate", StatusConstant.OPERATE_ADD);
        //保存报修信息信息
        repairServiceDaoImpl.saveBusinessRepairInfo(businessRepair);

    }

    public IRepairServiceDao getRepairServiceDaoImpl() {
        return repairServiceDaoImpl;
    }

    public void setRepairServiceDaoImpl(IRepairServiceDao repairServiceDaoImpl) {
        this.repairServiceDaoImpl = repairServiceDaoImpl;
    }
}
