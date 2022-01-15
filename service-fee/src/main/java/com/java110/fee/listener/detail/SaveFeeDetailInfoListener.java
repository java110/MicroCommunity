package com.java110.fee.listener.detail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.fee.dao.IFeeDetailServiceDao;
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
 * 保存 费用明细信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFeeDetailInfoListener")
@Transactional
public class SaveFeeDetailInfoListener extends AbstractFeeDetailBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveFeeDetailInfoListener.class);

    @Autowired
    private IFeeDetailServiceDao feeDetailServiceDaoImpl;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL;
    }

    /**
     * 保存费用明细信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessFeeDetail 节点
        if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            JSONArray businessFeeDetails = null;
            if (bObj instanceof JSONObject) {
                businessFeeDetails = new JSONArray();
                businessFeeDetails.add(bObj);
            } else {
                businessFeeDetails = (JSONArray) bObj;
            }
            //JSONObject businessFeeDetail = data.getJSONObject("businessFeeDetail");
            for (int bFeeDetailIndex = 0; bFeeDetailIndex < businessFeeDetails.size(); bFeeDetailIndex++) {
                JSONObject businessFeeDetail = businessFeeDetails.getJSONObject(bFeeDetailIndex);
                doBusinessFeeDetail(business, businessFeeDetail);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailId", businessFeeDetail.getString("detailId"));
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

        //费用明细信息
        List<Map> businessFeeDetailInfo = feeDetailServiceDaoImpl.getBusinessFeeDetailInfo(info);
        if (businessFeeDetailInfo != null && businessFeeDetailInfo.size() > 0) {
            reFreshShareColumn(info, businessFeeDetailInfo.get(0));
            feeDetailServiceDaoImpl.saveFeeDetailInfoInstance(info);

            //更新优惠申请
            ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
            applyRoomDiscountDto.setbId(business.getbId());
            List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
            if (applyRoomDiscountDtos != null && applyRoomDiscountDtos.size() == 1) {
                ApplyRoomDiscountPo applyRoomDiscountPo = new ApplyRoomDiscountPo();
                applyRoomDiscountPo.setArdId(applyRoomDiscountDtos.get(0).getArdId());
                applyRoomDiscountPo.setInUse("1");
                applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);
            }
            if (businessFeeDetailInfo.size() == 1) {
                dataFlowContext.addParamOut("detailId", businessFeeDetailInfo.get(0).get("detail_id"));
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
        //费用明细信息
        List<Map> feeDetailInfo = feeDetailServiceDaoImpl.getFeeDetailInfo(info);
        if (feeDetailInfo != null && feeDetailInfo.size() > 0) {
            reFreshShareColumn(paramIn, feeDetailInfo.get(0));
            feeDetailServiceDaoImpl.updateFeeDetailInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessFeeDetail 节点
     *
     * @param business          总的数据节点
     * @param businessFeeDetail 费用明细节点
     */
    private void doBusinessFeeDetail(Business business, JSONObject businessFeeDetail) {

        Assert.jsonObjectHaveKey(businessFeeDetail, "detailId", "businessFeeDetail 节点下没有包含 detailId 节点");

        if (businessFeeDetail.getString("detailId").startsWith("-")) {
            //刷新缓存
            //flushFeeDetailId(business.getDatas());

            businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

        }

        businessFeeDetail.put("bId", business.getbId());
        businessFeeDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存费用明细信息
        feeDetailServiceDaoImpl.saveBusinessFeeDetailInfo(businessFeeDetail);

    }

    public IFeeDetailServiceDao getFeeDetailServiceDaoImpl() {
        return feeDetailServiceDaoImpl;
    }

    public void setFeeDetailServiceDaoImpl(IFeeDetailServiceDao feeDetailServiceDaoImpl) {
        this.feeDetailServiceDaoImpl = feeDetailServiceDaoImpl;
    }
}
