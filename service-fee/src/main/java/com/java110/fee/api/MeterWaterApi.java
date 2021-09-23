package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.fee.bmo.meterWater.IQueryPreMeterWater;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/meterWater")
public class MeterWaterApi {


    @Autowired
    private IQueryPreMeterWater queryPreMeterWaterImpl;


    /**
     * 查询上期度数信息
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /meterWater/queryPreMeterWater
     * @path /app/meterWater/queryPreMeterWater
     */
    @RequestMapping(value = "/queryPreMeterWater", method = RequestMethod.GET)
    public ResponseEntity<String> queryPreMeterWater(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "objId", required = false) String objId,
                                                     @RequestParam(value = "objType", required = false) String objType,
                                                     @RequestParam(value = "roomNum", required = false) String roomNum,
                                                     @RequestParam(value = "meterType", required = false) String meterType) {
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setObjId(objId);
        meterWaterDto.setObjType(objType);
        meterWaterDto.setPage(1);
        meterWaterDto.setRow(1);
        meterWaterDto.setCommunityId(communityId);
        meterWaterDto.setMeterType(meterType);
        return queryPreMeterWaterImpl.query(meterWaterDto, roomNum);
    }

    /**
     * 查询 导出模板数据
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /meterWater/queryExportRoomAndMeterWater
     * @path /app/meterWater/queryExportRoomAndMeterWater
     */
    @RequestMapping(value = "/queryExportRoomAndMeterWater", method = RequestMethod.GET)
    public ResponseEntity<String> queryExportRoomAndMeterWater(@RequestParam(value = "communityId") String communityId,
                                                               @RequestParam(value = "meterType") String meterType) {
        return queryPreMeterWaterImpl.queryExportRoomAndMeterWater(communityId, meterType);
    }

    /**
     * 导入水电抄表
     *
     * @param reqString 请求报文
     * @return
     * @serviceCode /meterWater/importMeterWater
     * @path /app/meterWater/importMeterWater
     */
    @RequestMapping(value = "/importMeterWater", method = RequestMethod.POST)
    public ResponseEntity<String> importMeterWater(@RequestBody String reqString) {
        JSONObject reqJson = JSONObject.parseObject(reqString);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求信息中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "configId", "请求信息中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求信息中未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求信息中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "batchId", "请求信息中未包含批次号");
        Assert.hasKeyAndValue(reqJson, "meterType", "请求信息中未包含抄表类型");
        return queryPreMeterWaterImpl.importMeterWater(reqJson);
    }


}
