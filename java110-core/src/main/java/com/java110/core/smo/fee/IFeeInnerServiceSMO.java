package com.java110.core.smo.fee;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeInnerServiceSMO
 * @Description 费用接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeApi")
public interface IFeeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeDto 数据对象分享
     * @return FeeDto 对象数据
     */
    @RequestMapping(value = "/queryFees", method = RequestMethod.POST)
    List<FeeDto> queryFees(@RequestBody FeeDto feeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeesCount", method = RequestMethod.POST)
    int queryFeesCount(@RequestBody FeeDto feeDto);
}
