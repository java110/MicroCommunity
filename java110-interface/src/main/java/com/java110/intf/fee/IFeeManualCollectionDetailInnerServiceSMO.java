package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeManualCollectionDetailDto;
import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeManualCollectionDetailInnerServiceSMO
 * @Description 托收明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeManualCollectionDetailApi")
public interface IFeeManualCollectionDetailInnerServiceSMO {


    @RequestMapping(value = "/saveFeeManualCollectionDetail", method = RequestMethod.POST)
    public int saveFeeManualCollectionDetail(@RequestBody FeeManualCollectionDetailPo feeManualCollectionDetailPo);

    @RequestMapping(value = "/updateFeeManualCollectionDetail", method = RequestMethod.POST)
    public int updateFeeManualCollectionDetail(@RequestBody  FeeManualCollectionDetailPo feeManualCollectionDetailPo);

    @RequestMapping(value = "/deleteFeeManualCollectionDetail", method = RequestMethod.POST)
    public int deleteFeeManualCollectionDetail(@RequestBody  FeeManualCollectionDetailPo feeManualCollectionDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeManualCollectionDetailDto 数据对象分享
     * @return FeeManualCollectionDetailDto 对象数据
     */
    @RequestMapping(value = "/queryFeeManualCollectionDetails", method = RequestMethod.POST)
    List<FeeManualCollectionDetailDto> queryFeeManualCollectionDetails(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeManualCollectionDetailDto 数据对象分享
     * @return FeeManualCollectionDetailDto 对象数据
     */
    @RequestMapping(value = "/queryFeeManualCollectionDetailTotalFee", method = RequestMethod.POST)
    double queryFeeManualCollectionDetailTotalFee(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeManualCollectionDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeManualCollectionDetailsCount", method = RequestMethod.POST)
    int queryFeeManualCollectionDetailsCount(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto);
}
