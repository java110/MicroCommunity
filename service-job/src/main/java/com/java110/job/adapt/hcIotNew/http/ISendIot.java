package com.java110.job.adapt.hcIotNew.http;

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


    /***
     * post 请求
     * @param url
     * @return
     */
    ResultVo get(String url);
}
