package com.java110.core.smo.code;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @ClassName IPrimaryKeyInnerServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/23 8:57
 * @Version 1.0
 * add by wuxw 2019/4/23
 **/
@FeignClient("code-service")
public interface IPrimaryKeyInnerServiceSMO {

    /**
     * 查询 主见信息
     * @param data 入参信息
     * @return {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
     */
    String queryPrimaryKey(String data);
}
