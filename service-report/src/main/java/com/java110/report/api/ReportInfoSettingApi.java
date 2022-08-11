package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoSetting.ReportInfoSettingDto;
import com.java110.po.reportInfoSetting.ReportInfoSettingPo;
import com.java110.report.bmo.reportInfoSetting.IDeleteReportInfoSettingBMO;
import com.java110.report.bmo.reportInfoSetting.IGetReportInfoSettingBMO;
import com.java110.report.bmo.reportInfoSetting.ISaveReportInfoSettingBMO;
import com.java110.report.bmo.reportInfoSetting.IUpdateReportInfoSettingBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportInfoSetting")
public class ReportInfoSettingApi {

    @Autowired
    private ISaveReportInfoSettingBMO saveReportInfoSettingBMOImpl;
    @Autowired
    private IUpdateReportInfoSettingBMO updateReportInfoSettingBMOImpl;
    @Autowired
    private IDeleteReportInfoSettingBMO deleteReportInfoSettingBMOImpl;

    @Autowired
    private IGetReportInfoSettingBMO getReportInfoSettingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSetting/saveReportInfoSetting
     * @path /app/reportInfoSetting/saveReportInfoSetting
     */
    @RequestMapping(value = "/saveReportInfoSetting", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoSetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "reportType", "请求报文中未包含reportType");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        ReportInfoSettingPo reportInfoSettingPo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingPo.class);
        return saveReportInfoSettingBMOImpl.save(reportInfoSettingPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSetting/updateReportInfoSetting
     * @path /app/reportInfoSetting/updateReportInfoSetting
     */
    @RequestMapping(value = "/updateReportInfoSetting", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportInfoSetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "reportType", "请求报文中未包含reportType");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
//        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");
        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        ReportInfoSettingPo reportInfoSettingPo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingPo.class);
        return updateReportInfoSettingBMOImpl.update(reportInfoSettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSetting/deleteReportInfoSetting
     * @path /app/reportInfoSetting/deleteReportInfoSetting
     */
    @RequestMapping(value = "/deleteReportInfoSetting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportInfoSetting(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        ReportInfoSettingPo reportInfoSettingPo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingPo.class);
        return deleteReportInfoSettingBMOImpl.delete(reportInfoSettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportInfoSetting/queryReportInfoSetting
     * @path /app/reportInfoSetting/queryReportInfoSetting
     */
    @RequestMapping(value = "/queryReportInfoSetting", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoSetting(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "settingId", required = false) String settingId,
                                                         @RequestParam(value = "reportType", required = false) String reportType,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ReportInfoSettingDto reportInfoSettingDto = new ReportInfoSettingDto();
        reportInfoSettingDto.setPage(page);
        reportInfoSettingDto.setRow(row);
        reportInfoSettingDto.setCommunityId(communityId);
        reportInfoSettingDto.setNameLike(name);
        reportInfoSettingDto.setSettingId(settingId);
        reportInfoSettingDto.setReportType(reportType);
        return getReportInfoSettingBMOImpl.get(reportInfoSettingDto);
    }
}
