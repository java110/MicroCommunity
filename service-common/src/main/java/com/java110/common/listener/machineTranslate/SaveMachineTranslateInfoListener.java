package com.java110.common.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachineTranslatePo;
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
 * 保存 设备同步信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMachineTranslateInfoListener")
@Transactional
public class SaveMachineTranslateInfoListener extends AbstractMachineTranslateBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMachineTranslateInfoListener.class);

    @Autowired
    private IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_TRANSLATE;
    }

    /**
     * 保存设备同步信息 business 表中
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
            Object bObj = data.get(MachineTranslatePo.class.getSimpleName());
            JSONArray businessMachineTranslates = null;
            if (bObj instanceof JSONObject) {
                businessMachineTranslates = new JSONArray();
                businessMachineTranslates.add(bObj);
            } else {
                businessMachineTranslates = (JSONArray) bObj;
            }
            //JSONObject businessMachineTranslate = data.getJSONObject("businessMachineTranslate");
            for (int bMachineTranslateIndex = 0; bMachineTranslateIndex < businessMachineTranslates.size(); bMachineTranslateIndex++) {
                JSONObject businessMachineTranslate = businessMachineTranslates.getJSONObject(bMachineTranslateIndex);
                doBusinessMachineTranslate(business, businessMachineTranslate);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("machineTranslateId", businessMachineTranslate.getString("machineTranslateId"));
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

        //设备同步信息
        List<Map> businessMachineTranslateInfo = machineTranslateServiceDaoImpl.getBusinessMachineTranslateInfo(info);
        if (businessMachineTranslateInfo != null && businessMachineTranslateInfo.size() > 0) {
            reFreshShareColumn(info, businessMachineTranslateInfo.get(0));
            machineTranslateServiceDaoImpl.saveMachineTranslateInfoInstance(info);
            if (businessMachineTranslateInfo.size() == 1) {
                dataFlowContext.addParamOut("machineTranslateId", businessMachineTranslateInfo.get(0).get("machine_translate_id"));
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
        //设备同步信息
        List<Map> machineTranslateInfo = machineTranslateServiceDaoImpl.getMachineTranslateInfo(info);
        if (machineTranslateInfo != null && machineTranslateInfo.size() > 0) {
            reFreshShareColumn(paramIn, machineTranslateInfo.get(0));
            machineTranslateServiceDaoImpl.updateMachineTranslateInfoInstance(paramIn);
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
            //刷新缓存
            //flushMachineTranslateId(business.getDatas());

            businessMachineTranslate.put("machineTranslateId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));

        }

        businessMachineTranslate.put("bId", business.getbId());
        businessMachineTranslate.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备同步信息
        machineTranslateServiceDaoImpl.saveBusinessMachineTranslateInfo(businessMachineTranslate);

    }

    public IMachineTranslateServiceDao getMachineTranslateServiceDaoImpl() {
        return machineTranslateServiceDaoImpl;
    }

    public void setMachineTranslateServiceDaoImpl(IMachineTranslateServiceDao machineTranslateServiceDaoImpl) {
        this.machineTranslateServiceDaoImpl = machineTranslateServiceDaoImpl;
    }
}
