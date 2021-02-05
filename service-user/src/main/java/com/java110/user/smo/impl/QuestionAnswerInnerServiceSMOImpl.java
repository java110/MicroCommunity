package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.user.dao.IQuestionAnswerServiceDao;
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
public class QuestionAnswerInnerServiceSMOImpl extends BaseServiceSMO implements IQuestionAnswerInnerServiceSMO {

    @Autowired
    private IQuestionAnswerServiceDao questionAnswerServiceDaoImpl;


    @Override
    public int saveQuestionAnswer(@RequestBody QuestionAnswerPo questionAnswerPo) {
        int saveFlag = 1;
        questionAnswerServiceDaoImpl.saveQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(questionAnswerPo));
        return saveFlag;
    }

    @Override
    public int updateQuestionAnswer(@RequestBody QuestionAnswerPo questionAnswerPo) {
        int saveFlag = 1;
        questionAnswerServiceDaoImpl.updateQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(questionAnswerPo));
        return saveFlag;
    }

    @Override
    public int deleteQuestionAnswer(@RequestBody QuestionAnswerPo questionAnswerPo) {
        int saveFlag = 1;
        questionAnswerPo.setStatusCd("1");
        questionAnswerServiceDaoImpl.updateQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(questionAnswerPo));
        return saveFlag;
    }

    @Override
    public List<QuestionAnswerDto> queryQuestionAnswers(@RequestBody QuestionAnswerDto questionAnswerDto) {

        //校验是否传了 分页信息

        int page = questionAnswerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            questionAnswerDto.setPage((page - 1) * questionAnswerDto.getRow());
        }

        List<QuestionAnswerDto> questionAnswers = BeanConvertUtil.covertBeanList(questionAnswerServiceDaoImpl.getQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(questionAnswerDto)), QuestionAnswerDto.class);

        return questionAnswers;
    }


    @Override
    public int queryQuestionAnswersCount(@RequestBody QuestionAnswerDto questionAnswerDto) {
        return questionAnswerServiceDaoImpl.queryQuestionAnswersCount(BeanConvertUtil.beanCovertMap(questionAnswerDto));
    }

    public IQuestionAnswerServiceDao getQuestionAnswerServiceDaoImpl() {
        return questionAnswerServiceDaoImpl;
    }

    public void setQuestionAnswerServiceDaoImpl(IQuestionAnswerServiceDao questionAnswerServiceDaoImpl) {
        this.questionAnswerServiceDaoImpl = questionAnswerServiceDaoImpl;
    }
}
