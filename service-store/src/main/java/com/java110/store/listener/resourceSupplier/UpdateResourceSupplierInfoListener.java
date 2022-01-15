package com.java110.store.listener.resourceSupplier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resourceSupplier.ResourceSupplierPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceSupplierServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改物品供应商信息 侦听
 * <p>
 * 处理节点
 * 1、businessResourceSupplier:{} 物品供应商基本信息节点
 * 2、businessResourceSupplierAttr:[{}] 物品供应商属性信息节点
 * 3、businessResourceSupplierPhoto:[{}] 物品供应商照片信息节点
 * 4、businessResourceSupplierCerdentials:[{}] 物品供应商证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateResourceSupplierInfoListener")
@Transactional
public class UpdateResourceSupplierInfoListener extends AbstractResourceSupplierBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateResourceSupplierInfoListener.class);
    @Autowired
    private IResourceSupplierServiceDao resourceSupplierServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_SUPPLIER;
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


        //处理 businessResourceSupplier 节点
        if (data.containsKey(ResourceSupplierPo.class.getSimpleName())) {
            Object _obj = data.get(ResourceSupplierPo.class.getSimpleName());
            JSONArray businessResourceSuppliers = null;
            if (_obj instanceof JSONObject) {
                businessResourceSuppliers = new JSONArray();
                businessResourceSuppliers.add(_obj);
            } else {
                businessResourceSuppliers = (JSONArray) _obj;
            }
            //JSONObject businessResourceSupplier = data.getJSONObject(ResourceSupplierPo.class.getSimpleName());
            for (int _resourceSupplierIndex = 0; _resourceSupplierIndex < businessResourceSuppliers.size(); _resourceSupplierIndex++) {
                JSONObject businessResourceSupplier = businessResourceSuppliers.getJSONObject(_resourceSupplierIndex);
                doBusinessResourceSupplier(business, businessResourceSupplier);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rsId", businessResourceSupplier.getString("rsId"));
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

        //物品供应商信息
        List<Map> businessResourceSupplierInfos = resourceSupplierServiceDaoImpl.getBusinessResourceSupplierInfo(info);
        if (businessResourceSupplierInfos != null && businessResourceSupplierInfos.size() > 0) {
            for (int _resourceSupplierIndex = 0; _resourceSupplierIndex < businessResourceSupplierInfos.size(); _resourceSupplierIndex++) {
                Map businessResourceSupplierInfo = businessResourceSupplierInfos.get(_resourceSupplierIndex);
                flushBusinessResourceSupplierInfo(businessResourceSupplierInfo, StatusConstant.STATUS_CD_VALID);
                resourceSupplierServiceDaoImpl.updateResourceSupplierInfoInstance(businessResourceSupplierInfo);
                if (businessResourceSupplierInfo.size() == 1) {
                    dataFlowContext.addParamOut("rsId", businessResourceSupplierInfo.get("rs_id"));
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
        //物品供应商信息
        List<Map> resourceSupplierInfo = resourceSupplierServiceDaoImpl.getResourceSupplierInfo(info);
        if (resourceSupplierInfo != null && resourceSupplierInfo.size() > 0) {

            //物品供应商信息
            List<Map> businessResourceSupplierInfos = resourceSupplierServiceDaoImpl.getBusinessResourceSupplierInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessResourceSupplierInfos == null || businessResourceSupplierInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（resourceSupplier），程序内部异常,请检查！ " + delInfo);
            }
            for (int _resourceSupplierIndex = 0; _resourceSupplierIndex < businessResourceSupplierInfos.size(); _resourceSupplierIndex++) {
                Map businessResourceSupplierInfo = businessResourceSupplierInfos.get(_resourceSupplierIndex);
                flushBusinessResourceSupplierInfo(businessResourceSupplierInfo, StatusConstant.STATUS_CD_VALID);
                resourceSupplierServiceDaoImpl.updateResourceSupplierInfoInstance(businessResourceSupplierInfo);
            }
        }

    }


    /**
     * 处理 businessResourceSupplier 节点
     *
     * @param business                 总的数据节点
     * @param businessResourceSupplier 物品供应商节点
     */
    private void doBusinessResourceSupplier(Business business, JSONObject businessResourceSupplier) {

        Assert.jsonObjectHaveKey(businessResourceSupplier, "rsId", "businessResourceSupplier 节点下没有包含 rsId 节点");

        if (businessResourceSupplier.getString("rsId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "rsId 错误，不能自动生成（必须已经存在的rsId）" + businessResourceSupplier);
        }
        //自动保存DEL
        autoSaveDelBusinessResourceSupplier(business, businessResourceSupplier);

        businessResourceSupplier.put("bId", business.getbId());
        businessResourceSupplier.put("operate", StatusConstant.OPERATE_ADD);
        //保存物品供应商信息
        resourceSupplierServiceDaoImpl.saveBusinessResourceSupplierInfo(businessResourceSupplier);

    }


    @Override
    public IResourceSupplierServiceDao getResourceSupplierServiceDaoImpl() {
        return resourceSupplierServiceDaoImpl;
    }

    public void setResourceSupplierServiceDaoImpl(IResourceSupplierServiceDao resourceSupplierServiceDaoImpl) {
        this.resourceSupplierServiceDaoImpl = resourceSupplierServiceDaoImpl;
    }


}
