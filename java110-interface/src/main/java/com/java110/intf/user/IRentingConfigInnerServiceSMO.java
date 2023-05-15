package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.rentingPool.RentingConfigDto;
import com.java110.po.rentingConfig.RentingConfigPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRentingConfigInnerServiceSMO
 * @Description 房屋出租配置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/rentingConfigApi")
public interface IRentingConfigInnerServiceSMO {


    @RequestMapping(value = "/saveRentingConfig", method = RequestMethod.POST)
    public int saveRentingConfig(@RequestBody RentingConfigPo rentingConfigPo);

    @RequestMapping(value = "/updateRentingConfig", method = RequestMethod.POST)
    public int updateRentingConfig(@RequestBody RentingConfigPo rentingConfigPo);

    @RequestMapping(value = "/deleteRentingConfig", method = RequestMethod.POST)
    public int deleteRentingConfig(@RequestBody RentingConfigPo rentingConfigPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param rentingConfigDto 数据对象分享
     * @return RentingConfigDto 对象数据
     */
    @RequestMapping(value = "/queryRentingConfigs", method = RequestMethod.POST)
    List<RentingConfigDto> queryRentingConfigs(@RequestBody RentingConfigDto rentingConfigDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param rentingConfigDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRentingConfigsCount", method = RequestMethod.POST)
    int queryRentingConfigsCount(@RequestBody RentingConfigDto rentingConfigDto);
}
