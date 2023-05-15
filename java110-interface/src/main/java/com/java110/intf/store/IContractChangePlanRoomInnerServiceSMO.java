package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractChangePlanRoomDto;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractChangePlanRoomInnerServiceSMO
 * @Description 合同房屋变更接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractChangePlanRoomApi")
public interface IContractChangePlanRoomInnerServiceSMO {


    @RequestMapping(value = "/saveContractChangePlanRoom", method = RequestMethod.POST)
    public int saveContractChangePlanRoom(@RequestBody ContractChangePlanRoomPo contractChangePlanRoomPo);

    @RequestMapping(value = "/updateContractChangePlanRoom", method = RequestMethod.POST)
    public int updateContractChangePlanRoom(@RequestBody  ContractChangePlanRoomPo contractChangePlanRoomPo);

    @RequestMapping(value = "/deleteContractChangePlanRoom", method = RequestMethod.POST)
    public int deleteContractChangePlanRoom(@RequestBody  ContractChangePlanRoomPo contractChangePlanRoomPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractChangePlanRoomDto 数据对象分享
     * @return ContractChangePlanRoomDto 对象数据
     */
    @RequestMapping(value = "/queryContractChangePlanRooms", method = RequestMethod.POST)
    List<ContractChangePlanRoomDto> queryContractChangePlanRooms(@RequestBody ContractChangePlanRoomDto contractChangePlanRoomDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractChangePlanRoomDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractChangePlanRoomsCount", method = RequestMethod.POST)
    int queryContractChangePlanRoomsCount(@RequestBody ContractChangePlanRoomDto contractChangePlanRoomDto);
}
