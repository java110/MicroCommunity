package com.java110.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.user.UserPo;
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
 * 停用 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("removeUserInfoListener")
@Transactional
public class RemoveUserInfoListener extends AbstractUserBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(RemoveUserInfoListener.class);

    @Autowired
    IUserServiceDao userServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 停用用户信息
     *
     * @return
     */
    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_REMOVE_USER_INFO;
    }


    /**
     * 修改用户信息至 business表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        Assert.jsonObjectHaveKey(data, UserPo.class.getSimpleName(), "datas 节点下没有包含 businessUser 节点");

        JSONObject businessUser = data.getJSONArray(UserPo.class.getSimpleName()).getJSONObject(0);

        Assert.jsonObjectHaveKey(businessUser, "userId", "businessUser 节点下没有包含 userId 节点");

        if (businessUser.getString("userId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "userId 错误，不能自动生成（必须已经存在的userId）" + businessUser);
        }

        //自动插入DEL
        autoSaveDelBusinessUser(business, businessUser);
    }

    /**
     * 将 business的用户信息 保存至 instance表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //商户信息
        Map businessUserInfo = userServiceDaoImpl.queryBusinessUserInfo(info);
        if (businessUserInfo != null && !businessUserInfo.isEmpty()) {
            flushBusinessUserInfo(businessUserInfo, StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserInfoInstance(businessUserInfo);
            dataFlowContext.addParamOut("userId", businessUserInfo.get("user_id"));
        }
    }

    /**
     * 将instance 作废
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //商户信息
        Map userInfo = userServiceDaoImpl.queryUserInfo(info);
        if (userInfo != null && !userInfo.isEmpty()) {

            //商户信息
            Map businessUserInfo = userServiceDaoImpl.queryBusinessUserInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessUserInfo == null || businessUserInfo.isEmpty()) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（user），程序内部异常,请检查！ " + delInfo);
            }

            flushBusinessUserInfo(businessUserInfo, StatusConstant.STATUS_CD_VALID);
            userServiceDaoImpl.updateUserInfoInstance(businessUserInfo);
            dataFlowContext.addParamOut("userId", userInfo.get("user_id"));
        }
    }

    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
