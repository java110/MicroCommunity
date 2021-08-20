package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.po.fee.PayFeeDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeDetailInnerServiceSMO
 * @Description 费用明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeDetailApi")
public interface IFeeDetailInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeDetailDto 数据对象分享
     * @return FeeDetailDto 对象数据
     */
    @RequestMapping(value = "/queryFeeDetails", method = RequestMethod.POST)
    List<FeeDetailDto> queryFeeDetails(@RequestBody FeeDetailDto feeDetailDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeDetailDto 数据对象分享
     * @return FeeDetailDto 对象数据
     */
    @RequestMapping(value = "/queryBusinessFeeDetails", method = RequestMethod.POST)
    List<FeeDetailDto> queryBusinessFeeDetails(@RequestBody FeeDetailDto feeDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeDetailsCount", method = RequestMethod.POST)
    int queryFeeDetailsCount(@RequestBody FeeDetailDto feeDetailDto);

    /**
     * 保存费用明细
     * @param payFeeDetailPo
     * @return
     */
    @RequestMapping(value = "/saveFeeDetail")
    int saveFeeDetail(@RequestBody PayFeeDetailPo payFeeDetailPo);
}
