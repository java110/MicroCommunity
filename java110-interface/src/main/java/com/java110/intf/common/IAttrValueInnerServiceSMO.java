package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attrSpec.AttrValueDto;
import com.java110.po.attrValue.AttrValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttrValueInnerServiceSMO
 * @Description 属性值接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attrValueApi")
public interface IAttrValueInnerServiceSMO {


    @RequestMapping(value = "/saveAttrValue", method = RequestMethod.POST)
    public int saveAttrValue(@RequestBody AttrValuePo attrValuePo);

    @RequestMapping(value = "/updateAttrValue", method = RequestMethod.POST)
    public int updateAttrValue(@RequestBody AttrValuePo attrValuePo);

    @RequestMapping(value = "/deleteAttrValue", method = RequestMethod.POST)
    public int deleteAttrValue(@RequestBody AttrValuePo attrValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param attrValueDto 数据对象分享
     * @return AttrValueDto 对象数据
     */
    @RequestMapping(value = "/queryAttrValues", method = RequestMethod.POST)
    List<AttrValueDto> queryAttrValues(@RequestBody AttrValueDto attrValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attrValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttrValuesCount", method = RequestMethod.POST)
    int queryAttrValuesCount(@RequestBody AttrValueDto attrValueDto);
}
