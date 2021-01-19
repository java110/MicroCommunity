package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeSmallWeChatConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatDataVo;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小程序配置
 */
@Java110Listener("listSmallWeChatOnlyAppIdListener")
public class ListSmallWeChatOnlyAppIdListener extends AbstractServiceApiListener {

    private static String OWNER_APP = "992019111758490006";
    private static String OWNER_WECHAT_APP = "992020061452450002";

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeSmallWeChatConstant.LIST_SMALL_WE_CHAT_ONLY_APP_ID;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ISmallWeChatInnerServiceSMO getSmallWeChatInnerServiceSMOImpl() {
        return smallWeChatInnerServiceSMOImpl;
    }

    public void setSmallWeChatInnerServiceSMOImpl(ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl) {
        this.smallWeChatInnerServiceSMOImpl = smallWeChatInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        //Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        SmallWeChatDto smallWeChatDto = BeanConvertUtil.covertBean(reqJson, SmallWeChatDto.class);
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(reqJson.getString("communityId"));
        int count = smallWeChatInnerServiceSMOImpl.querySmallWeChatsCount(smallWeChatDto);
        List<ApiSmallWeChatDataVo> smallWeChats = null;
        if (count > 0) {
            smallWeChats = BeanConvertUtil.covertBeanList(smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto), ApiSmallWeChatDataVo.class);
            freshSecure(smallWeChats);
        } else {
            smallWeChats = new ArrayList<>();
        }
        ApiSmallWeChatVo apiSmallWeChatVo = new ApiSmallWeChatVo();
        apiSmallWeChatVo.setTotal(count);
        apiSmallWeChatVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiSmallWeChatVo.setSmallWeChats(smallWeChats);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiSmallWeChatVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void freshSecure(List<ApiSmallWeChatDataVo> smallWeChats) {
        for (ApiSmallWeChatDataVo apiSmallWeChatDataVo : smallWeChats) {
            apiSmallWeChatDataVo.setAppSecret("");
            apiSmallWeChatDataVo.setPayPassword("");
            apiSmallWeChatDataVo.setMchId("");
        }
    }
}
