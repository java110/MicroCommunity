package com.java110.store.listener.storehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.storehouse.StorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IStorehouseServiceDao;
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
 * 保存 仓库信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveStorehouseInfoListener")
@Transactional
public class SaveStorehouseInfoListener extends AbstractStorehouseBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveStorehouseInfoListener.class);

    @Autowired
    private IStorehouseServiceDao storehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_STOREHOUSE;
    }

    /**
     * 保存仓库信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessStorehouse 节点
        if(data.containsKey(StorehousePo.class.getSimpleName())){
            Object bObj = data.get(StorehousePo.class.getSimpleName());
            JSONArray businessStorehouses = null;
            if(bObj instanceof JSONObject){
                businessStorehouses = new JSONArray();
                businessStorehouses.add(bObj);
            }else {
                businessStorehouses = (JSONArray)bObj;
            }
            //JSONObject businessStorehouse = data.getJSONObject(StorehousePo.class.getSimpleName());
            for (int bStorehouseIndex = 0; bStorehouseIndex < businessStorehouses.size();bStorehouseIndex++) {
                JSONObject businessStorehouse = businessStorehouses.getJSONObject(bStorehouseIndex);
                doBusinessStorehouse(business, businessStorehouse);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("shId", businessStorehouse.getString("shId"));
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

        //仓库信息
        List<Map> businessStorehouseInfo = storehouseServiceDaoImpl.getBusinessStorehouseInfo(info);
        if( businessStorehouseInfo != null && businessStorehouseInfo.size() >0) {
            reFreshShareColumn(info, businessStorehouseInfo.get(0));
            storehouseServiceDaoImpl.saveStorehouseInfoInstance(info);
            if(businessStorehouseInfo.size() == 1) {
                dataFlowContext.addParamOut("shId", businessStorehouseInfo.get(0).get("sh_id"));
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
        //仓库信息
        List<Map> storehouseInfo = storehouseServiceDaoImpl.getStorehouseInfo(info);
        if(storehouseInfo != null && storehouseInfo.size() > 0){
            reFreshShareColumn(paramIn, storehouseInfo.get(0));
            storehouseServiceDaoImpl.updateStorehouseInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessStorehouse 节点
     * @param business 总的数据节点
     * @param businessStorehouse 仓库节点
     */
    private void doBusinessStorehouse(Business business,JSONObject businessStorehouse){

        Assert.jsonObjectHaveKey(businessStorehouse,"shId","businessStorehouse 节点下没有包含 shId 节点");

        if(businessStorehouse.getString("shId").startsWith("-")){
            //刷新缓存
            //flushStorehouseId(business.getDatas());

            businessStorehouse.put("shId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shId));

        }

        businessStorehouse.put("bId",business.getbId());
        businessStorehouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存仓库信息
        storehouseServiceDaoImpl.saveBusinessStorehouseInfo(businessStorehouse);

    }
    @Override
    public IStorehouseServiceDao getStorehouseServiceDaoImpl() {
        return storehouseServiceDaoImpl;
    }

    public void setStorehouseServiceDaoImpl(IStorehouseServiceDao storehouseServiceDaoImpl) {
        this.storehouseServiceDaoImpl = storehouseServiceDaoImpl;
    }
}
