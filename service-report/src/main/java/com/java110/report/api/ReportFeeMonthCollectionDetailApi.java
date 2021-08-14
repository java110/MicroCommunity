package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailDto;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.IDeleteReportFeeMonthCollectionDetailBMO;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.IGetReportFeeMonthCollectionDetailBMO;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.ISaveReportFeeMonthCollectionDetailBMO;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.IUpdateReportFeeMonthCollectionDetailBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reportFeeMonthCollectionDetail")
public class ReportFeeMonthCollectionDetailApi {

    @Autowired
    private ISaveReportFeeMonthCollectionDetailBMO saveReportFeeMonthCollectionDetailBMOImpl;
    @Autowired
    private IUpdateReportFeeMonthCollectionDetailBMO updateReportFeeMonthCollectionDetailBMOImpl;
    @Autowired
    private IDeleteReportFeeMonthCollectionDetailBMO deleteReportFeeMonthCollectionDetailBMOImpl;

    @Autowired
    private IGetReportFeeMonthCollectionDetailBMO getReportFeeMonthCollectionDetailBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthCollectionDetail/saveReportFeeMonthCollectionDetail
     * @path /app/reportFeeMonthCollectionDetail/saveReportFeeMonthCollectionDetail
     */
    @RequestMapping(value = "/saveReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportFeeMonthCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "collectionYear", "请求报文中未包含collectionYear");
        Assert.hasKeyAndValue(reqJson, "collectionMonth", "请求报文中未包含collectionMonth");


        ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthCollectionDetailPo.class);
        return saveReportFeeMonthCollectionDetailBMOImpl.save(reportFeeMonthCollectionDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthCollectionDetail/updateReportFeeMonthCollectionDetail
     * @path /app/reportFeeMonthCollectionDetail/updateReportFeeMonthCollectionDetail
     */
    @RequestMapping(value = "/updateReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportFeeMonthCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "collectionYear", "请求报文中未包含collectionYear");
        Assert.hasKeyAndValue(reqJson, "collectionMonth", "请求报文中未包含collectionMonth");
        Assert.hasKeyAndValue(reqJson, "cdId", "cdId不能为空");


        ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthCollectionDetailPo.class);
        return updateReportFeeMonthCollectionDetailBMOImpl.update(reportFeeMonthCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthCollectionDetail/deleteReportFeeMonthCollectionDetail
     * @path /app/reportFeeMonthCollectionDetail/deleteReportFeeMonthCollectionDetail
     */
    @RequestMapping(value = "/deleteReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportFeeMonthCollectionDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "cdId", "cdId不能为空");


        ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthCollectionDetailPo.class);
        return deleteReportFeeMonthCollectionDetailBMOImpl.delete(reportFeeMonthCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthCollectionDetail/queryReportFeeMonthCollectionDetail
     * @path /app/reportFeeMonthCollectionDetail/queryReportFeeMonthCollectionDetail
     */
    @RequestMapping(value = "/queryReportFeeMonthCollectionDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportFeeMonthCollectionDetail(@RequestParam(value = "communityId") String communityId,
                                                                      @RequestParam(value = "page") int page,
                                                                      @RequestParam(value = "row") int row) {
        ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto = new ReportFeeMonthCollectionDetailDto();
        reportFeeMonthCollectionDetailDto.setPage(page);
        reportFeeMonthCollectionDetailDto.setRow(row);
        reportFeeMonthCollectionDetailDto.setCommunityId(communityId);
        return getReportFeeMonthCollectionDetailBMOImpl.get(reportFeeMonthCollectionDetailDto);
    }
}
