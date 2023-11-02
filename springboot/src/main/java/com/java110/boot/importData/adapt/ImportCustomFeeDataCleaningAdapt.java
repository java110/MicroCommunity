package com.java110.boot.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.importData.DefaultImportDataAdapt;
import com.java110.boot.importData.IImportDataCleaningAdapt;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
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
 * 自定义创建费用
 * 方式1
 */
@Service("importCustomFeeDataCleaning")
public class ImportCustomFeeDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = new ArrayList<ImportCustomCreateFeeDto>();
        //获取楼信息
        getImportCustomCreateFeeDtos(workbook, importCustomCreateFeeDtos, result);
        generatorBatch(paramIn);
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            importRoomFee.setBatchId(paramIn.getString("batchId"));
            importRoomFee.setUserId(paramIn.getString("userId"));
            importRoomFee.setStoreId(paramIn.getString("storeId"));
            importRoomFee.setCommunityId(paramIn.getString("communityId"));

        }
        return importCustomCreateFeeDtos;
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param importCustomCreateFeeDtos
     */
    private void getImportCustomCreateFeeDtos(Workbook workbook, List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos, ComponentValidateResult result) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "创建费用");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportCustomCreateFeeDto importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            //费用名称没有填写，默认跳过
            if (StringUtil.isNullOrNone(os[5])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行房号/车牌号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行类型不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行费用项ID不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行收费项目不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行建账时间不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行计费起始时间不能为空");

            String createTime = excelDoubleToDate(os[4].toString());
            String startTime = excelDoubleToDate(os[5].toString());
            Assert.isDate(createTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行建账时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行计费起始时间格式错误 请填写YYYY-MM-DD 文本格式");
            String endTime = "";
            if(os.length > 6 && os[6] != null){
                endTime = excelDoubleToDate(os[6].toString());
                Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行计费结束时间格式错误 请填写YYYY-MM-DD 文本格式");
            }


            importRoomFee = new ImportCustomCreateFeeDto();
            importRoomFee.setObjName(os[0].toString());
            importRoomFee.setObjType(os[1].toString());
            importRoomFee.setConfigId(os[2].toString());
            importRoomFee.setConfigName(os[3].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setCreateTime(createTime);
            importRoomFee.setCommunityId(result.getCommunityId());
            importRoomFee.setEndTime(endTime);
            importCustomCreateFeeDtos.add(importRoomFee);
        }
    }


}
