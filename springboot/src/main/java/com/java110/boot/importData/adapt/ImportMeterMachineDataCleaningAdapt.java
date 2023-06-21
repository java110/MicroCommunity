package com.java110.boot.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.importData.DefaultImportDataAdapt;
import com.java110.boot.importData.IImportDataCleaningAdapt;
import com.java110.dto.importData.ImportMeterMachineDto;
import com.java110.dto.importData.ImportOwnerRoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 水电表导入处理类
 *
 * 导入请求参数中必须包含
 * param.append('importAdapt', "importRoomOwner");
 */
@Service("importMeterMachineDataCleaning")
public class ImportMeterMachineDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {
    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception{

        List<ImportMeterMachineDto> importMeterMachineDtos = new ArrayList<ImportMeterMachineDto>();
        //封装对象
        getOwnerRooms(workbook, importMeterMachineDtos, result);
        //数据格式校验
        validateRoomInfo(importMeterMachineDtos);

        return importMeterMachineDtos;
    }

    /**
     * 获取业主车辆信息
     *
     * @param workbook
     * @param importMeterMachineDtos
     */
    private void getOwnerRooms(Workbook workbook, List<ImportMeterMachineDto> importMeterMachineDtos, ComponentValidateResult result) throws ParseException {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "水电表");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportMeterMachineDto importMeterMachineDto = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {

            Object[] os = oList.get(osIndex);

            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (os == null || StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行名称不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行表号不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行表类型不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行模式不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行房屋不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行费用项不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行厂家不能为空");

            importMeterMachineDto = new ImportMeterMachineDto();
            importMeterMachineDto.setCommunityId(result.getCommunityId());
            importMeterMachineDto.setUserId(result.getUserId());
            importMeterMachineDto.setMachineName(os[0].toString().trim());
            importMeterMachineDto.setAddress(os[1].toString().trim());
            importMeterMachineDto.setMeterType(os[2].toString().trim());
            importMeterMachineDto.setMachineModel(os[3].toString().trim());
            importMeterMachineDto.setRoomName(os[4].toString().trim());
            importMeterMachineDto.setFeeName(os[5].toString().trim());
            importMeterMachineDto.setImplBean(os[6].toString().trim());
            if (os.length > 7 && !StringUtil.isNullOrNone(os[7])) {
                importMeterMachineDto.setValue1(os[7].toString().trim());

            }

            importMeterMachineDtos.add(importMeterMachineDto);

        }
    }


    /**
     * 数据格式校验
     *
     * @param importMeterMachineDtos
     */
    private void validateRoomInfo(List<ImportMeterMachineDto> importMeterMachineDtos) {
        ImportMeterMachineDto importOwnerRoomDto = null;
        for (int roomIndex = 0; roomIndex < importMeterMachineDtos.size(); roomIndex++) {
            importOwnerRoomDto = importMeterMachineDtos.get(roomIndex);


            if (StringUtil.isEmpty(importOwnerRoomDto.getRoomName())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行房屋不能为空");
            }

            String[] item = importOwnerRoomDto.getRoomName().split("-",3);
            if(item.length != 3){
                throw new IllegalArgumentException((roomIndex + 1) + "行房屋格式错误 必须为 楼栋-单元-房屋");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getAddress())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行表号不能为空");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getMachineModel())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行模式不能为空");
            }
            if (StringUtil.isEmpty(importOwnerRoomDto.getFeeName())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行费用项不能为空");
            }
            if (StringUtil.isEmpty(importOwnerRoomDto.getMeterType())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行表类型不能为空");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getImplBean())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行厂家不能为空");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getMachineName())) {
                throw new IllegalArgumentException((roomIndex + 1) + "行名称不能为空");
            }
        }
    }
}
