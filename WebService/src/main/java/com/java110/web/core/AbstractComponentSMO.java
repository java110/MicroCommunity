package com.java110.web.core;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 组件抽象类
 *
 * add by wuxw 2019-06-19
 */
public abstract class AbstractComponentSMO extends BaseComponentSMO{

    /**
     * 统一业务处理类
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    protected final ResponseEntity<String> businessProcess(IPageData pd){

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());


        //业务数据校验
        validate(pd, paramIn);

        ResponseEntity<String> businessResult = doBusinessProcess(pd, paramIn);

        return businessResult;

    }

    /**
     * 页面数据校验 方法
     * @param pd 页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract void validate(IPageData pd, JSONObject paramIn);

    /**
     * 业务数据处理类
     * @param pd 页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn);
}
