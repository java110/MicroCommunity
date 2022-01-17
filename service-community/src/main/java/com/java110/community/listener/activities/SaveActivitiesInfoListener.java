package com.java110.community.listener.activities;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.activities.ActivitiesPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IActivitiesServiceDao;
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
 * 保存 活动信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveActivitiesInfoListener")
@Transactional
public class SaveActivitiesInfoListener extends AbstractActivitiesBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveActivitiesInfoListener.class);

    @Autowired
    private IActivitiesServiceDao activitiesServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACTIVITIES;
    }

    /**
     * 保存活动信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessActivities 节点
        if(data.containsKey(ActivitiesPo.class.getSimpleName())){
            Object bObj = data.get(ActivitiesPo.class.getSimpleName());
            JSONArray businessActivitiess = null;
            if(bObj instanceof JSONObject){
                businessActivitiess = new JSONArray();
                businessActivitiess.add(bObj);
            }else {
                businessActivitiess = (JSONArray)bObj;
            }
            //JSONObject businessActivities = data.getJSONObject("businessActivities");
            for (int bActivitiesIndex = 0; bActivitiesIndex < businessActivitiess.size();bActivitiesIndex++) {
                JSONObject businessActivities = businessActivitiess.getJSONObject(bActivitiesIndex);
                doBusinessActivities(business, businessActivities);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("activitiesId", businessActivities.getString("activitiesId"));
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

        //活动信息
        List<Map> businessActivitiesInfo = activitiesServiceDaoImpl.getBusinessActivitiesInfo(info);
        if( businessActivitiesInfo != null && businessActivitiesInfo.size() >0) {
            //reFreshShareColumn(info, businessActivitiesInfo.get(0));
            activitiesServiceDaoImpl.saveActivitiesInfoInstance(info);
            if(businessActivitiesInfo.size() == 1) {
                dataFlowContext.addParamOut("activitiesId", businessActivitiesInfo.get(0).get("activities_id"));
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
        //活动信息
        List<Map> activitiesInfo = activitiesServiceDaoImpl.getActivitiesInfo(info);
        if(activitiesInfo != null && activitiesInfo.size() > 0){
            reFreshShareColumn(paramIn, activitiesInfo.get(0));
            activitiesServiceDaoImpl.updateActivitiesInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessActivities 节点
     * @param business 总的数据节点
     * @param businessActivities 活动节点
     */
    private void doBusinessActivities(Business business,JSONObject businessActivities){

        Assert.jsonObjectHaveKey(businessActivities,"activitiesId","businessActivities 节点下没有包含 activitiesId 节点");

        if(businessActivities.getString("activitiesId").startsWith("-")){
            //刷新缓存
            //flushActivitiesId(business.getDatas());

            businessActivities.put("activitiesId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_activitiesId));

        }

        businessActivities.put("bId",business.getbId());
        businessActivities.put("operate", StatusConstant.OPERATE_ADD);
        //保存活动信息
        activitiesServiceDaoImpl.saveBusinessActivitiesInfo(businessActivities);

    }

    public IActivitiesServiceDao getActivitiesServiceDaoImpl() {
        return activitiesServiceDaoImpl;
    }

    public void setActivitiesServiceDaoImpl(IActivitiesServiceDao activitiesServiceDaoImpl) {
        this.activitiesServiceDaoImpl = activitiesServiceDaoImpl;
    }
}
