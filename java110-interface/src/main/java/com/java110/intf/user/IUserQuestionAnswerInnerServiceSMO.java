package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.userQuestionAnswer.UserQuestionAnswerDto;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserQuestionAnswerInnerServiceSMO
 * @Description 答卷接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userQuestionAnswerApi")
public interface IUserQuestionAnswerInnerServiceSMO {


    @RequestMapping(value = "/saveUserQuestionAnswer", method = RequestMethod.POST)
    public int saveUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo);

    @RequestMapping(value = "/updateUserQuestionAnswer", method = RequestMethod.POST)
    public int updateUserQuestionAnswer(@RequestBody  UserQuestionAnswerPo userQuestionAnswerPo);

    @RequestMapping(value = "/deleteUserQuestionAnswer", method = RequestMethod.POST)
    public int deleteUserQuestionAnswer(@RequestBody  UserQuestionAnswerPo userQuestionAnswerPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param userQuestionAnswerDto 数据对象分享
     * @return UserQuestionAnswerDto 对象数据
     */
    @RequestMapping(value = "/queryUserQuestionAnswers", method = RequestMethod.POST)
    List<UserQuestionAnswerDto> queryUserQuestionAnswers(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userQuestionAnswerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserQuestionAnswersCount", method = RequestMethod.POST)
    int queryUserQuestionAnswersCount(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto);
}
