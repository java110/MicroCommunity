package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.po.appraise.AppraisePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAppraiseInnerServiceSMO
 * @Description 评价接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/appraiseApi")
public interface IAppraiseInnerServiceSMO {


    @RequestMapping(value = "/saveAppraise", method = RequestMethod.POST)
    public int saveAppraise(@RequestBody AppraisePo appraisePo);

    @RequestMapping(value = "/updateAppraise", method = RequestMethod.POST)
    public int updateAppraise(@RequestBody AppraisePo appraisePo);

    @RequestMapping(value = "/deleteAppraise", method = RequestMethod.POST)
    public int deleteAppraise(@RequestBody AppraisePo appraisePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param appraiseDto 数据对象分享
     * @return AppraiseDto 对象数据
     */
    @RequestMapping(value = "/queryAppraises", method = RequestMethod.POST)
    List<AppraiseDto> queryAppraises(@RequestBody AppraiseDto appraiseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param appraiseDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAppraisesCount", method = RequestMethod.POST)
    int queryAppraisesCount(@RequestBody AppraiseDto appraiseDto);
}
