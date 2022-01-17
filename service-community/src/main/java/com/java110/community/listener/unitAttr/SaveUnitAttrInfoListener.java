package com.java110.community.listener.unitAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.unitAttr.UnitAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IUnitAttrServiceDao;
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
 * 保存 单元属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUnitAttrInfoListener")
@Transactional
public class SaveUnitAttrInfoListener extends AbstractUnitAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveUnitAttrInfoListener.class);

    @Autowired
    private IUnitAttrServiceDao unitAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_UNIT_ATTR_INFO;
    }

    /**
     * 保存单元属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessUnitAttr 节点
        if(data.containsKey(UnitAttrPo.class.getSimpleName())){
            Object bObj = data.get(UnitAttrPo.class.getSimpleName());
            JSONArray businessUnitAttrs = null;
            if(bObj instanceof JSONObject){
                businessUnitAttrs = new JSONArray();
                businessUnitAttrs.add(bObj);
            }else {
                businessUnitAttrs = (JSONArray)bObj;
            }
            //JSONObject businessUnitAttr = data.getJSONObject(UnitAttrPo.class.getSimpleName());
            for (int bUnitAttrIndex = 0; bUnitAttrIndex < businessUnitAttrs.size();bUnitAttrIndex++) {
                JSONObject businessUnitAttr = businessUnitAttrs.getJSONObject(bUnitAttrIndex);
                doBusinessUnitAttr(business, businessUnitAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessUnitAttr.getString("attrId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //单元属性信息
        List<Map> businessUnitAttrInfo = unitAttrServiceDaoImpl.getBusinessUnitAttrInfo(info);
        if( businessUnitAttrInfo != null && businessUnitAttrInfo.size() >0) {
            reFreshShareColumn(info, businessUnitAttrInfo.get(0));
            unitAttrServiceDaoImpl.saveUnitAttrInfoInstance(info);
            if(businessUnitAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessUnitAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("unitId")) {
            return;
        }

        if (!businessInfo.containsKey("unit_id")) {
            return;
        }

        info.put("unitId", businessInfo.get("unit_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //单元属性信息
        List<Map> unitAttrInfo = unitAttrServiceDaoImpl.getUnitAttrInfo(info);
        if(unitAttrInfo != null && unitAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, unitAttrInfo.get(0));
            unitAttrServiceDaoImpl.updateUnitAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessUnitAttr 节点
     * @param business 总的数据节点
     * @param businessUnitAttr 单元属性节点
     */
    private void doBusinessUnitAttr(Business business,JSONObject businessUnitAttr){

        Assert.jsonObjectHaveKey(businessUnitAttr,"attrId","businessUnitAttr 节点下没有包含 attrId 节点");

        if(businessUnitAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushUnitAttrId(business.getDatas());

            businessUnitAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessUnitAttr.put("bId",business.getbId());
        businessUnitAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存单元属性信息
        unitAttrServiceDaoImpl.saveBusinessUnitAttrInfo(businessUnitAttr);

    }
    @Override
    public IUnitAttrServiceDao getUnitAttrServiceDaoImpl() {
        return unitAttrServiceDaoImpl;
    }

    public void setUnitAttrServiceDaoImpl(IUnitAttrServiceDao unitAttrServiceDaoImpl) {
        this.unitAttrServiceDaoImpl = unitAttrServiceDaoImpl;
    }
}
