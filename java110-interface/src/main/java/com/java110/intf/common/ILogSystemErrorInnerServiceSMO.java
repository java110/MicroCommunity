package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.po.logSystemError.LogSystemErrorPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ILogSystemErrorInnerServiceSMO
 * @Description 系统异常接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/logSystemErrorApi")
public interface ILogSystemErrorInnerServiceSMO {


    @RequestMapping(value = "/saveLogSystemError", method = RequestMethod.POST)
    public int saveLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo);

    @RequestMapping(value = "/updateLogSystemError", method = RequestMethod.POST)
    public int updateLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo);

    @RequestMapping(value = "/deleteLogSystemError", method = RequestMethod.POST)
    public int deleteLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param logSystemErrorDto 数据对象分享
     * @return LogSystemErrorDto 对象数据
     */
    @RequestMapping(value = "/queryLogSystemErrors", method = RequestMethod.POST)
    List<LogSystemErrorDto> queryLogSystemErrors(@RequestBody LogSystemErrorDto logSystemErrorDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param logSystemErrorDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryLogSystemErrorsCount", method = RequestMethod.POST)
    int queryLogSystemErrorsCount(@RequestBody LogSystemErrorDto logSystemErrorDto);
}
