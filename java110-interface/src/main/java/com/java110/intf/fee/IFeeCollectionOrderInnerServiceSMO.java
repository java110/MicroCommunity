package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeCollectionOrderDto;
import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeCollectionOrderInnerServiceSMO
 * @Description 催缴单接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})

@RequestMapping("/feeCollectionOrderApi")
public interface IFeeCollectionOrderInnerServiceSMO {


    @RequestMapping(value = "/saveFeeCollectionOrder", method = RequestMethod.POST)
    public int saveFeeCollectionOrder(@RequestBody FeeCollectionOrderPo feeCollectionOrderPo);

    @RequestMapping(value = "/updateFeeCollectionOrder", method = RequestMethod.POST)
    public int updateFeeCollectionOrder(@RequestBody  FeeCollectionOrderPo feeCollectionOrderPo);

    @RequestMapping(value = "/deleteFeeCollectionOrder", method = RequestMethod.POST)
    public int deleteFeeCollectionOrder(@RequestBody  FeeCollectionOrderPo feeCollectionOrderPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeCollectionOrderDto 数据对象分享
     * @return FeeCollectionOrderDto 对象数据
     */
    @RequestMapping(value = "/queryFeeCollectionOrders", method = RequestMethod.POST)
    List<FeeCollectionOrderDto> queryFeeCollectionOrders(@RequestBody FeeCollectionOrderDto feeCollectionOrderDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeCollectionOrderDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeCollectionOrdersCount", method = RequestMethod.POST)
    int queryFeeCollectionOrdersCount(@RequestBody FeeCollectionOrderDto feeCollectionOrderDto);
}
