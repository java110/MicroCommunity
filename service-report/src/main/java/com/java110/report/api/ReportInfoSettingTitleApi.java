package com.java110.report.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleDto;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import com.java110.report.bmo.reportInfoSettingTitle.IDeleteReportInfoSettingTitleBMO;
import com.java110.report.bmo.reportInfoSettingTitle.IGetReportInfoSettingTitleBMO;
import com.java110.report.bmo.reportInfoSettingTitle.ISaveReportInfoSettingTitleBMO;
import com.java110.report.bmo.reportInfoSettingTitle.IUpdateReportInfoSettingTitleBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportInfoSettingTitle")
public class ReportInfoSettingTitleApi {

    @Autowired
    private ISaveReportInfoSettingTitleBMO saveReportInfoSettingTitleBMOImpl;
    @Autowired
    private IUpdateReportInfoSettingTitleBMO updateReportInfoSettingTitleBMOImpl;
    @Autowired
    private IDeleteReportInfoSettingTitleBMO deleteReportInfoSettingTitleBMOImpl;

    @Autowired
    private IGetReportInfoSettingTitleBMO getReportInfoSettingTitleBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSettingTitle/saveReportInfoSettingTitle
     * @path /app/reportInfoSettingTitle/saveReportInfoSettingTitle
     */
    @RequestMapping(value = "/saveReportInfoSettingTitle", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportInfoSettingTitle(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "title", "请求报文中未包含title");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        JSONArray titleValues = null;
        if (!ReportInfoSettingTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");
            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
            for (int index = 0; index < titleValues.size(); index++) {
                JSONObject param = titleValues.getJSONObject(index);
                if (StringUtil.isEmpty(param.getString("qaValue"))) {
                    throw new IllegalArgumentException("题目选项不能为空");
                }
            }
        }

        ReportInfoSettingTitlePo reportInfoSettingTitlePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitlePo.class);
        return saveReportInfoSettingTitleBMOImpl.save(reportInfoSettingTitlePo, titleValues);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSettingTitle/updateSettingTitle
     * @path /app/reportInfoSettingTitle/updateSettingTitle
     */
    @RequestMapping(value = "/updateSettingTitle", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportInfoSettingTitle(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "title", "请求报文中未包含title");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        JSONArray titleValues = null;
        if (!ReportInfoSettingTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");
            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
            for (int index = 0; index < titleValues.size(); index++) {
                JSONObject param = titleValues.getJSONObject(index);
                if (StringUtil.isEmpty(param.getString("qaValue"))) {
                    throw new IllegalArgumentException("题目选项不能为空");
                }
            }
        }

        ReportInfoSettingTitlePo reportInfoSettingTitlePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitlePo.class);
        deleteReportInfoSettingTitleBMOImpl.delete(reportInfoSettingTitlePo);

        return saveReportInfoSettingTitleBMOImpl.save(reportInfoSettingTitlePo, titleValues);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportInfoSettingTitle/deleteSettingTitle
     * @path /app/reportInfoSettingTitle/deleteReportInfoSettingTitle
     */
    @RequestMapping(value = "/deleteSettingTitle", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportInfoSettingTitle(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "titleId", "titleId不能为空");


        ReportInfoSettingTitlePo reportInfoSettingTitlePo = BeanConvertUtil.covertBean(reqJson, ReportInfoSettingTitlePo.class);
        return deleteReportInfoSettingTitleBMOImpl.delete(reportInfoSettingTitlePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportInfoSettingTitle/querySettingTitle
     * @path /app/reportInfoSettingTitle/queryReportInfoSettingTitle
     */
    @RequestMapping(value = "/querySettingTitle", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportInfoSettingTitle(@RequestParam(value = "communityId") String communityId,
                                                              @RequestParam(value = "titleType", required = false) String titleType,
                                                              @RequestParam(value = "title", required = false) String title,
                                                              @RequestParam(value = "titleId", required = false) String titleId,
                                                              @RequestParam(value = "settingId", required = false) String settingId,
                                                              @RequestParam(value = "page") int page,
                                                              @RequestParam(value = "row") int row) {
        ReportInfoSettingTitleDto reportInfoSettingTitleDto = new ReportInfoSettingTitleDto();
        reportInfoSettingTitleDto.setPage(page);
        reportInfoSettingTitleDto.setRow(row);
        reportInfoSettingTitleDto.setTitleType(titleType);
        reportInfoSettingTitleDto.setTitleLike(title);
        reportInfoSettingTitleDto.setTitleId(titleId);
        reportInfoSettingTitleDto.setSettingId(settingId);
        reportInfoSettingTitleDto.setCommunityId(communityId);
        return getReportInfoSettingTitleBMOImpl.get(reportInfoSettingTitleDto);
    }
}
