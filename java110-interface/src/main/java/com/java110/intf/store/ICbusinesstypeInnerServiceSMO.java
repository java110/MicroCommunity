package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.businesstype.CbusinesstypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICbusinesstypeInnerServiceSMO
 * @Description cbusinesstype接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/cbusinesstypeApi")
public interface ICbusinesstypeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param cbusinesstypeDto 数据对象分享
     * @return CbusinesstypeDto 对象数据
     */
    @RequestMapping(value = "/queryCbusinesstypes", method = RequestMethod.POST)
    List<CbusinesstypeDto> queryCbusinesstypes(@RequestBody CbusinesstypeDto cbusinesstypeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param cbusinesstypeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCbusinesstypesCount", method = RequestMethod.POST)
    int queryCbusinesstypesCount(@RequestBody CbusinesstypeDto cbusinesstypeDto);
}
