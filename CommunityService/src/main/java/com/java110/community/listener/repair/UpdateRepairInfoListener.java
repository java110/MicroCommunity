package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改报修信息信息 侦听
 * <p>
 * 处理节点
 * 1、businessRepair:{} 报修信息基本信息节点
 * 2、businessRepairAttr:[{}] 报修信息属性信息节点
 * 3、businessRepairPhoto:[{}] 报修信息照片信息节点
 * 4、businessRepairCerdentials:[{}] 报修信息证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateRepairInfoListener")
@Transactional
public class UpdateRepairInfoListener extends AbstractRepairBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateRepairInfoListener.class);
    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR;
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


        //处理 businessRepair 节点
        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR)) {
            Object _obj = data.get(BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
            JSONArray businessRepairs = null;
            if (_obj instanceof JSONObject) {
                businessRepairs = new JSONArray();
                businessRepairs.add(_obj);
            } else {
                businessRepairs = (JSONArray) _obj;
            }
            //JSONObject businessRepair = data.getJSONObject("businessRepair");
            for (int _repairIndex = 0; _repairIndex < businessRepairs.size(); _repairIndex++) {
                JSONObject businessRepair = businessRepairs.getJSONObject(_repairIndex);
                doBusinessRepair(business, businessRepair);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("repairId", businessRepair.getString("repairId"));
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

        //报修信息信息
        List<Map> businessRepairInfos = repairServiceDaoImpl.getBusinessRepairInfo(info);
        if (businessRepairInfos != null && businessRepairInfos.size() > 0) {
            for (int _repairIndex = 0; _repairIndex < businessRepairInfos.size(); _repairIndex++) {
                Map businessRepairInfo = businessRepairInfos.get(_repairIndex);
                flushBusinessRepairInfo(businessRepairInfo, StatusConstant.STATUS_CD_VALID);
                repairServiceDaoImpl.updateRepairInfoInstance(businessRepairInfo);
                if (businessRepairInfo.size() == 1) {
                    dataFlowContext.addParamOut("repairId", businessRepairInfo.get("repair_id"));
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
        //报修信息信息
        List<Map> repairInfo = repairServiceDaoImpl.getRepairInfo(info);
        if (repairInfo != null && repairInfo.size() > 0) {

            //报修信息信息
            List<Map> businessRepairInfos = repairServiceDaoImpl.getBusinessRepairInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessRepairInfos == null || businessRepairInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（repair），程序内部异常,请检查！ " + delInfo);
            }
            for (int _repairIndex = 0; _repairIndex < businessRepairInfos.size(); _repairIndex++) {
                Map businessRepairInfo = businessRepairInfos.get(_repairIndex);
                flushBusinessRepairInfo(businessRepairInfo, StatusConstant.STATUS_CD_VALID);
                repairServiceDaoImpl.updateRepairInfoInstance(businessRepairInfo);
            }
        }

    }


    /**
     * 处理 businessRepair 节点
     *
     * @param business       总的数据节点
     * @param businessRepair 报修信息节点
     */
    private void doBusinessRepair(Business business, JSONObject businessRepair) {

        Assert.jsonObjectHaveKey(businessRepair, "repairId", "businessRepair 节点下没有包含 repairId 节点");

        if (businessRepair.getString("repairId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "repairId 错误，不能自动生成（必须已经存在的repairId）" + businessRepair);
        }
        //自动保存DEL
        autoSaveDelBusinessRepair(business, businessRepair);

        businessRepair.put("bId", business.getbId());
        businessRepair.put("operate", StatusConstant.OPERATE_ADD);
        //保存报修信息信息
        repairServiceDaoImpl.saveBusinessRepairInfo(businessRepair);

    }


    public IRepairServiceDao getRepairServiceDaoImpl() {
        return repairServiceDaoImpl;
    }

    public void setRepairServiceDaoImpl(IRepairServiceDao repairServiceDaoImpl) {
        this.repairServiceDaoImpl = repairServiceDaoImpl;
    }


}
