package com.java110.intf.oa;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.oaWorkflow.OaWorkflowDataDto;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOaWorkflowDataInnerServiceSMO
 * @Description OA表单审批数据接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "oa-service", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowDataApi")
public interface IOaWorkflowDataInnerServiceSMO {


    @RequestMapping(value = "/saveOaWorkflowData", method = RequestMethod.POST)
    public int saveOaWorkflowData(@RequestBody OaWorkflowDataPo oaWorkflowDataPo);

    @RequestMapping(value = "/updateOaWorkflowData", method = RequestMethod.POST)
    public int updateOaWorkflowData(@RequestBody  OaWorkflowDataPo oaWorkflowDataPo);

    @RequestMapping(value = "/deleteOaWorkflowData", method = RequestMethod.POST)
    public int deleteOaWorkflowData(@RequestBody  OaWorkflowDataPo oaWorkflowDataPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param oaWorkflowDataDto 数据对象分享
     * @return OaWorkflowDataDto 对象数据
     */
    @RequestMapping(value = "/queryOaWorkflowDatas", method = RequestMethod.POST)
    List<OaWorkflowDataDto> queryOaWorkflowDatas(@RequestBody OaWorkflowDataDto oaWorkflowDataDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param oaWorkflowDataDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOaWorkflowDatasCount", method = RequestMethod.POST)
    int queryOaWorkflowDatasCount(@RequestBody OaWorkflowDataDto oaWorkflowDataDto);
}
