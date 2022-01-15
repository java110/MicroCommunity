package com.java110.fee.listener.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IReturnPayFeeServiceDao;
import com.java110.po.fee.ReturnPayFeePo;
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
 * 保存 退费表信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveReturnPayFeeInfoListener")
@Transactional
public class SaveReturnPayFeeInfoListener extends AbstractReturnPayFeeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveReturnPayFeeInfoListener.class);

    @Autowired
    private IReturnPayFeeServiceDao returnPayFeeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_RETURN_PAY_FEE;
    }

    /**
     * 保存退费表信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessReturnPayFee 节点
        if (data.containsKey(ReturnPayFeePo.class.getSimpleName())) {
            Object bObj = data.get(ReturnPayFeePo.class.getSimpleName());
            JSONArray businessReturnPayFees = null;
            if (bObj instanceof JSONObject) {
                businessReturnPayFees = new JSONArray();
                businessReturnPayFees.add(bObj);
            } else {
                businessReturnPayFees = (JSONArray) bObj;
            }
            //JSONObject businessReturnPayFee = data.getJSONObject("businessReturnPayFee");
            for (int bReturnPayFeeIndex = 0; bReturnPayFeeIndex < businessReturnPayFees.size(); bReturnPayFeeIndex++) {
                JSONObject businessReturnPayFee = businessReturnPayFees.getJSONObject(bReturnPayFeeIndex);
                doBusinessReturnPayFee(business, businessReturnPayFee);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("returnFeeId", businessReturnPayFee.getString("returnFeeId"));
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

        //退费表信息
        List<Map> businessReturnPayFeeInfo = returnPayFeeServiceDaoImpl.getBusinessReturnPayFeeInfo(info);
        if (businessReturnPayFeeInfo != null && businessReturnPayFeeInfo.size() > 0) {
            reFreshShareColumn(info, businessReturnPayFeeInfo.get(0));
            returnPayFeeServiceDaoImpl.saveReturnPayFeeInfoInstance(info);
            if (businessReturnPayFeeInfo.size() == 1) {
                dataFlowContext.addParamOut("returnFeeId", businessReturnPayFeeInfo.get(0).get("return_fee_id"));
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
        //退费表信息
        List<Map> returnPayFeeInfo = returnPayFeeServiceDaoImpl.getReturnPayFeeInfo(info);
        if (returnPayFeeInfo != null && returnPayFeeInfo.size() > 0) {
            reFreshShareColumn(paramIn, returnPayFeeInfo.get(0));
            returnPayFeeServiceDaoImpl.updateReturnPayFeeInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessReturnPayFee 节点
     *
     * @param business             总的数据节点
     * @param businessReturnPayFee 退费表节点
     */
    private void doBusinessReturnPayFee(Business business, JSONObject businessReturnPayFee) {

        Assert.jsonObjectHaveKey(businessReturnPayFee, "returnFeeId", "businessReturnPayFee 节点下没有包含 returnFeeId 节点");

        if (businessReturnPayFee.getString("returnFeeId").startsWith("-")) {
            //刷新缓存
            //flushReturnPayFeeId(business.getDatas());

            businessReturnPayFee.put("returnFeeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_returnFeeId));

        }

        businessReturnPayFee.put("bId", business.getbId());
        businessReturnPayFee.put("operate", StatusConstant.OPERATE_ADD);
        //保存退费表信息
        businessReturnPayFee.put("cycles", "-" + businessReturnPayFee.get("cycles"));
        businessReturnPayFee.put("receivableAmount", "-" + businessReturnPayFee.get("receivableAmount"));
        businessReturnPayFee.put("receivedAmount", "-" + businessReturnPayFee.get("receivedAmount"));
        businessReturnPayFee.put("state", "1000");
        returnPayFeeServiceDaoImpl.saveBusinessReturnPayFeeInfo(businessReturnPayFee);

    }

    public IReturnPayFeeServiceDao getReturnPayFeeServiceDaoImpl() {
        return returnPayFeeServiceDaoImpl;
    }

    public void setReturnPayFeeServiceDaoImpl(IReturnPayFeeServiceDao returnPayFeeServiceDaoImpl) {
        this.returnPayFeeServiceDaoImpl = returnPayFeeServiceDaoImpl;
    }
}
