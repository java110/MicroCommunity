package com.java110.common.listener.advertItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertItemServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.advert.AdvertItemPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 广告项信息信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAdvertItemInfoListener")
@Transactional
public class SaveAdvertItemInfoListener extends AbstractAdvertItemBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveAdvertItemInfoListener.class);

    @Autowired
    private IAdvertItemServiceDao advertItemServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM;
    }

    /**
     * 保存广告项信息信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessAdvertItem 节点
        if (data.containsKey(AdvertItemPo.class.getSimpleName())) {
            Object bObj = data.get(AdvertItemPo.class.getSimpleName());
            JSONArray businessAdvertItems = null;
            if (bObj instanceof JSONObject) {
                businessAdvertItems = new JSONArray();
                businessAdvertItems.add(bObj);
            } else {
                businessAdvertItems = (JSONArray) bObj;
            }
            //JSONObject businessAdvertItem = data.getJSONObject("businessAdvertItem");
            for (int bAdvertItemIndex = 0; bAdvertItemIndex < businessAdvertItems.size(); bAdvertItemIndex++) {
                JSONObject businessAdvertItem = businessAdvertItems.getJSONObject(bAdvertItemIndex);
                doBusinessAdvertItem(business, businessAdvertItem);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("advertItemId", businessAdvertItem.getString("advertItemId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
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
        List<Map> businessAdvertItemInfo = advertItemServiceDaoImpl.getBusinessAdvertItemInfo(info);
        if (businessAdvertItemInfo != null && businessAdvertItemInfo.size() > 0) {
            reFreshShareColumn(info, businessAdvertItemInfo.get(0));
            advertItemServiceDaoImpl.saveAdvertItemInfoInstance(info);
            if (businessAdvertItemInfo.size() == 1) {
                dataFlowContext.addParamOut("advertItemId", businessAdvertItemInfo.get(0).get("advert_item_id"));
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

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //广告项信息信息
        List<Map> advertItemInfo = advertItemServiceDaoImpl.getAdvertItemInfo(info);
        if (advertItemInfo != null && advertItemInfo.size() > 0) {
            reFreshShareColumn(paramIn, advertItemInfo.get(0));
            advertItemServiceDaoImpl.updateAdvertItemInfoInstance(paramIn);
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
            //刷新缓存
            //flushAdvertItemId(business.getDatas());

            businessAdvertItem.put("advertItemId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_advertItemId));

        }

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
