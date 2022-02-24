package com.java110.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.po.user.UserCredentialPo;
import com.java110.user.dao.IUserServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存 用户证件 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUserCredentialsListener")
@Transactional
public class SaveUserCredentialsListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveUserCredentialsListener.class);

    @Autowired
    IUserServiceDao userServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_CREDENTIALS;
    }


    /**
     * 保存用户证件信息
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        Assert.jsonObjectHaveKey(data, UserCredentialPo.class.getSimpleName(), "datas 节点下没有包含 businessUser 节点");

        JSONObject businessUser = data.getJSONObject(UserCredentialPo.class.getSimpleName());

        Assert.jsonObjectHaveKey(businessUser, "userId", "businessUser 节点下没有包含 userId 节点");

        if (businessUser.getLong("userId") <= 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "用户打标（saveUserCredentials）保存失败，userId 不正确" + businessUser.getInteger("userId"));
        }
        dataFlowContext.addParamOut("userId", businessUser.getString("userId"));
        businessUser.put("bId", business.getbId());
        businessUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存用户信息
        userServiceDaoImpl.saveBusinessUserCredentials(businessUser);
    }

    /**
     * 保存用户证件信息至 instance中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);
        Map businessUserCredentials = userServiceDaoImpl.queryBusinessUserCredentials(info);
        if (businessUserCredentials != null && !businessUserCredentials.isEmpty()) {
            userServiceDaoImpl.saveUserCredentialsInstance(businessUserCredentials);
            dataFlowContext.addParamOut("userId", businessUserCredentials.get("user_id"));
            return;
        }

        throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "当前数据未找到business 数据" + info);

    }

    /**
     * 作废用户证件信息
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        Map userCredentials = userServiceDaoImpl.queryBusinessUserCredentials(info);
        if (userCredentials != null && !userCredentials.isEmpty()) {
            info.put("bId", bId);
            info.put("userId", userCredentials.get("user_id").toString());
            info.put("credentialsId", userCredentials.get("credentials_id").toString());
            info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserCredentialsInstance(userCredentials);
            dataFlowContext.addParamOut("userId", userCredentials.get("user_id"));
        }
    }

    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
