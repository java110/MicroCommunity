package com.java110.api.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.IApiServiceSMO;
import com.java110.api.smo.ITestServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * rest api
 * Created by wuxw on 2018/10/16.
 */
@RestController
@RequestMapping(path = "/test")
@Api(value = "对外统一提供服务接口服务")
public class TestApi extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TestApi.class);
    private static final String VERSION = "version";
    private static final String VERSION_2 = "2.0";
    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;

    @Autowired
    private ITestServiceSMO testServiceSMOImpl;

    /**
     * 健康检查 服务
     *
     * @return
     */
    @RequestMapping(path = "/health", method = RequestMethod.GET)
    public String health() {
        return testServiceSMOImpl.getVersion("123");
    }


}
