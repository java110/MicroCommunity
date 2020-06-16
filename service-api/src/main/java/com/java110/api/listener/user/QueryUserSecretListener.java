package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 这个类专门查询用户秘钥信息
 */
@Java110Listener("queryUserSecretListener")
public class QueryUserSecretListener extends AbstractServiceApiListener {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.QUERY_USER_SECRET;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;
        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);

        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (userDtos == null || userDtos.size() < 1) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未找到用户信息");
            context.setResponseEntity(responseEntity);
            return;
        }

        userDto = userDtos.get(0);
        List<UserAttrDto> userAttrDtos = userDto.getUserAttrs();
        String key = "";
        String openId = "";
        for (UserAttrDto userAttrDto : userAttrDtos) {
            if (UserAttrDto.SPEC_KEY.equals(userAttrDto.getSpecCd())) {
                key = userAttrDto.getValue();
            }

            if (UserAttrDto.SPEC_OPEN_ID.equals(userAttrDto.getSpecCd())) {
                openId = userAttrDto.getValue();
            }
        }
        userDto.setKey(key);
        userDto.setOpenId(openId);
        responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "成功", userDto);
        context.setResponseEntity(responseEntity);
    }
}
