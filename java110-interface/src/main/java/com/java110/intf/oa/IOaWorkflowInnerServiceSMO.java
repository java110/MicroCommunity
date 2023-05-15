package com.java110.intf.oa;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOaWorkflowInnerServiceSMO
 * @Description OA工作流接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "oa-service", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowApi")
public interface IOaWorkflowInnerServiceSMO {


    @RequestMapping(value = "/saveOaWorkflow", method = RequestMethod.POST)
    public int saveOaWorkflow(@RequestBody OaWorkflowPo oaWorkflowPo);

    @RequestMapping(value = "/updateOaWorkflow", method = RequestMethod.POST)
    public int updateOaWorkflow(@RequestBody  OaWorkflowPo oaWorkflowPo);

    @RequestMapping(value = "/deleteOaWorkflow", method = RequestMethod.POST)
    public int deleteOaWorkflow(@RequestBody  OaWorkflowPo oaWorkflowPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param oaWorkflowDto 数据对象分享
     * @return OaWorkflowDto 对象数据
     */
    @RequestMapping(value = "/queryOaWorkflows", method = RequestMethod.POST)
    List<OaWorkflowDto> queryOaWorkflows(@RequestBody OaWorkflowDto oaWorkflowDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param oaWorkflowDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOaWorkflowsCount", method = RequestMethod.POST)
    int queryOaWorkflowsCount(@RequestBody OaWorkflowDto oaWorkflowDto);
}
