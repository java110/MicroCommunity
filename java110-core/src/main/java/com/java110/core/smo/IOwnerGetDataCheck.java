package com.java110.core.smo;

import com.alibaba.fastjson.JSONObject;

/***
 * 业主 查询数据合法性校验
 */
public interface IOwnerGetDataCheck {

    /**
     * 校验业主 账户查询
     *
     * @param appId       应用ID
     * @param loginUserId 登陆用户ID
     * @param reqJson     传递参数内容
     */
    void checkOwnerAccount(String appId, String loginUserId, JSONObject reqJson);

    /**
     * 查询费用校验
     *
     * @param appId
     * @param loginUserId
     * @param reqJson
     */
    void checkOwnerFee(String appId, String loginUserId, JSONObject reqJson);
}
