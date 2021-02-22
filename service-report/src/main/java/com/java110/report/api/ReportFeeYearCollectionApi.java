package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import com.java110.report.bmo.reportFeeYearCollection.IDeleteReportFeeYearCollectionBMO;
import com.java110.report.bmo.reportFeeYearCollection.IGetReportFeeYearCollectionBMO;
import com.java110.report.bmo.reportFeeYearCollection.ISaveReportFeeYearCollectionBMO;
import com.java110.report.bmo.reportFeeYearCollection.IUpdateReportFeeYearCollectionBMO;
import com.java110.report.bmo.reportFeeYearCollectionDetail.IDeleteReportFeeYearCollectionDetailBMO;
import com.java110.report.bmo.reportFeeYearCollectionDetail.IGetReportFeeYearCollectionDetailBMO;
import com.java110.report.bmo.reportFeeYearCollectionDetail.ISaveReportFeeYearCollectionDetailBMO;
import com.java110.report.bmo.reportFeeYearCollectionDetail.IUpdateReportFeeYearCollectionDetailBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/reportFeeYearCollection")
public class ReportFeeYearCollectionApi {

    @Autowired
    private ISaveReportFeeYearCollectionBMO saveReportFeeYearCollectionBMOImpl;
    @Autowired
    private IUpdateReportFeeYearCollectionBMO updateReportFeeYearCollectionBMOImpl;
    @Autowired
    private IDeleteReportFeeYearCollectionBMO deleteReportFeeYearCollectionBMOImpl;

    @Autowired
    private IGetReportFeeYearCollectionBMO getReportFeeYearCollectionBMOImpl;

    @Autowired
    private ISaveReportFeeYearCollectionDetailBMO saveReportFeeYearCollectionDetailBMOImpl;
    @Autowired
    private IUpdateReportFeeYearCollectionDetailBMO updateReportFeeYearCollectionDetailBMOImpl;
    @Autowired
    private IDeleteReportFeeYearCollectionDetailBMO deleteReportFeeYearCollectionDetailBMOImpl;

    @Autowired
    private IGetReportFeeYearCollectionDetailBMO getReportFeeYearCollectionDetailBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeYearCollection/saveReportFeeYearCollection
     * @path /app/reportFeeYearCollection/saveReportFeeYearCollection
     */
    @RequestMapping(value = "/saveReportFeeYearCollection", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportFeeYearCollection(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");


        ReportFeeYearCollectionPo reportFeeYearCollectionPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionPo.class);
        return saveReportFeeYearCollectionBMOImpl.save(reportFeeYearCollectionPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeYearCollection/updateReportFeeYearCollection
     * @path /app/reportFeeYearCollection/updateReportFeeYearCollection
     */
    @RequestMapping(value = "/updateReportFeeYearCollection", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportFeeYearCollection(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "collectionId", "collectionId不能为空");


        ReportFeeYearCollectionPo reportFeeYearCollectionPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionPo.class);
        return updateReportFeeYearCollectionBMOImpl.update(reportFeeYearCollectionPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeYearCollection/deleteReportFeeYearCollection
     * @path /app/reportFeeYearCollection/deleteReportFeeYearCollection
     */
    @RequestMapping(value = "/deleteReportFeeYearCollection", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportFeeYearCollection(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "collectionId", "collectionId不能为空");


        ReportFeeYearCollectionPo reportFeeYearCollectionPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionPo.class);
        return deleteReportFeeYearCollectionBMOImpl.delete(reportFeeYearCollectionPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeYearCollection/queryReportFeeYearCollection
     * @path /app/reportFeeYearCollection/queryReportFeeYearCollection
     */
    @RequestMapping(value = "/queryReportFeeYearCollection", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportFeeYearCollection(@RequestParam(value = "communityId") String communityId,
                                                               @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "row") int row) {
        ReportFeeYearCollectionDto reportFeeYearCollectionDto = new ReportFeeYearCollectionDto();
        reportFeeYearCollectionDto.setPage(page);
        reportFeeYearCollectionDto.setRow(row);
        reportFeeYearCollectionDto.setCommunityId(communityId);
        return getReportFeeYearCollectionBMOImpl.get(reportFeeYearCollectionDto);
    }

    /**
     * 微信保存消息模板
     * @serviceCode /reportFeeYearCollectionDetail/saveReportFeeYearCollectionDetail
     * @path /app/reportFeeYearCollectionDetail/saveReportFeeYearCollectionDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportFeeYearCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionId", "请求报文中未包含collectionId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");


        ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionDetailPo.class);
        return saveReportFeeYearCollectionDetailBMOImpl.save(reportFeeYearCollectionDetailPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /reportFeeYearCollection/updateReportFeeYearCollectionDetail
     * @path /app/reportFeeYearCollection/updateReportFeeYearCollectionDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportFeeYearCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionId", "请求报文中未包含collectionId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionDetailPo.class);
        return updateReportFeeYearCollectionDetailBMOImpl.update(reportFeeYearCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportFeeYearCollection/deleteReportFeeYearCollectionDetail
     * @path /app/reportFeeYearCollection/deleteReportFeeYearCollectionDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportFeeYearCollectionDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionDetailPo.class);
        return deleteReportFeeYearCollectionDetailBMOImpl.delete(reportFeeYearCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /reportFeeYearCollection/queryReportFeeYearCollectionDetail
     * @path /app/reportFeeYearCollection/queryReportFeeYearCollectionDetail
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryReportFeeYearCollectionDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportFeeYearCollectionDetail(@RequestParam(value = "communityId") String communityId,
                                                                     @RequestParam(value = "page") int page,
                                                                     @RequestParam(value = "row") int row) {
        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
        reportFeeYearCollectionDetailDto.setPage(page);
        reportFeeYearCollectionDetailDto.setRow(row);
        reportFeeYearCollectionDetailDto.setCommunityId(communityId);
        return getReportFeeYearCollectionDetailBMOImpl.get(reportFeeYearCollectionDetailDto);
    }
}
