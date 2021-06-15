package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserQuestionAnswerValueInnerServiceSMO
 * @Description 答卷答案接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userQuestionAnswerValueApi")
public interface IUserQuestionAnswerValueInnerServiceSMO {


    @RequestMapping(value = "/saveUserQuestionAnswerValue", method = RequestMethod.POST)
    public int saveUserQuestionAnswerValue(@RequestBody List<UserQuestionAnswerValuePo> userQuestionAnswerValuePos);

    @RequestMapping(value = "/updateUserQuestionAnswerValue", method = RequestMethod.POST)
    public int updateUserQuestionAnswerValue(@RequestBody  UserQuestionAnswerValuePo userQuestionAnswerValuePo);

    @RequestMapping(value = "/deleteUserQuestionAnswerValue", method = RequestMethod.POST)
    public int deleteUserQuestionAnswerValue(@RequestBody  UserQuestionAnswerValuePo userQuestionAnswerValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param userQuestionAnswerValueDto 数据对象分享
     * @return UserQuestionAnswerValueDto 对象数据
     */
    @RequestMapping(value = "/queryUserQuestionAnswerValues", method = RequestMethod.POST)
    List<UserQuestionAnswerValueDto> queryUserQuestionAnswerValues(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userQuestionAnswerValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserQuestionAnswerValuesCount", method = RequestMethod.POST)
    int queryUserQuestionAnswerValuesCount(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto);
}
