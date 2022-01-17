package com.java110.community.listener.communityLocation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.community.CommunityLocationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.ICommunityLocationServiceDao;
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
 * 保存 小区位置信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommunityLocationInfoListener")
@Transactional
public class SaveCommunityLocationInfoListener extends AbstractCommunityLocationBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCommunityLocationInfoListener.class);

    @Autowired
    private ICommunityLocationServiceDao communityLocationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_LOCATION;
    }

    /**
     * 保存小区位置信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunityLocation 节点
        if(data.containsKey(CommunityLocationPo.class.getSimpleName())){
            Object bObj = data.get(CommunityLocationPo.class.getSimpleName());
            JSONArray businessCommunityLocations = null;
            if(bObj instanceof JSONObject){
                businessCommunityLocations = new JSONArray();
                businessCommunityLocations.add(bObj);
            }else {
                businessCommunityLocations = (JSONArray)bObj;
            }
            //JSONObject businessCommunityLocation = data.getJSONObject(CommunityLocationPo.class.getSimpleName());
            for (int bCommunityLocationIndex = 0; bCommunityLocationIndex < businessCommunityLocations.size();bCommunityLocationIndex++) {
                JSONObject businessCommunityLocation = businessCommunityLocations.getJSONObject(bCommunityLocationIndex);
                doBusinessCommunityLocation(business, businessCommunityLocation);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("locationId", businessCommunityLocation.getString("locationId"));
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

        //小区位置信息
        List<Map> businessCommunityLocationInfo = communityLocationServiceDaoImpl.getBusinessCommunityLocationInfo(info);
        if( businessCommunityLocationInfo != null && businessCommunityLocationInfo.size() >0) {
            reFreshShareColumn(info, businessCommunityLocationInfo.get(0));
            communityLocationServiceDaoImpl.saveCommunityLocationInfoInstance(info);
            if(businessCommunityLocationInfo.size() == 1) {
                dataFlowContext.addParamOut("locationId", businessCommunityLocationInfo.get(0).get("location_id"));
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
        //小区位置信息
        List<Map> communityLocationInfo = communityLocationServiceDaoImpl.getCommunityLocationInfo(info);
        if(communityLocationInfo != null && communityLocationInfo.size() > 0){
            reFreshShareColumn(paramIn, communityLocationInfo.get(0));
            communityLocationServiceDaoImpl.updateCommunityLocationInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessCommunityLocation 节点
     * @param business 总的数据节点
     * @param businessCommunityLocation 小区位置节点
     */
    private void doBusinessCommunityLocation(Business business,JSONObject businessCommunityLocation){

        Assert.jsonObjectHaveKey(businessCommunityLocation,"locationId","businessCommunityLocation 节点下没有包含 locationId 节点");

        if(businessCommunityLocation.getString("locationId").startsWith("-")){
            //刷新缓存
            //flushCommunityLocationId(business.getDatas());

            businessCommunityLocation.put("locationId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_locationId));

        }

        businessCommunityLocation.put("bId",business.getbId());
        businessCommunityLocation.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区位置信息
        communityLocationServiceDaoImpl.saveBusinessCommunityLocationInfo(businessCommunityLocation);

    }
    @Override
    public ICommunityLocationServiceDao getCommunityLocationServiceDaoImpl() {
        return communityLocationServiceDaoImpl;
    }

    public void setCommunityLocationServiceDaoImpl(ICommunityLocationServiceDao communityLocationServiceDaoImpl) {
        this.communityLocationServiceDaoImpl = communityLocationServiceDaoImpl;
    }
}
