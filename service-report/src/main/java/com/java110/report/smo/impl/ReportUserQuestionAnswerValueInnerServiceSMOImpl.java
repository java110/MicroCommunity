package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.intf.report.IReportUserQuestionAnswerValueInnerServiceSMO;
import com.java110.report.dao.IReportUserQuestionAnswerValueServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 答卷答案内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportUserQuestionAnswerValueInnerServiceSMOImpl extends BaseServiceSMO implements IReportUserQuestionAnswerValueInnerServiceSMO {

    @Autowired
    private IReportUserQuestionAnswerValueServiceDao reportUserQuestionAnswerValueServiceDaoImpl;


    @Override
    public List<UserQuestionAnswerValueDto> queryUserQuestionAnswerValues(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto) {

        //校验是否传了 分页信息

        int page = userQuestionAnswerValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userQuestionAnswerValueDto.setPage((page - 1) * userQuestionAnswerValueDto.getRow());
        }

        List<UserQuestionAnswerValueDto> userQuestionAnswerValues = BeanConvertUtil.covertBeanList(reportUserQuestionAnswerValueServiceDaoImpl.getUserQuestionAnswerValueInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerValueDto)), UserQuestionAnswerValueDto.class);

        return userQuestionAnswerValues;
    }


    @Override
    public int queryUserQuestionAnswerValuesCount(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto) {
        return reportUserQuestionAnswerValueServiceDaoImpl.queryUserQuestionAnswerValuesCount(BeanConvertUtil.beanCovertMap(userQuestionAnswerValueDto));
    }

}
