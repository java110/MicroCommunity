package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IQuestionAnswerInnerServiceSMO
 * @Description 答卷接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/questionAnswerApi")
public interface IQuestionAnswerInnerServiceSMO {


    @RequestMapping(value = "/saveQuestionAnswer", method = RequestMethod.POST)
    public int saveQuestionAnswer(@RequestBody QuestionAnswerPo questionAnswerPo);

    @RequestMapping(value = "/updateQuestionAnswer", method = RequestMethod.POST)
    public int updateQuestionAnswer(@RequestBody  QuestionAnswerPo questionAnswerPo);

    @RequestMapping(value = "/deleteQuestionAnswer", method = RequestMethod.POST)
    public int deleteQuestionAnswer(@RequestBody  QuestionAnswerPo questionAnswerPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param questionAnswerDto 数据对象分享
     * @return QuestionAnswerDto 对象数据
     */
    @RequestMapping(value = "/queryQuestionAnswers", method = RequestMethod.POST)
    List<QuestionAnswerDto> queryQuestionAnswers(@RequestBody QuestionAnswerDto questionAnswerDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param questionAnswerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryQuestionAnswersCount", method = RequestMethod.POST)
    int queryQuestionAnswersCount(@RequestBody QuestionAnswerDto questionAnswerDto);
}
