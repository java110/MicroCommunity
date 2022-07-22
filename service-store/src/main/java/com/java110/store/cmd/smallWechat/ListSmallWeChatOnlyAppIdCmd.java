package com.java110.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatDataVo;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "smallWeChat.listSmallWeChatOnlyAppId")
public class ListSmallWeChatOnlyAppIdCmd extends Cmd {

    private static String OWNER_APP = "992019111758490006";
    private static String OWNER_WECHAT_APP = "992020061452450002";

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        //Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
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
