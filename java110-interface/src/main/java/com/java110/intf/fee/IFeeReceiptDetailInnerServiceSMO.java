package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeReceiptDetailInnerServiceSMO
 * @Description 收据明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeReceiptDetailApi")
public interface IFeeReceiptDetailInnerServiceSMO {


    @RequestMapping(value = "/saveFeeReceiptDetail", method = RequestMethod.POST)
    public int saveFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo);


    @RequestMapping(value = "/saveFeeReceiptDetails", method = RequestMethod.POST)
    public int saveFeeReceiptDetails(@RequestBody List<FeeReceiptDetailPo> feeReceiptDetailPos);

    @RequestMapping(value = "/updateFeeReceiptDetail", method = RequestMethod.POST)
    public int updateFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo);

    @RequestMapping(value = "/deleteFeeReceiptDetail", method = RequestMethod.POST)
    public int deleteFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeReceiptDetailDto 数据对象分享
     * @return FeeReceiptDetailDto 对象数据
     */
    @RequestMapping(value = "/queryFeeReceiptDetails", method = RequestMethod.POST)
    List<FeeReceiptDetailDto> queryFeeReceiptDetails(@RequestBody FeeReceiptDetailDto feeReceiptDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeReceiptDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeReceiptDetailsCount", method = RequestMethod.POST)
    int queryFeeReceiptDetailsCount(@RequestBody FeeReceiptDetailDto feeReceiptDetailDto);
}
