package com.java110.user.smo;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IUserServiceSMO {

    /**
     * 新建用户
     * @param userInfoJson
     * @return
     */
    public String saveUser(String userInfoJson);

    /**
     * 所有服务类（增删改查用户）
     * @param userInfoJson
     * @return
     */
    public String soUserService(String userInfoJson);
}
