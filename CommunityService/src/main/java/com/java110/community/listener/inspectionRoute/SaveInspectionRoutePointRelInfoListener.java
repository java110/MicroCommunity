package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
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
 * 保存 巡检路线巡检点关系信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionRoutePointRelInfoListener")
@Transactional
public class SaveInspectionRoutePointRelInfoListener extends AbstractInspectionRoutePointRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionRoutePointRelInfoListener.class);

    @Autowired
    private IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_ROUTE_POINT_REL;
    }

    /**
     * 保存巡检路线巡检点关系信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionRoutePointRel 节点
        if (data.containsKey("businessInspectionRoutePointRel")) {
            Object bObj = data.get("businessInspectionRoutePointRel");
            JSONArray businessInspectionRoutePointRels = null;
            if (bObj instanceof JSONObject) {
                businessInspectionRoutePointRels = new JSONArray();
                businessInspectionRoutePointRels.add(bObj);
            } else {
                businessInspectionRoutePointRels = (JSONArray) bObj;
            }
            //JSONObject businessInspectionRoutePointRel = data.getJSONObject("businessInspectionRoutePointRel");
            for (int bInspectionRoutePointRelIndex = 0; bInspectionRoutePointRelIndex < businessInspectionRoutePointRels.size(); bInspectionRoutePointRelIndex++) {
                JSONObject businessInspectionRoutePointRel = businessInspectionRoutePointRels.getJSONObject(bInspectionRoutePointRelIndex);
                doBusinessInspectionRoutePointRel(business, businessInspectionRoutePointRel);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("irmRelId", businessInspectionRoutePointRel.getString("irmRelId"));
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

        //巡检路线巡检点关系信息
        List<Map> businessInspectionRoutePointRelInfo = inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo(info);
        if (businessInspectionRoutePointRelInfo != null && businessInspectionRoutePointRelInfo.size() > 0) {
            reFreshShareColumn(info, businessInspectionRoutePointRelInfo.get(0));
            inspectionRoutePointRelServiceDaoImpl.saveInspectionRoutePointRelInfoInstance(info);
            if (businessInspectionRoutePointRelInfo.size() == 1) {
                dataFlowContext.addParamOut("irmRelId", businessInspectionRoutePointRelInfo.get(0).get("irm_rel_id"));
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
        //巡检路线巡检点关系信息
        List<Map> inspectionRoutePointRelInfo = inspectionRoutePointRelServiceDaoImpl.getInspectionRoutePointRelInfo(info);
        if (inspectionRoutePointRelInfo != null && inspectionRoutePointRelInfo.size() > 0) {
            reFreshShareColumn(paramIn, inspectionRoutePointRelInfo.get(0));
            inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessInspectionRoutePointRel 节点
     *
     * @param business                        总的数据节点
     * @param businessInspectionRoutePointRel 巡检路线巡检点关系节点
     */
    private void doBusinessInspectionRoutePointRel(Business business, JSONObject businessInspectionRoutePointRel) {

        Assert.jsonObjectHaveKey(businessInspectionRoutePointRel, "irmRelId", "businessInspectionRoutePointRel 节点下没有包含 irmRelId 节点");

        if (businessInspectionRoutePointRel.getString("irmRelId").startsWith("-")) {
            //刷新缓存
            //flushInspectionRoutePointRelId(business.getDatas());

            businessInspectionRoutePointRel.put("irmRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_irmRelId));

        }

        businessInspectionRoutePointRel.put("bId", business.getbId());
        businessInspectionRoutePointRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存巡检路线巡检点关系信息
        inspectionRoutePointRelServiceDaoImpl.saveBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRel);

    }

    public IInspectionRoutePointRelServiceDao getInspectionRoutePointRelServiceDaoImpl() {
        return inspectionRoutePointRelServiceDaoImpl;
    }

    public void setInspectionRoutePointRelServiceDaoImpl(IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl) {
        this.inspectionRoutePointRelServiceDaoImpl = inspectionRoutePointRelServiceDaoImpl;
    }
}
