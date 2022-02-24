package com.java110.common.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachineTranslatePo;
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
 * 删除设备同步信息 侦听
 * <p>
 * 处理节点
 * 1、businessMachineTranslate:{} 设备同步基本信息节点
 * 2、businessMachineTranslateAttr:[{}] 设备同步属性信息节点
 * 3、businessMachineTranslatePhoto:[{}] 设备同步照片信息节点
 * 4、businessMachineTranslateCerdentials:[{}] 设备同步证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteMachineTranslateInfoListener")
@Transactional
public class DeleteMachineTranslateInfoListener extends AbstractMachineTranslateBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteMachineTranslateInfoListener.class);
    @Autowired
    IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_TRANSLATE;
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


        //处理 businessMachineTranslate 节点
        if (data.containsKey(MachineTranslatePo.class.getSimpleName())) {
            Object _obj = data.get(MachineTranslatePo.class.getSimpleName());
            JSONArray businessMachineTranslates = null;
            if (_obj instanceof JSONObject) {
                businessMachineTranslates = new JSONArray();
                businessMachineTranslates.add(_obj);
            } else {
                businessMachineTranslates = (JSONArray) _obj;
            }
            //JSONObject businessMachineTranslate = data.getJSONObject("businessMachineTranslate");
            for (int _machineTranslateIndex = 0; _machineTranslateIndex < businessMachineTranslates.size(); _machineTranslateIndex++) {
                JSONObject businessMachineTranslate = businessMachineTranslates.getJSONObject(_machineTranslateIndex);
                doBusinessMachineTranslate(business, businessMachineTranslate);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineTranslateId", businessMachineTranslate.getString("machineTranslateId"));
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

        //设备同步信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //设备同步信息
        List<Map> businessMachineTranslateInfos = machineTranslateServiceDaoImpl.getBusinessMachineTranslateInfo(info);
        if (businessMachineTranslateInfos != null && businessMachineTranslateInfos.size() > 0) {
            for (int _machineTranslateIndex = 0; _machineTranslateIndex < businessMachineTranslateInfos.size(); _machineTranslateIndex++) {
                Map businessMachineTranslateInfo = businessMachineTranslateInfos.get(_machineTranslateIndex);
                flushBusinessMachineTranslateInfo(businessMachineTranslateInfo, StatusConstant.STATUS_CD_INVALID);
                machineTranslateServiceDaoImpl.updateMachineTranslateInfoInstance(businessMachineTranslateInfo);
                dataFlowContext.addParamOut("machineTranslateId", businessMachineTranslateInfo.get("machine_translate_id"));
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
        //设备同步信息
        List<Map> machineTranslateInfo = machineTranslateServiceDaoImpl.getMachineTranslateInfo(info);
        if (machineTranslateInfo != null && machineTranslateInfo.size() > 0) {

            //设备同步信息
            List<Map> businessMachineTranslateInfos = machineTranslateServiceDaoImpl.getBusinessMachineTranslateInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessMachineTranslateInfos == null || businessMachineTranslateInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（machineTranslate），程序内部异常,请检查！ " + delInfo);
            }
            for (int _machineTranslateIndex = 0; _machineTranslateIndex < businessMachineTranslateInfos.size(); _machineTranslateIndex++) {
                Map businessMachineTranslateInfo = businessMachineTranslateInfos.get(_machineTranslateIndex);
                flushBusinessMachineTranslateInfo(businessMachineTranslateInfo, StatusConstant.STATUS_CD_VALID);
                machineTranslateServiceDaoImpl.updateMachineTranslateInfoInstance(businessMachineTranslateInfo);
            }
        }
    }


    /**
     * 处理 businessMachineTranslate 节点
     *
     * @param business                 总的数据节点
     * @param businessMachineTranslate 设备同步节点
     */
    private void doBusinessMachineTranslate(Business business, JSONObject businessMachineTranslate) {

        Assert.jsonObjectHaveKey(businessMachineTranslate, "machineTranslateId", "businessMachineTranslate 节点下没有包含 machineTranslateId 节点");

        if (businessMachineTranslate.getString("machineTranslateId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "machineTranslateId 错误，不能自动生成（必须已经存在的machineTranslateId）" + businessMachineTranslate);
        }
        //自动插入DEL
        autoSaveDelBusinessMachineTranslate(business, businessMachineTranslate);
    }

    public IMachineTranslateServiceDao getMachineTranslateServiceDaoImpl() {
        return machineTranslateServiceDaoImpl;
    }

    public void setMachineTranslateServiceDaoImpl(IMachineTranslateServiceDao machineTranslateServiceDaoImpl) {
        this.machineTranslateServiceDaoImpl = machineTranslateServiceDaoImpl;
    }
}
