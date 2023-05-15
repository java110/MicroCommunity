package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.rentingPool.RentingPoolFlowDto;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRentingPoolFlowInnerServiceSMO
 * @Description 出租流程接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/rentingPoolFlowApi")
public interface IRentingPoolFlowInnerServiceSMO {


    @RequestMapping(value = "/saveRentingPoolFlow", method = RequestMethod.POST)
    public int saveRentingPoolFlow(@RequestBody RentingPoolFlowPo rentingPoolFlowPo);

    @RequestMapping(value = "/updateRentingPoolFlow", method = RequestMethod.POST)
    public int updateRentingPoolFlow(@RequestBody  RentingPoolFlowPo rentingPoolFlowPo);

    @RequestMapping(value = "/deleteRentingPoolFlow", method = RequestMethod.POST)
    public int deleteRentingPoolFlow(@RequestBody  RentingPoolFlowPo rentingPoolFlowPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param rentingPoolFlowDto 数据对象分享
     * @return RentingPoolFlowDto 对象数据
     */
    @RequestMapping(value = "/queryRentingPoolFlows", method = RequestMethod.POST)
    List<RentingPoolFlowDto> queryRentingPoolFlows(@RequestBody RentingPoolFlowDto rentingPoolFlowDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param rentingPoolFlowDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRentingPoolFlowsCount", method = RequestMethod.POST)
    int queryRentingPoolFlowsCount(@RequestBody RentingPoolFlowDto rentingPoolFlowDto);
}
