package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleValueDto;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import com.java110.report.bmo.reportInfoSettingTitleValue.IDeleteReportInfoSettingTitleValueBMO;
import com.java110.report.bmo.reportInfoSettingTitleValue.IGetReportInfoSettingTitleValueBMO;
import com.java110.report.bmo.reportInfoSettingTitleValue.ISaveReportInfoSettingTitleValueBMO;
import com.java110.report.bmo.reportInfoSettingTitleValue.IUpdateReportInfoSettingTitleValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportInfoSettingTitleValue")
public class ReportInfoSettingTitleValueApi {

    @Autowired
    private ISaveReportInfoSettingTitleValueBMO saveReportInfoSettingTitleValueBMOImpl;
    @Autowired
    private IUpdateReportInfoSettingTitleValueBMO updateReportInfoSettingTitleValueBMOImpl;
    @Autowired
    private IDeleteReportInfoSettingTitleValueBMO deleteReportInfoSettingTitleValueBMOImpl;

    @Autowired
    private IGetReportInfoSettingTitleValueBMO getReportInfoSettingTitleValueBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /reportInfoSettingTitleValue/saveSettingTitleValue
     * @path /app/reportInfoSettingTitleValue/saveReportInfoSettingTitleValue
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveSettingTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoSettingTitleValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "qaValue", "请求报文中未包含qaValue");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");


        ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitleValuePo.class);
        return saveReportInfoSettingTitleValueBMOImpl.save(reportInfoSettingTitleValuePo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /reportInfoSettingTitleValue/updateTitleValue
     * @path /app/reportInfoSettingTitleValue/updateReportInfoSettingTitleValue
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportInfoSettingTitleValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "qaValue", "请求报文中未包含qaValue");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitleValuePo.class);
        return updateReportInfoSettingTitleValueBMOImpl.update(reportInfoSettingTitleValuePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportInfoSettingTitleValue/deleteTitleValue
     * @path /app/reportInfoSettingTitleValue/deleteReportInfoSettingTitleValue
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportInfoSettingTitleValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitleValuePo.class);
        return deleteReportInfoSettingTitleValueBMOImpl.delete(reportInfoSettingTitleValuePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportInfoSettingTitleValue/queryTitleValue
     * @path /app/reportInfoSettingTitleValue/queryReportInfoSettingTitleValue
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryTitleValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoSettingTitleValue(@RequestParam(value = "communityId") String communityId,
                                                                   @RequestParam(value = "titleId") String titleId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto = new ReportInfoSettingTitleValueDto();
        reportInfoSettingTitleValueDto.setPage(page);
        reportInfoSettingTitleValueDto.setRow(row);
        reportInfoSettingTitleValueDto.setTitleId(titleId);
        reportInfoSettingTitleValueDto.setCommunityId(communityId);
        return getReportInfoSettingTitleValueBMOImpl.get(reportInfoSettingTitleValueDto);
    }
    /**
     * 微信删除消息模板
     * @serviceCode /reportInfoSettingTitleValue/queryTitleValueResult
     * @path /app/reportInfoSettingTitleValue/queryReportInfoSettingTitleValue
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryTitleValueResult", method = RequestMethod.GET)
    public ResponseEntity<String> getReportInfoSettingTitleValueInfoResult(@RequestParam(value = "communityId") String communityId,
                                                                   @RequestParam(value = "titleId") String titleId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto = new ReportInfoSettingTitleValueDto();
        reportInfoSettingTitleValueDto.setPage(page);
        reportInfoSettingTitleValueDto.setRow(row);
        reportInfoSettingTitleValueDto.setTitleId(titleId);
        reportInfoSettingTitleValueDto.setCommunityId(communityId);
        return getReportInfoSettingTitleValueBMOImpl.getReportInfoSettingTitleValueInfoResult(reportInfoSettingTitleValueDto);
    }
}
