package com.java110.fee.listener.tempCarFeeConfigAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.fee.dao.ITempCarFeeConfigAttrServiceDao;
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
 * 保存 临时车收费标准属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveTempCarFeeConfigAttrInfoListener")
@Transactional
public class SaveTempCarFeeConfigAttrInfoListener extends AbstractTempCarFeeConfigAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveTempCarFeeConfigAttrInfoListener.class);

    @Autowired
    private ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_ATTR_INFO;
    }

    /**
     * 保存临时车收费标准属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessTempCarFeeConfigAttr 节点
        if(data.containsKey(TempCarFeeConfigAttrPo.class.getSimpleName())){
            Object bObj = data.get(TempCarFeeConfigAttrPo.class.getSimpleName());
            JSONArray businessTempCarFeeConfigAttrs = null;
            if(bObj instanceof JSONObject){
                businessTempCarFeeConfigAttrs = new JSONArray();
                businessTempCarFeeConfigAttrs.add(bObj);
            }else {
                businessTempCarFeeConfigAttrs = (JSONArray)bObj;
            }
            //JSONObject businessTempCarFeeConfigAttr = data.getJSONObject(TempCarFeeConfigAttrPo.class.getSimpleName());
            for (int bTempCarFeeConfigAttrIndex = 0; bTempCarFeeConfigAttrIndex < businessTempCarFeeConfigAttrs.size();bTempCarFeeConfigAttrIndex++) {
                JSONObject businessTempCarFeeConfigAttr = businessTempCarFeeConfigAttrs.getJSONObject(bTempCarFeeConfigAttrIndex);
                doBusinessTempCarFeeConfigAttr(business, businessTempCarFeeConfigAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessTempCarFeeConfigAttr.getString("attrId"));
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

        //临时车收费标准属性信息
        List<Map> businessTempCarFeeConfigAttrInfo = tempCarFeeConfigAttrServiceDaoImpl.getBusinessTempCarFeeConfigAttrInfo(info);
        if( businessTempCarFeeConfigAttrInfo != null && businessTempCarFeeConfigAttrInfo.size() >0) {
            reFreshShareColumn(info, businessTempCarFeeConfigAttrInfo.get(0));
            tempCarFeeConfigAttrServiceDaoImpl.saveTempCarFeeConfigAttrInfoInstance(info);
            if(businessTempCarFeeConfigAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessTempCarFeeConfigAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("config_id")) {
            return;
        }

        if (!businessInfo.containsKey("configId")) {
            return;
        }

        info.put("config_id", businessInfo.get("configId"));
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
        //临时车收费标准属性信息
        List<Map> tempCarFeeConfigAttrInfo = tempCarFeeConfigAttrServiceDaoImpl.getTempCarFeeConfigAttrInfo(info);
        if(tempCarFeeConfigAttrInfo != null && tempCarFeeConfigAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, tempCarFeeConfigAttrInfo.get(0));
            tempCarFeeConfigAttrServiceDaoImpl.updateTempCarFeeConfigAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessTempCarFeeConfigAttr 节点
     * @param business 总的数据节点
     * @param businessTempCarFeeConfigAttr 临时车收费标准属性节点
     */
    private void doBusinessTempCarFeeConfigAttr(Business business,JSONObject businessTempCarFeeConfigAttr){

        Assert.jsonObjectHaveKey(businessTempCarFeeConfigAttr,"attrId","businessTempCarFeeConfigAttr 节点下没有包含 attrId 节点");

        if(businessTempCarFeeConfigAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushTempCarFeeConfigAttrId(business.getDatas());

            businessTempCarFeeConfigAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessTempCarFeeConfigAttr.put("bId",business.getbId());
        businessTempCarFeeConfigAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存临时车收费标准属性信息
        tempCarFeeConfigAttrServiceDaoImpl.saveBusinessTempCarFeeConfigAttrInfo(businessTempCarFeeConfigAttr);

    }
    @Override
    public ITempCarFeeConfigAttrServiceDao getTempCarFeeConfigAttrServiceDaoImpl() {
        return tempCarFeeConfigAttrServiceDaoImpl;
    }

    public void setTempCarFeeConfigAttrServiceDaoImpl(ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl) {
        this.tempCarFeeConfigAttrServiceDaoImpl = tempCarFeeConfigAttrServiceDaoImpl;
    }
}
