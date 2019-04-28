package com.java110.community.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 小区成员加入 侦听
 * <p>
 * businessCommunityMember
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("memberJoinedCommunityListener")
@Transactional
public class MemberJoinedCommunityListener extends AbstractCommunityBusinessServiceDataFlowListener {

    private static final int CURRENT_ORDER = 5;

    private static Logger logger = LoggerFactory.getLogger(MemberJoinedCommunityListener.class);

    @Autowired
    ICommunityServiceDao communityServiceDaoImpl;

    @Override
    public int getOrder() {
        return CURRENT_ORDER;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY;
    }

    /**
     * 保存小区信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey("businessCommunityMember")) {
            JSONObject businessCommunityMember = data.getJSONObject("businessCommunityMember");
            doBusinessCommunityMember(business, businessCommunityMember);
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

        //小区信息
        Map businessCommunityMember = communityServiceDaoImpl.getBusinessCommunityMember(info);
        if (businessCommunityMember != null && !businessCommunityMember.isEmpty()) {
            communityServiceDaoImpl.saveCommunityMemberInstance(info);
            dataFlowContext.addParamOut("communityId", businessCommunityMember.get("community_id"));
            dataFlowContext.addParamOut("memberId", businessCommunityMember.get("member_id"));
        }
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
        //小区信息
        Map communityMember = communityServiceDaoImpl.getCommunityMember(info);
        if (communityMember != null && !communityMember.isEmpty()) {
            paramIn.put("communityMemberId", communityMember.get("member_community_id").toString());
            communityServiceDaoImpl.updateCommunityMemberInstance(paramIn);
            dataFlowContext.addParamOut("communityMemberId", communityMember.get("member_community_id"));
        }
    }

    /**
     * 处理 businessCommunityMember 节点
     *
     * @param business                总的数据节点
     * @param businessCommunityMember 小区成员节点
     */
    private void doBusinessCommunityMember(Business business, JSONObject businessCommunityMember) {

        Assert.jsonObjectHaveKey(businessCommunityMember, "communityId", "businessCommunityMember 节点下没有包含 communityId 节点");
        Assert.jsonObjectHaveKey(businessCommunityMember, "memberId", "businessCommunityMember 节点下没有包含 memberId 节点");
        Assert.jsonObjectHaveKey(businessCommunityMember, "memberTypeCd", "businessCommunityMember 节点下没有包含 memberTypeCd 节点");

        if (businessCommunityMember.getString("communityId").startsWith("-") || businessCommunityMember.getString("memberId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "传入参数 communityId 或 communityId 必须是已有小区ID，" + businessCommunityMember);
        }

        if (businessCommunityMember.getString("communityMemberId").startsWith("-")) {
            //刷新缓存
            flushCommunityMemberId(business.getDatas());
        }

        businessCommunityMember.put("bId", business.getbId());
        businessCommunityMember.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区信息
        communityServiceDaoImpl.saveBusinessCommunityMember(businessCommunityMember);

    }


    /**
     * 刷新 小区ID
     *
     * @param data
     */
    private void flushCommunityMemberId(JSONObject data) {

        String communityMemberId = GenerateCodeFactory.getCommunityMemberId();
        JSONObject businessCommunityMember = data.getJSONObject("businessCommunityMember");
        businessCommunityMember.put("communityMemberId", communityMemberId);

    }

    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
