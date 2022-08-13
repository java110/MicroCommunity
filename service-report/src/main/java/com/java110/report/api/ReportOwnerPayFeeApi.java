package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import com.java110.report.bmo.reportOwnerPayFee.IDeleteReportOwnerPayFeeBMO;
import com.java110.report.bmo.reportOwnerPayFee.IGetReportOwnerPayFeeBMO;
import com.java110.report.bmo.reportOwnerPayFee.ISaveReportOwnerPayFeeBMO;
import com.java110.report.bmo.reportOwnerPayFee.IUpdateReportOwnerPayFeeBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/reportOwnerPayFee")
public class ReportOwnerPayFeeApi {

    @Autowired
    private ISaveReportOwnerPayFeeBMO saveReportOwnerPayFeeBMOImpl;
    @Autowired
    private IUpdateReportOwnerPayFeeBMO updateReportOwnerPayFeeBMOImpl;
    @Autowired
    private IDeleteReportOwnerPayFeeBMO deleteReportOwnerPayFeeBMOImpl;

    @Autowired
    private IGetReportOwnerPayFeeBMO getReportOwnerPayFeeBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOwnerPayFee/saveReportOwnerPayFee
     * @path /app/reportOwnerPayFee/saveReportOwnerPayFee
     */
    @RequestMapping(value = "/saveReportOwnerPayFee", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportOwnerPayFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "pfId", "请求报文中未包含pfId");


        ReportOwnerPayFeePo reportOwnerPayFeePo = BeanConvertUtil.covertBean(reqJson, ReportOwnerPayFeePo.class);
        return saveReportOwnerPayFeeBMOImpl.save(reportOwnerPayFeePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOwnerPayFee/updateReportOwnerPayFee
     * @path /app/reportOwnerPayFee/updateReportOwnerPayFee
     */
    @RequestMapping(value = "/updateReportOwnerPayFee", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportOwnerPayFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "pfId", "请求报文中未包含pfId");
        Assert.hasKeyAndValue(reqJson, "pfId", "pfId不能为空");


        ReportOwnerPayFeePo reportOwnerPayFeePo = BeanConvertUtil.covertBean(reqJson, ReportOwnerPayFeePo.class);
        return updateReportOwnerPayFeeBMOImpl.update(reportOwnerPayFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOwnerPayFee/deleteReportOwnerPayFee
     * @path /app/reportOwnerPayFee/deleteReportOwnerPayFee
     */
    @RequestMapping(value = "/deleteReportOwnerPayFee", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportOwnerPayFee(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "pfId", "pfId不能为空");


        ReportOwnerPayFeePo reportOwnerPayFeePo = BeanConvertUtil.covertBean(reqJson, ReportOwnerPayFeePo.class);
        return deleteReportOwnerPayFeeBMOImpl.delete(reportOwnerPayFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportOwnerPayFee/queryReportOwnerPayFee
     * @path /app/reportOwnerPayFee/queryReportOwnerPayFee
     */
    @RequestMapping(value = "/queryReportOwnerPayFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportOwnerPayFee(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "pfYear", required = false) String pfYear,
                                                         @RequestParam(value = "configId", required = false) String configId,
                                                         @RequestParam(value = "roomName", required = false) String roomName,
                                                         @RequestParam(value = "ownerName", required = false) String ownerName,
                                                         @RequestParam(value = "feeTypeCd",required = false) String feeTypeCd,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ReportOwnerPayFeeDto reportOwnerPayFeeDto = new ReportOwnerPayFeeDto();
        reportOwnerPayFeeDto.setPage(page);
        reportOwnerPayFeeDto.setRow(row);
        reportOwnerPayFeeDto.setCommunityId(communityId);
        reportOwnerPayFeeDto.setPfYear(pfYear);
        reportOwnerPayFeeDto.setConfigId(configId);
        reportOwnerPayFeeDto.setRoomName(roomName);
        reportOwnerPayFeeDto.setOwnerName(ownerName);
        reportOwnerPayFeeDto.setFeeTypeCd(feeTypeCd);
        if (!StringUtil.isEmpty(roomName) && roomName.contains("-")) {
            String[] datas = roomName.split("-",3);
            if (datas.length != 3) {
                throw new IllegalArgumentException("房屋格式错误，请填写 楼栋-单元-房屋格式");
            }
            reportOwnerPayFeeDto.setFloorNum(datas[0]);
            reportOwnerPayFeeDto.setUnitNum(datas[1]);
            reportOwnerPayFeeDto.setRoomNum(datas[2]);
        }
        return getReportOwnerPayFeeBMOImpl.get(reportOwnerPayFeeDto);
    }
}
