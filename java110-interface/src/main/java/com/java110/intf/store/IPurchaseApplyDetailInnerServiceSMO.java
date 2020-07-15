package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @ClassName IPurchaseApplyDetailInnerServiceSMO
 * @Description 订单明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/purchaseApplyDetailApi")
public interface IPurchaseApplyDetailInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param purchaseApplyDetailDto 数据对象分享
     * @return PurchaseApplyDetailDto 对象数据
     */
    @RequestMapping(value = "/queryPurchaseApplyDetails", method = RequestMethod.POST)
    List<PurchaseApplyDetailDto> queryPurchaseApplyDetails(@RequestBody PurchaseApplyDetailDto purchaseApplyDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param purchaseApplyDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPurchaseApplyDetailsCount", method = RequestMethod.POST)
    int queryPurchaseApplyDetailsCount(@RequestBody PurchaseApplyDetailDto purchaseApplyDetailDto);

    /**
     * 修改采购申请明细
     *
     * @param purchaseApplyDetailPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updatePurchaseApplyDetail", method = RequestMethod.POST)
    int updatePurchaseApplyDetail(@RequestBody PurchaseApplyDetailPo purchaseApplyDetailPo);
}
