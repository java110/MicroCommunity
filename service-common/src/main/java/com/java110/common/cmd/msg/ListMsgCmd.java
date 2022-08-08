package com.java110.common.cmd.msg;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.msg.MsgDto;
import com.java110.intf.common.IMsgInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.msg.ApiMsgDataVo;
import com.java110.vo.api.msg.ApiMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "msg.listMsg")
public class ListMsgCmd extends Cmd {

    @Autowired
    private IMsgInnerServiceSMO msgInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MsgDto msgDto = BeanConvertUtil.covertBean(reqJson, MsgDto.class);
        String[] viewObjIds = new String[]{"9999", reqJson.getString("communityId"), reqJson.getString("storeId"), reqJson.getString("userId")};
        msgDto.setViewObjIds(viewObjIds);
        int count = msgInnerServiceSMOImpl.queryMsgsCount(msgDto);

        List<ApiMsgDataVo> msgs = null;

        if (count > 0) {
            msgs = BeanConvertUtil.covertBeanList(msgInnerServiceSMOImpl.queryMsgs(msgDto), ApiMsgDataVo.class);
        } else {
            msgs = new ArrayList<>();
        }

        ApiMsgVo apiMsgVo = new ApiMsgVo();

        apiMsgVo.setTotal(count);
        apiMsgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMsgVo.setMsgs(msgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMsgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
