package com.java110.boot.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.importData.DefaultImportDataAdapt;
import com.java110.boot.importData.IImportDataCleaningAdapt;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service("importOwnerCarDataCleaning")
public class ImportOwnerCarDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn,  ComponentValidateResult result) throws Exception{

        List<OwnerCarDto> ownerCars = new ArrayList<OwnerCarDto>();
        //封装对象
        getOwnerCars(workbook, ownerCars, result);
        //数据格式校验
        validateCarInfo(ownerCars);


        return ownerCars;
    }

    /**
     * 获取业主车辆信息
     *
     * @param workbook
     * @param ownerCars
     */
    private void getOwnerCars(Workbook workbook, List<OwnerCarDto> ownerCars,  ComponentValidateResult result) throws ParseException {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "业主车辆信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        OwnerCarDto importOwnerCar = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "车牌号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "业主不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "手机号不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "车辆品牌不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "车辆类型不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "颜色不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "停车场不能为空");
            Assert.hasValue(os[7], (osIndex + 1) + "车位不能为空");
            Assert.hasValue(os[8], (osIndex + 1) + "起租时间不能为空");
            Assert.hasValue(os[9], (osIndex + 1) + "截止时间不能为空");
            Assert.hasValue(os[10], (osIndex + 1) + "停车场类型不能为空");
            Assert.hasValue(os[11], (osIndex + 1) + "车位类型不能为空");
            String startTime = excelDoubleToDate(os[8].toString());
            String endTime = excelDoubleToDate(os[9].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD文本格式");
            importOwnerCar = new OwnerCarDto();
            importOwnerCar.setCarNum(os[0].toString().trim());
            importOwnerCar.setOwnerName(os[1].toString().trim());
            importOwnerCar.setLink(os[2].toString().trim());
            importOwnerCar.setCarBrand(os[3].toString().trim());
            importOwnerCar.setCarType(os[4].toString().trim());
            importOwnerCar.setCarColor(os[5].toString().trim());
            importOwnerCar.setAreaNum(os[6].toString().trim());
            //获取车位
            String parkingLot = os[7].toString().trim();
            importOwnerCar.setNum(parkingLot);
            importOwnerCar.setLogStartTime(startTime);
            importOwnerCar.setLogEndTime(endTime);
            importOwnerCar.setTypeCd(os[10].toString().trim());
            importOwnerCar.setSpaceSate(os[11].toString().trim());
            importOwnerCar.setCommunityId(result.getCommunityId());
            importOwnerCar.setUserId(result.getUserId());
            ownerCars.add(importOwnerCar);
        }
    }


    /**
     * 数据格式校验
     *
     * @param ownerCars
     */
    private void validateCarInfo(List<OwnerCarDto> ownerCars) {
        for (OwnerCarDto ownerCarDto : ownerCars) {

            if (!"1001".equals(ownerCarDto.getTypeCd()) && !"2001".equals(ownerCarDto.getTypeCd())) {
                throw new IllegalArgumentException(ownerCarDto.getCarNum() + "停车场类型应填写 1001(地上停车场)或者 2001 (地下停车场)");
            }
            if (!"H".equals(ownerCarDto.getSpaceSate()) && !"S".equals(ownerCarDto.getSpaceSate())) {
                throw new IllegalArgumentException(ownerCarDto.getCarNum() + "车位状态应填写 S（出售）或者 H （出租）");
            }


        }
    }
}
