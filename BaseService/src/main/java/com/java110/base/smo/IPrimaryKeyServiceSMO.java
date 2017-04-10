package com.java110.base.smo;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IPrimaryKeyServiceSMO {

    /**
     * 根据sequence 表中name 查询ID
     *
     * @param primaryKeyInfo name信息封装
     * @return
     */
    public JSONObject queryPrimaryKey(JSONObject primaryKeyInfo) throws Exception ;
}
