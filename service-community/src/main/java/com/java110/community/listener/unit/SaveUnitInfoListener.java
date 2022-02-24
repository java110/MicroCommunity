package com.java110.community.listener.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IUnitServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 小区单元信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUnitInfoListener")
@Transactional
public class SaveUnitInfoListener extends AbstractUnitBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveUnitInfoListener.class);

    @Autowired
    IUnitServiceDao unitServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_UNIT_INFO;
    }

    /**
     * 保存小区单元信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessUnit 节点
        if (data.containsKey(UnitPo.class.getSimpleName())) {
            Object _obj = data.get(UnitPo.class.getSimpleName());
            JSONArray businessUnits = null;
            if (_obj instanceof JSONObject) {
                businessUnits = new JSONArray();
                businessUnits.add(_obj);
            } else {
                businessUnits = (JSONArray) _obj;
            }
            //JSONObject businessUnit = data.getJSONObject("businessUnit");
            for (int _unitIndex = 0; _unitIndex < businessUnits.size(); _unitIndex++) {
                JSONObject businessUnit = businessUnits.getJSONObject(_unitIndex);
                doBusinessUnit(business, businessUnit);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("unitId", businessUnit.getString("unitId"));
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

        //小区单元信息
        List<Map> businessUnitInfo = unitServiceDaoImpl.getBusinessUnitInfo(info);
        if (businessUnitInfo != null && businessUnitInfo.size() > 0) {
            unitServiceDaoImpl.saveUnitInfoInstance(info);
            if (businessUnitInfo.size() == 1) {
                dataFlowContext.addParamOut("unitId", businessUnitInfo.get(0).get("unit_id"));
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //小区单元信息
        List<Map> unitInfo = unitServiceDaoImpl.getUnitInfo(info);
        if (unitInfo != null && unitInfo.size() > 0) {
            unitServiceDaoImpl.updateUnitInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessUnit 节点
     *
     * @param business     总的数据节点
     * @param businessUnit 小区单元节点
     */
    private void doBusinessUnit(Business business, JSONObject businessUnit) {

        Assert.jsonObjectHaveKey(businessUnit, "unitId", "businessUnit 节点下没有包含 unitId 节点");

        if (businessUnit.getString("unitId").startsWith("-")) {
            //刷新缓存
            //flushUnitId(business.getDatas());

            businessUnit.put("unitId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId));

        }

        businessUnit.put("bId", business.getbId());
        businessUnit.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区单元信息
        unitServiceDaoImpl.saveBusinessUnitInfo(businessUnit);

    }

    public IUnitServiceDao getUnitServiceDaoImpl() {
        return unitServiceDaoImpl;
    }

    public void setUnitServiceDaoImpl(IUnitServiceDao unitServiceDaoImpl) {
        this.unitServiceDaoImpl = unitServiceDaoImpl;
    }
}
