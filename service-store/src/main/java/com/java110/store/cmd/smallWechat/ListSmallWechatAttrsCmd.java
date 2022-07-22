package com.java110.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "smallWechat.listSmallWechatAttrs")
public class ListSmallWechatAttrsCmd extends Cmd {

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "wechatId", "微信ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        SmallWechatAttrDto smallWechatAttrDto = BeanConvertUtil.covertBean(reqJson, SmallWechatAttrDto.class);

        int count = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrsCount(smallWechatAttrDto);

        List<SmallWechatAttrDto> smallWechatAttrDtos = null;

        if (count > 0) {
            smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        } else {
            smallWechatAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, smallWechatAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
