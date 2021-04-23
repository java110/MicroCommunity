package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.unitAttr.UnitAttrDto;
import com.java110.po.unitAttr.UnitAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUnitAttrInnerServiceSMO
 * @Description 单元属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/unitAttrApi")
public interface IUnitAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param unitAttrDto 数据对象分享
     * @return UnitAttrDto 对象数据
     */
    @RequestMapping(value = "/queryUnitAttrs", method = RequestMethod.POST)
    List<UnitAttrDto> queryUnitAttrs(@RequestBody UnitAttrDto unitAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param unitAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUnitAttrsCount", method = RequestMethod.POST)
    int queryUnitAttrsCount(@RequestBody UnitAttrDto unitAttrDto);


    /**
     * 保存单元属性
     *
     * @param unitAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveUnitAttr", method = RequestMethod.POST)
    int saveUnitAttr(@RequestBody UnitAttrPo unitAttrPo);
}
