package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeManualCollectionDto;
import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeManualCollectionInnerServiceSMO
 * @Description 人工托收接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeManualCollectionApi")
public interface IFeeManualCollectionInnerServiceSMO {


    @RequestMapping(value = "/saveFeeManualCollection", method = RequestMethod.POST)
    public int saveFeeManualCollection(@RequestBody FeeManualCollectionPo feeManualCollectionPo);

    @RequestMapping(value = "/updateFeeManualCollection", method = RequestMethod.POST)
    public int updateFeeManualCollection(@RequestBody  FeeManualCollectionPo feeManualCollectionPo);

    @RequestMapping(value = "/deleteFeeManualCollection", method = RequestMethod.POST)
    public int deleteFeeManualCollection(@RequestBody  FeeManualCollectionPo feeManualCollectionPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeManualCollectionDto 数据对象分享
     * @return FeeManualCollectionDto 对象数据
     */
    @RequestMapping(value = "/queryFeeManualCollections", method = RequestMethod.POST)
    List<FeeManualCollectionDto> queryFeeManualCollections(@RequestBody FeeManualCollectionDto feeManualCollectionDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeManualCollectionDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeManualCollectionsCount", method = RequestMethod.POST)
    int queryFeeManualCollectionsCount(@RequestBody FeeManualCollectionDto feeManualCollectionDto);
}
