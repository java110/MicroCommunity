package com.java110.api.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importData.ImportOwnerRoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import com.java110.api.importData.DefaultImportDataAdapt;
import com.java110.api.importData.IImportDataCleaningAdapt;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 房产数据导入处理类
 *
 * 导入请求参数中必须包含
 * param.append('importAdapt', "importRoomOwner");
 */
@Service("importRoomOwnerDataCleaning")
public class ImportRoomOwnerDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {
    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception{

        List<ImportOwnerRoomDto> ownerRooms = new ArrayList<ImportOwnerRoomDto>();
        //封装对象
        getOwnerRooms(workbook, ownerRooms, result);
        //数据格式校验
        validateRoomInfo(ownerRooms);


        return ownerRooms;
    }

    /**
     * 获取业主车辆信息
     *
     * @param workbook
     * @param ownerRoomDtos
     */
    private void getOwnerRooms(Workbook workbook, List<ImportOwnerRoomDto> ownerRoomDtos, ComponentValidateResult result) throws ParseException {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "资产信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportOwnerRoomDto importOwnerRoomDto = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {

            Object[] os = oList.get(osIndex);

            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (os == null || StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行楼号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行单元不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行房屋不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行楼层不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行总楼层不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行是否有电梯不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行房屋类型不能为空");
            Assert.hasValue(os[7], (osIndex + 1) + "行房屋户型不能为空");
            Assert.hasValue(os[8], (osIndex + 1) + "行建筑面积不能为空");
            Assert.hasValue(os[9], (osIndex + 1) + "行室内面积不能为空");
            Assert.hasValue(os[10], (osIndex + 1) + "行租金不能为空");
            Assert.hasValue(os[11], (osIndex + 1) + "行房屋状态不能为空");
            if (os.length > 12 && !StringUtil.isNullOrNone(os[12])) {
                Assert.hasValue(os[12], (osIndex + 1) + "行业主名称不能为空");
                Assert.hasValue(os[13], (osIndex + 1) + "行性别不能为空");
                // Assert.hasValue(os[14], (osIndex + 1) + "行年龄不能为空");
                Assert.hasValue(os[14], (osIndex + 1) + "行手机号不能为空");
                //Assert.hasValue(os[15], (osIndex + 1) + "行身份证不能为空");
                Assert.hasValue(os[16], (osIndex + 1) + "行业主类型不能为空");
            }

            importOwnerRoomDto = new ImportOwnerRoomDto();
            importOwnerRoomDto.setCommunityId(result.getCommunityId());
            importOwnerRoomDto.setUserId(result.getUserId());
            importOwnerRoomDto.setFloorNum(os[0].toString().trim());
            importOwnerRoomDto.setUnitNum(os[1].toString().trim());
            importOwnerRoomDto.setRoomNum(os[2].toString().trim());
            importOwnerRoomDto.setLayer(os[3].toString().trim());
            importOwnerRoomDto.setLayerCount(os[4].toString().trim());
            //importOwnerRoomDto.setLift(os[5].toString().trim());
            importOwnerRoomDto.setLift("有".equals(os[5].toString().trim()) ? "1010" : "2020");
            importOwnerRoomDto.setRoomSubType(os[6].toString().trim());
            importOwnerRoomDto.setSection(os[7].toString().trim());
            importOwnerRoomDto.setBuiltUpArea(os[8].toString().trim());
            importOwnerRoomDto.setRoomArea(os[9].toString().trim());
            importOwnerRoomDto.setRoomRent(os[10].toString().trim());
            importOwnerRoomDto.setRoomState(os[11].toString().trim());
            if (os.length > 12 && !StringUtil.isNullOrNone(os[12])) {
                importOwnerRoomDto.setOwnerName(os[12].toString().trim());
                importOwnerRoomDto.setSex("男".equals(os[13].toString().trim()) ? "0" : "1");
                // String age = StringUtil.isNullOrNone(os[14]) ? CommonUtil.getAgeByCertId(os[16].toString().trim()) : os[14].toString().trim();
                importOwnerRoomDto.setAge("1");
                importOwnerRoomDto.setTel(os[14].toString().trim());
                String idCard = StringUtil.isNullOrNone(os[15]) ? "" : os[15].toString().trim();
                importOwnerRoomDto.setIdCard(idCard);
                importOwnerRoomDto.setOwnerTypeCd(os[16].toString().trim());
            }


            ownerRoomDtos.add(importOwnerRoomDto);

        }
    }


