package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.po.feeAccountDetail.FeeAccountDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 抵扣明细接口类
 *
 * @author fqz
 * @date 2022-05-16
 */
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeAccountDetailApi")
public interface IFeeAccountDetailServiceSMO {

    @RequestMapping(value = "/queryFeeAccountDetails", method = RequestMethod.POST)
    List<FeeAccountDetailDto> queryFeeAccountDetails(@RequestBody FeeAccountDetailDto feeAccountDetailDto);

    @RequestMapping(value = "/queryFeeAccountDetailsCount", method = RequestMethod.POST)
    int queryFeeAccountDetailsCount(@RequestBody FeeAccountDetailDto feeAccountDetailDto);

    @RequestMapping(value = "saveFeeAccountDetail")
    int saveFeeAccountDetail(@RequestBody FeeAccountDetailPo feeAccountDetailPo);

}
