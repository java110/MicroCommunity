package com.java110.boot.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.importData.DefaultImportDataAdapt;
import com.java110.boot.importData.IImportDataCleaningAdapt;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.importData.ImportRoomFee;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 一次性费用导入
 * 方式1
 */
@Service("importRoomFeeDataCleaning")
public class ImportRoomFeeDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportRoomFee> importRoomFees = new ArrayList<ImportRoomFee>();
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(paramIn.getString("objType"))) {
            getRooms(workbook, importRoomFees);
        } else {
            getCars(workbook, importRoomFees);
        }
        //todo 生成批次号
        generatorBatch(paramIn);
        for (ImportRoomFee importRoomFee : importRoomFees) {
            importRoomFee.setBatchId(paramIn.getString("batchId"));
            importRoomFee.setUserId(paramIn.getString("userId"));
            importRoomFee.setStoreId(paramIn.getString("storeId"));
            importRoomFee.setCommunityId(paramIn.getString("communityId"));
            importRoomFee.setFeeTypeCd(paramIn.getString("feeTypeCd"));
            importRoomFee.setObjType(paramIn.getString("objType"));

        }
        return importRoomFees;
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms(Workbook workbook, List<ImportRoomFee> rooms) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "房屋费用信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportRoomFee importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            //费用名称没有填写，默认跳过
            if (StringUtil.isNullOrNone(os[4])) {
                continue;
            }
            Assert.hasValue(os[1], (osIndex + 1) + "行费用名称不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行开始时间不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行结束时间不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行费用不能为空");

//

            String startTime = excelDoubleToDate(os[2].toString());
            String endTime = excelDoubleToDate(os[3].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportRoomFee();
            importRoomFee.setPayerObjName(os[0].toString());
            importRoomFee.setFeeName(os[1].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setEndTime(endTime);
            importRoomFee.setAmount(os[4].toString());
            rooms.add(importRoomFee);
        }
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param cars
     */
    private void getCars(Workbook workbook, List<ImportRoomFee> cars) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "车位费用信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportRoomFee importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            //费用名称没有填写，默认跳过
            if (StringUtil.isNullOrNone(os[1])) {
                continue;
            }
            Assert.hasValue(os[2], (osIndex + 1) + "行开始时间不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行结束时间不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行费用不能为空");

//

            String startTime = excelDoubleToDate(os[2].toString());
            String endTime = excelDoubleToDate(os[3].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportRoomFee();
            importRoomFee.setPayerObjName(os[0].toString());
            importRoomFee.setFeeName(os[1].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setEndTime(endTime);
            importRoomFee.setAmount(os[4].toString());
            cars.add(importRoomFee);
        }
    }


}
