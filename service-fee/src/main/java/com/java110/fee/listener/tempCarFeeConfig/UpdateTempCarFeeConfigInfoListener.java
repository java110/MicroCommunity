package com.java110.fee.listener.tempCarFeeConfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
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
 * 修改临时车收费标准信息 侦听
 * <p>
 * 处理节点
 * 1、businessTempCarFeeConfig:{} 临时车收费标准基本信息节点
 * 2、businessTempCarFeeConfigAttr:[{}] 临时车收费标准属性信息节点
 * 3、businessTempCarFeeConfigPhoto:[{}] 临时车收费标准照片信息节点
 * 4、businessTempCarFeeConfigCerdentials:[{}] 临时车收费标准证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateTempCarFeeConfigInfoListener")
@Transactional
public class UpdateTempCarFeeConfigInfoListener extends AbstractTempCarFeeConfigBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateTempCarFeeConfigInfoListener.class);
    @Autowired
    private ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_INFO;
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


        //处理 businessTempCarFeeConfig 节点
        if (data.containsKey(TempCarFeeConfigPo.class.getSimpleName())) {
            Object _obj = data.get(TempCarFeeConfigPo.class.getSimpleName());
            JSONArray businessTempCarFeeConfigs = null;
            if (_obj instanceof JSONObject) {
                businessTempCarFeeConfigs = new JSONArray();
                businessTempCarFeeConfigs.add(_obj);
            } else {
                businessTempCarFeeConfigs = (JSONArray) _obj;
            }
            //JSONObject businessTempCarFeeConfig = data.getJSONObject(TempCarFeeConfigPo.class.getSimpleName());
            for (int _tempCarFeeConfigIndex = 0; _tempCarFeeConfigIndex < businessTempCarFeeConfigs.size(); _tempCarFeeConfigIndex++) {
                JSONObject businessTempCarFeeConfig = businessTempCarFeeConfigs.getJSONObject(_tempCarFeeConfigIndex);
                doBusinessTempCarFeeConfig(business, businessTempCarFeeConfig);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("configId", businessTempCarFeeConfig.getString("configId"));
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

        //临时车收费标准信息
        List<Map> businessTempCarFeeConfigInfos = tempCarFeeConfigServiceDaoImpl.getBusinessTempCarFeeConfigInfo(info);
        if (businessTempCarFeeConfigInfos != null && businessTempCarFeeConfigInfos.size() > 0) {
            for (int _tempCarFeeConfigIndex = 0; _tempCarFeeConfigIndex < businessTempCarFeeConfigInfos.size(); _tempCarFeeConfigIndex++) {
                Map businessTempCarFeeConfigInfo = businessTempCarFeeConfigInfos.get(_tempCarFeeConfigIndex);
                flushBusinessTempCarFeeConfigInfo(businessTempCarFeeConfigInfo, StatusConstant.STATUS_CD_VALID);
                tempCarFeeConfigServiceDaoImpl.updateTempCarFeeConfigInfoInstance(businessTempCarFeeConfigInfo);
                if (businessTempCarFeeConfigInfo.size() == 1) {
                    dataFlowContext.addParamOut("configId", businessTempCarFeeConfigInfo.get("config_id"));
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
        //临时车收费标准信息
        List<Map> tempCarFeeConfigInfo = tempCarFeeConfigServiceDaoImpl.getTempCarFeeConfigInfo(info);
        if (tempCarFeeConfigInfo != null && tempCarFeeConfigInfo.size() > 0) {

            //临时车收费标准信息
            List<Map> businessTempCarFeeConfigInfos = tempCarFeeConfigServiceDaoImpl.getBusinessTempCarFeeConfigInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessTempCarFeeConfigInfos == null || businessTempCarFeeConfigInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（tempCarFeeConfig），程序内部异常,请检查！ " + delInfo);
            }
            for (int _tempCarFeeConfigIndex = 0; _tempCarFeeConfigIndex < businessTempCarFeeConfigInfos.size(); _tempCarFeeConfigIndex++) {
                Map businessTempCarFeeConfigInfo = businessTempCarFeeConfigInfos.get(_tempCarFeeConfigIndex);
                flushBusinessTempCarFeeConfigInfo(businessTempCarFeeConfigInfo, StatusConstant.STATUS_CD_VALID);
                tempCarFeeConfigServiceDaoImpl.updateTempCarFeeConfigInfoInstance(businessTempCarFeeConfigInfo);
            }
        }

    }


    /**
     * 处理 businessTempCarFeeConfig 节点
     *
     * @param business                 总的数据节点
     * @param businessTempCarFeeConfig 临时车收费标准节点
     */
    private void doBusinessTempCarFeeConfig(Business business, JSONObject businessTempCarFeeConfig) {

        Assert.jsonObjectHaveKey(businessTempCarFeeConfig, "configId", "businessTempCarFeeConfig 节点下没有包含 configId 节点");

        if (businessTempCarFeeConfig.getString("configId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "configId 错误，不能自动生成（必须已经存在的configId）" + businessTempCarFeeConfig);
        }
        //自动保存DEL
        autoSaveDelBusinessTempCarFeeConfig(business, businessTempCarFeeConfig);

        businessTempCarFeeConfig.put("bId", business.getbId());
        businessTempCarFeeConfig.put("operate", StatusConstant.OPERATE_ADD);
        //保存临时车收费标准信息
        tempCarFeeConfigServiceDaoImpl.saveBusinessTempCarFeeConfigInfo(businessTempCarFeeConfig);

    }


    @Override
    public ITempCarFeeConfigServiceDao getTempCarFeeConfigServiceDaoImpl() {
        return tempCarFeeConfigServiceDaoImpl;
    }

    public void setTempCarFeeConfigServiceDaoImpl(ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl) {
        this.tempCarFeeConfigServiceDaoImpl = tempCarFeeConfigServiceDaoImpl;
    }


}
