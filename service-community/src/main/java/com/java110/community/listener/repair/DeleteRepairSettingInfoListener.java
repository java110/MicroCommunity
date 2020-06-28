package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairSettingServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.repair.RepairSettingPo;
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
 * 删除报修设置信息 侦听
 * <p>
 * 处理节点
 * 1、businessRepairSetting:{} 报修设置基本信息节点
 * 2、businessRepairSettingAttr:[{}] 报修设置属性信息节点
 * 3、businessRepairSettingPhoto:[{}] 报修设置照片信息节点
 * 4、businessRepairSettingCerdentials:[{}] 报修设置证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteRepairSettingInfoListener")
@Transactional
public class DeleteRepairSettingInfoListener extends AbstractRepairSettingBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteRepairSettingInfoListener.class);
    @Autowired
    IRepairSettingServiceDao repairSettingServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_SETTING;
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

        //处理 businessRepairSetting 节点
        if (data.containsKey(RepairSettingPo.class.getSimpleName())) {
            Object _obj = data.get(RepairSettingPo.class.getSimpleName());
            JSONArray businessRepairSettings = null;
            if (_obj instanceof JSONObject) {
                businessRepairSettings = new JSONArray();
                businessRepairSettings.add(_obj);
            } else {
                businessRepairSettings = (JSONArray) _obj;
            }
            //JSONObject businessRepairSetting = data.getJSONObject(RepairSettingPo.class.getSimpleName());
            for (int _repairSettingIndex = 0; _repairSettingIndex < businessRepairSettings.size(); _repairSettingIndex++) {
                JSONObject businessRepairSetting = businessRepairSettings.getJSONObject(_repairSettingIndex);
                doBusinessRepairSetting(business, businessRepairSetting);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("settingId", businessRepairSetting.getString("settingId"));
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

        //报修设置信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //报修设置信息
        List<Map> businessRepairSettingInfos = repairSettingServiceDaoImpl.getBusinessRepairSettingInfo(info);
        if (businessRepairSettingInfos != null && businessRepairSettingInfos.size() > 0) {
            for (int _repairSettingIndex = 0; _repairSettingIndex < businessRepairSettingInfos.size(); _repairSettingIndex++) {
                Map businessRepairSettingInfo = businessRepairSettingInfos.get(_repairSettingIndex);
                flushBusinessRepairSettingInfo(businessRepairSettingInfo, StatusConstant.STATUS_CD_INVALID);
                repairSettingServiceDaoImpl.updateRepairSettingInfoInstance(businessRepairSettingInfo);
                dataFlowContext.addParamOut("settingId", businessRepairSettingInfo.get("setting_id"));
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
        //报修设置信息
        List<Map> repairSettingInfo = repairSettingServiceDaoImpl.getRepairSettingInfo(info);
        if (repairSettingInfo != null && repairSettingInfo.size() > 0) {

            //报修设置信息
            List<Map> businessRepairSettingInfos = repairSettingServiceDaoImpl.getBusinessRepairSettingInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessRepairSettingInfos == null || businessRepairSettingInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（repairSetting），程序内部异常,请检查！ " + delInfo);
            }
            for (int _repairSettingIndex = 0; _repairSettingIndex < businessRepairSettingInfos.size(); _repairSettingIndex++) {
                Map businessRepairSettingInfo = businessRepairSettingInfos.get(_repairSettingIndex);
                flushBusinessRepairSettingInfo(businessRepairSettingInfo, StatusConstant.STATUS_CD_VALID);
                repairSettingServiceDaoImpl.updateRepairSettingInfoInstance(businessRepairSettingInfo);
            }
        }
    }


    /**
     * 处理 businessRepairSetting 节点
     *
     * @param business              总的数据节点
     * @param businessRepairSetting 报修设置节点
     */
    private void doBusinessRepairSetting(Business business, JSONObject businessRepairSetting) {

        Assert.jsonObjectHaveKey(businessRepairSetting, "settingId", "businessRepairSetting 节点下没有包含 settingId 节点");

        if (businessRepairSetting.getString("settingId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "settingId 错误，不能自动生成（必须已经存在的settingId）" + businessRepairSetting);
        }
        //自动插入DEL
        autoSaveDelBusinessRepairSetting(business, businessRepairSetting);
    }

    @Override
    public IRepairSettingServiceDao getRepairSettingServiceDaoImpl() {
        return repairSettingServiceDaoImpl;
    }

    public void setRepairSettingServiceDaoImpl(IRepairSettingServiceDao repairSettingServiceDaoImpl) {
        this.repairSettingServiceDaoImpl = repairSettingServiceDaoImpl;
    }
}
