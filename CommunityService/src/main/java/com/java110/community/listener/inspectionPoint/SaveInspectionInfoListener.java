package com.java110.community.listener.inspectionPoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IInspectionServiceDao;
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
 * 保存 巡检点信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionInfoListener")
@Transactional
public class SaveInspectionInfoListener extends AbstractInspectionBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionInfoListener.class);

    @Autowired
    private IInspectionServiceDao inspectionServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION;
    }

    /**
     * 保存巡检点信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessInspection 节点
        if(data.containsKey("businessInspection")){
            Object bObj = data.get("businessInspection");
            JSONArray businessInspections = null;
            if(bObj instanceof JSONObject){
                businessInspections = new JSONArray();
                businessInspections.add(bObj);
            }else {
                businessInspections = (JSONArray)bObj;
            }
            //JSONObject businessInspection = data.getJSONObject("businessInspection");
            for (int bInspectionIndex = 0; bInspectionIndex < businessInspections.size();bInspectionIndex++) {
                JSONObject businessInspection = businessInspections.getJSONObject(bInspectionIndex);
                doBusinessInspection(business, businessInspection);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("inspectionId", businessInspection.getString("inspectionId"));
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

        //巡检点信息
        List<Map> businessInspectionInfo = inspectionServiceDaoImpl.getBusinessInspectionInfo(info);
        if( businessInspectionInfo != null && businessInspectionInfo.size() >0) {
            reFreshShareColumn(info, businessInspectionInfo.get(0));
            inspectionServiceDaoImpl.saveInspectionInfoInstance(info);
            if(businessInspectionInfo.size() == 1) {
                dataFlowContext.addParamOut("inspectionId", businessInspectionInfo.get(0).get("inspection_id"));
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
        //巡检点信息
        List<Map> inspectionInfo = inspectionServiceDaoImpl.getInspectionInfo(info);
        if(inspectionInfo != null && inspectionInfo.size() > 0){
            reFreshShareColumn(paramIn, inspectionInfo.get(0));
            inspectionServiceDaoImpl.updateInspectionInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessInspection 节点
     * @param business 总的数据节点
     * @param businessInspection 巡检点节点
     */
    private void doBusinessInspection(Business business,JSONObject businessInspection){

        Assert.jsonObjectHaveKey(businessInspection,"inspectionId","businessInspection 节点下没有包含 inspectionId 节点");

        if(businessInspection.getString("inspectionId").startsWith("-")){
            //刷新缓存
            //flushInspectionId(business.getDatas());

            businessInspection.put("inspectionId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inspectionId));

        }

        businessInspection.put("bId",business.getbId());
        businessInspection.put("operate", StatusConstant.OPERATE_ADD);
        //保存巡检点信息
        inspectionServiceDaoImpl.saveBusinessInspectionInfo(businessInspection);

    }

    public IInspectionServiceDao getInspectionServiceDaoImpl() {
        return inspectionServiceDaoImpl;
    }

    public void setInspectionServiceDaoImpl(IInspectionServiceDao inspectionServiceDaoImpl) {
        this.inspectionServiceDaoImpl = inspectionServiceDaoImpl;
    }
}
