package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IApplyRoomDiscountInnerServiceSMO
 * @Description 房屋折扣申请接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/applyRoomDiscountApi")
public interface IApplyRoomDiscountInnerServiceSMO {


    @RequestMapping(value = "/saveApplyRoomDiscount", method = RequestMethod.POST)
    public int saveApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo);

    @RequestMapping(value = "/updateApplyRoomDiscount", method = RequestMethod.POST)
    public int updateApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo);

    @RequestMapping(value = "/deleteApplyRoomDiscount", method = RequestMethod.POST)
    public int deleteApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param applyRoomDiscountDto 数据对象分享
     * @return ApplyRoomDiscountDto 对象数据
     */
    @RequestMapping(value = "/queryApplyRoomDiscounts", method = RequestMethod.POST)
    List<ApplyRoomDiscountDto> queryApplyRoomDiscounts(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto);

    /**
     * 查询最新添加的符合条件的优惠申请信息
     *
     * @param applyRoomDiscountDto
     * @return
     * @author fqz
     */
    @RequestMapping(value = "/queryFirstApplyRoomDiscounts", method = RequestMethod.POST)
    List<ApplyRoomDiscountDto> queryFirstApplyRoomDiscounts(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param applyRoomDiscountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryApplyRoomDiscountsCount", method = RequestMethod.POST)
    int queryApplyRoomDiscountsCount(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto);
}
