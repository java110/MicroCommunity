package com.java110.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.smallWeChat.IWechatMenuBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IWechatMenuInnerServiceSMO;
import com.java110.po.wechatMenu.WechatMenuPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wechatMenuBMOImpl")
public class WechatMenuBMOImpl extends ApiBaseBMO implements IWechatMenuBMO {

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("wechatMenuId", "-1");
        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.insert(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WECHAT_MENU);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.update(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WECHAT_MENU);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.update(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WECHAT_MENU);
    }

}
