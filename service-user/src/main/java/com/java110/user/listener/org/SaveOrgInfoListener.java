package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.org.OrgPo;
import com.java110.user.dao.IOrgServiceDao;
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
 * 保存 组织信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOrgInfoListener")
@Transactional
public class SaveOrgInfoListener extends AbstractOrgBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOrgInfoListener.class);

    @Autowired
    private IOrgServiceDao orgServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG;
    }

    /**
     * 保存组织信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOrg 节点
        if (data.containsKey(OrgPo.class.getSimpleName())) {
            Object bObj = data.get(OrgPo.class.getSimpleName());
            JSONArray businessOrgs = null;
            if (bObj instanceof JSONObject) {
                businessOrgs = new JSONArray();
                businessOrgs.add(bObj);
            } else {
                businessOrgs = (JSONArray) bObj;
            }
            //JSONObject businessOrg = data.getJSONObject("businessOrg");
            for (int bOrgIndex = 0; bOrgIndex < businessOrgs.size(); bOrgIndex++) {
                JSONObject businessOrg = businessOrgs.getJSONObject(bOrgIndex);
                doBusinessOrg(business, businessOrg);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("orgId", businessOrg.getString("orgId"));
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

        //组织信息
        List<Map> businessOrgInfo = orgServiceDaoImpl.getBusinessOrgInfo(info);
        if (businessOrgInfo != null && businessOrgInfo.size() > 0) {
            reFreshShareColumn(info, businessOrgInfo.get(0));
            orgServiceDaoImpl.saveOrgInfoInstance(info);
            if (businessOrgInfo.size() == 1) {
                dataFlowContext.addParamOut("orgId", businessOrgInfo.get(0).get("org_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        //组织信息
        List<Map> orgInfo = orgServiceDaoImpl.getOrgInfo(info);
        if (orgInfo != null && orgInfo.size() > 0) {
            reFreshShareColumn(paramIn, orgInfo.get(0));
            orgServiceDaoImpl.updateOrgInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOrg 节点
     *
     * @param business    总的数据节点
     * @param businessOrg 组织节点
     */
    private void doBusinessOrg(Business business, JSONObject businessOrg) {

        Assert.jsonObjectHaveKey(businessOrg, "orgId", "businessOrg 节点下没有包含 orgId 节点");

        if (businessOrg.getString("orgId").startsWith("-")) {
            //刷新缓存
            //flushOrgId(business.getDatas());

            businessOrg.put("orgId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId));

        }

        businessOrg.put("bId", business.getbId());
        businessOrg.put("operate", StatusConstant.OPERATE_ADD);
        //保存组织信息
        orgServiceDaoImpl.saveBusinessOrgInfo(businessOrg);

    }

    public IOrgServiceDao getOrgServiceDaoImpl() {
        return orgServiceDaoImpl;
    }

    public void setOrgServiceDaoImpl(IOrgServiceDao orgServiceDaoImpl) {
        this.orgServiceDaoImpl = orgServiceDaoImpl;
    }
}
