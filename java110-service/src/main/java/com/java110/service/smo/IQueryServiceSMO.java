package com.java110.service.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.exception.BusinessException;
import com.java110.service.context.DataQuery;
import org.springframework.http.ResponseEntity;

/**
 * 公用查询处理
 * Created by wuxw on 2018/4/19.
 */
public interface IQueryServiceSMO {

    /**
     * c_common_sql
     * 公共查询服务
     * @return
     * @throws BusinessException
     */
    public void commonQueryService(DataQuery dataQuery) throws BusinessException;


    /**
     * c_common_sql
     * 公共受理服务
     * @return
     * @throws BusinessException
     */
    public void commonDoService(DataQuery dataQuery) throws BusinessException;


    /**
     * c_common_sql
     * 公共受理服务
     * @return
     * @throws BusinessException
     */
    public ResponseEntity<String> fallBack(String fallBackInfo) throws BusinessException;

    /**
     * 执行查询sql
     * @param param
     * @param sql
     * @return {
     *     th:[],
     *     td:[{}]
     * }
     */
    JSONObject execQuerySql(JSONObject param, String sql) throws BusinessException;

    /**
     * 执行java脚本
     *
     * @param javaCode
     * @throws BusinessException
     */
    JSONObject execJava(JSONObject params, String javaCode);
}
