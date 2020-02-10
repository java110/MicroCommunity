package com.java110.app.controller;

import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName EnvController 环境查询控制类
 * @Description TODO
 * @Author wuxw
 * @Date 2020/2/10 23:17
 * @Version 1.0
 * add by wuxw 2020/2/10
 **/
@RestController
@RequestMapping(path = "/app/env")
public class EnvController {

    @RequestMapping(path = "/getEnv", method = RequestMethod.GET)
    public ResponseEntity<String> getEnv(HttpServletRequest request) {
        String env = MappingCache.getValue("HC_ENV");
        return new ResponseEntity<>(env, HttpStatus.OK);
    }
}
