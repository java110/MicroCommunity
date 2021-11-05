package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.purchaseApply.PurchaseApplyDetailDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.po.purchase.PurchaseApplyPo;
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
     * <p>保存 采购申请</p>
     *
     * @param purchaseApplyPo 数据对象分享
     * @return PurchaseApplyDto 对象数据
     */
    @RequestMapping(value = "/savePurchaseApply", method = RequestMethod.POST)
    int savePurchaseApply(@RequestBody PurchaseApplyPo purchaseApplyPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param purchaseApplyDto 数据对象分享
     * @return PurchaseApplyDto 对象数据
     */
    @RequestMapping(value = "/queryPurchaseApplys", method = RequestMethod.POST)
    List<PurchaseApplyDto> queryPurchaseApplys(@RequestBody PurchaseApplyDto purchaseApplyDto);


    @RequestMapping(value = "/queryPurchaseApplyAndDetails", method = RequestMethod.POST)
    List<PurchaseApplyDto> queryPurchaseApplyAndDetails(@RequestBody PurchaseApplyDto purchaseApplyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param purchaseApplyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPurchaseApplysCount", method = RequestMethod.POST)
    int queryPurchaseApplysCount(@RequestBody PurchaseApplyDto purchaseApplyDto);


    //查询采购明细表
    @RequestMapping(value = "/queryPurchaseApplyDetails", method = RequestMethod.POST)
    List<PurchaseApplyDetailDto> queryPurchaseApplyDetails(@RequestBody PurchaseApplyDetailDto purchaseApplyDetailDto);

    //修改采购申请
    @RequestMapping(value = "/updatePurchaseApply", method = RequestMethod.POST)
    void updatePurchaseApply(@RequestBody PurchaseApplyPo purchaseApplyPo);

    /**
     * 获取下级处理人id
     *
     * @return
     */
    @RequestMapping(value = "/getActRuTaskUserId", method = RequestMethod.POST)
    List<PurchaseApplyDto> getActRuTaskUserId(PurchaseApplyDto purchaseApplyDto);

    /**
     * 获取流程任务id
     *
     * @return
     */
    @RequestMapping(value = "/getActRuTaskId", method = RequestMethod.POST)
    List<PurchaseApplyDto> getActRuTaskId(PurchaseApplyDto purchaseApplyDto);
    /**
     * 获取流程任务id
     *
     * @return
     */
    @RequestMapping(value = "/updateActRuTaskById", method = RequestMethod.POST)
    void updateActRuTaskById(@RequestBody PurchaseApplyDto purchaseApplyDto);

}
