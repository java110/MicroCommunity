package com.java110.store.listener.storeAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.store.StoreAttrPo;
import com.java110.store.dao.IStoreAttrServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
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
 * 保存 商户属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveStoreAttrInfoListener")
@Transactional
public class SaveStoreAttrInfoListener extends AbstractStoreAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveStoreAttrInfoListener.class);

    @Autowired
    private IStoreAttrServiceDao storeAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_ATTR;
    }

    /**
     * 保存商户属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessStoreAttr 节点
        if(data.containsKey(StoreAttrPo.class.getSimpleName())){
            Object bObj = data.get(StoreAttrPo.class.getSimpleName());
            JSONArray businessStoreAttrs = null;
            if(bObj instanceof JSONObject){
                businessStoreAttrs = new JSONArray();
                businessStoreAttrs.add(bObj);
            }else {
                businessStoreAttrs = (JSONArray)bObj;
            }
            //JSONObject businessStoreAttr = data.getJSONObject("businessStoreAttr");
            for (int bStoreAttrIndex = 0; bStoreAttrIndex < businessStoreAttrs.size();bStoreAttrIndex++) {
                JSONObject businessStoreAttr = businessStoreAttrs.getJSONObject(bStoreAttrIndex);
                doBusinessStoreAttr(business, businessStoreAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessStoreAttr.getString("attrId"));
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

        //商户属性信息
        List<Map> businessStoreAttrInfo = storeAttrServiceDaoImpl.getBusinessStoreAttrInfo(info);
        if( businessStoreAttrInfo != null && businessStoreAttrInfo.size() >0) {
            reFreshShareColumn(info, businessStoreAttrInfo.get(0));
            storeAttrServiceDaoImpl.saveStoreAttrInfoInstance(info);
            if(businessStoreAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessStoreAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        //商户属性信息
        List<Map> storeAttrInfo = storeAttrServiceDaoImpl.getStoreAttrInfo(info);
        if(storeAttrInfo != null && storeAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, storeAttrInfo.get(0));
            storeAttrServiceDaoImpl.updateStoreAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessStoreAttr 节点
     * @param business 总的数据节点
     * @param businessStoreAttr 商户属性节点
     */
    private void doBusinessStoreAttr(Business business,JSONObject businessStoreAttr){

        Assert.jsonObjectHaveKey(businessStoreAttr,"attrId","businessStoreAttr 节点下没有包含 attrId 节点");

        if(businessStoreAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushStoreAttrId(business.getDatas());

            businessStoreAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessStoreAttr.put("bId",business.getbId());
        businessStoreAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存商户属性信息
        storeAttrServiceDaoImpl.saveBusinessStoreAttrInfo(businessStoreAttr);

    }

    public IStoreAttrServiceDao getStoreAttrServiceDaoImpl() {
        return storeAttrServiceDaoImpl;
    }

    public void setStoreAttrServiceDaoImpl(IStoreAttrServiceDao storeAttrServiceDaoImpl) {
        this.storeAttrServiceDaoImpl = storeAttrServiceDaoImpl;
    }
}
