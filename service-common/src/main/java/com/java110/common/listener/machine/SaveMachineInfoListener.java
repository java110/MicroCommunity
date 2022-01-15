package com.java110.common.listener.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachinePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 设备信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMachineInfoListener")
@Transactional
public class SaveMachineInfoListener extends AbstractMachineBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMachineInfoListener.class);

    @Autowired
    private IMachineServiceDao machineServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE;
    }

    /**
     * 保存设备信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMachine 节点
        if (data.containsKey(MachinePo.class.getSimpleName())) {
            Object bObj = data.get(MachinePo.class.getSimpleName());
            JSONArray businessMachines = null;
            if (bObj instanceof JSONObject) {
                businessMachines = new JSONArray();
                businessMachines.add(bObj);
            } else {
                businessMachines = (JSONArray) bObj;
            }
            //JSONObject businessMachine = data.getJSONObject("businessMachine");
            for (int bMachineIndex = 0; bMachineIndex < businessMachines.size(); bMachineIndex++) {
                JSONObject businessMachine = businessMachines.getJSONObject(bMachineIndex);
                doBusinessMachine(business, businessMachine);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineId", businessMachine.getString("machineId"));
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

        //设备信息
        List<Map> businessMachineInfo = machineServiceDaoImpl.getBusinessMachineInfo(info);
        if (businessMachineInfo != null && businessMachineInfo.size() > 0) {
            reFreshShareColumn(info, businessMachineInfo.get(0));
            machineServiceDaoImpl.saveMachineInfoInstance(info);
            if (businessMachineInfo.size() == 1) {
                dataFlowContext.addParamOut("machineId", businessMachineInfo.get(0).get("machine_id"));
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
        //设备信息
        List<Map> machineInfo = machineServiceDaoImpl.getMachineInfo(info);
        if (machineInfo != null && machineInfo.size() > 0) {
            reFreshShareColumn(paramIn, machineInfo.get(0));
            machineServiceDaoImpl.updateMachineInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMachine 节点
     *
     * @param business        总的数据节点
     * @param businessMachine 设备节点
     */
    private void doBusinessMachine(Business business, JSONObject businessMachine) {

        Assert.jsonObjectHaveKey(businessMachine, "machineId", "businessMachine 节点下没有包含 machineId 节点");

        if (businessMachine.getString("machineId").startsWith("-")) {
            //刷新缓存
            //flushMachineId(business.getDatas());

            businessMachine.put("machineId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineId));

        }

        businessMachine.put("heartbeatTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

        businessMachine.put("bId", business.getbId());
        businessMachine.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备信息
        machineServiceDaoImpl.saveBusinessMachineInfo(businessMachine);

    }

    public IMachineServiceDao getMachineServiceDaoImpl() {
        return machineServiceDaoImpl;
    }

    public void setMachineServiceDaoImpl(IMachineServiceDao machineServiceDaoImpl) {
        this.machineServiceDaoImpl = machineServiceDaoImpl;
    }
}
