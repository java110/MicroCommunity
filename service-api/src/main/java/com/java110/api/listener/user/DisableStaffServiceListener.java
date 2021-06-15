package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.po.user.UserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 员工停用接口
 *
 * @author wuxw
 * @create 2018-12-08 下午2:46
 * @desc 停用员工信息，如离职等情况 员工账号信息停用处理
 **/
@Java110Listener("disableStaffServiceListener")
public class DisableStaffServiceListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(DisableStaffServiceListener.class);
    @Autowired
    private IUserBMO userBMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_DISABLE;
    }

    /**
     * 接口请求方法
     *
     * @return
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "userId", "当前请求报文中未包含userId节点");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UserPo userPo = BeanConvertUtil.covertBean(reqJson, UserPo.class);
        super.update(context, userPo, BusinessTypeConstant.BUSINESS_TYPE_REMOVE_USER_INFO);
    }


}
