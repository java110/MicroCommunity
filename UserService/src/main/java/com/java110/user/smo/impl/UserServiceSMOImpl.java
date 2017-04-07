package com.java110.user.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.user.smo.IUserServiceSMO;
import com.java110.core.base.smo.BaseServiceSMO;
import org.springframework.stereotype.Service;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("userServiceSMOImpl")
public class UserServiceSMOImpl extends BaseServiceSMO implements IUserServiceSMO {

    //新增用户
    private final static String USER_ACTION_ADD = "ADD";

    //新增用户
    private final static String USER_ACTION_KIP = "KIP";

    //新增用户
    private final static String USER_ACTION_DEL = "DEL";

    /**
     * 保存用户信息
     *
     * @param userInfoJson 入参为用户信息json传
     * @return
     */
    public String saveUser(String userInfoJson) {

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(userInfoJson);
            //boCust增加Action (动作)
            if (reqUserJSON.containsKey("boCust")) {
                JSONObject boCust = reqUserJSON.getJSONObject("boCust");
                boCust.put("state", USER_ACTION_ADD);
            }
            //boCustAttr增加Action（动作）
            if (reqUserJSON.containsKey("boCustAttr")) {
                JSONArray boCustAttrs = reqUserJSON.getJSONArray("boCustAttr");

                for (int attrIndex = 0; attrIndex < boCustAttrs.size(); attrIndex++) {
                    JSONObject boCustAttr = boCustAttrs.getJSONObject(attrIndex);
                    boCustAttr.put("state", USER_ACTION_ADD);
                }
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soUserService(reqUserJSON.toJSONString());
    }


    /**
     * 所有服务处理类
     *
     * @param userInfoJson
     * @return
     */
    public String soUserService(String userInfoJson) {
        LoggerEngine.debug("用户服务操作客户入参：" + userInfoJson);
        String resultUserInfo = null;
        try {
            //1.0规则校验，报文是否合法

            //2.0
        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }
}
