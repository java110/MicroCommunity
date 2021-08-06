package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerDto;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.report.bmo.reportInfoAnswer.IDeleteReportInfoAnswerBMO;
import com.java110.report.bmo.reportInfoAnswer.IGetReportInfoAnswerBMO;
import com.java110.report.bmo.reportInfoAnswer.ISaveReportInfoAnswerBMO;
import com.java110.report.bmo.reportInfoAnswer.IUpdateReportInfoAnswerBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportInfoAnswer")
public class ReportInfoAnswerApi {

    @Autowired
    private ISaveReportInfoAnswerBMO saveReportInfoAnswerBMOImpl;
    @Autowired
    private IUpdateReportInfoAnswerBMO updateReportInfoAnswerBMOImpl;
    @Autowired
    private IDeleteReportInfoAnswerBMO deleteReportInfoAnswerBMOImpl;

    @Autowired
    private IGetReportInfoAnswerBMO getReportInfoAnswerBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /reportInfoAnswer/saveReportInfoAnswer
     * @path /app/reportInfoAnswer/saveReportInfoAnswer
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveReportInfoAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userAnId", "请求报文中未包含userAnId");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        ReportInfoAnswerPo reportInfoAnswerPo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerPo.class);
        return saveReportInfoAnswerBMOImpl.save(reportInfoAnswerPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /reportInfoAnswer/updateReportInfoAnswer
     * @path /app/reportInfoAnswer/updateReportInfoAnswer
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateReportInfoAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportInfoAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userAnId", "请求报文中未包含userAnId");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "userAnId", "userAnId不能为空");


        ReportInfoAnswerPo reportInfoAnswerPo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerPo.class);
        return updateReportInfoAnswerBMOImpl.update(reportInfoAnswerPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportInfoAnswer/deleteReportInfoAnswer
     * @path /app/reportInfoAnswer/deleteReportInfoAnswer
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteReportInfoAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportInfoAnswer(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "userAnId", "userAnId不能为空");


        ReportInfoAnswerPo reportInfoAnswerPo = BeanConvertUtil.covertBean(reqJson, ReportInfoAnswerPo.class);
        return deleteReportInfoAnswerBMOImpl.delete(reportInfoAnswerPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportInfoAnswer/queryReportInfoAnswer
     * @path /app/reportInfoAnswer/queryReportInfoAnswer
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryReportInfoAnswer", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoAnswer(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ReportInfoAnswerDto reportInfoAnswerDto = new ReportInfoAnswerDto();
        reportInfoAnswerDto.setPage(page);
        reportInfoAnswerDto.setRow(row);
        reportInfoAnswerDto.setCommunityId(communityId);
        return getReportInfoAnswerBMOImpl.get(reportInfoAnswerDto);
    }
}
