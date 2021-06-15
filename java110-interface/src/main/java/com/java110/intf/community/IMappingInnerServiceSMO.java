package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.mapping.MappingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMappingInnerMappingSMO
 * @Description 映射接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/mappingApi")
public interface IMappingInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param mappingDto 数据对象分享
     * @return MappingDto 对象数据
     */
    @RequestMapping(value = "/queryMappings", method = RequestMethod.POST)
    List<MappingDto> queryMappings(@RequestBody MappingDto mappingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param mappingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMappingsCount", method = RequestMethod.POST)
    int queryMappingsCount(@RequestBody MappingDto mappingDto);


    /**
     * <p>修改APP信息</p>
     *
     *
     * @param serviceDto 数据对象分享
     * @return MappingDto 对象数据
     */
    @RequestMapping(value = "/updateMapping", method = RequestMethod.POST)
    int updateMapping(@RequestBody MappingDto serviceDto);


    /**
     * <p>添加APP信息</p>
     *
     *
     * @param serviceDto 数据对象分享
     * @return MappingDto 对象数据
     */
    @RequestMapping(value = "/saveMapping", method = RequestMethod.POST)
    int saveMapping(@RequestBody MappingDto serviceDto);

    /**
     * <p>删除APP信息</p>
     *
     *
     * @param serviceDto 数据对象分享
     * @return MappingDto 对象数据
     */
    @RequestMapping(value = "/deleteMapping", method = RequestMethod.POST)
    int deleteMapping(@RequestBody MappingDto serviceDto);
}
