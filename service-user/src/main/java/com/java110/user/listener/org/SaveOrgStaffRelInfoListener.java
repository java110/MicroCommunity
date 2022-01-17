package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.user.dao.IOrgStaffRelServiceDao;
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
 * 保存 组织员工关系信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOrgStaffRelInfoListener")
@Transactional
public class SaveOrgStaffRelInfoListener extends AbstractOrgStaffRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOrgStaffRelInfoListener.class);

    @Autowired
    private IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_STAFF_REL;
    }

    /**
     * 保存组织员工关系信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOrgStaffRel 节点
        if (data.containsKey(OrgStaffRelPo.class.getSimpleName())) {
            Object bObj = data.get(OrgStaffRelPo.class.getSimpleName());
            JSONArray businessOrgStaffRels = null;
            if (bObj instanceof JSONObject) {
                businessOrgStaffRels = new JSONArray();
                businessOrgStaffRels.add(bObj);
            } else {
                businessOrgStaffRels = (JSONArray) bObj;
            }
            //JSONObject businessOrgStaffRel = data.getJSONObject("businessOrgStaffRel");
            for (int bOrgStaffRelIndex = 0; bOrgStaffRelIndex < businessOrgStaffRels.size(); bOrgStaffRelIndex++) {
                JSONObject businessOrgStaffRel = businessOrgStaffRels.getJSONObject(bOrgStaffRelIndex);
                doBusinessOrgStaffRel(business, businessOrgStaffRel);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("relId", businessOrgStaffRel.getString("relId"));
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

        //组织员工关系信息
        List<Map> businessOrgStaffRelInfo = orgStaffRelServiceDaoImpl.getBusinessOrgStaffRelInfo(info);
        if (businessOrgStaffRelInfo != null && businessOrgStaffRelInfo.size() > 0) {
            reFreshShareColumn(info, businessOrgStaffRelInfo.get(0));
            orgStaffRelServiceDaoImpl.saveOrgStaffRelInfoInstance(info);
            if (businessOrgStaffRelInfo.size() == 1) {
                dataFlowContext.addParamOut("relId", businessOrgStaffRelInfo.get(0).get("rel_id"));
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
        //组织员工关系信息
        List<Map> orgStaffRelInfo = orgStaffRelServiceDaoImpl.getOrgStaffRelInfo(info);
        if (orgStaffRelInfo != null && orgStaffRelInfo.size() > 0) {
            reFreshShareColumn(paramIn, orgStaffRelInfo.get(0));
            orgStaffRelServiceDaoImpl.updateOrgStaffRelInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOrgStaffRel 节点
     *
     * @param business            总的数据节点
     * @param businessOrgStaffRel 组织员工关系节点
     */
    private void doBusinessOrgStaffRel(Business business, JSONObject businessOrgStaffRel) {

        Assert.jsonObjectHaveKey(businessOrgStaffRel, "relId", "businessOrgStaffRel 节点下没有包含 relId 节点");

        if (businessOrgStaffRel.getString("relId").startsWith("-")) {
            //刷新缓存
            //flushOrgStaffRelId(business.getDatas());

            businessOrgStaffRel.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));

        }

        businessOrgStaffRel.put("bId", business.getbId());
        businessOrgStaffRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存组织员工关系信息
        orgStaffRelServiceDaoImpl.saveBusinessOrgStaffRelInfo(businessOrgStaffRel);

    }

    public IOrgStaffRelServiceDao getOrgStaffRelServiceDaoImpl() {
        return orgStaffRelServiceDaoImpl;
    }

    public void setOrgStaffRelServiceDaoImpl(IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl) {
        this.orgStaffRelServiceDaoImpl = orgStaffRelServiceDaoImpl;
    }
}
