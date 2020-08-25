package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.po.rentingPool.RentingPoolPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRentingPoolInnerServiceSMO
 * @Description 房屋出租接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/rentingPoolApi")
public interface IRentingPoolInnerServiceSMO {


    @RequestMapping(value = "/saveRentingPool", method = RequestMethod.POST)
    public int saveRentingPool(@RequestBody RentingPoolPo rentingPoolPo);

    @RequestMapping(value = "/updateRentingPool", method = RequestMethod.POST)
    public int updateRentingPool(@RequestBody RentingPoolPo rentingPoolPo);

    @RequestMapping(value = "/deleteRentingPool", method = RequestMethod.POST)
    public int deleteRentingPool(@RequestBody RentingPoolPo rentingPoolPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param rentingPoolDto 数据对象分享
     * @return RentingPoolDto 对象数据
     */
    @RequestMapping(value = "/queryRentingPools", method = RequestMethod.POST)
    List<RentingPoolDto> queryRentingPools(@RequestBody RentingPoolDto rentingPoolDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param rentingPoolDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRentingPoolsCount", method = RequestMethod.POST)
    int queryRentingPoolsCount(@RequestBody RentingPoolDto rentingPoolDto);
}
