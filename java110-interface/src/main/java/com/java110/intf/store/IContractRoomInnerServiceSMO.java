package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.po.contractRoom.ContractRoomPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IContractRoomInnerServiceSMO
 * @Description 合同房屋接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractRoomApi")
public interface IContractRoomInnerServiceSMO {


    @RequestMapping(value = "/saveContractRoom", method = RequestMethod.POST)
    public int saveContractRoom(@RequestBody ContractRoomPo contractRoomPo);

    @RequestMapping(value = "/updateContractRoom", method = RequestMethod.POST)
    public int updateContractRoom(@RequestBody  ContractRoomPo contractRoomPo);

    @RequestMapping(value = "/deleteContractRoom", method = RequestMethod.POST)
    public int deleteContractRoom(@RequestBody  ContractRoomPo contractRoomPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractRoomDto 数据对象分享
     * @return ContractRoomDto 对象数据
     */
    @RequestMapping(value = "/queryContractRooms", method = RequestMethod.POST)
    List<ContractRoomDto> queryContractRooms(@RequestBody ContractRoomDto contractRoomDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractRoomDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractRoomsCount", method = RequestMethod.POST)
    int queryContractRoomsCount(@RequestBody ContractRoomDto contractRoomDto);

    @RequestMapping(value = "/queryContractByRoomIds", method = RequestMethod.POST)
    List<Map> queryContractByRoomIds(@RequestBody Map info);
}
