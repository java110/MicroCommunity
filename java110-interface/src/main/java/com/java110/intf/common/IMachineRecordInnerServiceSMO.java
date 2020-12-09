package com.java110.intf.common;

import com.alibaba.fastjson.JSONArray;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.po.machine.MachineRecordPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineRecordInnerServiceSMO
 * @Description 设备上报接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineRecordApi")
public interface IMachineRecordInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineRecordDto 数据对象分享
     * @return MachineRecordDto 对象数据
     */
    @RequestMapping(value = "/queryMachineRecords", method = RequestMethod.POST)
    List<MachineRecordDto> queryMachineRecords(@RequestBody MachineRecordDto machineRecordDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineRecordDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachineRecordsCount", method = RequestMethod.POST)
    int queryMachineRecordsCount(@RequestBody MachineRecordDto machineRecordDto);

    @RequestMapping(value = "/getAssetsMachineRecords", method = RequestMethod.POST)
    JSONArray getAssetsMachineRecords(@RequestBody String communityId);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineRecordPos 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveMachineRecords", method = RequestMethod.POST)
    int saveMachineRecords(@RequestBody List<MachineRecordPo> machineRecordPos);
}
