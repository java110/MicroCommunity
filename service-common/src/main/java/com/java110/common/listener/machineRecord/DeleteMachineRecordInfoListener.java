package com.java110.common.listener.machineRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineRecordServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachineRecordPo;
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
 * 删除设备上报信息 侦听
 * <p>
 * 处理节点
 * 1、businessMachineRecord:{} 设备上报基本信息节点
 * 2、businessMachineRecordAttr:[{}] 设备上报属性信息节点
 * 3、businessMachineRecordPhoto:[{}] 设备上报照片信息节点
 * 4、businessMachineRecordCerdentials:[{}] 设备上报证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteMachineRecordInfoListener")
@Transactional
public class DeleteMachineRecordInfoListener extends AbstractMachineRecordBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteMachineRecordInfoListener.class);
    @Autowired
    IMachineRecordServiceDao machineRecordServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_RECORD;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
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
            Object _obj = data.get(MachineRecordPo.class.getSimpleName());
            JSONArray businessMachineRecords = null;
            if (_obj instanceof JSONObject) {
                businessMachineRecords = new JSONArray();
                businessMachineRecords.add(_obj);
            } else {
                businessMachineRecords = (JSONArray) _obj;
            }
            //JSONObject businessMachineRecord = data.getJSONObject("businessMachineRecord");
            for (int _machineRecordIndex = 0; _machineRecordIndex < businessMachineRecords.size(); _machineRecordIndex++) {
                JSONObject businessMachineRecord = businessMachineRecords.getJSONObject(_machineRecordIndex);
                doBusinessMachineRecord(business, businessMachineRecord);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineRecordId", businessMachineRecord.getString("machineRecordId"));
                }
            }
        }


    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //设备上报信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //设备上报信息
        List<Map> businessMachineRecordInfos = machineRecordServiceDaoImpl.getBusinessMachineRecordInfo(info);
        if (businessMachineRecordInfos != null && businessMachineRecordInfos.size() > 0) {
            for (int _machineRecordIndex = 0; _machineRecordIndex < businessMachineRecordInfos.size(); _machineRecordIndex++) {
                Map businessMachineRecordInfo = businessMachineRecordInfos.get(_machineRecordIndex);
                flushBusinessMachineRecordInfo(businessMachineRecordInfo, StatusConstant.STATUS_CD_INVALID);
                machineRecordServiceDaoImpl.updateMachineRecordInfoInstance(businessMachineRecordInfo);
                dataFlowContext.addParamOut("machineRecordId", businessMachineRecordInfo.get("machine_record_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //设备上报信息
        List<Map> machineRecordInfo = machineRecordServiceDaoImpl.getMachineRecordInfo(info);
        if (machineRecordInfo != null && machineRecordInfo.size() > 0) {

            //设备上报信息
            List<Map> businessMachineRecordInfos = machineRecordServiceDaoImpl.getBusinessMachineRecordInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessMachineRecordInfos == null || businessMachineRecordInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（machineRecord），程序内部异常,请检查！ " + delInfo);
            }
            for (int _machineRecordIndex = 0; _machineRecordIndex < businessMachineRecordInfos.size(); _machineRecordIndex++) {
                Map businessMachineRecordInfo = businessMachineRecordInfos.get(_machineRecordIndex);
                flushBusinessMachineRecordInfo(businessMachineRecordInfo, StatusConstant.STATUS_CD_VALID);
                machineRecordServiceDaoImpl.updateMachineRecordInfoInstance(businessMachineRecordInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "machineRecordId 错误，不能自动生成（必须已经存在的machineRecordId）" + businessMachineRecord);
        }
        //自动插入DEL
        autoSaveDelBusinessMachineRecord(business, businessMachineRecord);
    }

    public IMachineRecordServiceDao getMachineRecordServiceDaoImpl() {
        return machineRecordServiceDaoImpl;
    }

    public void setMachineRecordServiceDaoImpl(IMachineRecordServiceDao machineRecordServiceDaoImpl) {
        this.machineRecordServiceDaoImpl = machineRecordServiceDaoImpl;
    }
}
