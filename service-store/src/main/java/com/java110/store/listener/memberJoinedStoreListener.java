package com.java110.store.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.store.dao.IStoreServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户成员加入 侦听
 *
 * businessMemberStore
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("memberJoinedStoreListener")
@Transactional
public class memberJoinedStoreListener extends AbstractStoreBusinessServiceDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(memberJoinedStoreListener.class);

    @Autowired
    IStoreServiceDao storeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_STORE;
    }

    /**
     * 保存商户信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_STORE)){
            JSONObject businessMemberStore = data.getJSONObject("businessMemberStore");
            doBusinessMemberStore(business,businessMemberStore);
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

        //商户信息
        Map businessMemberStore = storeServiceDaoImpl.getBusinessMemberStore(info);
        if( businessMemberStore != null && !businessMemberStore.isEmpty()) {
            storeServiceDaoImpl.saveMemberStoreInstance(info);
            dataFlowContext.addParamOut("storeId",businessMemberStore.get("store_id"));
            dataFlowContext.addParamOut("memberId",businessMemberStore.get("member_id"));
        }
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
        //商户信息
        Map memberStore = storeServiceDaoImpl.getMemberStore(info);
        if(memberStore != null && !memberStore.isEmpty()){
            paramIn.put("memberStoreId",memberStore.get("member_store_id").toString());
            storeServiceDaoImpl.updateMemberStoreInstance(paramIn);
            dataFlowContext.addParamOut("memberStoreId",memberStore.get("member_store_id"));
        }
    }

    /**
     * 处理 businessMemberStore 节点
     * @param business 总的数据节点
     * @param businessMemberStore 商户成员节点
     */
    private void doBusinessMemberStore(Business business,JSONObject businessMemberStore){

        Assert.jsonObjectHaveKey(businessMemberStore,"storeId","businessMemberStore 节点下没有包含 storeId 节点");
        Assert.jsonObjectHaveKey(businessMemberStore,"memberId","businessMemberStore 节点下没有包含 memberId 节点");

        if(businessMemberStore.getString("storeId").startsWith("-") || businessMemberStore.getString("memberId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"传入参数 storeId 或 storeId 必须是已有商户ID，"+businessMemberStore);
        }

        if(businessMemberStore.getString("memberStoreId").startsWith("-")){
            //刷新缓存
            flushMemberStoreId(business.getDatas());
        }

        businessMemberStore.put("bId",business.getbId());
        businessMemberStore.put("operate", StatusConstant.OPERATE_ADD);
        //保存商户信息
        storeServiceDaoImpl.saveBusinessMemberStore(businessMemberStore);

    }


    /**
     * 刷新 商户ID
     * @param data
     */
    private void flushMemberStoreId(JSONObject data) {

        String memberStoreId = GenerateCodeFactory.getMemberStoreId();
        JSONObject businessMemberStore = data.getJSONObject("businessMemberStore");
        businessMemberStore.put("memberStoreId",memberStoreId);

    }
    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
