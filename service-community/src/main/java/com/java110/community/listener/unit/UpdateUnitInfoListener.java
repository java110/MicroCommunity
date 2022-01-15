package com.java110.community.listener.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IUnitServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.unit.UnitPo;
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
 * 修改小区单元信息 侦听
 * <p>
 * 处理节点
 * 1、businessUnit:{} 小区单元基本信息节点
 * 2、businessUnitAttr:[{}] 小区单元属性信息节点
 * 3、businessUnitPhoto:[{}] 小区单元照片信息节点
 * 4、businessUnitCerdentials:[{}] 小区单元证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateUnitInfoListener")
@Transactional
public class UpdateUnitInfoListener extends AbstractUnitBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateUnitInfoListener.class);
    @Autowired
    IUnitServiceDao unitServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_UNIT_INFO;
    }

    /**
     * business过程
     *
     * @param dataFlowContext
     * @param business
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

        //小区单元信息
        List<Map> businessUnitInfos = unitServiceDaoImpl.getBusinessUnitInfo(info);
        if (businessUnitInfos != null && businessUnitInfos.size() > 0) {
            for (int _unitIndex = 0; _unitIndex < businessUnitInfos.size(); _unitIndex++) {
                Map businessUnitInfo = businessUnitInfos.get(_unitIndex);
                flushBusinessUnitInfo(businessUnitInfo, StatusConstant.STATUS_CD_VALID);
                unitServiceDaoImpl.updateUnitInfoInstance(businessUnitInfo);
                if (businessUnitInfo.size() == 1) {
                    dataFlowContext.addParamOut("unitId", businessUnitInfo.get("unit_id"));
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
        //小区单元信息
        List<Map> unitInfo = unitServiceDaoImpl.getUnitInfo(info);
        if (unitInfo != null && unitInfo.size() > 0) {

            //小区单元信息
            List<Map> businessUnitInfos = unitServiceDaoImpl.getBusinessUnitInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessUnitInfos == null || businessUnitInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（unit），程序内部异常,请检查！ " + delInfo);
            }
            for (int _unitIndex = 0; _unitIndex < businessUnitInfos.size(); _unitIndex++) {
                Map businessUnitInfo = businessUnitInfos.get(_unitIndex);
                flushBusinessUnitInfo(businessUnitInfo, StatusConstant.STATUS_CD_VALID);
                unitServiceDaoImpl.updateUnitInfoInstance(businessUnitInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "unitId 错误，不能自动生成（必须已经存在的unitId）" + businessUnit);
        }
        //自动保存DEL
        autoSaveDelBusinessUnit(business, businessUnit);

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
