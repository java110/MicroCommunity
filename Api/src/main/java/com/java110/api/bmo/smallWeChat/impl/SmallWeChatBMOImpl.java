package com.java110.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.smallWeChat.ISmallWeChatBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.smallWeChat.ISmallWeChatInnerServiceSMO;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("smallWeChatBMOImpl")
public class SmallWeChatBMOImpl extends ApiBaseBMO implements ISmallWeChatBMO {

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WE_CHAT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessSmallWeChat = new JSONObject();
        businessSmallWeChat.putAll(paramInJson);
        businessSmallWeChat.put("weChatId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessSmallWeChat", businessSmallWeChat);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatId(paramInJson.getString("weChatId"));
        smallWeChatDto.setStoreId(paramInJson.getString("soreId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        Assert.listOnlyOne(smallWeChatDtos, "未找到需要修改的小程序信息 或多条数据");
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SMALL_WE_CHAT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessSmallWeChat = new JSONObject();
        businessSmallWeChat.putAll(BeanConvertUtil.beanCovertMap(smallWeChatDtos.get(0)));
        businessSmallWeChat.putAll(paramInJson);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessSmallWeChat", businessSmallWeChat);
        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WE_CHAT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessSmallWeChat = new JSONObject();
        businessSmallWeChat.putAll(paramInJson);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessSmallWeChat", businessSmallWeChat);
        return business;
    }

}
