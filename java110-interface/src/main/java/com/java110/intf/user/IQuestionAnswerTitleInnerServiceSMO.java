package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.questionAnswer.QuestionAnswerTitleDto;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IQuestionAnswerTitleInnerServiceSMO
 * @Description 答卷接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/questionAnswerTitleApi")
public interface IQuestionAnswerTitleInnerServiceSMO {


    @RequestMapping(value = "/saveQuestionAnswerTitle", method = RequestMethod.POST)
    public int saveQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo);

    @RequestMapping(value = "/updateQuestionAnswerTitle", method = RequestMethod.POST)
    public int updateQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo);

    @RequestMapping(value = "/deleteQuestionAnswerTitle", method = RequestMethod.POST)
    public int deleteQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param questionAnswerTitleDto 数据对象分享
     * @return QuestionAnswerTitleDto 对象数据
     */
    @RequestMapping(value = "/queryQuestionAnswerTitles", method = RequestMethod.POST)
    List<QuestionAnswerTitleDto> queryQuestionAnswerTitles(@RequestBody QuestionAnswerTitleDto questionAnswerTitleDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param questionAnswerTitleDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryQuestionAnswerTitlesCount", method = RequestMethod.POST)
    int queryQuestionAnswerTitlesCount(@RequestBody QuestionAnswerTitleDto questionAnswerTitleDto);
}
