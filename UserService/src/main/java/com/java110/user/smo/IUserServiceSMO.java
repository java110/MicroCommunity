package com.java110.user.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.user.Cust;

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
    public String saveUser(String userInfoJson) throws Exception;

    /**
     * 所有服务类（增删改查用户）
     * @param userInfoJson
     * @return
     */
    public String soUserService(JSONObject userInfoJson) throws Exception;

    /**
     * 客户信息处理
     * 协议：
     *{
     *     boCust:[{},{}]
     * }
     * @param boCusts
     * @return
     * @throws Exception
     */
    public String soBoCust(String boCusts) throws Exception;

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boCustAttr:[{},{}]
     * }
     * @param boCustAttrs
     * @return
     * @throws Exception
     */
    public String soBoCustAttr(String boCustAttrs) throws Exception;


    /**
     * 查询客户信息
     * 包括 基本信息cust 和 属性信息 custAttr
     * @param cust
     * @return
     * @throws Exception
     */
    public String queryCust(Cust cust) throws Exception;
}
