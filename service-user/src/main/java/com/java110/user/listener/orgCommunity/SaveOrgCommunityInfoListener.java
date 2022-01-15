package com.java110.user.listener.orgCommunity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.community.OrgCommunityPo;
import com.java110.user.dao.IOrgCommunityServiceDao;
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
 * 保存 隶属小区信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOrgCommunityInfoListener")
@Transactional
public class SaveOrgCommunityInfoListener extends AbstractOrgCommunityBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOrgCommunityInfoListener.class);

    @Autowired
    private IOrgCommunityServiceDao orgCommunityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_COMMUNITY;
    }

    /**
     * 保存隶属小区信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOrgCommunity 节点
        if (data.containsKey(OrgCommunityPo.class.getSimpleName())) {
            Object bObj = data.get(OrgCommunityPo.class.getSimpleName());
            JSONArray businessOrgCommunitys = null;
            if (bObj instanceof JSONObject) {
                businessOrgCommunitys = new JSONArray();
                businessOrgCommunitys.add(bObj);
            } else {
                businessOrgCommunitys = (JSONArray) bObj;
            }
            //JSONObject businessOrgCommunity = data.getJSONObject("businessOrgCommunity");
            for (int bOrgCommunityIndex = 0; bOrgCommunityIndex < businessOrgCommunitys.size(); bOrgCommunityIndex++) {
                JSONObject businessOrgCommunity = businessOrgCommunitys.getJSONObject(bOrgCommunityIndex);
                doBusinessOrgCommunity(business, businessOrgCommunity);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("orgCommunityId", businessOrgCommunity.getString("orgCommunityId"));
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

        //隶属小区信息
        List<Map> businessOrgCommunityInfo = orgCommunityServiceDaoImpl.getBusinessOrgCommunityInfo(info);
        if (businessOrgCommunityInfo != null && businessOrgCommunityInfo.size() > 0) {
            reFreshShareColumn(info, businessOrgCommunityInfo.get(0));
            orgCommunityServiceDaoImpl.saveOrgCommunityInfoInstance(info);
            if (businessOrgCommunityInfo.size() == 1) {
                dataFlowContext.addParamOut("orgCommunityId", businessOrgCommunityInfo.get(0).get("org_community_id"));
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
        //隶属小区信息
        List<Map> orgCommunityInfo = orgCommunityServiceDaoImpl.getOrgCommunityInfo(info);
        if (orgCommunityInfo != null && orgCommunityInfo.size() > 0) {
            reFreshShareColumn(paramIn, orgCommunityInfo.get(0));
            orgCommunityServiceDaoImpl.updateOrgCommunityInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOrgCommunity 节点
     *
     * @param business             总的数据节点
     * @param businessOrgCommunity 隶属小区节点
     */
    private void doBusinessOrgCommunity(Business business, JSONObject businessOrgCommunity) {

        Assert.jsonObjectHaveKey(businessOrgCommunity, "orgCommunityId", "businessOrgCommunity 节点下没有包含 orgCommunityId 节点");

        if (businessOrgCommunity.getString("orgCommunityId").startsWith("-")) {
            //刷新缓存
            //flushOrgCommunityId(business.getDatas());

            businessOrgCommunity.put("orgCommunityId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgCommunityId));

        }

        businessOrgCommunity.put("bId", business.getbId());
        businessOrgCommunity.put("operate", StatusConstant.OPERATE_ADD);
        //保存隶属小区信息
        orgCommunityServiceDaoImpl.saveBusinessOrgCommunityInfo(businessOrgCommunity);

    }

    public IOrgCommunityServiceDao getOrgCommunityServiceDaoImpl() {
        return orgCommunityServiceDaoImpl;
    }

    public void setOrgCommunityServiceDaoImpl(IOrgCommunityServiceDao orgCommunityServiceDaoImpl) {
        this.orgCommunityServiceDaoImpl = orgCommunityServiceDaoImpl;
    }
}
