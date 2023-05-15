package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
import com.java110.po.feeReceipt.FeeReceiptPo;
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
@RequestMapping("/feeReceiptApi")
public interface IFeeReceiptInnerServiceSMO {


    @RequestMapping(value = "/saveFeeReceipt", method = RequestMethod.POST)
    public int saveFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo);

    @RequestMapping(value = "/saveFeeReceipts", method = RequestMethod.POST)
    public int saveFeeReceipts(@RequestBody List<FeeReceiptPo> feeReceiptPos);

    @RequestMapping(value = "/updateFeeReceipt", method = RequestMethod.POST)
    public int updateFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo);

    @RequestMapping(value = "/deleteFeeReceipt", method = RequestMethod.POST)
    public int deleteFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeReceiptDto 数据对象分享
     * @return FeeReceiptDto 对象数据
     */
    @RequestMapping(value = "/queryFeeReceipts", method = RequestMethod.POST)
    List<FeeReceiptDto> queryFeeReceipts(@RequestBody FeeReceiptDto feeReceiptDto);


    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeReceiptDto 数据对象分享
     * @return FeeReceiptDtoNew 对象数据
     */
    @RequestMapping(value = "/queryFeeReceiptsNew", method = RequestMethod.POST)
    List<FeeReceiptDtoNew> queryFeeReceiptsNew(@RequestBody FeeReceiptDtoNew feeReceiptDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeReceiptDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeReceiptsCount", method = RequestMethod.POST)
    int queryFeeReceiptsCount(@RequestBody FeeReceiptDto feeReceiptDto);
}
