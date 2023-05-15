package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountRecordDto;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IApplyRoomDiscountRecordInnerServiceSMO
 * @Description 验房记录接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/applyRoomDiscountRecordApi")
public interface IApplyRoomDiscountRecordInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param applyRoomDiscountRecordDto 数据对象分享
     * @return ApplyRoomDiscountRecordDto 对象数据
     */
    @RequestMapping(value = "/queryApplyRoomDiscountRecords", method = RequestMethod.POST)
    List<ApplyRoomDiscountRecordDto> queryApplyRoomDiscountRecords(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

    /**
     * <p>查询空置房验房记录(不关联文件表)</p>
     *
     *
     * @param applyRoomDiscountRecordDto 数据对象分享
     * @return ApplyRoomDiscountRecordDto 对象数据
     */
    @RequestMapping(value = "/selectApplyRoomDiscountRecords", method = RequestMethod.POST)
    List<ApplyRoomDiscountRecordDto> selectApplyRoomDiscountRecords(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param applyRoomDiscountRecordDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryApplyRoomDiscountRecordsCount", method = RequestMethod.POST)
    int queryApplyRoomDiscountRecordsCount(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

    /**
     * 查询空置房验房记录数(不关联文件表)
     *
     * @param applyRoomDiscountRecordDto 数据对象分享
     * @return 记录数
     */
    @RequestMapping(value = "/selectApplyRoomDiscountRecordsCount", method = RequestMethod.POST)
    int selectApplyRoomDiscountRecordsCount(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

    /**
     * 新增空置房验房记录
     *
     * @param applyRoomDiscountRecordPo
     * @return
     */
    @RequestMapping(value = "/saveApplyRoomDiscountRecord", method = RequestMethod.POST)
    int saveApplyRoomDiscountRecord(@RequestBody ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo);

    /**
     * 删除空置房验房记录
     *
     * @param applyRoomDiscountRecordPo
     * @return
     */
    @RequestMapping(value = "/deleteApplyRoomDiscountRecord", method = RequestMethod.POST)
    int deleteApplyRoomDiscountRecord(@RequestBody ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo);
}
