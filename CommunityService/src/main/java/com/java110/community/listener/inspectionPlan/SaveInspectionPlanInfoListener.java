package com.java110.community.listener.inspectionPlan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IInspectionPlanServiceDao;
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
 * 保存 巡检计划信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionPlanInfoListener")
@Transactional
public class SaveInspectionPlanInfoListener extends AbstractInspectionPlanBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionPlanInfoListener.class);

    @Autowired
    private IInspectionPlanServiceDao inspectionPlanServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_PLAN;
    }

    /**
     * 保存巡检计划信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionPlan 节点
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_PLAN)){
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_PLAN);
            JSONArray businessInspectionPlans = null;
            if(bObj instanceof JSONObject){
                businessInspectionPlans = new JSONArray();
                businessInspectionPlans.add(bObj);
            }else {
                businessInspectionPlans = (JSONArray)bObj;
            }
            //JSONObject businessInspectionPlan = data.getJSONObject("businessInspectionPlan");
            for (int bInspectionPlanIndex = 0; bInspectionPlanIndex < businessInspectionPlans.size();bInspectionPlanIndex++) {
                JSONObject businessInspectionPlan = businessInspectionPlans.getJSONObject(bInspectionPlanIndex);
                doBusinessInspectionPlan(business, businessInspectionPlan);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("inspectionPlanId", businessInspectionPlan.getString("inspectionPlanId"));
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

        //巡检计划信息
        List<Map> businessInspectionPlanInfo = inspectionPlanServiceDaoImpl.getBusinessInspectionPlanInfo(info);
        if( businessInspectionPlanInfo != null && businessInspectionPlanInfo.size() >0) {
            reFreshShareColumn(info, businessInspectionPlanInfo.get(0));
            inspectionPlanServiceDaoImpl.saveInspectionPlanInfoInstance(info);
            if(businessInspectionPlanInfo.size() == 1) {
                dataFlowContext.addParamOut("inspectionPlanId", businessInspectionPlanInfo.get(0).get("inspection_plan_id"));
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
        //巡检计划信息
        List<Map> inspectionPlanInfo = inspectionPlanServiceDaoImpl.getInspectionPlanInfo(info);
        if(inspectionPlanInfo != null && inspectionPlanInfo.size() > 0){
            reFreshShareColumn(paramIn, inspectionPlanInfo.get(0));
            inspectionPlanServiceDaoImpl.updateInspectionPlanInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessInspectionPlan 节点
     * @param business 总的数据节点
     * @param businessInspectionPlan 巡检计划节点
     */
    private void doBusinessInspectionPlan(Business business,JSONObject businessInspectionPlan){

        Assert.jsonObjectHaveKey(businessInspectionPlan,"inspectionPlanId","businessInspectionPlan 节点下没有包含 inspectionPlanId 节点");

        if(businessInspectionPlan.getString("inspectionPlanId").startsWith("-")){
            //刷新缓存
            //flushInspectionPlanId(business.getDatas());

            businessInspectionPlan.put("inspectionPlanId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inspectionPlanId));

        }

        businessInspectionPlan.put("bId",business.getbId());
        businessInspectionPlan.put("operate", StatusConstant.OPERATE_ADD);


        //保存巡检计划信息
        inspectionPlanServiceDaoImpl.saveBusinessInspectionPlanInfo(businessInspectionPlan);

    }

    public IInspectionPlanServiceDao getInspectionPlanServiceDaoImpl() {
        return inspectionPlanServiceDaoImpl;
    }

    public void setInspectionPlanServiceDaoImpl(IInspectionPlanServiceDao inspectionPlanServiceDaoImpl) {
        this.inspectionPlanServiceDaoImpl = inspectionPlanServiceDaoImpl;
    }
}
