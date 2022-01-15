package com.java110.fee.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IFeeServiceDao;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
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
 * 保存 费用信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFeeInfoListener")
@Transactional
public class SaveFeeInfoListener extends AbstractFeeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveFeeInfoListener.class);

    @Autowired
    private IFeeServiceDao feeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO;
    }

    /**
     * 保存费用信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessFee 节点
        if (data.containsKey(PayFeePo.class.getSimpleName())) {
            Object bObj = data.get(PayFeePo.class.getSimpleName());
            JSONArray businessFees = null;
            if (bObj instanceof JSONObject) {
                businessFees = new JSONArray();
                businessFees.add(bObj);
            } else {
                businessFees = (JSONArray) bObj;
            }
            //JSONObject businessFee = data.getJSONObject("businessFee");
            for (int bFeeIndex = 0; bFeeIndex < businessFees.size(); bFeeIndex++) {
                JSONObject businessFee = businessFees.getJSONObject(bFeeIndex);
                doBusinessFee(business, businessFee);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("feeId", businessFee.getString("feeId"));
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

        //费用信息
        List<Map> businessFeeInfo = feeServiceDaoImpl.getBusinessFeeInfo(info);
        if (businessFeeInfo != null && businessFeeInfo.size() > 0) {
            reFreshShareColumn(info, businessFeeInfo.get(0));
            feeServiceDaoImpl.saveFeeInfoInstance(info);
            if (businessFeeInfo.size() == 1) {
                dataFlowContext.addParamOut("feeId", businessFeeInfo.get(0).get("fee_id"));
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

        if (info.containsKey("feeId")) {
            return;
        }

        if (!businessInfo.containsKey("fee_id")) {
            return;
        }

        info.put("feeId", businessInfo.get("fee_id"));
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
        //费用信息
        List<Map> feeInfo = feeServiceDaoImpl.getFeeInfo(info);
        if (feeInfo != null && feeInfo.size() > 0) {
            reFreshShareColumn(paramIn, feeInfo.get(0));
            feeServiceDaoImpl.updateFeeInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessFee 节点
     *
     * @param business    总的数据节点
     * @param businessFee 费用节点
     */
    private void doBusinessFee(Business business, JSONObject businessFee) {

        Assert.jsonObjectHaveKey(businessFee, "feeId", "businessFee 节点下没有包含 feeId 节点");

        if (businessFee.getString("feeId").startsWith("-")) {
            //刷新缓存
            flushFeeId(businessFee, business.getDatas());

            //businessFee.put("feeId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));

        }

        businessFee.put("bId", business.getbId());
        businessFee.put("operate", StatusConstant.OPERATE_ADD);
        //保存费用信息
        feeServiceDaoImpl.saveBusinessFeeInfo(businessFee);

    }


    /**
     * 刷新 小区ID
     *
     * @param businessFee
     */
    private void flushFeeId(JSONObject businessFee, JSONObject data) {

        String feeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId);

        businessFee.put("feeId", feeId);
        //刷费用属性
        if (data.containsKey(FeeAttrPo.class.getSimpleName())) {
            JSONArray businessFeeAttrs = data.getJSONArray(FeeAttrPo.class.getSimpleName());
            for (int businessFeeAttrIndex = 0; businessFeeAttrIndex < businessFeeAttrs.size(); businessFeeAttrIndex++) {
                JSONObject businessFeeAttr = businessFeeAttrs.getJSONObject(businessFeeAttrIndex);
                businessFeeAttr.put("feeId", feeId);
            }
        }

    }

    public IFeeServiceDao getFeeServiceDaoImpl() {
        return feeServiceDaoImpl;
    }

    public void setFeeServiceDaoImpl(IFeeServiceDao feeServiceDaoImpl) {
        this.feeServiceDaoImpl = feeServiceDaoImpl;
    }
}
