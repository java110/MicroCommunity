package com.java110.community.listener.fastuser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IActivitiesServiceDao;
import com.java110.community.dao.IFastuserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
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
@Java110Listener("saveFastuserInfoListener")
@Transactional
public class SaveFastuserInfoListener extends AbstractFastuserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveFastuserInfoListener.class);

    @Autowired
    private IFastuserServiceDao fastuserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FASTUSER;
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
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_FASTUSER)){
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_FASTUSER);
            JSONArray businessFastusers = null;
            if(bObj instanceof JSONObject){
                businessFastusers = new JSONArray();
                businessFastusers.add(bObj);
            }else {
                businessFastusers = (JSONArray)bObj;
            }
            //JSONObject businessActivities = data.getJSONObject("businessActivities");
            for (int bFastusersIndex = 0; bFastusersIndex < businessFastusers.size();bFastusersIndex++) {
                JSONObject businessFastuser = businessFastusers.getJSONObject(bFastusersIndex);
                doBusinessFastuser(business, businessFastuser);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("fastuserId", businessFastuser.getString("fastuserId"));
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
        List<Map> businessActivitiesInfo = fastuserServiceDaoImpl.getBusinessFastuserInfo(info);
        if( businessActivitiesInfo != null && businessActivitiesInfo.size() >0) {
            reFreshShareColumn(info, businessActivitiesInfo.get(0));
            fastuserServiceDaoImpl.saveFastuserInfoInstance(info);
            if(businessActivitiesInfo.size() == 1) {
                dataFlowContext.addParamOut("fastuserId", businessActivitiesInfo.get(0).get("fast_user_id"));
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
        List<Map> activitiesInfo = fastuserServiceDaoImpl.getFastuserInfo(info);
        if(activitiesInfo != null && activitiesInfo.size() > 0){
            reFreshShareColumn(paramIn, activitiesInfo.get(0));
            fastuserServiceDaoImpl.updateFastuserInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessActivities 节点
     * @param business 总的数据节点
     * @param businessFastusers 活动节点
     */
    private void doBusinessFastuser(Business business,JSONObject businessFastusers){

        Assert.jsonObjectHaveKey(businessFastusers,"fastuserId","businessFastusers 节点下没有包含 fastuserId 节点");

        if(businessFastusers.getString("fastuserId").startsWith("-")){
            //刷新缓存
            //flushActivitiesId(business.getDatas());

            businessFastusers.put("fastuserId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fastuserId));

        }

        businessFastusers.put("bId",business.getbId());
        businessFastusers.put("operate", StatusConstant.OPERATE_ADD);
        //保存活动信息
        fastuserServiceDaoImpl.saveBusinessFastuserInfo(businessFastusers);

    }

    public IFastuserServiceDao getFastuserServiceDaoImpl() {
        return fastuserServiceDaoImpl;
    }

    public void setFastuserServiceDaoImpl(IFastuserServiceDao fastuserServiceDaoImpl) {
        this.fastuserServiceDaoImpl = fastuserServiceDaoImpl;
    }

}
