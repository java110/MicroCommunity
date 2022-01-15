package com.java110.acct.listener.accountDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IAccountDetailServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.accountDetail.AccountDetailPo;
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
 * 修改账户交易信息 侦听
 * <p>
 * 处理节点
 * 1、businessAccountDetail:{} 账户交易基本信息节点
 * 2、businessAccountDetailAttr:[{}] 账户交易属性信息节点
 * 3、businessAccountDetailPhoto:[{}] 账户交易照片信息节点
 * 4、businessAccountDetailCerdentials:[{}] 账户交易证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAccountDetailInfoListener")
@Transactional
public class UpdateAccountDetailInfoListener extends AbstractAccountDetailBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAccountDetailInfoListener.class);
    @Autowired
    private IAccountDetailServiceDao accountDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACCT_DETAIL;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessAccountDetail 节点
        if (data.containsKey(AccountDetailPo.class.getSimpleName())) {
            Object _obj = data.get(AccountDetailPo.class.getSimpleName());
            JSONArray businessAccountDetails = null;
            if (_obj instanceof JSONObject) {
                businessAccountDetails = new JSONArray();
                businessAccountDetails.add(_obj);
            } else {
                businessAccountDetails = (JSONArray) _obj;
            }
            //JSONObject businessAccountDetail = data.getJSONObject(AccountDetailPo.class.getSimpleName());
            for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetails.size(); _accountDetailIndex++) {
                JSONObject businessAccountDetail = businessAccountDetails.getJSONObject(_accountDetailIndex);
                doBusinessAccountDetail(business, businessAccountDetail);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailId", businessAccountDetail.getString("detailId"));
                }
            }
        }
    }


    /**
     * business to instance 过程
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

        //账户交易信息
        List<Map> businessAccountDetailInfos = accountDetailServiceDaoImpl.getBusinessAccountDetailInfo(info);
        if (businessAccountDetailInfos != null && businessAccountDetailInfos.size() > 0) {
            for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetailInfos.size(); _accountDetailIndex++) {
                Map businessAccountDetailInfo = businessAccountDetailInfos.get(_accountDetailIndex);
                flushBusinessAccountDetailInfo(businessAccountDetailInfo, StatusConstant.STATUS_CD_VALID);
                accountDetailServiceDaoImpl.updateAccountDetailInfoInstance(businessAccountDetailInfo);
                if (businessAccountDetailInfo.size() == 1) {
                    dataFlowContext.addParamOut("detailId", businessAccountDetailInfo.get("detail_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //账户交易信息
        List<Map> accountDetailInfo = accountDetailServiceDaoImpl.getAccountDetailInfo(info);
        if (accountDetailInfo != null && accountDetailInfo.size() > 0) {

            //账户交易信息
            List<Map> businessAccountDetailInfos = accountDetailServiceDaoImpl.getBusinessAccountDetailInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessAccountDetailInfos == null || businessAccountDetailInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（accountDetail），程序内部异常,请检查！ " + delInfo);
            }
            for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetailInfos.size(); _accountDetailIndex++) {
                Map businessAccountDetailInfo = businessAccountDetailInfos.get(_accountDetailIndex);
                flushBusinessAccountDetailInfo(businessAccountDetailInfo, StatusConstant.STATUS_CD_VALID);
                accountDetailServiceDaoImpl.updateAccountDetailInfoInstance(businessAccountDetailInfo);
            }
        }

    }


    /**
     * 处理 businessAccountDetail 节点
     *
     * @param business              总的数据节点
     * @param businessAccountDetail 账户交易节点
     */
    private void doBusinessAccountDetail(Business business, JSONObject businessAccountDetail) {

        Assert.jsonObjectHaveKey(businessAccountDetail, "detailId", "businessAccountDetail 节点下没有包含 detailId 节点");

        if (businessAccountDetail.getString("detailId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "detailId 错误，不能自动生成（必须已经存在的detailId）" + businessAccountDetail);
        }
        //自动保存DEL
        autoSaveDelBusinessAccountDetail(business, businessAccountDetail);

        businessAccountDetail.put("bId", business.getbId());
        businessAccountDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存账户交易信息
        accountDetailServiceDaoImpl.saveBusinessAccountDetailInfo(businessAccountDetail);

    }


    @Override
    public IAccountDetailServiceDao getAccountDetailServiceDaoImpl() {
        return accountDetailServiceDaoImpl;
    }

    public void setAccountDetailServiceDaoImpl(IAccountDetailServiceDao accountDetailServiceDaoImpl) {
        this.accountDetailServiceDaoImpl = accountDetailServiceDaoImpl;
    }


}
