package com.java110.core.smo.businessytpe;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.businesstypecd.C_business_typeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IC_business_typeInnerServiceSMO
 * @Description BusinessType接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/c_business_typeApi")
public interface IC_business_typeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param c_business_typeDto 数据对象分享
     * @return C_business_typeDto 对象数据
     */
    @RequestMapping(value = "/queryC_business_types", method = RequestMethod.POST)
    List<C_business_typeDto> queryC_business_types(@RequestBody C_business_typeDto c_business_typeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param c_business_typeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryC_business_typesCount", method = RequestMethod.POST)
    int queryC_business_typesCount(@RequestBody C_business_typeDto c_business_typeDto);
}
