package com.java110.community.listener.communityMember;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.community.listener.community.AbstractCommunityBusinessServiceDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.community.CommunityMemberPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
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
    public void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessCommunity 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if (data.containsKey(CommunityMemberPo.class.getSimpleName())) {
            Object _obj = data.get(CommunityMemberPo.class.getSimpleName());
            JSONArray businessMemberCommunitys = null;
            if (_obj instanceof JSONObject) {
                businessMemberCommunitys = new JSONArray();
                businessMemberCommunitys.add(_obj);
            } else {
                businessMemberCommunitys = (JSONArray) _obj;
            }
            //JSONObject businessFloor = data.getJSONObject("businessFloor");
            for (int _memberIndex = 0; _memberIndex < businessMemberCommunitys.size(); _memberIndex++) {
                JSONObject memberCommunity = businessMemberCommunitys.getJSONObject(_memberIndex);
                doBusinessCommunityMember(business, memberCommunity);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("communityMemberId", memberCommunity.getString("communityMemberId"));
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


        //小区楼信息
        List<Map> businessCommunityMembers = communityServiceDaoImpl.getBusinessCommunityMember(info);
        if (businessCommunityMembers != null && businessCommunityMembers.size() > 0) {
            //for (int _memberIndex = 0; _memberIndex < businessCommunityMembers.size(); _memberIndex++) {
                Map businessCommunityMemberInfo = businessCommunityMembers.get(0);
                communityServiceDaoImpl.saveCommunityMemberInstance(info);
                dataFlowContext.addParamOut("communityId", businessCommunityMemberInfo.get("community_id"));
                dataFlowContext.addParamOut("memberId", businessCommunityMemberInfo.get("member_id"));
            //}
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
        List<Map> communityMembers = communityServiceDaoImpl.getCommunityMember(info);
        if (communityMembers != null && !communityMembers.isEmpty()) {

            //for (int _memberIndex = 0; _memberIndex < communityMembers.size(); _memberIndex++) {
                Map businessCommunityMemberInfo = communityMembers.get(0);
                paramIn.put("communityMemberId", businessCommunityMemberInfo.get("member_community_id").toString());
                communityServiceDaoImpl.updateCommunityMemberInstance(paramIn);
                dataFlowContext.addParamOut("communityMemberId", businessCommunityMemberInfo.get("member_community_id"));
            //}
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
            flushCommunityMemberId(businessCommunityMember);
        }

        businessCommunityMember.put("bId", business.getbId());
        businessCommunityMember.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区信息
        communityServiceDaoImpl.saveBusinessCommunityMember(businessCommunityMember);

    }


    /**
     * 刷新 小区ID
     *
     * @param businessCommunityMember
     */
    private void flushCommunityMemberId(JSONObject businessCommunityMember) {

        String communityMemberId = GenerateCodeFactory.getCommunityMemberId();
        businessCommunityMember.put("communityMemberId", communityMemberId);

    }

    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
