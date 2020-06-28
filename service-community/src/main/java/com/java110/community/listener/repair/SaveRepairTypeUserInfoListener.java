package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairTypeUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.repair.RepairTypeUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 报修设置信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRepairTypeUserInfoListener")
@Transactional
public class SaveRepairTypeUserInfoListener extends AbstractRepairTypeUserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveRepairTypeUserInfoListener.class);

    @Autowired
    private IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_TYPE_USER;
    }

    /**
     * 保存报修设置信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessRepairTypeUser 节点
        if (data.containsKey(RepairTypeUserPo.class.getSimpleName())) {
            Object bObj = data.get(RepairTypeUserPo.class.getSimpleName());
            JSONArray businessRepairTypeUsers = null;
            if (bObj instanceof JSONObject) {
                businessRepairTypeUsers = new JSONArray();
                businessRepairTypeUsers.add(bObj);
            } else {
                businessRepairTypeUsers = (JSONArray) bObj;
            }
            //JSONObject businessRepairTypeUser = data.getJSONObject(RepairTypeUserPo.class.getSimpleName());
            for (int bRepairTypeUserIndex = 0; bRepairTypeUserIndex < businessRepairTypeUsers.size(); bRepairTypeUserIndex++) {
                JSONObject businessRepairTypeUser = businessRepairTypeUsers.getJSONObject(bRepairTypeUserIndex);
                doBusinessRepairTypeUser(business, businessRepairTypeUser);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("typeUserId", businessRepairTypeUser.getString("typeUserId"));
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

        //报修设置信息
        List<Map> businessRepairTypeUserInfo = repairTypeUserServiceDaoImpl.getBusinessRepairTypeUserInfo(info);
        if (businessRepairTypeUserInfo != null && businessRepairTypeUserInfo.size() > 0) {
            reFreshShareColumn(info, businessRepairTypeUserInfo.get(0));
            repairTypeUserServiceDaoImpl.saveRepairTypeUserInfoInstance(info);
            if (businessRepairTypeUserInfo.size() == 1) {
                dataFlowContext.addParamOut("typeUserId", businessRepairTypeUserInfo.get(0).get("type_user_id"));
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
        //报修设置信息
        List<Map> repairTypeUserInfo = repairTypeUserServiceDaoImpl.getRepairTypeUserInfo(info);
        if (repairTypeUserInfo != null && repairTypeUserInfo.size() > 0) {
            reFreshShareColumn(paramIn, repairTypeUserInfo.get(0));
            repairTypeUserServiceDaoImpl.updateRepairTypeUserInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessRepairTypeUser 节点
     *
     * @param business               总的数据节点
     * @param businessRepairTypeUser 报修设置节点
     */
    private void doBusinessRepairTypeUser(Business business, JSONObject businessRepairTypeUser) {

        Assert.jsonObjectHaveKey(businessRepairTypeUser, "typeUserId", "businessRepairTypeUser 节点下没有包含 typeUserId 节点");

        if (businessRepairTypeUser.getString("typeUserId").startsWith("-")) {
            //刷新缓存
            //flushRepairTypeUserId(business.getDatas());

            businessRepairTypeUser.put("typeUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_typeUserId));

        }

        businessRepairTypeUser.put("bId", business.getbId());
        businessRepairTypeUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存报修设置信息
        repairTypeUserServiceDaoImpl.saveBusinessRepairTypeUserInfo(businessRepairTypeUser);

    }

    @Override
    public IRepairTypeUserServiceDao getRepairTypeUserServiceDaoImpl() {
        return repairTypeUserServiceDaoImpl;
    }

    public void setRepairTypeUserServiceDaoImpl(IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl) {
        this.repairTypeUserServiceDaoImpl = repairTypeUserServiceDaoImpl;
    }
}
