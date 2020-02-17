package com.java110.community.listener.inspectionPlan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除巡检计划信息 侦听
 *
 * 处理节点
 * 1、businessInspectionPlan:{} 巡检计划基本信息节点
 * 2、businessInspectionPlanAttr:[{}] 巡检计划属性信息节点
 * 3、businessInspectionPlanPhoto:[{}] 巡检计划照片信息节点
 * 4、businessInspectionPlanCerdentials:[{}] 巡检计划证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteInspectionPlanInfoListener")
@Transactional
public class DeleteInspectionPlanInfoListener extends AbstractInspectionPlanBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteInspectionPlanInfoListener.class);
    @Autowired
    IInspectionPlanServiceDao inspectionPlanServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_PLAN;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionPlan 节点
        if(data.containsKey("businessInspectionPlan")){
            //处理 businessInspectionPlan 节点
            if(data.containsKey("businessInspectionPlan")){
                Object _obj = data.get("businessInspectionPlan");
                JSONArray businessInspectionPlans = null;
                if(_obj instanceof JSONObject){
                    businessInspectionPlans = new JSONArray();
                    businessInspectionPlans.add(_obj);
                }else {
                    businessInspectionPlans = (JSONArray)_obj;
                }
                //JSONObject businessInspectionPlan = data.getJSONObject("businessInspectionPlan");
                for (int _inspectionPlanIndex = 0; _inspectionPlanIndex < businessInspectionPlans.size();_inspectionPlanIndex++) {
                    JSONObject businessInspectionPlan = businessInspectionPlans.getJSONObject(_inspectionPlanIndex);
                    doBusinessInspectionPlan(business, businessInspectionPlan);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("inspectionPlanId", businessInspectionPlan.getString("inspectionPlanId"));
                    }
                }
            }
        }


    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //巡检计划信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //巡检计划信息
        List<Map> businessInspectionPlanInfos = inspectionPlanServiceDaoImpl.getBusinessInspectionPlanInfo(info);
        if( businessInspectionPlanInfos != null && businessInspectionPlanInfos.size() >0) {
            for (int _inspectionPlanIndex = 0; _inspectionPlanIndex < businessInspectionPlanInfos.size();_inspectionPlanIndex++) {
                Map businessInspectionPlanInfo = businessInspectionPlanInfos.get(_inspectionPlanIndex);
                flushBusinessInspectionPlanInfo(businessInspectionPlanInfo,StatusConstant.STATUS_CD_INVALID);
                inspectionPlanServiceDaoImpl.updateInspectionPlanInfoInstance(businessInspectionPlanInfo);
                dataFlowContext.addParamOut("inspectionPlanId",businessInspectionPlanInfo.get("inspection_plan_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //巡检计划信息
        List<Map> inspectionPlanInfo = inspectionPlanServiceDaoImpl.getInspectionPlanInfo(info);
        if(inspectionPlanInfo != null && inspectionPlanInfo.size() > 0){

            //巡检计划信息
            List<Map> businessInspectionPlanInfos = inspectionPlanServiceDaoImpl.getBusinessInspectionPlanInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessInspectionPlanInfos == null ||  businessInspectionPlanInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（inspectionPlan），程序内部异常,请检查！ "+delInfo);
            }
            for (int _inspectionPlanIndex = 0; _inspectionPlanIndex < businessInspectionPlanInfos.size();_inspectionPlanIndex++) {
                Map businessInspectionPlanInfo = businessInspectionPlanInfos.get(_inspectionPlanIndex);
                flushBusinessInspectionPlanInfo(businessInspectionPlanInfo,StatusConstant.STATUS_CD_VALID);
                inspectionPlanServiceDaoImpl.updateInspectionPlanInfoInstance(businessInspectionPlanInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"inspectionPlanId 错误，不能自动生成（必须已经存在的inspectionPlanId）"+businessInspectionPlan);
        }
        //自动插入DEL
        autoSaveDelBusinessInspectionPlan(business,businessInspectionPlan);
    }

    public IInspectionPlanServiceDao getInspectionPlanServiceDaoImpl() {
        return inspectionPlanServiceDaoImpl;
    }

    public void setInspectionPlanServiceDaoImpl(IInspectionPlanServiceDao inspectionPlanServiceDaoImpl) {
        this.inspectionPlanServiceDaoImpl = inspectionPlanServiceDaoImpl;
    }
}
