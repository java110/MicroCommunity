package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeCollectionDetailDto;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeCollectionDetailInnerServiceSMO
 * @Description 催缴单接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeCollectionDetailApi")
public interface IFeeCollectionDetailInnerServiceSMO {


    @RequestMapping(value = "/saveFeeCollectionDetail", method = RequestMethod.POST)
    public int saveFeeCollectionDetail(@RequestBody FeeCollectionDetailPo feeCollectionDetailPo);

    @RequestMapping(value = "/updateFeeCollectionDetail", method = RequestMethod.POST)
    public int updateFeeCollectionDetail(@RequestBody  FeeCollectionDetailPo feeCollectionDetailPo);

    @RequestMapping(value = "/deleteFeeCollectionDetail", method = RequestMethod.POST)
    public int deleteFeeCollectionDetail(@RequestBody  FeeCollectionDetailPo feeCollectionDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeCollectionDetailDto 数据对象分享
     * @return FeeCollectionDetailDto 对象数据
     */
    @RequestMapping(value = "/queryFeeCollectionDetails", method = RequestMethod.POST)
    List<FeeCollectionDetailDto> queryFeeCollectionDetails(@RequestBody FeeCollectionDetailDto feeCollectionDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeCollectionDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeCollectionDetailsCount", method = RequestMethod.POST)
    int queryFeeCollectionDetailsCount(@RequestBody FeeCollectionDetailDto feeCollectionDetailDto);
}
