package com.java110.store.listener.storeAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.store.StoreAttrPo;
import com.java110.store.dao.IStoreAttrServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改商户属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessStoreAttr:{} 商户属性基本信息节点
 * 2、businessStoreAttrAttr:[{}] 商户属性属性信息节点
 * 3、businessStoreAttrPhoto:[{}] 商户属性照片信息节点
 * 4、businessStoreAttrCerdentials:[{}] 商户属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateStoreAttrInfoListener")
@Transactional
public class UpdateStoreAttrInfoListener extends AbstractStoreAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateStoreAttrInfoListener.class);
    @Autowired
    private IStoreAttrServiceDao storeAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_STORE_ATTR;
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


        //处理 businessStoreAttr 节点
        if (data.containsKey(StoreAttrPo.class.getSimpleName())) {
            Object _obj = data.get(StoreAttrPo.class.getSimpleName());
            JSONArray businessStoreAttrs = null;
            if (_obj instanceof JSONObject) {
                businessStoreAttrs = new JSONArray();
                businessStoreAttrs.add(_obj);
            } else {
                businessStoreAttrs = (JSONArray) _obj;
            }
            //JSONObject businessStoreAttr = data.getJSONObject("businessStoreAttr");
            for (int _storeAttrIndex = 0; _storeAttrIndex < businessStoreAttrs.size(); _storeAttrIndex++) {
                JSONObject businessStoreAttr = businessStoreAttrs.getJSONObject(_storeAttrIndex);
                doBusinessStoreAttr(business, businessStoreAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessStoreAttr.getString("attrId"));
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

        //商户属性信息
        List<Map> businessStoreAttrInfos = storeAttrServiceDaoImpl.getBusinessStoreAttrInfo(info);
        if (businessStoreAttrInfos != null && businessStoreAttrInfos.size() > 0) {
            for (int _storeAttrIndex = 0; _storeAttrIndex < businessStoreAttrInfos.size(); _storeAttrIndex++) {
                Map businessStoreAttrInfo = businessStoreAttrInfos.get(_storeAttrIndex);
                flushBusinessStoreAttrInfo(businessStoreAttrInfo, StatusConstant.STATUS_CD_VALID);
                storeAttrServiceDaoImpl.updateStoreAttrInfoInstance(businessStoreAttrInfo);
                if (businessStoreAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("attrId", businessStoreAttrInfo.get("attr_id"));
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
        //商户属性信息
        List<Map> storeAttrInfo = storeAttrServiceDaoImpl.getStoreAttrInfo(info);
        if (storeAttrInfo != null && storeAttrInfo.size() > 0) {

            //商户属性信息
            List<Map> businessStoreAttrInfos = storeAttrServiceDaoImpl.getBusinessStoreAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessStoreAttrInfos == null || businessStoreAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（storeAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _storeAttrIndex = 0; _storeAttrIndex < businessStoreAttrInfos.size(); _storeAttrIndex++) {
                Map businessStoreAttrInfo = businessStoreAttrInfos.get(_storeAttrIndex);
                flushBusinessStoreAttrInfo(businessStoreAttrInfo, StatusConstant.STATUS_CD_VALID);
                storeAttrServiceDaoImpl.updateStoreAttrInfoInstance(businessStoreAttrInfo);
            }
        }

    }


    /**
     * 处理 businessStoreAttr 节点
     *
     * @param business          总的数据节点
     * @param businessStoreAttr 商户属性节点
     */
    private void doBusinessStoreAttr(Business business, JSONObject businessStoreAttr) {

        Assert.jsonObjectHaveKey(businessStoreAttr, "attrId", "businessStoreAttr 节点下没有包含 attrId 节点");

        if (businessStoreAttr.getString("attrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + businessStoreAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessStoreAttr(business, businessStoreAttr);

        businessStoreAttr.put("bId", business.getbId());
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
