package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("resetStaffPwdListener")
public class ResetStaffPwdListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(ResetStaffPwdListener.class);
    @Autowired
    private IUserBMO userBMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_RESET_STAFF_PWD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        modifyStaff(reqJson, context);

        commit(context);

        if (context.getResponseEntity().getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = new JSONObject();
            paramOut.put("pwd", reqJson.getString("pwd"));
            ResponseEntity<String> responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        }
    }


    private JSONObject modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext) {
        //校验json 格式中是否包含 name,email,levelCd,tel

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser", builderStaffInfo(paramObj, dataFlowContext));

        return business;
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        UserDto userDto = new UserDto();
        userDto.setStatusCd("0");
        userDto.setUserId(paramObj.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUserHasPwd(userDto);

        Assert.listOnlyOne(userDtos, "数据错误查询到多条用户信息或单条");

        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDtos.get(0)));
        String pwd = GenerateCodeFactory.getRandomCode(6);
        userInfo.putAll(paramObj);
        userInfo.put("password", AuthenticationFactory.passwdMd5(pwd));
        paramObj.put("pwd", pwd);

        return userInfo;
    }


}
