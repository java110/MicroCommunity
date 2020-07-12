package com.java110.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.smallWeChat.ISmallWechatAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("smallWechatAttrBMOImpl")
public class SmallWechatAttrBMOImpl extends ApiBaseBMO implements ISmallWechatAttrBMO {

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.insert(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.update(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SMALL_WECHAT_ATTR);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.update(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WECHAT_ATTR);
    }

}
