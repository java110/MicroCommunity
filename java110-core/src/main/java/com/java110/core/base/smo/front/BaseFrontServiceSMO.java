package com.java110.core.base.smo.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName BaseFrontServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/15 21:22
 * @Version 1.0
 * add by wuxw 2020/6/15
 **/
public class BaseFrontServiceSMO extends BaseServiceSMO {

    private static final String URL_API =  "";
    //日志
    private static Logger logger = LoggerFactory.getLogger(BaseFrontServiceSMO.class);

    @Autowired
    private RestTemplate restTemplate;





}
