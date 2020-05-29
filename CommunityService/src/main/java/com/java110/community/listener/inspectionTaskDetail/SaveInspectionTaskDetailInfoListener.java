package com.java110.community.listener.inspectionTaskDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IInspectionTaskDetailServiceDao;
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
 * 保存 巡检任务明细信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionTaskDetailInfoListener")
@Transactional
public class SaveInspectionTaskDetailInfoListener extends AbstractInspectionTaskDetailBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionTaskDetailInfoListener.class);

    @Autowired
    private IInspectionTaskDetailServiceDao inspectionTaskDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL;
    }

    /**
     * 保存巡检任务明细信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionTaskDetail 节点
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL)){
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL);
            JSONArray businessInspectionTaskDetails = null;
            if(bObj instanceof JSONObject){
                businessInspectionTaskDetails = new JSONArray();
                businessInspectionTaskDetails.add(bObj);
            }else {
                businessInspectionTaskDetails = (JSONArray)bObj;
            }
            //JSONObject businessInspectionTaskDetail = data.getJSONObject("businessInspectionTaskDetail");
            for (int bInspectionTaskDetailIndex = 0; bInspectionTaskDetailIndex < businessInspectionTaskDetails.size();bInspectionTaskDetailIndex++) {
                JSONObject businessInspectionTaskDetail = businessInspectionTaskDetails.getJSONObject(bInspectionTaskDetailIndex);
                doBusinessInspectionTaskDetail(business, businessInspectionTaskDetail);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("taskDetailId", businessInspectionTaskDetail.getString("taskDetailId"));
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

        //巡检任务明细信息
        List<Map> businessInspectionTaskDetailInfo = inspectionTaskDetailServiceDaoImpl.getBusinessInspectionTaskDetailInfo(info);
        if( businessInspectionTaskDetailInfo != null && businessInspectionTaskDetailInfo.size() >0) {
            reFreshShareColumn(info, businessInspectionTaskDetailInfo.get(0));
            inspectionTaskDetailServiceDaoImpl.saveInspectionTaskDetailInfoInstance(info);
            if(businessInspectionTaskDetailInfo.size() == 1) {
                dataFlowContext.addParamOut("taskDetailId", businessInspectionTaskDetailInfo.get(0).get("task_detail_id"));
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
        //巡检任务明细信息
        List<Map> inspectionTaskDetailInfo = inspectionTaskDetailServiceDaoImpl.getInspectionTaskDetailInfo(info);
        if(inspectionTaskDetailInfo != null && inspectionTaskDetailInfo.size() > 0){
            reFreshShareColumn(paramIn, inspectionTaskDetailInfo.get(0));
            inspectionTaskDetailServiceDaoImpl.updateInspectionTaskDetailInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessInspectionTaskDetail 节点
     * @param business 总的数据节点
     * @param businessInspectionTaskDetail 巡检任务明细节点
     */
    private void doBusinessInspectionTaskDetail(Business business,JSONObject businessInspectionTaskDetail){

        Assert.jsonObjectHaveKey(businessInspectionTaskDetail,"taskDetailId","businessInspectionTaskDetail 节点下没有包含 taskDetailId 节点");

        if(businessInspectionTaskDetail.getString("taskDetailId").startsWith("-")){
            //刷新缓存
            //flushInspectionTaskDetailId(business.getDatas());

            businessInspectionTaskDetail.put("taskDetailId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));

        }

        businessInspectionTaskDetail.put("bId",business.getbId());
        businessInspectionTaskDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存巡检任务明细信息
        inspectionTaskDetailServiceDaoImpl.saveBusinessInspectionTaskDetailInfo(businessInspectionTaskDetail);

    }

    public IInspectionTaskDetailServiceDao getInspectionTaskDetailServiceDaoImpl() {
        return inspectionTaskDetailServiceDaoImpl;
    }

    public void setInspectionTaskDetailServiceDaoImpl(IInspectionTaskDetailServiceDao inspectionTaskDetailServiceDaoImpl) {
        this.inspectionTaskDetailServiceDaoImpl = inspectionTaskDetailServiceDaoImpl;
    }
}
