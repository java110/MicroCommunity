package com.java110.intf.oa;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOaWorkflowFormInnerServiceSMO
 * @Description OA表单接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "oa-service", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowFormApi")
public interface IOaWorkflowFormInnerServiceSMO {


    @RequestMapping(value = "/saveOaWorkflowForm", method = RequestMethod.POST)
    public int saveOaWorkflowForm(@RequestBody OaWorkflowFormPo oaWorkflowFormPo);

    @RequestMapping(value = "/updateOaWorkflowForm", method = RequestMethod.POST)
    public int updateOaWorkflowForm(@RequestBody  OaWorkflowFormPo oaWorkflowFormPo);

    @RequestMapping(value = "/deleteOaWorkflowForm", method = RequestMethod.POST)
    public int deleteOaWorkflowForm(@RequestBody  OaWorkflowFormPo oaWorkflowFormPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param oaWorkflowFormDto 数据对象分享
     * @return OaWorkflowFormDto 对象数据
     */
    @RequestMapping(value = "/queryOaWorkflowForms", method = RequestMethod.POST)
    List<OaWorkflowFormDto> queryOaWorkflowForms(@RequestBody OaWorkflowFormDto oaWorkflowFormDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param oaWorkflowFormDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOaWorkflowFormsCount", method = RequestMethod.POST)
    int queryOaWorkflowFormsCount(@RequestBody OaWorkflowFormDto oaWorkflowFormDto);

    /**
     *  是否有表
     *
     * @param table 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/hasTable", method = RequestMethod.POST)
    int hasTable(@RequestBody String table);

    @RequestMapping(value = "/createTable", method = RequestMethod.POST)
    int createTable(@RequestBody String sql);
}
