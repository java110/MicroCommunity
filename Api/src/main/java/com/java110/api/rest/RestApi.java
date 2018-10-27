package com.java110.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * rest api
 * Created by wuxw on 2018/10/16.
 */
@RestController

@Api(value = "Rest Api 用户服务")
@RequestMapping(path = "/rest")
public class RestApi {

    /**
     * 健康检查 服务
     * @return
     */
    @RequestMapping(path = "/health",method = RequestMethod.GET)
    @ApiOperation(value="服务健康检查", notes="test: 返回 2XX 表示服务正常")
    //@ApiImplicitParam(paramType="query", name = "userNumber", value = "用户编号", required = true, dataType = "Integer")
    public String health(){
        return "";
    }

    @ApiOperation(value="保存用户信息", notes="test: res_code 为0000表示成功，其他表示失败")
    @ApiImplicitParam(paramType="save", name = "info", value = "用户编号", required = true, dataType = "String")
    @RequestMapping(path = "/saveUser",method = RequestMethod.PUT)
    public String saveUser(@RequestParam("info") String info){
        return "{}";
    }
}
