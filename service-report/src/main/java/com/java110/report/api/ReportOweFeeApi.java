package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.report.bmo.reportOweFee.IDeleteReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.IGetReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.ISaveReportOweFeeBMO;
import com.java110.report.bmo.reportOweFee.IUpdateReportOweFeeBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reportOweFee")
public class ReportOweFeeApi {

    @Autowired
    private ISaveReportOweFeeBMO saveReportOweFeeBMOImpl;
    @Autowired
    private IUpdateReportOweFeeBMO updateReportOweFeeBMOImpl;
    @Autowired
    private IDeleteReportOweFeeBMO deleteReportOweFeeBMOImpl;

    @Autowired
    private IGetReportOweFeeBMO getReportOweFeeBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOweFee/saveReportOweFee
     * @path /app/reportOweFee/saveReportOweFee
     */
    @RequestMapping(value = "/saveReportOweFee", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportOweFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configName", "请求报文中未包含configName");


        ReportOweFeePo reportOweFeePo = BeanConvertUtil.covertBean(reqJson, ReportOweFeePo.class);
        return saveReportOweFeeBMOImpl.save(reportOweFeePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOweFee/updateReportOweFee
     * @path /app/reportOweFee/updateReportOweFee
     */
    @RequestMapping(value = "/updateReportOweFee", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportOweFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configName", "请求报文中未包含configName");
        Assert.hasKeyAndValue(reqJson, "oweId", "oweId不能为空");


        ReportOweFeePo reportOweFeePo = BeanConvertUtil.covertBean(reqJson, ReportOweFeePo.class);
        return updateReportOweFeeBMOImpl.update(reportOweFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportOweFee/deleteReportOweFee
     * @path /app/reportOweFee/deleteReportOweFee
     */
    @RequestMapping(value = "/deleteReportOweFee", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportOweFee(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "oweId", "oweId不能为空");


        ReportOweFeePo reportOweFeePo = BeanConvertUtil.covertBean(reqJson, ReportOweFeePo.class);
        return deleteReportOweFeeBMOImpl.delete(reportOweFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportOweFee/queryReportOweFee
     * @path /app/reportOweFee/queryReportOweFee
     */
    @RequestMapping(value = "/queryReportOweFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportOweFee(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "configIds", required = false) String configIds,
                                                    @RequestParam(value = "payObjType", required = false) String payObjType,
                                                    @RequestParam(value = "num", required = false) String num,
                                                    @RequestParam(value = "ownerName", required = false) String ownerName,
                                                    @RequestParam(value = "floorId", required = false) String floorId,
                                                    @RequestParam(value = "unitId", required = false) String unitId,
                                                    @RequestParam(value = "roomSubType", required = false) String roomSubType,
                                                    @RequestParam(value = "roomNum", required = false) String roomNum,
                                                    @RequestParam(value = "ownerId", required = false) String ownerId,
                                                    @RequestParam(value = "payerObjId", required = false) String payerObjId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPage(page);
        reportOweFeeDto.setRow(row);
        reportOweFeeDto.setPayerObjType(payObjType);
        reportOweFeeDto.setPayerObjName(num);
        reportOweFeeDto.setCommunityId(communityId);
        reportOweFeeDto.setOwnerName(ownerName);
        reportOweFeeDto.setFloorId(floorId);
        reportOweFeeDto.setUnitId(unitId);
        reportOweFeeDto.setRoomSubType(roomSubType);
        reportOweFeeDto.setRoomNum(roomNum);
        reportOweFeeDto.setOwnerId(ownerId);
        reportOweFeeDto.setPayerObjId(payerObjId);
        if (!StringUtil.isEmpty(configIds)) {
            String[] tmpConfigIds = configIds.split(",");
            reportOweFeeDto.setConfigIds(tmpConfigIds);
        }
        return getReportOweFeeBMOImpl.get(reportOweFeeDto);
    }

    /**
     * 查询所有欠费信息
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportOweFee/queryReportAllOweFee
     * @path /app/reportOweFee/queryReportAllOweFee
     */
    @RequestMapping(value = "/queryReportAllOweFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportAllOweFee(@RequestParam(value = "communityId") String communityId,
                                                       @RequestParam(value = "configIds", required = false) String configIds,
                                                       @RequestParam(value = "payObjType", required = false) String payObjType,
                                                       @RequestParam(value = "hasOweFee", required = false) String hasOweFee,
                                                       @RequestParam(value = "ownerName", required = false) String ownerName,
                                                       @RequestParam(value = "floorId", required = false) String floorId,
                                                       @RequestParam(value = "unitId", required = false) String unitId,
                                                       @RequestParam(value = "roomSubType", required = false) String roomSubType,
                                                       @RequestParam(value = "roomNum", required = false) String roomNum,
                                                       @RequestParam(value = "num", required = false) String num) {
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPayerObjType(payObjType);
        reportOweFeeDto.setPayerObjName(num);
        reportOweFeeDto.setCommunityId(communityId);
        reportOweFeeDto.setHasOweFee(hasOweFee);
        reportOweFeeDto.setOwnerName(ownerName);
        reportOweFeeDto.setFloorId(floorId);
        reportOweFeeDto.setUnitId(unitId);
        reportOweFeeDto.setRoomSubType(roomSubType);
        reportOweFeeDto.setRoomNum(roomNum);
        if (!StringUtil.isEmpty(configIds)) {
            String[] tmpConfigIds = configIds.split(",");
            reportOweFeeDto.setConfigIds(tmpConfigIds);
        }
        return getReportOweFeeBMOImpl.getAllFees(reportOweFeeDto);
    }
}
