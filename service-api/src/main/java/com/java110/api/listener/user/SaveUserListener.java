package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

/**
 * 添加员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUserListener")
public class SaveUserListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveUserListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_USER_INFO;
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
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "name", "请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "orgId", "请求报文格式错误或未包含部门信息");
        Assert.jsonObjectHaveKey(reqJson, "address", "请求报文格式错误或未包含地址信息");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(reqJson, "levelCd", "请求报文格式错误或未包含员工角色");

        if (reqJson.containsKey("email") && !StringUtil.isEmpty(reqJson.getString("email"))) {
            Assert.isEmail(reqJson, "email", "不是有效的邮箱格式");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        String userId = "";
        if (!reqJson.containsKey("userId") || "-1".equals(reqJson.getString("userId"))) {
            userId = GenerateCodeFactory.getUserId();
            reqJson.put("userId", userId);
        }

        //设置默认密码
        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);
        reqJson.put("password", staffDefaultPassword);
        UserPo userPo = BeanConvertUtil.covertBean(reqJson, UserPo.class);
        super.insert(context, userPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO);
    }

}
