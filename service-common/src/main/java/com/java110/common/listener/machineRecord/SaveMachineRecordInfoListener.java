package com.java110.common.listener.machineRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineRecordServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachineRecordPo;
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
 * 保存 设备上报信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMachineRecordInfoListener")
@Transactional
public class SaveMachineRecordInfoListener extends AbstractMachineRecordBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMachineRecordInfoListener.class);

    @Autowired
    private IMachineRecordServiceDao machineRecordServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_RECORD;
    }

    /**
     * 保存设备上报信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMachineRecord 节点
        if (data.containsKey(MachineRecordPo.class.getSimpleName())) {
            Object bObj = data.get(MachineRecordPo.class.getSimpleName());
            JSONArray businessMachineRecords = null;
            if (bObj instanceof JSONObject) {
                businessMachineRecords = new JSONArray();
                businessMachineRecords.add(bObj);
            } else {
                businessMachineRecords = (JSONArray) bObj;
            }
            //JSONObject businessMachineRecord = data.getJSONObject("businessMachineRecord");
            for (int bMachineRecordIndex = 0; bMachineRecordIndex < businessMachineRecords.size(); bMachineRecordIndex++) {
                JSONObject businessMachineRecord = businessMachineRecords.getJSONObject(bMachineRecordIndex);
                doBusinessMachineRecord(business, businessMachineRecord);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineRecordId", businessMachineRecord.getString("machineRecordId"));
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

        //设备上报信息
        List<Map> businessMachineRecordInfo = machineRecordServiceDaoImpl.getBusinessMachineRecordInfo(info);
        if (businessMachineRecordInfo != null && businessMachineRecordInfo.size() > 0) {
            reFreshShareColumn(info, businessMachineRecordInfo.get(0));
            machineRecordServiceDaoImpl.saveMachineRecordInfoInstance(info);
            if (businessMachineRecordInfo.size() == 1) {
                dataFlowContext.addParamOut("machineRecordId", businessMachineRecordInfo.get(0).get("machine_record_id"));
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
        //设备上报信息
        List<Map> machineRecordInfo = machineRecordServiceDaoImpl.getMachineRecordInfo(info);
        if (machineRecordInfo != null && machineRecordInfo.size() > 0) {
            reFreshShareColumn(paramIn, machineRecordInfo.get(0));
            machineRecordServiceDaoImpl.updateMachineRecordInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMachineRecord 节点
     *
     * @param business              总的数据节点
     * @param businessMachineRecord 设备上报节点
     */
    private void doBusinessMachineRecord(Business business, JSONObject businessMachineRecord) {

        Assert.jsonObjectHaveKey(businessMachineRecord, "machineRecordId", "businessMachineRecord 节点下没有包含 machineRecordId 节点");

        if (businessMachineRecord.getString("machineRecordId").startsWith("-")) {
            //刷新缓存
            //flushMachineRecordId(business.getDatas());

            businessMachineRecord.put("machineRecordId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));

        }

        businessMachineRecord.put("bId", business.getbId());
        businessMachineRecord.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备上报信息
        machineRecordServiceDaoImpl.saveBusinessMachineRecordInfo(businessMachineRecord);

    }

    public IMachineRecordServiceDao getMachineRecordServiceDaoImpl() {
        return machineRecordServiceDaoImpl;
    }

    public void setMachineRecordServiceDaoImpl(IMachineRecordServiceDao machineRecordServiceDaoImpl) {
        this.machineRecordServiceDaoImpl = machineRecordServiceDaoImpl;
    }
}
