package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.questionAnswer.QuestionAnswerTitleDto;
import com.java110.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import com.java110.user.dao.IQuestionAnswerTitleServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 答卷内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class QuestionAnswerTitleInnerServiceSMOImpl extends BaseServiceSMO implements IQuestionAnswerTitleInnerServiceSMO {

    @Autowired
    private IQuestionAnswerTitleServiceDao questionAnswerTitleServiceDaoImpl;


    @Override
    public int saveQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo) {
        int saveFlag = 1;
        questionAnswerTitleServiceDaoImpl.saveQuestionAnswerTitleInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitlePo));
        return saveFlag;
    }

    @Override
    public int updateQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo) {
        int saveFlag = 1;
        questionAnswerTitleServiceDaoImpl.updateQuestionAnswerTitleInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitlePo));
        return saveFlag;
    }

    @Override
    public int deleteQuestionAnswerTitle(@RequestBody QuestionAnswerTitlePo questionAnswerTitlePo) {
        int saveFlag = 1;
        questionAnswerTitlePo.setStatusCd("1");
        questionAnswerTitleServiceDaoImpl.updateQuestionAnswerTitleInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitlePo));
        return saveFlag;
    }

    @Override
    public List<QuestionAnswerTitleDto> queryQuestionAnswerTitles(@RequestBody QuestionAnswerTitleDto questionAnswerTitleDto) {

        //校验是否传了 分页信息

        int page = questionAnswerTitleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            questionAnswerTitleDto.setPage((page - 1) * questionAnswerTitleDto.getRow());
        }

        List<QuestionAnswerTitleDto> questionAnswerTitles = BeanConvertUtil.covertBeanList(questionAnswerTitleServiceDaoImpl.getQuestionAnswerTitleInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleDto)), QuestionAnswerTitleDto.class);

        return questionAnswerTitles;
    }


    @Override
    public int queryQuestionAnswerTitlesCount(@RequestBody QuestionAnswerTitleDto questionAnswerTitleDto) {
        return questionAnswerTitleServiceDaoImpl.queryQuestionAnswerTitlesCount(BeanConvertUtil.beanCovertMap(questionAnswerTitleDto));
    }

    public IQuestionAnswerTitleServiceDao getQuestionAnswerTitleServiceDaoImpl() {
        return questionAnswerTitleServiceDaoImpl;
    }

    public void setQuestionAnswerTitleServiceDaoImpl(IQuestionAnswerTitleServiceDao questionAnswerTitleServiceDaoImpl) {
        this.questionAnswerTitleServiceDaoImpl = questionAnswerTitleServiceDaoImpl;
    }
}
