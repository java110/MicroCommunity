package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.hcGovTranslate.HcGovTranslateDetailDto;
import com.java110.po.hcGovTranslateDetail.HcGovTranslateDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IHcGovTranslateDetailInnerServiceSMO
 * @Description 信息分类接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/hcGovTranslateDetailApi")
public interface IHcGovTranslateDetailInnerServiceSMO {


    @RequestMapping(value = "/saveHcGovTranslateDetail", method = RequestMethod.POST)
    public int saveHcGovTranslateDetail(@RequestBody HcGovTranslateDetailPo hcGovTranslateDetailPo);

    @RequestMapping(value = "/updateHcGovTranslateDetail", method = RequestMethod.POST)
    public int updateHcGovTranslateDetail(@RequestBody  HcGovTranslateDetailPo hcGovTranslateDetailPo);

    @RequestMapping(value = "/deleteHcGovTranslateDetail", method = RequestMethod.POST)
    public int deleteHcGovTranslateDetail(@RequestBody  HcGovTranslateDetailPo hcGovTranslateDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param hcGovTranslateDetailDto 数据对象分享
     * @return HcGovTranslateDetailDto 对象数据
     */
    @RequestMapping(value = "/queryHcGovTranslateDetails", method = RequestMethod.POST)
    List<HcGovTranslateDetailDto> queryHcGovTranslateDetails(@RequestBody HcGovTranslateDetailDto hcGovTranslateDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param hcGovTranslateDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryHcGovTranslateDetailsCount", method = RequestMethod.POST)
    int queryHcGovTranslateDetailsCount(@RequestBody HcGovTranslateDetailDto hcGovTranslateDetailDto);
}
