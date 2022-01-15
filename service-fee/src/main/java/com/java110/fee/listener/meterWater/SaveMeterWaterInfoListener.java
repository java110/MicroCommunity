package com.java110.fee.listener.meterWater;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IMeterWaterServiceDao;
import com.java110.po.meterWater.MeterWaterPo;
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
 * 保存 水电费信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMeterWaterInfoListener")
@Transactional
public class SaveMeterWaterInfoListener extends AbstractMeterWaterBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMeterWaterInfoListener.class);

    @Autowired
    private IMeterWaterServiceDao meterWaterServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_METER_WATER;
    }

    /**
     * 保存水电费信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMeterWater 节点
        if (data.containsKey(MeterWaterPo.class.getSimpleName())) {
            Object bObj = data.get(MeterWaterPo.class.getSimpleName());
            JSONArray businessMeterWaters = null;
            if (bObj instanceof JSONObject) {
                businessMeterWaters = new JSONArray();
                businessMeterWaters.add(bObj);
            } else {
                businessMeterWaters = (JSONArray) bObj;
            }
            //JSONObject businessMeterWater = data.getJSONObject(MeterWaterPo.class.getSimpleName());
            for (int bMeterWaterIndex = 0; bMeterWaterIndex < businessMeterWaters.size(); bMeterWaterIndex++) {
                JSONObject businessMeterWater = businessMeterWaters.getJSONObject(bMeterWaterIndex);
                doBusinessMeterWater(business, businessMeterWater);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("waterId", businessMeterWater.getString("waterId"));
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

        //水电费信息
        List<Map> businessMeterWaterInfo = meterWaterServiceDaoImpl.getBusinessMeterWaterInfo(info);
        if (businessMeterWaterInfo != null && businessMeterWaterInfo.size() > 0) {
            reFreshShareColumn(info, businessMeterWaterInfo.get(0));
            meterWaterServiceDaoImpl.saveMeterWaterInfoInstance(info);
            if (businessMeterWaterInfo.size() == 1) {
                dataFlowContext.addParamOut("waterId", businessMeterWaterInfo.get(0).get("water_id"));
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
        //水电费信息
        List<Map> meterWaterInfo = meterWaterServiceDaoImpl.getMeterWaterInfo(info);
        if (meterWaterInfo != null && meterWaterInfo.size() > 0) {
            reFreshShareColumn(paramIn, meterWaterInfo.get(0));
            meterWaterServiceDaoImpl.updateMeterWaterInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMeterWater 节点
     *
     * @param business           总的数据节点
     * @param businessMeterWater 水电费节点
     */
    private void doBusinessMeterWater(Business business, JSONObject businessMeterWater) {

        Assert.jsonObjectHaveKey(businessMeterWater, "waterId", "businessMeterWater 节点下没有包含 waterId 节点");

        if (businessMeterWater.getString("waterId").startsWith("-")) {
            //刷新缓存
            //flushMeterWaterId(business.getDatas());

            businessMeterWater.put("waterId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_waterId));

        }

        businessMeterWater.put("bId", business.getbId());
        businessMeterWater.put("operate", StatusConstant.OPERATE_ADD);
        //保存水电费信息
        meterWaterServiceDaoImpl.saveBusinessMeterWaterInfo(businessMeterWater);

    }

    @Override
    public IMeterWaterServiceDao getMeterWaterServiceDaoImpl() {
        return meterWaterServiceDaoImpl;
    }

    public void setMeterWaterServiceDaoImpl(IMeterWaterServiceDao meterWaterServiceDaoImpl) {
        this.meterWaterServiceDaoImpl = meterWaterServiceDaoImpl;
    }
}
