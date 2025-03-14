package com.java110.job.adapt.hcIot.http;

import com.alibaba.fastjson.JSONObject;
import com.java110.vo.ResultVo;

/**
 *
 */
public interface ISendIot {

    /***
     * post 请求
     * @param url
     * @param paramIn
     * @return
     */
    ResultVo post(String url, JSONObject paramIn);

    /**
     * 调用物联网运营的通用接口
     * @param iotApiCode
     * @param paramIn
     * @return
     */
    ResultVo postAdmin(String iotApiCode,JSONObject paramIn);


    /***
     * post 请求
     * @param url
     * @return
     */
    ResultVo get(String url);
}
