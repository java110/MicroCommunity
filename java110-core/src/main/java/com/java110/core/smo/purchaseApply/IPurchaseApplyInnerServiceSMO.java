package com.java110.core.smo.purchaseApply;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPurchaseApplyInnerServiceSMO
 * @Description 采购申请接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/purchaseApplyApi")
public interface IPurchaseApplyInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param purchaseApplyDto 数据对象分享
     * @return PurchaseApplyDto 对象数据
     */
    @RequestMapping(value = "/queryPurchaseApplys", method = RequestMethod.POST)
    List<PurchaseApplyDto> queryPurchaseApplys(@RequestBody PurchaseApplyDto purchaseApplyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param purchaseApplyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPurchaseApplysCount", method = RequestMethod.POST)
    int queryPurchaseApplysCount(@RequestBody PurchaseApplyDto purchaseApplyDto);
}
