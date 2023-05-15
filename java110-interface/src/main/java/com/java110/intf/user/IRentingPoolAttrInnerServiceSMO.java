package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.rentingPool.RentingPoolAttrDto;
import com.java110.po.rentingPoolAttr.RentingPoolAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRentingPoolAttrInnerServiceSMO
 * @Description 出租房屋属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/rentingPoolAttrApi")
public interface IRentingPoolAttrInnerServiceSMO {


    @RequestMapping(value = "/saveRentingPoolAttr", method = RequestMethod.POST)
    public int saveRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo);

    @RequestMapping(value = "/updateRentingPoolAttr", method = RequestMethod.POST)
    public int updateRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo);

    @RequestMapping(value = "/deleteRentingPoolAttr", method = RequestMethod.POST)
    public int deleteRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param rentingPoolAttrDto 数据对象分享
     * @return RentingPoolAttrDto 对象数据
     */
    @RequestMapping(value = "/queryRentingPoolAttrs", method = RequestMethod.POST)
    List<RentingPoolAttrDto> queryRentingPoolAttrs(@RequestBody RentingPoolAttrDto rentingPoolAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param rentingPoolAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRentingPoolAttrsCount", method = RequestMethod.POST)
    int queryRentingPoolAttrsCount(@RequestBody RentingPoolAttrDto rentingPoolAttrDto);
}
