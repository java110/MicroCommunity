package com.java110.common.listener.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachinePo;
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
 * 修改设备信息 侦听
 * <p>
 * 处理节点
 * 1、businessMachine:{} 设备基本信息节点
 * 2、businessMachineAttr:[{}] 设备属性信息节点
 * 3、businessMachinePhoto:[{}] 设备照片信息节点
 * 4、businessMachineCerdentials:[{}] 设备证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateMachineInfoListener")
@Transactional
public class UpdateMachineInfoListener extends AbstractMachineBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateMachineInfoListener.class);
    @Autowired
    private IMachineServiceDao machineServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE;
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


        //处理 businessMachine 节点
        if (data.containsKey(MachinePo.class.getSimpleName())) {
            Object _obj = data.get(MachinePo.class.getSimpleName());
            JSONArray businessMachines = null;
            if (_obj instanceof JSONObject) {
                businessMachines = new JSONArray();
                businessMachines.add(_obj);
            } else {
                businessMachines = (JSONArray) _obj;
            }
            //JSONObject businessMachine = data.getJSONObject("businessMachine");
            for (int _machineIndex = 0; _machineIndex < businessMachines.size(); _machineIndex++) {
                JSONObject businessMachine = businessMachines.getJSONObject(_machineIndex);
                doBusinessMachine(business, businessMachine);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineId", businessMachine.getString("machineId"));
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

        //设备信息
        List<Map> businessMachineInfos = machineServiceDaoImpl.getBusinessMachineInfo(info);
        if (businessMachineInfos != null && businessMachineInfos.size() > 0) {
            for (int _machineIndex = 0; _machineIndex < businessMachineInfos.size(); _machineIndex++) {
                Map businessMachineInfo = businessMachineInfos.get(_machineIndex);
                flushBusinessMachineInfo(businessMachineInfo, StatusConstant.STATUS_CD_VALID);
                machineServiceDaoImpl.updateMachineInfoInstance(businessMachineInfo);
                if (businessMachineInfo.size() == 1) {
                    dataFlowContext.addParamOut("machineId", businessMachineInfo.get("machine_id"));
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
        //设备信息
        List<Map> machineInfo = machineServiceDaoImpl.getMachineInfo(info);
        if (machineInfo != null && machineInfo.size() > 0) {

            //设备信息
            List<Map> businessMachineInfos = machineServiceDaoImpl.getBusinessMachineInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessMachineInfos == null || businessMachineInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（machine），程序内部异常,请检查！ " + delInfo);
            }
            for (int _machineIndex = 0; _machineIndex < businessMachineInfos.size(); _machineIndex++) {
                Map businessMachineInfo = businessMachineInfos.get(_machineIndex);
                flushBusinessMachineInfo(businessMachineInfo, StatusConstant.STATUS_CD_VALID);
                machineServiceDaoImpl.updateMachineInfoInstance(businessMachineInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "machineId 错误，不能自动生成（必须已经存在的machineId）" + businessMachine);
        }
        //自动保存DEL
        autoSaveDelBusinessMachine(business, businessMachine);

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
