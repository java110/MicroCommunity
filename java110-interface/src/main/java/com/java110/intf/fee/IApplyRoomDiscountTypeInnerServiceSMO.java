package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountTypeDto;
import com.java110.po.applyRoomDiscountType.ApplyRoomDiscountTypePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IApplyRoomDiscountTypeInnerServiceSMO
 * @Description 优惠申请类型接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/applyRoomDiscountTypeApi")
public interface IApplyRoomDiscountTypeInnerServiceSMO {


    @RequestMapping(value = "/saveApplyRoomDiscountType", method = RequestMethod.POST)
    public int saveApplyRoomDiscountType(@RequestBody ApplyRoomDiscountTypePo applyRoomDiscountTypePo);

    @RequestMapping(value = "/updateApplyRoomDiscountType", method = RequestMethod.POST)
    public int updateApplyRoomDiscountType(@RequestBody  ApplyRoomDiscountTypePo applyRoomDiscountTypePo);

    @RequestMapping(value = "/deleteApplyRoomDiscountType", method = RequestMethod.POST)
    public int deleteApplyRoomDiscountType(@RequestBody  ApplyRoomDiscountTypePo applyRoomDiscountTypePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param applyRoomDiscountTypeDto 数据对象分享
     * @return ApplyRoomDiscountTypeDto 对象数据
     */
    @RequestMapping(value = "/queryApplyRoomDiscountTypes", method = RequestMethod.POST)
    List<ApplyRoomDiscountTypeDto> queryApplyRoomDiscountTypes(@RequestBody ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param applyRoomDiscountTypeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryApplyRoomDiscountTypesCount", method = RequestMethod.POST)
    int queryApplyRoomDiscountTypesCount(@RequestBody ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto);
}
