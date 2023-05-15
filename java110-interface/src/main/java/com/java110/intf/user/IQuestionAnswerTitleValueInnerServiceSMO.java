package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.questionAnswer.QuestionAnswerTitleValueDto;
import com.java110.po.questionAnswerTitleValue.QuestionAnswerTitleValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IQuestionAnswerTitleValueInnerServiceSMO
 * @Description 答卷选项接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/questionAnswerTitleValueApi")
public interface IQuestionAnswerTitleValueInnerServiceSMO {


    @RequestMapping(value = "/saveQuestionAnswerTitleValue", method = RequestMethod.POST)
    public int saveQuestionAnswerTitleValue(@RequestBody QuestionAnswerTitleValuePo questionAnswerTitleValuePo);

    @RequestMapping(value = "/updateQuestionAnswerTitleValue", method = RequestMethod.POST)
    public int updateQuestionAnswerTitleValue(@RequestBody  QuestionAnswerTitleValuePo questionAnswerTitleValuePo);

    @RequestMapping(value = "/deleteQuestionAnswerTitleValue", method = RequestMethod.POST)
    public int deleteQuestionAnswerTitleValue(@RequestBody  QuestionAnswerTitleValuePo questionAnswerTitleValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param questionAnswerTitleValueDto 数据对象分享
     * @return QuestionAnswerTitleValueDto 对象数据
     */
    @RequestMapping(value = "/queryQuestionAnswerTitleValues", method = RequestMethod.POST)
    List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValues(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param questionAnswerTitleValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryQuestionAnswerTitleValuesCount", method = RequestMethod.POST)
    int queryQuestionAnswerTitleValuesCount(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto);

    @RequestMapping(value = "/queryQuestionAnswerTitleValueResult", method = RequestMethod.POST)
    List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValueResult(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto);

    @RequestMapping(value = "/queryQuestionAnswerTitleValueResultCount", method = RequestMethod.POST)
    List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValueResultCount(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto);
}
