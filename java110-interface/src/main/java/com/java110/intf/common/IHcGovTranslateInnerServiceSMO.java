package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IHcGovTranslateInnerServiceSMO
 * @Description 社区政务同步接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/hcGovTranslateApi")
public interface IHcGovTranslateInnerServiceSMO {


    @RequestMapping(value = "/saveHcGovTranslate", method = RequestMethod.POST)
    public int saveHcGovTranslate(@RequestBody HcGovTranslatePo hcGovTranslatePo);

    @RequestMapping(value = "/updateHcGovTranslate", method = RequestMethod.POST)
    public int updateHcGovTranslate(@RequestBody  HcGovTranslatePo hcGovTranslatePo);

    @RequestMapping(value = "/deleteHcGovTranslate", method = RequestMethod.POST)
    public int deleteHcGovTranslate(@RequestBody  HcGovTranslatePo hcGovTranslatePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param hcGovTranslateDto 数据对象分享
     * @return HcGovTranslateDto 对象数据
     */
    @RequestMapping(value = "/queryHcGovTranslates", method = RequestMethod.POST)
    List<HcGovTranslateDto> queryHcGovTranslates(@RequestBody HcGovTranslateDto hcGovTranslateDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param hcGovTranslateDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryHcGovTranslatesCount", method = RequestMethod.POST)
    int queryHcGovTranslatesCount(@RequestBody HcGovTranslateDto hcGovTranslateDto);
}
