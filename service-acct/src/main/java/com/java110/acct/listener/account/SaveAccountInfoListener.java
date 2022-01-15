package com.java110.acct.listener.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.account.AccountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.acct.dao.IAccountServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 账户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAccountInfoListener")
@Transactional
public class SaveAccountInfoListener extends AbstractAccountBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveAccountInfoListener.class);

    @Autowired
    private IAccountServiceDao accountServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT;
    }

    /**
     * 保存账户信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessAccount 节点
        if(data.containsKey(AccountPo.class.getSimpleName())){
            Object bObj = data.get(AccountPo.class.getSimpleName());
            JSONArray businessAccounts = null;
            if(bObj instanceof JSONObject){
                businessAccounts = new JSONArray();
                businessAccounts.add(bObj);
            }else {
                businessAccounts = (JSONArray)bObj;
            }
            //JSONObject businessAccount = data.getJSONObject(AccountPo.class.getSimpleName());
            for (int bAccountIndex = 0; bAccountIndex < businessAccounts.size();bAccountIndex++) {
                JSONObject businessAccount = businessAccounts.getJSONObject(bAccountIndex);
                doBusinessAccount(business, businessAccount);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("acctId", businessAccount.getString("acctId"));
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

        //账户信息
        List<Map> businessAccountInfo = accountServiceDaoImpl.getBusinessAccountInfo(info);
        if( businessAccountInfo != null && businessAccountInfo.size() >0) {
            reFreshShareColumn(info, businessAccountInfo.get(0));
            accountServiceDaoImpl.saveAccountInfoInstance(info);
            if(businessAccountInfo.size() == 1) {
                dataFlowContext.addParamOut("acctId", businessAccountInfo.get(0).get("acct_id"));
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

        if (info.containsKey("objId")) {
            return;
        }

        if (!businessInfo.containsKey("obj_id")) {
            return;
        }

        info.put("objId", businessInfo.get("obj_id"));
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
        //账户信息
        List<Map> accountInfo = accountServiceDaoImpl.getAccountInfo(info);
        if(accountInfo != null && accountInfo.size() > 0){
            reFreshShareColumn(paramIn, accountInfo.get(0));
            accountServiceDaoImpl.updateAccountInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAccount 节点
     * @param business 总的数据节点
     * @param businessAccount 账户节点
     */
    private void doBusinessAccount(Business business,JSONObject businessAccount){

        Assert.jsonObjectHaveKey(businessAccount,"acctId","businessAccount 节点下没有包含 acctId 节点");

        if(businessAccount.getString("acctId").startsWith("-")){
            //刷新缓存
            //flushAccountId(business.getDatas());

            businessAccount.put("acctId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));

        }

        businessAccount.put("bId",business.getbId());
        businessAccount.put("operate", StatusConstant.OPERATE_ADD);
        //保存账户信息
        accountServiceDaoImpl.saveBusinessAccountInfo(businessAccount);

    }
    @Override
    public IAccountServiceDao getAccountServiceDaoImpl() {
        return accountServiceDaoImpl;
    }

    public void setAccountServiceDaoImpl(IAccountServiceDao accountServiceDaoImpl) {
        this.accountServiceDaoImpl = accountServiceDaoImpl;
    }
}
