package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserAttrDto;
import com.java110.entity.center.AppService;
import com.java110.intf.user.IUserAttrV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.user.UserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "user.saveOrUpdateUserAttr")
public class SaveOrUpdateUserAttrCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "specCd", "请求参数中未包含属性 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "value", "请求参数中未包含属性值 节点，请确认");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(reqJson.getString("userId"));
        userAttrDto.setSpecCd(reqJson.getString("specCd"));
        List<UserAttrDto> userAttrDtos = userInnerServiceSMOImpl.getUserAttrs(userAttrDto);
        int flag = 0;
        if(userAttrDtos != null && userAttrDtos.size() >0){
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userAttrDtos.get(0).getUserId());
            userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
            userAttrPo.setSpecCd(reqJson.getString("specCd"));
            userAttrPo.setValue(reqJson.getString("value"));
            flag = userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
            if(flag< 1){
                throw new CmdException("更新失败");
            }
            return;
        }

        UserAttrPo userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(reqJson.getString("userId"));
        userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        userAttrPo.setSpecCd(reqJson.getString("specCd"));
        userAttrPo.setValue(reqJson.getString("value"));
        flag = userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
        if(flag< 1){
            throw new CmdException("添加失败");
        }
    }
}
