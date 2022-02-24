package com.java110.common.listener.machineAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.machine.MachineAttrPo;
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
 * 保存 设备属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMachineAttrInfoListener")
@Transactional
public class SaveMachineAttrInfoListener extends AbstractMachineAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMachineAttrInfoListener.class);

    @Autowired
    private IMachineAttrServiceDao machineAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE;
    }

    /**
     * 保存设备属性信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMachineAttr 节点
        if (data.containsKey(MachineAttrPo.class.getSimpleName())) {
            Object bObj = data.get(MachineAttrPo.class.getSimpleName());
            JSONArray businessMachineAttrs = null;
            if (bObj instanceof JSONObject) {
                businessMachineAttrs = new JSONArray();
                businessMachineAttrs.add(bObj);
            } else {
                businessMachineAttrs = (JSONArray) bObj;
            }
            //JSONObject businessMachineAttr = data.getJSONObject("businessMachineAttr");
            for (int bMachineAttrIndex = 0; bMachineAttrIndex < businessMachineAttrs.size(); bMachineAttrIndex++) {
                JSONObject businessMachineAttr = businessMachineAttrs.getJSONObject(bMachineAttrIndex);
                doBusinessMachineAttr(business, businessMachineAttr);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessMachineAttr.getString("attrId"));
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

        //设备属性信息
        List<Map> businessMachineAttrInfo = machineAttrServiceDaoImpl.getBusinessMachineAttrInfo(info);
        if (businessMachineAttrInfo != null && businessMachineAttrInfo.size() > 0) {
            reFreshShareColumn(info, businessMachineAttrInfo.get(0));
            machineAttrServiceDaoImpl.saveMachineAttrInfoInstance(info);
            if (businessMachineAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessMachineAttrInfo.get(0).get("attr_id"));
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
        //设备属性信息
        List<Map> machineAttrInfo = machineAttrServiceDaoImpl.getMachineAttrInfo(info);
        if (machineAttrInfo != null && machineAttrInfo.size() > 0) {
            reFreshShareColumn(paramIn, machineAttrInfo.get(0));
            machineAttrServiceDaoImpl.updateMachineAttrInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMachineAttr 节点
     *
     * @param business            总的数据节点
     * @param businessMachineAttr 设备属性节点
     */
    private void doBusinessMachineAttr(Business business, JSONObject businessMachineAttr) {

        Assert.jsonObjectHaveKey(businessMachineAttr, "attrId", "businessMachineAttr 节点下没有包含 attrId 节点");

        if (businessMachineAttr.getString("attrId").startsWith("-")) {
            //刷新缓存
            //flushMachineAttrId(business.getDatas());

            businessMachineAttr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessMachineAttr.put("bId", business.getbId());
        businessMachineAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备属性信息
        machineAttrServiceDaoImpl.saveBusinessMachineAttrInfo(businessMachineAttr);

    }

    public IMachineAttrServiceDao getMachineAttrServiceDaoImpl() {
        return machineAttrServiceDaoImpl;
    }

    public void setMachineAttrServiceDaoImpl(IMachineAttrServiceDao machineAttrServiceDaoImpl) {
        this.machineAttrServiceDaoImpl = machineAttrServiceDaoImpl;
    }
}
