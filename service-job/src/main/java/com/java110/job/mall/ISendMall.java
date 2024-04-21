package com.java110.job.mall;

import com.alibaba.fastjson.JSONObject;
import com.java110.vo.ResultVo;

/**
 *
 */
public interface ISendMall {

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
