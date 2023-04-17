package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.meterWater.ImportExportMeterWaterDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.assetImport.IImportMeterWaterFeeSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("importMeterWaterFeeSMOImpl")
public class ImportMeterWaterFeeSMOImpl extends DefaultAbstractComponentSMO implements IImportMeterWaterFeeSMO {
    private final static Logger logger = LoggerFactory.getLogger(ImportMeterWaterFeeSMOImpl.class);

    private static final int DEFAULT_ADD_FEE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿

            List<ImportExportMeterWaterDto> rooms = new ArrayList<ImportExportMeterWaterDto>();

            //获取楼信息
            getRooms(workbook, rooms);
            // 保存数据
            return dealExcelData(pd, rooms, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> importExcelData2(IPageData pd, MultipartFile uploadFile) {
        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿

            List<ImportExportMeterWaterDto> rooms = new ArrayList<ImportExportMeterWaterDto>();

            //获取楼信息
            getRooms2(workbook, rooms);
            // 保存数据
            return dealExcelData(pd, rooms, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 处理ExcelData数据
     */
    private ResponseEntity<String> dealExcelData(IPageData pd,
                                                 List<ImportExportMeterWaterDto> rooms,

                                                 ComponentValidateResult result) {
        ResponseEntity<String> responseEntity = null;
        //保存单元信息 和 楼栋信息
        responseEntity = savedRoomFees(pd, rooms, result);

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        return responseEntity;
    }

    private ResponseEntity<String> savedRoomFees(IPageData pd, List<ImportExportMeterWaterDto> rooms, ComponentValidateResult result) {

        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        if (rooms.size() < 1) {
            throw new IllegalArgumentException("没有数据需要处理");
        }

        //生成批次
        JSONObject data = JSONObject.parseObject(pd.getReqData());
        data.put("userId", pd.getUserId());
        data.put("communityId", result.getCommunityId());
        generatorBatch(data);

        data.put("importFeeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        data.put("storeId", result.getStoreId());
        data.put("userId", result.getUserId());
        data.put("configId", reqJson.getString("configId"));
        data.put("feeTypeCd", reqJson.getString("feeTypeCd"));
        data.put("meterType", reqJson.getString("meterType"));

        List<ImportExportMeterWaterDto> tmpImportRoomFees = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {

            tmpImportRoomFees.add(rooms.get(roomIndex));

            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {

                createRoomFee(pd, tmpImportRoomFees, data);

                tmpImportRoomFees = new ArrayList<>();
            }
        }
        if (tmpImportRoomFees != null && tmpImportRoomFees.size() > 0) {

            createRoomFee(pd, tmpImportRoomFees, data);
        }
        return ResultVo.success();
    }

    /**
     * 创建费用
     *
     * @param pd
     * @param tmpImportRoomFees
     */
    private void createRoomFee(IPageData pd, List<ImportExportMeterWaterDto> tmpImportRoomFees, JSONObject data) {

        JSONArray importRoomFees = JSONArray.parseArray(JSONObject.toJSONString(tmpImportRoomFees));
        data.put("importMeteWaterFees", importRoomFees);

        String apiUrl = "/meterWater/importMeterWater";

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, data.toJSONString(), apiUrl, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        if (ResultVo.CODE_OK != paramOut.getInteger("code")) {
            throw new IllegalArgumentException(paramOut.getString("msg"));
        }

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
            Assert.hasValue(os[3], (osIndex + 1) + "上期度数不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "上期度数时间不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "本期度数不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "本期度数时间不能为空");

//

            String startTime = excelDoubleToDate(os[4].toString());
            String endTime = excelDoubleToDate(os[6].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行上期度数时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行本期度数时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportExportMeterWaterDto();
            importRoomFee.setFloorNum(os[0].toString());
            importRoomFee.setUnitNum(os[1].toString());
            importRoomFee.setRoomNum(os[2].toString());
            importRoomFee.setPreDegrees(os[3].toString());
            importRoomFee.setPreReadingTime(startTime);
            importRoomFee.setCurDegrees(os[5].toString());
            importRoomFee.setCurReadingTime(endTime);
            importRoomFee.setPrice(-1);
            rooms.add(importRoomFee);
            if (Double.parseDouble(importRoomFee.getCurDegrees()) < Double.parseDouble(importRoomFee.getPreDegrees())) {
                throw new IllegalArgumentException((osIndex + 1) + "行本期读数小于上期读数");
            }
        }
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms2(Workbook workbook, List<ImportExportMeterWaterDto> rooms) {
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

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }


    //解析Excel日期格式
    public static String excelDoubleToDate(String strDate) {
        if (strDate.length() == 5) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
