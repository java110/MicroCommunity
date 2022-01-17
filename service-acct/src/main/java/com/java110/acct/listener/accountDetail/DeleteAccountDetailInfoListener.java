package com.java110.acct.listener.accountDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.acct.dao.IAccountDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除账户交易信息 侦听
 *
 * 处理节点
 * 1、businessAccountDetail:{} 账户交易基本信息节点
 * 2、businessAccountDetailAttr:[{}] 账户交易属性信息节点
 * 3、businessAccountDetailPhoto:[{}] 账户交易照片信息节点
 * 4、businessAccountDetailCerdentials:[{}] 账户交易证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteAccountDetailInfoListener")
@Transactional
public class DeleteAccountDetailInfoListener extends AbstractAccountDetailBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteAccountDetailInfoListener.class);
    @Autowired
    IAccountDetailServiceDao accountDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_ACCT_DETAIL;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

            //处理 businessAccountDetail 节点
            if(data.containsKey(AccountDetailPo.class.getSimpleName())){
                Object _obj = data.get(AccountDetailPo.class.getSimpleName());
                JSONArray businessAccountDetails = null;
                if(_obj instanceof JSONObject){
                    businessAccountDetails = new JSONArray();
                    businessAccountDetails.add(_obj);
                }else {
                    businessAccountDetails = (JSONArray)_obj;
                }
                //JSONObject businessAccountDetail = data.getJSONObject(AccountDetailPo.class.getSimpleName());
                for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetails.size();_accountDetailIndex++) {
                    JSONObject businessAccountDetail = businessAccountDetails.getJSONObject(_accountDetailIndex);
                    doBusinessAccountDetail(business, businessAccountDetail);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("detailId", businessAccountDetail.getString("detailId"));
                    }
                }

        }


    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //账户交易信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //账户交易信息
        List<Map> businessAccountDetailInfos = accountDetailServiceDaoImpl.getBusinessAccountDetailInfo(info);
        if( businessAccountDetailInfos != null && businessAccountDetailInfos.size() >0) {
            for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetailInfos.size();_accountDetailIndex++) {
                Map businessAccountDetailInfo = businessAccountDetailInfos.get(_accountDetailIndex);
                flushBusinessAccountDetailInfo(businessAccountDetailInfo,StatusConstant.STATUS_CD_INVALID);
                accountDetailServiceDaoImpl.updateAccountDetailInfoInstance(businessAccountDetailInfo);
                dataFlowContext.addParamOut("detailId",businessAccountDetailInfo.get("detail_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //账户交易信息
        List<Map> accountDetailInfo = accountDetailServiceDaoImpl.getAccountDetailInfo(info);
        if(accountDetailInfo != null && accountDetailInfo.size() > 0){

            //账户交易信息
            List<Map> businessAccountDetailInfos = accountDetailServiceDaoImpl.getBusinessAccountDetailInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAccountDetailInfos == null ||  businessAccountDetailInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（accountDetail），程序内部异常,请检查！ "+delInfo);
            }
            for (int _accountDetailIndex = 0; _accountDetailIndex < businessAccountDetailInfos.size();_accountDetailIndex++) {
                Map businessAccountDetailInfo = businessAccountDetailInfos.get(_accountDetailIndex);
                flushBusinessAccountDetailInfo(businessAccountDetailInfo,StatusConstant.STATUS_CD_VALID);
                accountDetailServiceDaoImpl.updateAccountDetailInfoInstance(businessAccountDetailInfo);
            }
        }
    }



    /**
     * 处理 businessAccountDetail 节点
     * @param business 总的数据节点
     * @param businessAccountDetail 账户交易节点
     */
    private void doBusinessAccountDetail(Business business,JSONObject businessAccountDetail){

        Assert.jsonObjectHaveKey(businessAccountDetail,"detailId","businessAccountDetail 节点下没有包含 detailId 节点");

        if(businessAccountDetail.getString("detailId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"detailId 错误，不能自动生成（必须已经存在的detailId）"+businessAccountDetail);
        }
        //自动插入DEL
        autoSaveDelBusinessAccountDetail(business,businessAccountDetail);
    }
    @Override
    public IAccountDetailServiceDao getAccountDetailServiceDaoImpl() {
        return accountDetailServiceDaoImpl;
    }

    public void setAccountDetailServiceDaoImpl(IAccountDetailServiceDao accountDetailServiceDaoImpl) {
        this.accountDetailServiceDaoImpl = accountDetailServiceDaoImpl;
    }
}
