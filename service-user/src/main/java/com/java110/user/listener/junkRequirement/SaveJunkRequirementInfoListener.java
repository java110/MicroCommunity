package com.java110.user.listener.junkRequirement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.junkRequirement.JunkRequirementPo;
import com.java110.user.dao.IJunkRequirementServiceDao;
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
 * 保存 旧货市场信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveJunkRequirementInfoListener")
@Transactional
public class SaveJunkRequirementInfoListener extends AbstractJunkRequirementBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveJunkRequirementInfoListener.class);

    @Autowired
    private IJunkRequirementServiceDao junkRequirementServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_JUNK_REQUIREMENT;
    }

    /**
     * 保存旧货市场信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessJunkRequirement 节点
        if (data.containsKey(JunkRequirementPo.class.getSimpleName())) {
            Object bObj = data.get(JunkRequirementPo.class.getSimpleName());
            JSONArray businessJunkRequirements = null;
            if (bObj instanceof JSONObject) {
                businessJunkRequirements = new JSONArray();
                businessJunkRequirements.add(bObj);
            } else {
                businessJunkRequirements = (JSONArray) bObj;
            }
            //JSONObject businessJunkRequirement = data.getJSONObject("businessJunkRequirement");
            for (int bJunkRequirementIndex = 0; bJunkRequirementIndex < businessJunkRequirements.size(); bJunkRequirementIndex++) {
                JSONObject businessJunkRequirement = businessJunkRequirements.getJSONObject(bJunkRequirementIndex);
                doBusinessJunkRequirement(business, businessJunkRequirement);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("junkRequirementId", businessJunkRequirement.getString("junkRequirementId"));
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

        //旧货市场信息
        List<Map> businessJunkRequirementInfo = junkRequirementServiceDaoImpl.getBusinessJunkRequirementInfo(info);
        if (businessJunkRequirementInfo != null && businessJunkRequirementInfo.size() > 0) {
            reFreshShareColumn(info, businessJunkRequirementInfo.get(0));
            junkRequirementServiceDaoImpl.saveJunkRequirementInfoInstance(info);
            if (businessJunkRequirementInfo.size() == 1) {
                dataFlowContext.addParamOut("junkRequirementId", businessJunkRequirementInfo.get(0).get("junk_requirement_id"));
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
        //旧货市场信息
        List<Map> junkRequirementInfo = junkRequirementServiceDaoImpl.getJunkRequirementInfo(info);
        if (junkRequirementInfo != null && junkRequirementInfo.size() > 0) {
            reFreshShareColumn(paramIn, junkRequirementInfo.get(0));
            junkRequirementServiceDaoImpl.updateJunkRequirementInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessJunkRequirement 节点
     *
     * @param business                总的数据节点
     * @param businessJunkRequirement 旧货市场节点
     */
    private void doBusinessJunkRequirement(Business business, JSONObject businessJunkRequirement) {

        Assert.jsonObjectHaveKey(businessJunkRequirement, "junkRequirementId", "businessJunkRequirement 节点下没有包含 junkRequirementId 节点");

        if (businessJunkRequirement.getString("junkRequirementId").startsWith("-")) {
            //刷新缓存
            //flushJunkRequirementId(business.getDatas());

            businessJunkRequirement.put("junkRequirementId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_junkRequirementId));

        }

        businessJunkRequirement.put("bId", business.getbId());
        businessJunkRequirement.put("operate", StatusConstant.OPERATE_ADD);
        //保存旧货市场信息
        junkRequirementServiceDaoImpl.saveBusinessJunkRequirementInfo(businessJunkRequirement);

    }

    public IJunkRequirementServiceDao getJunkRequirementServiceDaoImpl() {
        return junkRequirementServiceDaoImpl;
    }

    public void setJunkRequirementServiceDaoImpl(IJunkRequirementServiceDao junkRequirementServiceDaoImpl) {
        this.junkRequirementServiceDaoImpl = junkRequirementServiceDaoImpl;
    }
}