    /**
     * 数据格式校验
     *
     * @param ownerRooms
     */
    private void validateRoomInfo(List<ImportOwnerRoomDto> ownerRooms) {
        ImportOwnerRoomDto importOwnerRoomDto = null;
        ImportOwnerRoomDto tmpImportOwnerRoomDto = null;
        boolean hasOwnerType = false;
        for (int roomIndex = 0; roomIndex < ownerRooms.size(); roomIndex++) {
            importOwnerRoomDto = ownerRooms.get(roomIndex);
            // 1、楼栋单元房屋 中不支持 -  #  ？ & 等特殊符号
            if (hasSpecialCharacters(importOwnerRoomDto.getFloorNum())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行楼栋中包含特殊符号  -  #  ？ & 请删除！");
            }
            if (hasSpecialCharacters(importOwnerRoomDto.getUnitNum())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行单元中包含特殊符号  -  #  ？ & 请删除！");
            }
            if (hasRoomSpecialCharacters(importOwnerRoomDto.getRoomNum())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行单元中包含特殊符号  -  #  ？ & 请删除！");
            }

//            if (!StringUtil.isNumber(importOwnerRoomDto.getLayer())) {
//                throw new IllegalArgumentException((roomIndex + 1) + "行楼层不是有效数字");
//            }

            if (!StringUtil.isNumber(importOwnerRoomDto.getLayerCount())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行总楼层不是有效数字");
            }
            if (!StringUtil.isNumber(importOwnerRoomDto.getRoomSubType())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行房屋类型不是有效数字");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getSection())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行房屋户型不能为空");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getBuiltUpArea())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行建筑面积不能为空");
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getRoomArea())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行室内面积不能为空");
            }
            if (StringUtil.isEmpty(importOwnerRoomDto.getRoomRent())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行租金不能为空");
            }
            // 如果为空说明 房屋目前是空闲没有业主
            if (StringUtil.isEmpty(importOwnerRoomDto.getOwnerName())) {
                continue;
            }

            if (StringUtil.isEmpty(importOwnerRoomDto.getSex())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行性别不能为空");
            }

//            if (StringUtil.isEmpty(importOwnerRoomDto.getAge())) {
//                throw new IllegalArgumentException((roomIndex + 1) + "行年龄不能为空");
//            }
            //系统目前 在香港台湾 以及新加坡等地都有商用 所以 并不是中国大陆手机号规则 所以不做严格校验
            //烦请专业测试团队勿喷
            if (StringUtil.isEmpty(importOwnerRoomDto.getTel())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行手机号不能为空");
            }

            if (importOwnerRoomDto.getTel().length() > 11) {
                throw new IllegalArgumentException((roomIndex + 2) + "行手机号超过11位,请核实");
            }

//            if (StringUtil.isEmpty(importOwnerRoomDto.getIdCard())) {
//                throw new IllegalArgumentException((roomIndex + 1) + "行身份证号不能为空");
//            }

            if (!StringUtil.isEmpty(importOwnerRoomDto.getIdCard()) && importOwnerRoomDto.getIdCard().length() > 18) {
                throw new IllegalArgumentException((roomIndex + 2) + " 的身份证超过18位,请核实");
            }
            if (!StringUtil.isNumber(importOwnerRoomDto.getOwnerTypeCd())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行业主类型不能为空");
            }

            if (RoomDto.STATE_FREE.equals(importOwnerRoomDto.getRoomState()) && !StringUtil.isEmpty(importOwnerRoomDto.getOwnerName())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行房屋状态为未销售状态，不能包含业主信息");
            }

            if (!RoomDto.STATE_FREE.equals(importOwnerRoomDto.getRoomState()) && StringUtil.isEmpty(importOwnerRoomDto.getOwnerName())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行房屋状态不是未销售状态，必须包含业主信息");
            }

            // 如果是业主 跳过
            if (OwnerDto.OWNER_TYPE_CD_OWNER.equals(importOwnerRoomDto.getOwnerTypeCd())) {
                continue;
            }
            // 校验成员之前是否存在 业主角色
            hasOwnerType = false;
            for (int preRoomIndex = 0; preRoomIndex < roomIndex; preRoomIndex++) {
                tmpImportOwnerRoomDto = ownerRooms.get(preRoomIndex);

                if (tmpImportOwnerRoomDto.getFloorNum().equals(importOwnerRoomDto.getFloorNum())
                        && tmpImportOwnerRoomDto.getUnitNum().equals(importOwnerRoomDto.getUnitNum())
                        && tmpImportOwnerRoomDto.getRoomNum().equals(importOwnerRoomDto.getRoomNum())
                        && OwnerDto.OWNER_TYPE_CD_OWNER.equals(tmpImportOwnerRoomDto.getOwnerTypeCd())) {
                    hasOwnerType = true;
                    break;
                }
            }

            if (!hasOwnerType) {
                throw new IllegalArgumentException((roomIndex + 2) + "行一个房屋必须要有业主存在 才能 写家庭成员和房屋租客 和临时人员，并且业主要写在 其他之前");
            }
        }
    }
}
