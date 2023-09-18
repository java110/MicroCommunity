package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.PayFeeDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeReceiptInnerServiceSMO
 * @Description 收据接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorFeeReceiptApi")
public interface IGeneratorFeeReceiptInnerServiceSMO {


    @RequestMapping(value = "/generator", method = RequestMethod.POST)
   int generator(@RequestBody PayFeeDetailPo payFeeDetailPo);


}
