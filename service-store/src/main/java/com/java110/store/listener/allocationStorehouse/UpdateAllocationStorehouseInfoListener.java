package com.java110.store.listener.allocationStorehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.store.listener.allocationStorehouse.AbstractAllocationStorehouseBusinessServiceDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IAllocationStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改仓库调拨信息 侦听
 *
 * 处理节点
 * 1、businessAllocationStorehouse:{} 仓库调拨基本信息节点
 * 2、businessAllocationStorehouseAttr:[{}] 仓库调拨属性信息节点
 * 3、businessAllocationStorehousePhoto:[{}] 仓库调拨照片信息节点
 * 4、businessAllocationStorehouseCerdentials:[{}] 仓库调拨证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAllocationStorehouseInfoListener")
@Transactional
public class UpdateAllocationStorehouseInfoListener extends AbstractAllocationStorehouseBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAllocationStorehouseInfoListener.class);
    @Autowired
    private IAllocationStorehouseServiceDao allocationAllocationStorehousehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");


            //处理 businessAllocationStorehouse 节点
            if(data.containsKey(AllocationStorehousePo.class.getSimpleName())){
                Object _obj = data.get(AllocationStorehousePo.class.getSimpleName());
                JSONArray businessAllocationStorehouses = null;
                if(_obj instanceof JSONObject){
                    businessAllocationStorehouses = new JSONArray();
                    businessAllocationStorehouses.add(_obj);
                }else {
                    businessAllocationStorehouses = (JSONArray)_obj;
                }
                //JSONObject businessAllocationStorehouse = data.getJSONObject(AllocationStorehousePo.class.getSimpleName());
                for (int _allocationAllocationStorehousehouseIndex = 0; _allocationAllocationStorehousehouseIndex < businessAllocationStorehouses.size();_allocationAllocationStorehousehouseIndex++) {
                    JSONObject businessAllocationStorehouse = businessAllocationStorehouses.getJSONObject(_allocationAllocationStorehousehouseIndex);
                    doBusinessAllocationStorehouse(business, businessAllocationStorehouse);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("asId", businessAllocationStorehouse.getString("asId"));
                    }
                }
            }
    }


    /**
     * business to instance 过程
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //仓库调拨信息
        List<Map> businessAllocationStorehouseInfos = allocationAllocationStorehousehouseServiceDaoImpl.getBusinessAllocationStorehouseInfo(info);
        if( businessAllocationStorehouseInfos != null && businessAllocationStorehouseInfos.size() >0) {
            for (int _allocationAllocationStorehousehouseIndex = 0; _allocationAllocationStorehousehouseIndex < businessAllocationStorehouseInfos.size();_allocationAllocationStorehousehouseIndex++) {
                Map businessAllocationStorehouseInfo = businessAllocationStorehouseInfos.get(_allocationAllocationStorehousehouseIndex);
                flushBusinessAllocationStorehouseInfo(businessAllocationStorehouseInfo,StatusConstant.STATUS_CD_VALID);
                allocationAllocationStorehousehouseServiceDaoImpl.updateAllocationStorehouseInfoInstance(businessAllocationStorehouseInfo);
                if(businessAllocationStorehouseInfo.size() == 1) {
                    dataFlowContext.addParamOut("asId", businessAllocationStorehouseInfo.get("as_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //仓库调拨信息
        List<Map> allocationAllocationStorehousehouseInfo = allocationAllocationStorehousehouseServiceDaoImpl.getAllocationStorehouseInfo(info);
        if(allocationAllocationStorehousehouseInfo != null && allocationAllocationStorehousehouseInfo.size() > 0){

            //仓库调拨信息
            List<Map> businessAllocationStorehouseInfos = allocationAllocationStorehousehouseServiceDaoImpl.getBusinessAllocationStorehouseInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAllocationStorehouseInfos == null || businessAllocationStorehouseInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（allocationAllocationStorehousehouse），程序内部异常,请检查！ "+delInfo);
            }
            for (int _allocationAllocationStorehousehouseIndex = 0; _allocationAllocationStorehousehouseIndex < businessAllocationStorehouseInfos.size();_allocationAllocationStorehousehouseIndex++) {
                Map businessAllocationStorehouseInfo = businessAllocationStorehouseInfos.get(_allocationAllocationStorehousehouseIndex);
                flushBusinessAllocationStorehouseInfo(businessAllocationStorehouseInfo,StatusConstant.STATUS_CD_VALID);
                allocationAllocationStorehousehouseServiceDaoImpl.updateAllocationStorehouseInfoInstance(businessAllocationStorehouseInfo);
            }
        }

    }



    /**
     * 处理 businessAllocationStorehouse 节点
     * @param business 总的数据节点
     * @param businessAllocationStorehouse 仓库调拨节点
     */
    private void doBusinessAllocationStorehouse(Business business,JSONObject businessAllocationStorehouse){

        Assert.jsonObjectHaveKey(businessAllocationStorehouse,"asId","businessAllocationStorehouse 节点下没有包含 asId 节点");

        if(businessAllocationStorehouse.getString("asId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"asId 错误，不能自动生成（必须已经存在的asId）"+businessAllocationStorehouse);
        }
        //自动保存DEL
        autoSaveDelBusinessAllocationStorehouse(business,businessAllocationStorehouse);

        businessAllocationStorehouse.put("bId",business.getbId());
        businessAllocationStorehouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存仓库调拨信息
        allocationAllocationStorehousehouseServiceDaoImpl.saveBusinessAllocationStorehouseInfo(businessAllocationStorehouse);

    }



    @Override
    public IAllocationStorehouseServiceDao getAllocationStorehouseServiceDaoImpl() {
        return allocationAllocationStorehousehouseServiceDaoImpl;
    }

    public void setAllocationStorehouseServiceDaoImpl(IAllocationStorehouseServiceDao allocationAllocationStorehousehouseServiceDaoImpl) {
        this.allocationAllocationStorehousehouseServiceDaoImpl = allocationAllocationStorehousehouseServiceDaoImpl;
    }



}
