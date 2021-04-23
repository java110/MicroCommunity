package com.java110.fee.bmo.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.meterWater.MeterWaterDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询上期信息
 */
public interface IQueryPreMeterWater {

    /**
     * 查询记录
     *
     * @param meterWaterDto 表信息
     * @return
     */
    ResponseEntity<String> query(MeterWaterDto meterWaterDto, String roomNum);

    /**
     * 查询导出房屋表读数
     *
     * @param communityId
     * @param meterType
     * @return
     */
    ResponseEntity<String> queryExportRoomAndMeterWater(String communityId, String meterType);

    /**
     * 导入
     *
     * @param reqJson 请求报文
     * @return
     */
    ResponseEntity<String> importMeterWater(JSONObject reqJson);

}
