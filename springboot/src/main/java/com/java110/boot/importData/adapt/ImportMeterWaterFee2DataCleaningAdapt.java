package com.java110.boot.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.importData.DefaultImportDataAdapt;
import com.java110.boot.importData.IImportDataCleaningAdapt;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 水电抄表
 * 方式1
 */
@Service("importMeterWaterFee2DataCleaning")
public class ImportMeterWaterFee2DataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {
    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportExportMeterWaterDto> rooms = new ArrayList<ImportExportMeterWaterDto>();
        //获取楼信息
        getRooms(workbook, rooms);
        generatorBatch(paramIn);
        for (ImportExportMeterWaterDto importRoomFee : rooms) {
            importRoomFee.setBatchId(paramIn.getString("batchId"));
            importRoomFee.setUserId(paramIn.getString("userId"));
            importRoomFee.setStoreId(paramIn.getString("storeId"));
            importRoomFee.setFeeTypeCd(paramIn.getString("feeTypeCd"));
            importRoomFee.setConfigId(paramIn.getString("configId"));
            importRoomFee.setMeterType(paramIn.getString("meterType"));
            importRoomFee.setCommunityId(paramIn.getString("communityId"));

        }
        return rooms;
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms(Workbook workbook, List<ImportExportMeterWaterDto> rooms) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "房屋费用信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportExportMeterWaterDto importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[1], (osIndex + 1) + "单元编号不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "房屋编号不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "单价不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "上期度数不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "上期度数时间不能为空");
            Assert.hasValue(os[7], (osIndex + 1) + "本期度数不能为空");
            Assert.hasValue(os[8], (osIndex + 1) + "本期度数时间不能为空");

//

            String startTime = excelDoubleToDate(os[6].toString());
            String endTime = excelDoubleToDate(os[8].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportExportMeterWaterDto();
            importRoomFee.setFloorNum(os[0].toString());
            importRoomFee.setUnitNum(os[1].toString());
            importRoomFee.setRoomNum(os[2].toString());
            importRoomFee.setPrice(Double.parseDouble(os[4].toString()));
            importRoomFee.setPreDegrees(os[5].toString());
            importRoomFee.setPreReadingTime(startTime);
            importRoomFee.setCurDegrees(os[7].toString());
            importRoomFee.setCurReadingTime(endTime);
            rooms.add(importRoomFee);
            if (Double.parseDouble(importRoomFee.getCurDegrees()) < Double.parseDouble(importRoomFee.getPreDegrees())) {
                throw new IllegalArgumentException((osIndex + 1) + "行本期读数小于上期读数");
            }
        }
    }



}
