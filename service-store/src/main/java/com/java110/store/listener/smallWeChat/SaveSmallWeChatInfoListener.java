package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.store.SmallWechatPo;
import com.java110.store.dao.ISmallWeChatServiceDao;
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
 * 保存 小程序管理信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveSmallWeChatInfoListener")
@Transactional
public class SaveSmallWeChatInfoListener extends AbstractSmallWeChatBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveSmallWeChatInfoListener.class);

    @Autowired
    private ISmallWeChatServiceDao smallWeChatServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WE_CHAT;
    }

    /**
     * 保存小程序管理信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");
        if (data.containsKey(SmallWechatPo.class.getSimpleName())) {
            Object bObj = data.get(SmallWechatPo.class.getSimpleName());
            JSONArray businessSmallWeChats = null;
            if (bObj instanceof JSONObject) {
                businessSmallWeChats = new JSONArray();
                businessSmallWeChats.add(bObj);
            } else {
                businessSmallWeChats = (JSONArray) bObj;
            }
            //JSONObject businessSmallWeChat = data.getJSONObject("businessSmallWeChat");
            for (int bSmallWeChatIndex = 0; bSmallWeChatIndex < businessSmallWeChats.size(); bSmallWeChatIndex++) {
                JSONObject businessSmallWeChat = businessSmallWeChats.getJSONObject(bSmallWeChatIndex);
                doBusinessSmallWeChat(business, businessSmallWeChat);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("weChatId", businessSmallWeChat.getString("weChatId"));
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

        //小程序管理信息
        List<Map> businessSmallWeChatInfo = smallWeChatServiceDaoImpl.getBusinessSmallWeChatInfo(info);
        if (businessSmallWeChatInfo != null && businessSmallWeChatInfo.size() > 0) {
            reFreshShareColumn(info, businessSmallWeChatInfo.get(0));
            smallWeChatServiceDaoImpl.saveSmallWeChatInfoInstance(info);
            if (businessSmallWeChatInfo.size() == 1) {
                dataFlowContext.addParamOut("weChatId", businessSmallWeChatInfo.get(0).get("weChat_id"));
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

        if (info.containsKey("store_Id")) {
            return;
        }

        if (!businessInfo.containsKey("storeId")) {
            return;
        }

        info.put("store_Id", businessInfo.get("storeId"));
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
        //小程序管理信息
        List<Map> smallWeChatInfo = smallWeChatServiceDaoImpl.getSmallWeChatInfo(info);
        if (smallWeChatInfo != null && smallWeChatInfo.size() > 0) {
            reFreshShareColumn(paramIn, smallWeChatInfo.get(0));
            smallWeChatServiceDaoImpl.updateSmallWeChatInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessSmallWeChat 节点
     *
     * @param business            总的数据节点
     * @param businessSmallWeChat 小程序管理节点
     */
    private void doBusinessSmallWeChat(Business business, JSONObject businessSmallWeChat) {

        Assert.jsonObjectHaveKey(businessSmallWeChat, "weChatId", "businessSmallWeChat 节点下没有包含 weChatId 节点");

        if (businessSmallWeChat.getString("weChatId").startsWith("-")) {
            //刷新缓存
            //flushSmallWeChatId(business.getDatas());

            businessSmallWeChat.put("weChatId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_weChatId));

        }

        businessSmallWeChat.put("bId", business.getbId());
        businessSmallWeChat.put("operate", StatusConstant.OPERATE_ADD);
        //保存小程序管理信息
        smallWeChatServiceDaoImpl.saveBusinessSmallWeChatInfo(businessSmallWeChat);

    }

    public ISmallWeChatServiceDao getSmallWeChatServiceDaoImpl() {
        return smallWeChatServiceDaoImpl;
    }

    public void setSmallWeChatServiceDaoImpl(ISmallWeChatServiceDao smallWeChatServiceDaoImpl) {
        this.smallWeChatServiceDaoImpl = smallWeChatServiceDaoImpl;
    }
}
