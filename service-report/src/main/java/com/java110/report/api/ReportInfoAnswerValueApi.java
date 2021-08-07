package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoAnswerValue.ReportInfoAnswerValueDto;
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
     * @serviceCode /reportInfoAnswerValue/saveReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/saveReportInfoAnswerValue
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveReportInfoAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoAnswerValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "anValueId", "请求报文中未包含anValueId");
        Assert.hasKeyAndValue(reqJson, "userAnId", "请求报文中未包含userAnId");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "valueContent", "请求报文中未包含valueContent");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        ReportInfoAnswerValuePo reportInfoAnswerValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerValuePo.class);
        return saveReportInfoAnswerValueBMOImpl.save(reportInfoAnswerValuePo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /reportInfoAnswerValue/updateReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/updateReportInfoAnswerValue
     * @param reqJson
     * @return
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
     * @serviceCode /reportInfoAnswerValue/deleteReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/deleteReportInfoAnswerValue
     * @param reqJson
     * @return
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
     * @serviceCode /reportInfoAnswerValue/queryReportInfoAnswerValue
     * @path /app/reportInfoAnswerValue/queryReportInfoAnswerValue
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryReportInfoAnswerValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoAnswerValue(@RequestParam(value = "communityId") String communityId,
                                                             @RequestParam(value = "titleId") String titleId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ReportInfoAnswerValueDto reportInfoAnswerValueDto = new ReportInfoAnswerValueDto();
        reportInfoAnswerValueDto.setPage(page);
        reportInfoAnswerValueDto.setRow(row);
        reportInfoAnswerValueDto.setCommunityId(communityId);
        reportInfoAnswerValueDto.setTitleId(titleId);
        return getReportInfoAnswerValueBMOImpl.get(reportInfoAnswerValueDto);
    }
}
