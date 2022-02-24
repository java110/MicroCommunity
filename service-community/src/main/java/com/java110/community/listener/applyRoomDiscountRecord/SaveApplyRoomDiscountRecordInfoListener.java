package com.java110.community.listener.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IApplyRoomDiscountRecordServiceDao;
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
 * 保存 验房记录信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveApplyRoomDiscountRecordInfoListener")
@Transactional
public class SaveApplyRoomDiscountRecordInfoListener extends AbstractApplyRoomDiscountRecordBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveApplyRoomDiscountRecordInfoListener.class);

    @Autowired
    private IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLY_ROOM_DISCOUNT_RECORD;
    }

    /**
     * 保存验房记录信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessApplyRoomDiscountRecord 节点
        if (data.containsKey(ApplyRoomDiscountRecordPo.class.getSimpleName())) {
            Object bObj = data.get(ApplyRoomDiscountRecordPo.class.getSimpleName());
            JSONArray businessApplyRoomDiscountRecords = null;
            if (bObj instanceof JSONObject) {
                businessApplyRoomDiscountRecords = new JSONArray();
                businessApplyRoomDiscountRecords.add(bObj);
            } else {
                businessApplyRoomDiscountRecords = (JSONArray) bObj;
            }
            //JSONObject businessApplyRoomDiscountRecord = data.getJSONObject(ApplyRoomDiscountRecordPo.class.getSimpleName());
            for (int bApplyRoomDiscountRecordIndex = 0; bApplyRoomDiscountRecordIndex < businessApplyRoomDiscountRecords.size(); bApplyRoomDiscountRecordIndex++) {
                JSONObject businessApplyRoomDiscountRecord = businessApplyRoomDiscountRecords.getJSONObject(bApplyRoomDiscountRecordIndex);
                doBusinessApplyRoomDiscountRecord(business, businessApplyRoomDiscountRecord);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ardrId", businessApplyRoomDiscountRecord.getString("ardrId"));
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

        //验房记录信息
        List<Map> businessApplyRoomDiscountRecordInfo = applyRoomDiscountRecordServiceDaoImpl.getBusinessApplyRoomDiscountRecordInfo(info);
        if (businessApplyRoomDiscountRecordInfo != null && businessApplyRoomDiscountRecordInfo.size() > 0) {
            reFreshShareColumn(info, businessApplyRoomDiscountRecordInfo.get(0));
            applyRoomDiscountRecordServiceDaoImpl.saveApplyRoomDiscountRecordInfoInstance(info);
            if (businessApplyRoomDiscountRecordInfo.size() == 1) {
                dataFlowContext.addParamOut("ardrId", businessApplyRoomDiscountRecordInfo.get(0).get("ardr_id"));
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
        //验房记录信息
        List<Map> applyRoomDiscountRecordInfo = applyRoomDiscountRecordServiceDaoImpl.getApplyRoomDiscountRecordInfo(info);
        if (applyRoomDiscountRecordInfo != null && applyRoomDiscountRecordInfo.size() > 0) {
            reFreshShareColumn(paramIn, applyRoomDiscountRecordInfo.get(0));
            applyRoomDiscountRecordServiceDaoImpl.updateApplyRoomDiscountRecordInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessApplyRoomDiscountRecord 节点
     *
     * @param business                        总的数据节点
     * @param businessApplyRoomDiscountRecord 验房记录节点
     */
    private void doBusinessApplyRoomDiscountRecord(Business business, JSONObject businessApplyRoomDiscountRecord) {

        Assert.jsonObjectHaveKey(businessApplyRoomDiscountRecord, "ardrId", "businessApplyRoomDiscountRecord 节点下没有包含 ardrId 节点");

        if (businessApplyRoomDiscountRecord.getString("ardrId").startsWith("-")) {
            //刷新缓存
            //flushApplyRoomDiscountRecordId(business.getDatas());

            businessApplyRoomDiscountRecord.put("ardrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ARDRID));

        }

        businessApplyRoomDiscountRecord.put("bId", business.getbId());
        businessApplyRoomDiscountRecord.put("operate", StatusConstant.OPERATE_ADD);
        //保存验房记录信息
        applyRoomDiscountRecordServiceDaoImpl.saveBusinessApplyRoomDiscountRecordInfo(businessApplyRoomDiscountRecord);

    }

    @Override
    public IApplyRoomDiscountRecordServiceDao getApplyRoomDiscountRecordServiceDaoImpl() {
        return applyRoomDiscountRecordServiceDaoImpl;
    }

    public void setApplyRoomDiscountRecordServiceDaoImpl(IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl) {
        this.applyRoomDiscountRecordServiceDaoImpl = applyRoomDiscountRecordServiceDaoImpl;
    }
}
