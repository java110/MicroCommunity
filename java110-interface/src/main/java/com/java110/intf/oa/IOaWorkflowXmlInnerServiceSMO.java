package com.java110.intf.oa;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOaWorkflowXmlInnerServiceSMO
 * @Description OA流程图接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "oa-service", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowXmlApi")
public interface IOaWorkflowXmlInnerServiceSMO {


    @RequestMapping(value = "/saveOaWorkflowXml", method = RequestMethod.POST)
    public int saveOaWorkflowXml(@RequestBody OaWorkflowXmlPo oaWorkflowXmlPo);

    @RequestMapping(value = "/updateOaWorkflowXml", method = RequestMethod.POST)
    public int updateOaWorkflowXml(@RequestBody  OaWorkflowXmlPo oaWorkflowXmlPo);

    @RequestMapping(value = "/deleteOaWorkflowXml", method = RequestMethod.POST)
    public int deleteOaWorkflowXml(@RequestBody  OaWorkflowXmlPo oaWorkflowXmlPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param oaWorkflowXmlDto 数据对象分享
     * @return OaWorkflowXmlDto 对象数据
     */
    @RequestMapping(value = "/queryOaWorkflowXmls", method = RequestMethod.POST)
    List<OaWorkflowXmlDto> queryOaWorkflowXmls(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param oaWorkflowXmlDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOaWorkflowXmlsCount", method = RequestMethod.POST)
    int queryOaWorkflowXmlsCount(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto);
}
