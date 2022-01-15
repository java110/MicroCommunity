package com.java110.community.listener.visit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.owner.VisitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IVisitServiceDao;
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
 * 保存 访客信息信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveVisitInfoListener")
@Transactional
public class SaveVisitInfoListener extends AbstractVisitBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveVisitInfoListener.class);

    @Autowired
    private IVisitServiceDao visitServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISIT;
    }

    /**
     * 保存访客信息信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessVisit 节点
        if(data.containsKey(VisitPo.class.getSimpleName())){
            Object bObj = data.get(VisitPo.class.getSimpleName());
            JSONArray businessVisits = null;
            if(bObj instanceof JSONObject){
                businessVisits = new JSONArray();
                businessVisits.add(bObj);
            }else {
                businessVisits = (JSONArray)bObj;
            }
            //JSONObject businessVisit = data.getJSONObject("businessVisit");
            for (int bVisitIndex = 0; bVisitIndex < businessVisits.size();bVisitIndex++) {
                JSONObject businessVisit = businessVisits.getJSONObject(bVisitIndex);
                doBusinessVisit(business, businessVisit);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("vId", businessVisit.getString("vId"));
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

        //访客信息信息
        List<Map> businessVisitInfo = visitServiceDaoImpl.getBusinessVisitInfo(info);
        if( businessVisitInfo != null && businessVisitInfo.size() >0) {
            reFreshShareColumn(info, businessVisitInfo.get(0));
            visitServiceDaoImpl.saveVisitInfoInstance(info);
            if(businessVisitInfo.size() == 1) {
                dataFlowContext.addParamOut("vId", businessVisitInfo.get(0).get("v_id"));
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

        if (info.containsKey("vId")) {
            return;
        }

        if (!businessInfo.containsKey("v_id")) {
            return;
        }

        info.put("vId", businessInfo.get("v_id"));
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
        //访客信息信息
        List<Map> visitInfo = visitServiceDaoImpl.getVisitInfo(info);
        if(visitInfo != null && visitInfo.size() > 0){
            reFreshShareColumn(paramIn, visitInfo.get(0));
            visitServiceDaoImpl.updateVisitInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessVisit 节点
     * @param business 总的数据节点
     * @param businessVisit 访客信息节点
     */
    private void doBusinessVisit(Business business,JSONObject businessVisit){

        Assert.jsonObjectHaveKey(businessVisit,"vId","businessVisit 节点下没有包含 vId 节点");

        if(businessVisit.getString("vId").startsWith("-")){
            //刷新缓存
            //flushVisitId(business.getDatas());

            businessVisit.put("vId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vId));

        }

        businessVisit.put("bId",business.getbId());
        businessVisit.put("operate", StatusConstant.OPERATE_ADD);
        //保存访客信息信息
        visitServiceDaoImpl.saveBusinessVisitInfo(businessVisit);

    }

    public IVisitServiceDao getVisitServiceDaoImpl() {
        return visitServiceDaoImpl;
    }

    public void setVisitServiceDaoImpl(IVisitServiceDao visitServiceDaoImpl) {
        this.visitServiceDaoImpl = visitServiceDaoImpl;
    }
}
