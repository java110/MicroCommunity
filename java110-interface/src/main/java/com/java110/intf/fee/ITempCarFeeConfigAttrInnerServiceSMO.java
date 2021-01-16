package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITempCarFeeConfigAttrInnerServiceSMO
 * @Description 临时车收费标准属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/tempCarFeeConfigAttrApi")
public interface ITempCarFeeConfigAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param tempCarFeeConfigAttrDto 数据对象分享
     * @return TempCarFeeConfigAttrDto 对象数据
     */
    @RequestMapping(value = "/queryTempCarFeeConfigAttrs", method = RequestMethod.POST)
    List<TempCarFeeConfigAttrDto> queryTempCarFeeConfigAttrs(@RequestBody TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param tempCarFeeConfigAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTempCarFeeConfigAttrsCount", method = RequestMethod.POST)
    int queryTempCarFeeConfigAttrsCount(@RequestBody TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto);
}
