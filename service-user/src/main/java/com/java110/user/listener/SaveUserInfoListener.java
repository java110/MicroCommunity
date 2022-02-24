package com.java110.user.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.user.UserPo;
import com.java110.user.dao.IUserServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUserInfoListener")
@Transactional
public class SaveUserInfoListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveUserInfoListener.class);

    @Autowired
    IUserServiceDao userServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO;
    }


    /**
     * 保存用户信息至 business表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        Assert.jsonObjectHaveKey(data, UserPo.class.getSimpleName(), "datas 节点下没有包含 businessUser 节点" + data);

        //这里先支持 jsonObject 后期再改造
        JSONObject businessUser = data.getJSONArray(UserPo.class.getSimpleName()).getJSONObject(0);

        Assert.jsonObjectHaveKey(businessUser, "userId", "businessUser 节点下没有包含 userId 节点");

        if (businessUser.getString("userId").startsWith("-")) {
            //生成userId
            String userId = GenerateCodeFactory.getUserId();
            businessUser.put("userId", userId);
        }
        dataFlowContext.addParamOut("userId", businessUser.getString("userId"));
        businessUser.put("bId", business.getbId());
        businessUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存用户信息
        userServiceDaoImpl.saveBusinessUserInfo(businessUser);

        if (businessUser.containsKey("businessUserAttr")) {
            doSaveUserAttrs(business);
        }
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
        info.put("operate", StatusConstant.OPERATE_ADD);
        Map businessUser = userServiceDaoImpl.queryBusinessUserInfo(info);
        if (businessUser != null && !businessUser.isEmpty()) {
            userServiceDaoImpl.saveUserInfoInstance(businessUser);
            dataFlowContext.addParamOut("userId", businessUser.get("user_id"));
        }
        List<Map> businessUserAttrs = userServiceDaoImpl.queryBusinessUserInfoAttrs(info);
        if (businessUserAttrs != null && businessUserAttrs.size() > 0) {
            userServiceDaoImpl.saveUserAttrInstance(businessUser);
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
        Map userInfo = userServiceDaoImpl.queryUserInfo(info);
        if (userInfo != null && !userInfo.isEmpty()) {
            info.put("bId", bId);
            info.put("userId", userInfo.get("user_id").toString());
            info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserInfoInstance(info);
            dataFlowContext.addParamOut("userId", userInfo.get("user_id"));
        }

        info.clear();
        info.put("bId", bId);

        List<Map> userAttrs = userServiceDaoImpl.queryUserInfoAttrs(info);

        if (userAttrs != null && userAttrs.size() > 0) {
            info.put("bId", bId);
            //info.put("userId",userInfo.get("user_id").toString());
            info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
            userServiceDaoImpl.updateUserAttrInstance(info);
        }
    }

    private void doSaveUserAttrs(Business business) {
        JSONObject data = business.getDatas();
        JSONObject businessUser = data.getJSONObject("businessUser");
        JSONArray businessUserAttrs = businessUser.getJSONArray("businessUserAttr");
        for (int userAttrIndex = 0; userAttrIndex < businessUserAttrs.size(); userAttrIndex++) {
            JSONObject userAttr = businessUserAttrs.getJSONObject(userAttrIndex);
            Assert.jsonObjectHaveKey(userAttr, "attrId", "businessUserAttr 节点下没有包含 attrId 节点");

            if (userAttr.getInteger("attrId") < 0) {
                String attrId = GenerateCodeFactory.getAttrId();
                userAttr.put("attrId", attrId);
            }

            userAttr.put("bId", business.getbId());
            userAttr.put("userId", businessUser.getString("userId"));
            userAttr.put("operate", StatusConstant.OPERATE_ADD);

            userServiceDaoImpl.saveBusinessUserAttr(userAttr);
        }
    }

    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
