package com.java110.acct.listener.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.account.AccountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.acct.dao.IAccountServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改账户信息 侦听
 *
 * 处理节点
 * 1、businessAccount:{} 账户基本信息节点
 * 2、businessAccountAttr:[{}] 账户属性信息节点
 * 3、businessAccountPhoto:[{}] 账户照片信息节点
 * 4、businessAccountCerdentials:[{}] 账户证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAccountInfoListener")
@Transactional
public class UpdateAccountInfoListener extends AbstractAccountBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAccountInfoListener.class);
    @Autowired
    private IAccountServiceDao accountServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACCT;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");


            //处理 businessAccount 节点
            if(data.containsKey(AccountPo.class.getSimpleName())){
                Object _obj = data.get(AccountPo.class.getSimpleName());
                JSONArray businessAccounts = null;
                if(_obj instanceof JSONObject){
                    businessAccounts = new JSONArray();
                    businessAccounts.add(_obj);
                }else {
                    businessAccounts = (JSONArray)_obj;
                }
                //JSONObject businessAccount = data.getJSONObject(AccountPo.class.getSimpleName());
                for (int _accountIndex = 0; _accountIndex < businessAccounts.size();_accountIndex++) {
                    JSONObject businessAccount = businessAccounts.getJSONObject(_accountIndex);
                    doBusinessAccount(business, businessAccount);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("acctId", businessAccount.getString("acctId"));
                    }
                }
            }
    }


    /**
     * business to instance 过程
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
        List<Map> businessAccountInfos = accountServiceDaoImpl.getBusinessAccountInfo(info);
        if( businessAccountInfos != null && businessAccountInfos.size() >0) {
            for (int _accountIndex = 0; _accountIndex < businessAccountInfos.size();_accountIndex++) {
                Map businessAccountInfo = businessAccountInfos.get(_accountIndex);
                flushBusinessAccountInfo(businessAccountInfo,StatusConstant.STATUS_CD_VALID);
                accountServiceDaoImpl.updateAccountInfoInstance(businessAccountInfo);
                if(businessAccountInfo.size() == 1) {
                    dataFlowContext.addParamOut("acctId", businessAccountInfo.get("acct_id"));
                }
            }
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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //账户信息
        List<Map> accountInfo = accountServiceDaoImpl.getAccountInfo(info);
        if(accountInfo != null && accountInfo.size() > 0){

            //账户信息
            List<Map> businessAccountInfos = accountServiceDaoImpl.getBusinessAccountInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAccountInfos == null || businessAccountInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（account），程序内部异常,请检查！ "+delInfo);
            }
            for (int _accountIndex = 0; _accountIndex < businessAccountInfos.size();_accountIndex++) {
                Map businessAccountInfo = businessAccountInfos.get(_accountIndex);
                flushBusinessAccountInfo(businessAccountInfo,StatusConstant.STATUS_CD_VALID);
                accountServiceDaoImpl.updateAccountInfoInstance(businessAccountInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"acctId 错误，不能自动生成（必须已经存在的acctId）"+businessAccount);
        }
        //自动保存DEL
        autoSaveDelBusinessAccount(business,businessAccount);

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
