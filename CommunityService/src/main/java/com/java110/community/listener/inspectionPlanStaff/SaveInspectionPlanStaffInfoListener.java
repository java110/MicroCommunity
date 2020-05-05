package com.java110.community.listener.inspectionPlanStaff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanStaffServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
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
 * 保存 执行计划人信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionPlanStaffInfoListener")
@Transactional
public class SaveInspectionPlanStaffInfoListener extends AbstractInspectionPlanStaffBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionPlanStaffInfoListener.class);

    @Autowired
    private IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PLAN_STAFF;
    }

    /**
     * 保存执行计划人信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionPlanStaff 节点
        if (data.containsKey("businessInspectionPlanStaff")) {
            Object bObj = data.get("businessInspectionPlanStaff");
            JSONArray businessInspectionPlanStaffs = null;
            if (bObj instanceof JSONObject) {
                businessInspectionPlanStaffs = new JSONArray();
                businessInspectionPlanStaffs.add(bObj);
            } else {
                businessInspectionPlanStaffs = (JSONArray) bObj;
            }
            //JSONObject businessInspectionPlanStaff = data.getJSONObject("businessInspectionPlanStaff");
            for (int bInspectionPlanStaffIndex = 0; bInspectionPlanStaffIndex < businessInspectionPlanStaffs.size(); bInspectionPlanStaffIndex++) {
                JSONObject businessInspectionPlanStaff = businessInspectionPlanStaffs.getJSONObject(bInspectionPlanStaffIndex);
                doBusinessInspectionPlanStaff(business, businessInspectionPlanStaff);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ipStaffId", businessInspectionPlanStaff.getString("ipStaffId"));
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

        //执行计划人信息
        List<Map> businessInspectionPlanStaffInfo = inspectionPlanStaffServiceDaoImpl.getBusinessInspectionPlanStaffInfo(info);
        if (businessInspectionPlanStaffInfo != null && businessInspectionPlanStaffInfo.size() > 0) {
            reFreshShareColumn(info, businessInspectionPlanStaffInfo.get(0));
            inspectionPlanStaffServiceDaoImpl.saveInspectionPlanStaffInfoInstance(info);
            if (businessInspectionPlanStaffInfo.size() == 1) {
                dataFlowContext.addParamOut("ipStaffId", businessInspectionPlanStaffInfo.get(0).get("ip_staff_id"));
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
        //执行计划人信息
        List<Map> inspectionPlanStaffInfo = inspectionPlanStaffServiceDaoImpl.getInspectionPlanStaffInfo(info);
        if (inspectionPlanStaffInfo != null && inspectionPlanStaffInfo.size() > 0) {
            reFreshShareColumn(paramIn, inspectionPlanStaffInfo.get(0));
            inspectionPlanStaffServiceDaoImpl.updateInspectionPlanStaffInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessInspectionPlanStaff 节点
     *
     * @param business                    总的数据节点
     * @param businessInspectionPlanStaff 执行计划人节点
     */
    private void doBusinessInspectionPlanStaff(Business business, JSONObject businessInspectionPlanStaff) {

        Assert.jsonObjectHaveKey(businessInspectionPlanStaff, "ipStaffId", "businessInspectionPlanStaff 节点下没有包含 ipStaffId 节点");

        if (businessInspectionPlanStaff.getString("ipStaffId").startsWith("-")) {
            //刷新缓存
            //flushInspectionPlanStaffId(business.getDatas());

            businessInspectionPlanStaff.put("ipStaffId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ipStaffId));

        }

        businessInspectionPlanStaff.put("bId", business.getbId());
        businessInspectionPlanStaff.put("operate", StatusConstant.OPERATE_ADD);
        //保存执行计划人信息
        inspectionPlanStaffServiceDaoImpl.saveBusinessInspectionPlanStaffInfo(businessInspectionPlanStaff);

    }

    public IInspectionPlanStaffServiceDao getInspectionPlanStaffServiceDaoImpl() {
        return inspectionPlanStaffServiceDaoImpl;
    }

    public void setInspectionPlanStaffServiceDaoImpl(IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl) {
        this.inspectionPlanStaffServiceDaoImpl = inspectionPlanStaffServiceDaoImpl;
    }
}
