package com.java110.community.listener.communityMember;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.community.listener.AbstractCommunityBusinessServiceDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 小区成员退出 侦听
 * <p>
 * 处理节点
 * 1、businessCommunityMember:{} 小区基本信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("auditCommunityMemberStateListener")
@Transactional
public class AuditCommunityMemberStateListener extends AbstractCommunityBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AuditCommunityMemberStateListener.class);
    @Autowired
    ICommunityServiceDao communityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_AUDIT_COMMUNITY_MEMBER_STATE;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunity 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if (data.containsKey("businessCommunityMember")) {
            JSONObject memberCommunity = data.getJSONObject("businessCommunityMember");
            doBusinessCommunityMember(business, memberCommunity);
            dataFlowContext.addParamOut("communityMemberId", memberCommunity.getString("communityMemberId"));
        }

    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //小区信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);


        //小区信息
        Map businessCommunityMember = communityServiceDaoImpl.getBusinessCommunityMember(info);
        if (businessCommunityMember != null && !businessCommunityMember.isEmpty()) {
            flushBusinessCommunityMember(businessCommunityMember, StatusConstant.STATUS_CD_VALID);
            communityServiceDaoImpl.updateCommunityMemberInstance(businessCommunityMember);
            dataFlowContext.addParamOut("communityMemberId", businessCommunityMember.get("member_community_id"));
        }
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //小区信息
        Map memberCommunity = communityServiceDaoImpl.getCommunityMember(info);
        if (memberCommunity != null && !memberCommunity.isEmpty()) {

            //小区信息
            Map businessCommunityMember = communityServiceDaoImpl.getBusinessCommunityMember(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityMember == null || businessCommunityMember.isEmpty()) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（member community），程序内部异常,请检查！ " + delInfo);
            }

            flushBusinessCommunityMember(businessCommunityMember, StatusConstant.STATUS_CD_VALID);
            communityServiceDaoImpl.updateCommunityMemberInstance(businessCommunityMember);
            dataFlowContext.addParamOut("communityMemberId", memberCommunity.get("member_community_id"));
        }
    }


    /**
     * 处理 businessCommunity 节点
     *
     * @param business          总的数据节点
     * @param businessCommunity 小区节点
     */
    private void doBusinessCommunityMember(Business business, JSONObject businessCommunity) {

        Assert.jsonObjectHaveKey(businessCommunity, "communityMemberId", "doBusinessCommunityMember 节点下没有包含 communityMemberId 节点");

        //自动插入DEL
        autoSaveDelBusinessCommunityMember(business, businessCommunity);

        businessCommunity.put("bId",business.getbId());
        businessCommunity.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区信息
        communityServiceDaoImpl.saveBusinessCommunityMember(businessCommunity);
    }


    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
