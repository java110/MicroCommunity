package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.report.bmo.reportOweFee.IDeleteReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.IGetReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.ISaveReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.IUpdateReportOweFeeBMO;
import com.java110.report.bmo.reportQuestionAnswer.IGetReportQuestionAnswerBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 问卷投票 接口类
 */
@RestController
@RequestMapping(value = "/reportQuestionAnswer")
public class ReportQuestionAnswerApi {


    @Autowired
    private IGetReportQuestionAnswerBMO getReportQuestionAnswerBMOImpl;



    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportQuestionAnswer/queryUserQuestionAnswerValue
     * @path /app/reportQuestionAnswer/queryUserQuestionAnswerValue
     */
    @RequestMapping(value = "/queryUserQuestionAnswerValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserQuestionAnswerValue(@RequestParam(value = "communityId") String communityId,
                                                               @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "row") int row) {
        UserQuestionAnswerValueDto userQuestionAnswerValueDto = new UserQuestionAnswerValueDto();
        userQuestionAnswerValueDto.setPage(page);
        userQuestionAnswerValueDto.setRow(row);
        userQuestionAnswerValueDto.setObjId(communityId);
        return getReportQuestionAnswerBMOImpl.get(userQuestionAnswerValueDto);
    }
}
