package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @ClassName IReturnPayFeeInnerServiceSMO
 * @Description 退费表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/returnPayFeeApi")
public interface IReturnPayFeeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param returnPayFeeDto 数据对象分享
     * @return ReturnPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryReturnPayFees", method = RequestMethod.POST)
    List<ReturnPayFeeDto> queryReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param returnPayFeeDto 数据对象分享
     * @return ReturnPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryRoomReturnPayFees", method = RequestMethod.POST)
    List<ReturnPayFeeDto> queryRoomReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param returnPayFeeDto 数据对象分享
     * @return ReturnPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryCarReturnPayFees", method = RequestMethod.POST)
    List<ReturnPayFeeDto> queryCarReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param returnPayFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReturnPayFeesCount", method = RequestMethod.POST)
    int queryReturnPayFeesCount(@RequestBody ReturnPayFeeDto returnPayFeeDto);
}
