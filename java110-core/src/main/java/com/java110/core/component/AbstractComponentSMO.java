package com.java110.core.component;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.UrlParamToJsonUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 组件抽象类
 * <p>
 * add by wuxw 2019-06-19
 */
public abstract class AbstractComponentSMO extends BaseComponentSMO {
    private static Logger logger = LoggerFactory.getLogger(AbstractComponentSMO.class);


    /**
     * 统一业务处理类
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    protected final ResponseEntity<String> businessProcess(IPageData pd) {

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        logger.debug("businessProcess: pd=" + JSONObject.toJSONString(pd));


        //业务数据校验
        validate(pd, paramIn);
        ResponseEntity<String> businessResult = null;
        try {
            businessResult = doBusinessProcess(pd, paramIn);
        } catch (Exception e) {
            logger.error("调用实现类异常：", e);
            businessResult = new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

        return businessResult;

    }





    /**
     * 页面数据校验 方法
     *
     * @param pd      页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract void validate(IPageData pd, JSONObject paramIn);

    /**
     * 业务数据处理类
     *
     * @param pd      页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception;
}
