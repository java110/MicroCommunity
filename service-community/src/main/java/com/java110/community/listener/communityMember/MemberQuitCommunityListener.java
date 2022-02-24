package com.java110.community.listener.communityMember;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.community.listener.community.AbstractCommunityBusinessServiceDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
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
 * 小区成员退出 侦听
 * <p>
 * 处理节点
 * 1、businessCommunityMember:{} 小区基本信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("memberQuitCommunityListener")
@Transactional
public class MemberQuitCommunityListener extends AbstractCommunityBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(MemberQuitCommunityListener.class);
    @Autowired
    ICommunityServiceDao communityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY;
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
//        //处理 businessCommunity 节点 按理这里不应该处理，程序上支持，以防真有这种业务
//        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY)) {
//            JSONObject memberCommunity = data.getJSONObject(BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
//            doBusinessCommunityMember(business, memberCommunity);
//            dataFlowContext.addParamOut("communityMemberId", memberCommunity.getString("communityMemberId"));
//        }

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
        info.put("operate", StatusConstant.OPERATE_DEL);


        //小区楼信息
        List<Map> businessCommunityMembers = communityServiceDaoImpl.getBusinessCommunityMember(info);
        if (businessCommunityMembers != null && businessCommunityMembers.size() > 0) {
            for (int _memberIndex = 0; _memberIndex < businessCommunityMembers.size(); _memberIndex++) {
                Map businessCommunityMemberInfo = businessCommunityMembers.get(_memberIndex);
                flushBusinessCommunityMember(businessCommunityMemberInfo, StatusConstant.STATUS_CD_INVALID);
                communityServiceDaoImpl.updateCommunityMemberInstance(businessCommunityMemberInfo);
                dataFlowContext.addParamOut("communityMemberId", businessCommunityMemberInfo.get("member_community_id"));
            }
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //小区信息
        List<Map> memberCommunitys = communityServiceDaoImpl.getCommunityMember(info);
        if (memberCommunitys != null && !memberCommunitys.isEmpty()) {


            //小区楼信息
            List<Map> businessCommunityMembers = communityServiceDaoImpl.getBusinessCommunityMember(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityMembers == null || businessCommunityMembers.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（community），程序内部异常,请检查！ " + delInfo);
            }
            for (int _memberIndex = 0; _memberIndex < businessCommunityMembers.size(); _memberIndex++) {
                Map businessCommunityMember = businessCommunityMembers.get(_memberIndex);
                flushBusinessCommunityMember(businessCommunityMember, StatusConstant.STATUS_CD_VALID);
                communityServiceDaoImpl.updateCommunityMemberInstance(businessCommunityMember);
            }
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

        if (businessCommunity.getString("communityMemberId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "communityMemberId 错误，不能自动生成（必须已经存在的communityMemberId）" + businessCommunity);
        }
        //自动插入DEL
        autoSaveDelBusinessCommunityMember(business, businessCommunity);
    }


    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
