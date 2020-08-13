package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attrSpec.AttrSpecDto;
import com.java110.po.attrSpec.AttrSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttrSpecInnerServiceSMO
 * @Description 属性规格表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attrSpecApi")
public interface IAttrSpecInnerServiceSMO {


    @RequestMapping(value = "/saveAttrSpec", method = RequestMethod.POST)
    public int saveAttrSpec(@RequestBody AttrSpecPo attrSpecPo);

    @RequestMapping(value = "/updateAttrSpec", method = RequestMethod.POST)
    public int updateAttrSpec(@RequestBody AttrSpecPo attrSpecPo);

    @RequestMapping(value = "/deleteAttrSpec", method = RequestMethod.POST)
    public int deleteAttrSpec(@RequestBody AttrSpecPo attrSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param attrSpecDto 数据对象分享
     * @return AttrSpecDto 对象数据
     */
    @RequestMapping(value = "/queryAttrSpecs", method = RequestMethod.POST)
    List<AttrSpecDto> queryAttrSpecs(@RequestBody AttrSpecDto attrSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attrSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttrSpecsCount", method = RequestMethod.POST)
    int queryAttrSpecsCount(@RequestBody AttrSpecDto attrSpecDto);
}
