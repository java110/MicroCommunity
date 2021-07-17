package com.java110.report.api;

import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.report.bmo.reportQuestionAnswer.IGetReportQuestionAnswerBMO;
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
    public ResponseEntity<String> queryUserQuestionAnswerValue(
            @RequestHeader(value = "store-id",required = false) String storeId,
            @RequestParam(value = "communityId") String communityId,
            @RequestParam(value = "qaType",required = false) String qaType,
            @RequestParam(value = "startTime",required = false) String startTime,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "titleId",required = false) String titleId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        UserQuestionAnswerValueDto userQuestionAnswerValueDto = new UserQuestionAnswerValueDto();
        userQuestionAnswerValueDto.setPage(page);
        userQuestionAnswerValueDto.setRow(row);
        userQuestionAnswerValueDto.setObjIds(new String[]{storeId, communityId});
        userQuestionAnswerValueDto.setQaType(qaType);
        userQuestionAnswerValueDto.setStartTime(startTime);
        if(!StringUtil.isEmpty(endTime)){
            userQuestionAnswerValueDto.setEndTime(endTime+" 23:59:59");
        }
        userQuestionAnswerValueDto.setTitleId(titleId);
        return getReportQuestionAnswerBMOImpl.get(userQuestionAnswerValueDto);
    }
}
