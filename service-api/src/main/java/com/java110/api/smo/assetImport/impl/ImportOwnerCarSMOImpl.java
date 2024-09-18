package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetImport.IImportOwnerCarSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.community.*;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.parking.ParkingAreaPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("importOwnerCarSMOImpl")
public class ImportOwnerCarSMOImpl extends DefaultAbstractComponentSMO implements IImportOwnerCarSMO {

    private final static Logger logger = LoggerFactory.getLogger(ImportOwnerCarSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {
        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
            //InputStream is = uploadFile.getInputStream();
            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿
            List<OwnerCarDto> ownerCars = new ArrayList<OwnerCarDto>();
            //获取楼信息
            getOwnerCars(workbook, ownerCars);
            // 保存数据
            return dealExcelData(pd, ownerCars, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取业主车辆信息
     *
     * @param workbook
     * @param ownerCarDtos
     */
    private void getOwnerCars(Workbook workbook, List<OwnerCarDto> ownerCarDtos) throws ParseException {
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            importOwnerCar.setStartTime(simpleDateFormat.parse(startTime));
            importOwnerCar.setEndTime(simpleDateFormat.parse(endTime));
            importOwnerCar.setTypeCd(os[10].toString().trim());
            importOwnerCar.setSpaceSate(os[11].toString().trim());
            ownerCarDtos.add(importOwnerCar);


        }
    }


    /**
     * 处理ExcelData数据
     */
    private ResponseEntity<String> dealExcelData(IPageData pd, List<OwnerCarDto> ownerCarDtos, ComponentValidateResult result) {
        ResponseEntity<String> responseEntity = null;
        //保存单元信息 和 楼栋信息
        responseEntity = savedOwnerCars(pd, ownerCarDtos, result);
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        return responseEntity;
    }

    private ResponseEntity<String> savedOwnerCars(IPageData pd, List<OwnerCarDto> ownerCars, ComponentValidateResult result) {
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        if (ownerCars.size() < 1) {
            throw new IllegalArgumentException("没有数据需要处理");
        }
        String psId = "";
        String paId = "";

        validateOwnerData(ownerCars, reqJson);
        for (OwnerCarDto ownerCarDto : ownerCars) {
            OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(ownerCarDto, OwnerCarPo.class);
            //获取房屋名称
            ownerCarPo.setCarTypeCd("1001"); //主车辆
            ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
            parkingAreaDto.setNum(ownerCarDto.getAreaNum());
            parkingAreaDto.setTypeCd(ownerCarDto.getTypeCd());
            parkingAreaDto.setCommunityId(result.getCommunityId());
            //查询停车场
            List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
            //Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
            if (parkingAreaDtos == null || parkingAreaDtos.size() < 1) {
                paId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_paId);
                ParkingAreaPo parkingAreaPo = new ParkingAreaPo();
                parkingAreaPo.setCommunityId(reqJson.getString("communityId"));
                parkingAreaPo.setNum(ownerCarDto.getAreaNum());
                parkingAreaPo.setPaId(paId);
                parkingAreaPo.setTypeCd(ownerCarDto.getTypeCd());
                parkingAreaPo.setRemark("导入数据");
                parkingAreaV1InnerServiceSMOImpl.saveParkingArea(parkingAreaPo);
            } else {
                paId = parkingAreaDtos.get(0).getPaId();
            }
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setNum(ownerCarDto.getNum());
            parkingSpaceDto.setPaId(paId);
            parkingSpaceDto.setCommunityId(result.getCommunityId());
            //查询停车位
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            String state = "";
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                psId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_psId);
                ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
                parkingSpacePo.setCommunityId(reqJson.getString("communityId"));
                parkingSpacePo.setNum(ownerCarDto.getNum());
                parkingSpacePo.setPaId(paId);
                parkingSpacePo.setArea("1");
                parkingSpacePo.setParkingType(ParkingSpaceDto.TYPE_CD_COMMON);
                parkingSpacePo.setState(ParkingSpaceDto.STATE_FREE);
                parkingSpacePo.setPsId(psId);
                parkingSpacePo.setRemark("导入数据");
                parkingSpaceV1InnerServiceSMOImpl.saveParkingSpace(parkingSpacePo);
                state = ParkingSpaceDto.STATE_FREE;
            } else {
                psId = parkingSpaceDtos.get(0).getPsId();
                //获取停车位状态(出售 S，出租 H ，空闲 F)
                state = parkingSpaceDtos.get(0).getState();
            }

            if (!StringUtil.isEmpty(state) && !state.equals("F")) {
                throw new IllegalArgumentException(ownerCarDto.getAreaNum() + "停车场-" + ownerCarDto.getNum() + "停车位不是空闲状态！");
            }
            ownerCarPo.setPsId(psId);
            ownerCarPo.setOwnerId(ownerCarDto.getOwnerId());
            ownerCarPo.setUserId("-1");
            ownerCarPo.setCommunityId(reqJson.getString("communityId"));
            ownerCarPo.setCarId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
            ownerCarPo.setMemberId(ownerCarPo.getCarId());
            ownerCarPo.setState("1001"); //1001 正常状态，2002 车位释放欠费状态，3003 车位释放
            ownerCarPo.setLeaseType(ownerCarDto.getSpaceSate());
            ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setPsId(psId); //车位id
            parkingSpacePo.setState(ownerCarDto.getSpaceSate());
            parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        }
        return ResultVo.success();
    }

    private void validateOwnerData(List<OwnerCarDto> ownerCars, JSONObject reqJson) {

        for (OwnerCarDto ownerCarDto : ownerCars) {

            if (!"1001".equals(ownerCarDto.getTypeCd()) && !"2001".equals(ownerCarDto.getTypeCd())) {
                throw new IllegalArgumentException(ownerCarDto.getCarNum() + "停车场类型应填写 1001(地上停车场)或者 2001 (地下停车场)");
            }
            if (!"H".equals(ownerCarDto.getSpaceSate()) && !"S".equals(ownerCarDto.getSpaceSate())) {
                throw new IllegalArgumentException(ownerCarDto.getCarNum() + "车位状态应填写 S（出售）或者 H （出租）");
            }

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setName(ownerCarDto.getOwnerName());
            ownerDto.setLink(ownerCarDto.getLink());
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            //查询业主
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            //Assert.listOnlyOne(ownerDtos, ownerCarDto.getCarNum() + "查询业主信息错误！");
            if (ownerDtos == null || ownerDtos.size() < 1) {
                throw new IllegalArgumentException(ownerCarDto.getCarNum() + "的业主不存在");
            }
            ownerCarDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        }

    }

    //解析Excel日期格式
    public static String excelDoubleToDate(String strDate) {
        if (strDate.length() == 5) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date tDate = DoubleToDate(Double.parseDouble(strDate));
                return sdf.format(tDate);
            } catch (Exception e) {
                e.printStackTrace();
                return strDate;
            }
        }
        return strDate;
    }

    //解析Excel日期格式
    public static Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return tDate;
    }
}
