package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 报修派单信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRepairUserInfoListener")
@Transactional
public class SaveRepairUserInfoListener extends AbstractRepairUserBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveRepairUserInfoListener.class);

    @Autowired
    private IRepairUserServiceDao repairUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER;
    }

    /**
     * 保存报修派单信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessRepairUser 节点
        if(data.containsKey(RepairUserPo.class.getSimpleName())){
            Object bObj = data.get(RepairUserPo.class.getSimpleName());
            JSONArray businessRepairUsers = null;
            if(bObj instanceof JSONObject){
                businessRepairUsers = new JSONArray();
                businessRepairUsers.add(bObj);
            }else {
                businessRepairUsers = (JSONArray)bObj;
            }
            //JSONObject businessRepairUser = data.getJSONObject("businessRepairUser");
            for (int bRepairUserIndex = 0; bRepairUserIndex < businessRepairUsers.size();bRepairUserIndex++) {
                JSONObject businessRepairUser = businessRepairUsers.getJSONObject(bRepairUserIndex);
                doBusinessRepairUser(business, businessRepairUser);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ruId", businessRepairUser.getString("ruId"));
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

        //报修派单信息
        List<Map> businessRepairUserInfo = repairUserServiceDaoImpl.getBusinessRepairUserInfo(info);
        if( businessRepairUserInfo != null && businessRepairUserInfo.size() >0) {
            reFreshShareColumn(info, businessRepairUserInfo.get(0));
            repairUserServiceDaoImpl.saveRepairUserInfoInstance(info);
            if(businessRepairUserInfo.size() == 1) {
                dataFlowContext.addParamOut("ruId", businessRepairUserInfo.get(0).get("ru_id"));
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
        //报修派单信息
        List<Map> repairUserInfo = repairUserServiceDaoImpl.getRepairUserInfo(info);
        if(repairUserInfo != null && repairUserInfo.size() > 0){
            reFreshShareColumn(paramIn, repairUserInfo.get(0));
            repairUserServiceDaoImpl.updateRepairUserInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessRepairUser 节点
     * @param business 总的数据节点
     * @param businessRepairUser 报修派单节点
     */
    private void doBusinessRepairUser(Business business,JSONObject businessRepairUser){

        Assert.jsonObjectHaveKey(businessRepairUser,"ruId","businessRepairUser 节点下没有包含 ruId 节点");

        if(businessRepairUser.getString("ruId").startsWith("-")){
            //刷新缓存
            //flushRepairUserId(business.getDatas());

            businessRepairUser.put("ruId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));

        }

        businessRepairUser.put("bId",business.getbId());
        businessRepairUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存报修派单信息
        repairUserServiceDaoImpl.saveBusinessRepairUserInfo(businessRepairUser);

    }

    public IRepairUserServiceDao getRepairUserServiceDaoImpl() {
        return repairUserServiceDaoImpl;
    }

    public void setRepairUserServiceDaoImpl(IRepairUserServiceDao repairUserServiceDaoImpl) {
        this.repairUserServiceDaoImpl = repairUserServiceDaoImpl;
    }
}
