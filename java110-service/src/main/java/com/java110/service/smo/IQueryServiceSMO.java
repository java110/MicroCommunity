package com.java110.service.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.BusinessException;
import com.java110.entity.service.DataQuery;

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
}
