package com.java110.report.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerValueDto;
import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import com.java110.report.bmo.reportInfoAnswerValue.IDeleteReportInfoAnswerValueBMO;
import com.java110.report.bmo.reportInfoAnswerValue.IGetReportInfoAnswerValueBMO;
import com.java110.report.bmo.reportInfoAnswerValue.ISaveReportInfoAnswerValueBMO;
import com.java110.report.bmo.reportInfoAnswerValue.IUpdateReportInfoAnswerValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportInfoAnswerValue")
public class ReportInfoAnswerValueApi {

    @Autowired
    private ISaveReportInfoAnswerValueBMO saveReportInfoAnswerValueBMOImpl;

    @Autowired
    private IUpdateReportInfoAnswerValueBMO updateReportInfoAnswerValueBMOImpl;

    @Autowired
    private IDeleteReportInfoAnswerValueBMO deleteReportInfoAnswerValueBMOImpl;

    @Autowired
    private IGetReportInfoAnswerValueBMO getReportInfoAnswerValueBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoAnswerValue/saveReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/saveReportInfoAnswerValue
     */
    @RequestMapping(value = "/saveReportInfoAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoAnswerValue(@RequestHeader(value = "user-id") String userId, @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "questionAnswerTitles", "请求报文中未包含回答项");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        JSONArray questionAnswerTitles = reqJson.getJSONArray("questionAnswerTitles");

        if (questionAnswerTitles == null || questionAnswerTitles.size() < 1) {
            throw new IllegalArgumentException("未包含题目及答案");
        }
        JSONObject titleObj = null;
        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            Assert.hasKeyAndValue(titleObj, "valueContent", titleObj.getString("title") + ",未填写答案");
        }

        return saveReportInfoAnswerValueBMOImpl.save(reqJson, userId);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoAnswerValue/updateReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/updateReportInfoAnswerValue
     */
    @RequestMapping(value = "/updateReportInfoAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportInfoAnswerValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "anValueId", "请求报文中未包含anValueId");
        Assert.hasKeyAndValue(reqJson, "userAnId", "请求报文中未包含userAnId");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "valueContent", "请求报文中未包含valueContent");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "anValueId", "anValueId不能为空");

        ReportInfoAnswerValuePo reportInfoAnswerValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerValuePo.class);
        return updateReportInfoAnswerValueBMOImpl.update(reportInfoAnswerValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoAnswerValue/deleteReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/deleteReportInfoAnswerValue
     */
    @RequestMapping(value = "/deleteReportInfoAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportInfoAnswerValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "anValueId", "anValueId不能为空");

        ReportInfoAnswerValuePo reportInfoAnswerValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerValuePo.class);
        return deleteReportInfoAnswerValueBMOImpl.delete(reportInfoAnswerValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportInfoAnswerValue/queryReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/queryReportInfoAnswerValue
     */
    @RequestMapping(value = "/queryReportInfoAnswerValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoAnswerValue(@RequestParam(value = "communityId") String communityId,
                                                             @RequestParam(value = "userName", required = false) String userName,
                                                             @RequestParam(value = "repName", required = false) String repName,
                                                             @RequestParam(value = "repTitle", required = false) String repTitle,
                                                             @RequestParam(value = "valueContent", required = false) String valueContent,
                                                             @RequestParam(value = "reportType",required = false) String reportType,
                                                             @RequestParam(value = "page") int page,
                                                             @RequestParam(value = "row") int row) {
        ReportInfoAnswerValueDto reportInfoAnswerValueDto = new ReportInfoAnswerValueDto();
        reportInfoAnswerValueDto.setPage(page);
        reportInfoAnswerValueDto.setRow(row);
        reportInfoAnswerValueDto.setCommunityId(communityId);
        reportInfoAnswerValueDto.setUserName(userName);
        reportInfoAnswerValueDto.setRepName(repName);
        reportInfoAnswerValueDto.setRepTitle(repTitle);
        reportInfoAnswerValueDto.setValueContent(valueContent);
        reportInfoAnswerValueDto.setReportType(reportType);
        return getReportInfoAnswerValueBMOImpl.get(reportInfoAnswerValueDto);
    }
}
