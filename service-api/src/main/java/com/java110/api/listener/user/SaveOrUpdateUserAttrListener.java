package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.DataFlowFactory;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserAttrDto;
import com.java110.entity.center.AppService;
import com.java110.po.user.UserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 添加或修改用户属性
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOrUpdateUserAttrListener")
public class SaveOrUpdateUserAttrListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveOrUpdateUserAttrListener.class);

    @Autowired
    private IUserBMO userBMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_UPDATE_USER_ATTR;
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
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "specCd", "请求参数中未包含属性 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "value", "请求参数中未包含属性值 节点，请确认");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(reqJson.getString("userId"));
        userAttrDto.setSpecCd(reqJson.getString("specCd"));
        List<UserAttrDto> userAttrDtos = userInnerServiceSMOImpl.getUserAttrs(userAttrDto);
        if(userAttrDtos != null && userAttrDtos.size() >0){
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userAttrDtos.get(0).getUserId());
            userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
            userAttrPo.setSpecCd(reqJson.getString("specCd"));
            userAttrPo.setValue(reqJson.getString("value"));
            super.update(context,userAttrPo,BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_ATTR_INFO);
            return;
        }

        UserAttrPo userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(reqJson.getString("userId"));
        userAttrPo.setAttrId("-1");
        userAttrPo.setSpecCd(reqJson.getString("specCd"));
        userAttrPo.setValue(reqJson.getString("value"));
        super.insert(context,userAttrPo,BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_ATTR_INFO);
    }


    private void modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext) {
        UserPo userPo = BeanConvertUtil.covertBean(builderStaffInfo(paramObj, dataFlowContext), UserPo.class);
        super.update(dataFlowContext, userPo, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //首先根据员工ID查询员工信息，根据员工信息修改相应的数据
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        if (appService == null) {
            throw new ListenerExecuteException(1999, "当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        }
        String requestUrl = appService.getUrl() + "?userId=" + paramObj.getString("userId");
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        dataFlowContext.getRequestHeaders().put("REQUEST_URL", requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        userInfo.putAll(paramObj);

        return userInfo;
    }

}
