package com.java110.generator.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.exception.SMOException;
import com.java110.core.context.CodeDataFlow;

/**
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
    public JSONObject queryPrimaryKey(JSONObject primaryKeyInfo) throws Exception;

    /**
     * 生成编码
     * @param dataFlow
     * @throws SMOException
     */
    public void generateCode(CodeDataFlow dataFlow) throws SMOException;
}
