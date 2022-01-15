package com.java110.common.listener.advertItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertItemServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.advert.AdvertItemPo;
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
 * 修改广告项信息信息 侦听
 * <p>
 * 处理节点
 * 1、businessAdvertItem:{} 广告项信息基本信息节点
 * 2、businessAdvertItemAttr:[{}] 广告项信息属性信息节点
 * 3、businessAdvertItemPhoto:[{}] 广告项信息照片信息节点
 * 4、businessAdvertItemCerdentials:[{}] 广告项信息证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAdvertItemInfoListener")
@Transactional
public class UpdateAdvertItemInfoListener extends AbstractAdvertItemBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAdvertItemInfoListener.class);
    @Autowired
    private IAdvertItemServiceDao advertItemServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ADVERT_ITEM;
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


        //处理 businessAdvertItem 节点
        if (data.containsKey(AdvertItemPo.class.getSimpleName())) {
            Object _obj = data.get(AdvertItemPo.class.getSimpleName());
            JSONArray businessAdvertItems = null;
            if (_obj instanceof JSONObject) {
                businessAdvertItems = new JSONArray();
                businessAdvertItems.add(_obj);
            } else {
                businessAdvertItems = (JSONArray) _obj;
            }
            //JSONObject businessAdvertItem = data.getJSONObject("businessAdvertItem");
            for (int _advertItemIndex = 0; _advertItemIndex < businessAdvertItems.size(); _advertItemIndex++) {
                JSONObject businessAdvertItem = businessAdvertItems.getJSONObject(_advertItemIndex);
                doBusinessAdvertItem(business, businessAdvertItem);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("advertItemId", businessAdvertItem.getString("advertItemId"));
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

        //广告项信息信息
        List<Map> businessAdvertItemInfos = advertItemServiceDaoImpl.getBusinessAdvertItemInfo(info);
        if (businessAdvertItemInfos != null && businessAdvertItemInfos.size() > 0) {
            for (int _advertItemIndex = 0; _advertItemIndex < businessAdvertItemInfos.size(); _advertItemIndex++) {
                Map businessAdvertItemInfo = businessAdvertItemInfos.get(_advertItemIndex);
                flushBusinessAdvertItemInfo(businessAdvertItemInfo, StatusConstant.STATUS_CD_VALID);
                advertItemServiceDaoImpl.updateAdvertItemInfoInstance(businessAdvertItemInfo);
                if (businessAdvertItemInfo.size() == 1) {
                    dataFlowContext.addParamOut("advertItemId", businessAdvertItemInfo.get("advert_item_id"));
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
        //广告项信息信息
        List<Map> advertItemInfo = advertItemServiceDaoImpl.getAdvertItemInfo(info);
        if (advertItemInfo != null && advertItemInfo.size() > 0) {

            //广告项信息信息
            List<Map> businessAdvertItemInfos = advertItemServiceDaoImpl.getBusinessAdvertItemInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessAdvertItemInfos == null || businessAdvertItemInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（advertItem），程序内部异常,请检查！ " + delInfo);
            }
            for (int _advertItemIndex = 0; _advertItemIndex < businessAdvertItemInfos.size(); _advertItemIndex++) {
                Map businessAdvertItemInfo = businessAdvertItemInfos.get(_advertItemIndex);
                flushBusinessAdvertItemInfo(businessAdvertItemInfo, StatusConstant.STATUS_CD_VALID);
                advertItemServiceDaoImpl.updateAdvertItemInfoInstance(businessAdvertItemInfo);
            }
        }

    }


    /**
     * 处理 businessAdvertItem 节点
     *
     * @param business           总的数据节点
     * @param businessAdvertItem 广告项信息节点
     */
    private void doBusinessAdvertItem(Business business, JSONObject businessAdvertItem) {

        Assert.jsonObjectHaveKey(businessAdvertItem, "advertItemId", "businessAdvertItem 节点下没有包含 advertItemId 节点");

        if (businessAdvertItem.getString("advertItemId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "advertItemId 错误，不能自动生成（必须已经存在的advertItemId）" + businessAdvertItem);
        }
        //自动保存DEL
        autoSaveDelBusinessAdvertItem(business, businessAdvertItem);

        businessAdvertItem.put("bId", business.getbId());
        businessAdvertItem.put("operate", StatusConstant.OPERATE_ADD);
        //保存广告项信息信息
        advertItemServiceDaoImpl.saveBusinessAdvertItemInfo(businessAdvertItem);

    }


    public IAdvertItemServiceDao getAdvertItemServiceDaoImpl() {
        return advertItemServiceDaoImpl;
    }

    public void setAdvertItemServiceDaoImpl(IAdvertItemServiceDao advertItemServiceDaoImpl) {
        this.advertItemServiceDaoImpl = advertItemServiceDaoImpl;
    }


}
