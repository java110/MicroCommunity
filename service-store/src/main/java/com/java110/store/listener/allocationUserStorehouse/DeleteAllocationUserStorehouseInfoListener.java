package com.java110.store.listener.allocationUserStorehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.allocationUserStorehouse.AllocationUserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IAllocationUserStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除物品供应商信息 侦听
 * <p>
 * 处理节点
 * 1、businessAllocationUserStorehouse:{} 物品供应商基本信息节点
 * 2、businessAllocationUserStorehouseAttr:[{}] 物品供应商属性信息节点
 * 3、businessAllocationUserStorehousePhoto:[{}] 物品供应商照片信息节点
 * 4、businessAllocationUserStorehouseCerdentials:[{}] 物品供应商证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteAllocationUserStorehouseInfoListener")
@Transactional
public class DeleteAllocationUserStorehouseInfoListener extends AbstractAllocationUserStorehouseBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteAllocationUserStorehouseInfoListener.class);
    @Autowired
    IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_USER_STOREHOUSE;
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

        //处理 businessAllocationUserStorehouse 节点
        if (data.containsKey(AllocationUserStorehousePo.class.getSimpleName())) {
            Object _obj = data.get(AllocationUserStorehousePo.class.getSimpleName());
            JSONArray businessAllocationUserStorehouses = null;
            if (_obj instanceof JSONObject) {
                businessAllocationUserStorehouses = new JSONArray();
                businessAllocationUserStorehouses.add(_obj);
            } else {
                businessAllocationUserStorehouses = (JSONArray) _obj;
            }
            //JSONObject businessAllocationUserStorehouse = data.getJSONObject(AllocationUserStorehousePo.class.getSimpleName());
            for (int _allocationUserAllocationUserStorehouseIndex = 0; _allocationUserAllocationUserStorehouseIndex < businessAllocationUserStorehouses.size(); _allocationUserAllocationUserStorehouseIndex++) {
                JSONObject businessAllocationUserStorehouse = businessAllocationUserStorehouses.getJSONObject(_allocationUserAllocationUserStorehouseIndex);
                doBusinessAllocationUserStorehouse(business, businessAllocationUserStorehouse);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ausId", businessAllocationUserStorehouse.getString("ausId"));
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

        //物品供应商信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //物品供应商信息
        List<Map> businessAllocationUserStorehouseInfos = allocationUserStorehouseServiceDaoImpl.getBusinessAllocationUserStorehouseInfo(info);
        if (businessAllocationUserStorehouseInfos != null && businessAllocationUserStorehouseInfos.size() > 0) {
            for (int _allocationUserAllocationUserStorehouseIndex = 0; _allocationUserAllocationUserStorehouseIndex < businessAllocationUserStorehouseInfos.size(); _allocationUserAllocationUserStorehouseIndex++) {
                Map businessAllocationUserStorehouseInfo = businessAllocationUserStorehouseInfos.get(_allocationUserAllocationUserStorehouseIndex);
                flushBusinessAllocationUserStorehouseInfo(businessAllocationUserStorehouseInfo, StatusConstant.STATUS_CD_INVALID);
                allocationUserStorehouseServiceDaoImpl.updateAllocationUserStorehouseInfoInstance(businessAllocationUserStorehouseInfo);
                dataFlowContext.addParamOut("ausId", businessAllocationUserStorehouseInfo.get("aus_id"));
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
        //物品供应商信息
        List<Map> allocationUserAllocationUserStorehouseInfo = allocationUserStorehouseServiceDaoImpl.getAllocationUserStorehouseInfo(info);
        if (allocationUserAllocationUserStorehouseInfo != null && allocationUserAllocationUserStorehouseInfo.size() > 0) {

            //物品供应商信息
            List<Map> businessAllocationUserStorehouseInfos = allocationUserStorehouseServiceDaoImpl.getBusinessAllocationUserStorehouseInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessAllocationUserStorehouseInfos == null || businessAllocationUserStorehouseInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（allocationUserAllocationUserStorehouse），程序内部异常,请检查！ " + delInfo);
            }
            for (int _allocationUserAllocationUserStorehouseIndex = 0; _allocationUserAllocationUserStorehouseIndex < businessAllocationUserStorehouseInfos.size(); _allocationUserAllocationUserStorehouseIndex++) {
                Map businessAllocationUserStorehouseInfo = businessAllocationUserStorehouseInfos.get(_allocationUserAllocationUserStorehouseIndex);
                flushBusinessAllocationUserStorehouseInfo(businessAllocationUserStorehouseInfo, StatusConstant.STATUS_CD_VALID);
                allocationUserStorehouseServiceDaoImpl.updateAllocationUserStorehouseInfoInstance(businessAllocationUserStorehouseInfo);
            }
        }
    }


    /**
     * 处理 businessAllocationUserStorehouse 节点
     *
     * @param business                         总的数据节点
     * @param businessAllocationUserStorehouse 物品供应商节点
     */
    private void doBusinessAllocationUserStorehouse(Business business, JSONObject businessAllocationUserStorehouse) {

        Assert.jsonObjectHaveKey(businessAllocationUserStorehouse, "ausId", "businessAllocationUserStorehouse 节点下没有包含 ausId 节点");

        if (businessAllocationUserStorehouse.getString("ausId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "ausId 错误，不能自动生成（必须已经存在的ausId）" + businessAllocationUserStorehouse);
        }
        //自动插入DEL
        autoSaveDelBusinessAllocationUserStorehouse(business, businessAllocationUserStorehouse);
    }

    @Override
    public IAllocationUserStorehouseServiceDao getAllocationUserStorehouseServiceDaoImpl() {
        return allocationUserStorehouseServiceDaoImpl;
    }

    public void setAllocationUserStorehouseServiceDaoImpl(IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl) {
        this.allocationUserStorehouseServiceDaoImpl = allocationUserStorehouseServiceDaoImpl;
    }
}
