package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.ISaveTransactionLogSMO;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.assetImport.IImportFeeDetailSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
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
@Service("importFeeDetailSMOImpl")
public class ImportFeeDetailSMOImpl extends DefaultAbstractComponentSMO implements IImportFeeDetailSMO {
    private final static Logger logger = LoggerFactory.getLogger(ImportFeeDetailSMOImpl.class);

    private static final int DEFAULT_ADD_FEE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISaveTransactionLogSMO saveTransactionLogSMOImpl;


    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
            Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");
            Assert.hasKeyAndValue(paramIn, "objType", "请求中未包含费用对象");
            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿

            List<ImportRoomFee> rooms = new ArrayList<ImportRoomFee>();
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(paramIn.getString("objType"))) {
                //获取楼信息
                getRooms(workbook, rooms);
            } else {
                //获取楼信息
                getCars(workbook, rooms);
            }
            // 保存数据
            return dealExcelData(pd, rooms, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void getCars(Workbook workbook, List<ImportRoomFee> rooms) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "车辆缴费历史");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportRoomFee importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            //费用名称没有填写，默认跳过
            if (StringUtil.isNullOrNone(os[4])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行车牌号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行收费项目不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行缴费周期不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行开始时间不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行结束时间不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行缴费时间不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行缴费金额不能为空");

//

            String startTime = excelDoubleToDate(os[3].toString());
            String endTime = excelDoubleToDate(os[4].toString());
            String createTime = excelDoubleToDate(os[5].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(createTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportRoomFee();
            importRoomFee.setCarNum(os[0].toString());
            importRoomFee.setFeeName(os[1].toString());
            importRoomFee.setCycle(os[2].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setEndTime(endTime);
            importRoomFee.setCreateTime(createTime);
            importRoomFee.setAmount(os[6].toString());
            importRoomFee.setRemark(!StringUtil.isNullOrNone(os[7]) ? os[7].toString() : "");
            rooms.add(importRoomFee);
        }
    }

    /**
     * 处理ExcelData数据
     *
     * @param roomFees 房屋费用
     */
    private ResponseEntity<String> dealExcelData(IPageData pd,
                                                 List<ImportRoomFee> roomFees,

                                                 ComponentValidateResult result) {
        ResponseEntity<String> responseEntity = null;
        //保存单元信息 和 楼栋信息
        responseEntity = savedRoomFees(pd, roomFees, result);

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        return responseEntity;
    }

    private ResponseEntity<String> savedRoomFees(IPageData pd, List<ImportRoomFee> roomFees, ComponentValidateResult result) {

        if (roomFees.size() < 1) {
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
        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_HISTORY_FEE_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        try {
            List<ImportRoomFee> tmpImportRoomFees = new ArrayList<>();
            for (int roomIndex = 0; roomIndex < roomFees.size(); roomIndex++) {

                tmpImportRoomFees.add(roomFees.get(roomIndex));

                if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {

                    createRoomFee(pd, tmpImportRoomFees, data, assetImportLogDto);

                    tmpImportRoomFees = new ArrayList<>();
                }
            }

            if (tmpImportRoomFees != null && tmpImportRoomFees.size() > 0) {

                createRoomFee(pd, tmpImportRoomFees, data, assetImportLogDto);
            }
        } finally {
            //存入导入日志
            saveImportLog(assetImportLogDto);
        }
        return ResultVo.success();
    }

    /**
     * 保存日志
     *
     * @param assetImportLogDto
     */
    private void saveImportLog(AssetImportLogDto assetImportLogDto) {
        saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
    }

    /**
     * 创建费用
     *
     * @param pd
     * @param tmpImportRoomFee
     */
    private void createRoomFee(IPageData pd, List<ImportRoomFee> tmpImportRoomFee, JSONObject data, AssetImportLogDto assetImportLogDto) {
        Long successCount = assetImportLogDto.getSuccessCount();
        Long failCount = assetImportLogDto.getErrorCount();

        List<AssetImportLogDetailDto> assetImportLogDetailDtos = assetImportLogDto.getAssetImportLogDetailDtos();
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        JSONArray importRoomFees = JSONArray.parseArray(JSONObject.toJSONString(tmpImportRoomFee));
        data.put("importRoomFees", importRoomFees);

        String apiUrl = "/payFeeDetail/importPayFeeDetail";

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, data.toJSONString(), apiUrl, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            failCount += importRoomFees.size();
            assetImportLogDetailDto = new AssetImportLogDetailDto();
            assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
            if (Assert.isJsonObject(responseEntity.getBody())) {
                JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                assetImportLogDetailDto.setState(paramOut.getString("code"));
                assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
            } else {
                assetImportLogDetailDto.setState("F");
                assetImportLogDetailDto.setMessage(responseEntity.getBody());
            }
            assetImportLogDetailDto.setObjName("无");
            assetImportLogDetailDtos.add(assetImportLogDetailDto);
        } else {
            successCount += importRoomFees.size();
        }

        assetImportLogDto.setSuccessCount(successCount);
        assetImportLogDto.setErrorCount(failCount);
    }


    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms(Workbook workbook, List<ImportRoomFee> rooms) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "房屋缴费历史");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportRoomFee importRoomFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            //费用名称没有填写，默认跳过
            if (StringUtil.isNullOrNone(os[4])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行楼栋编号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行单元编号不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行房屋编号不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行收费项目不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行缴费周期不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行开始时间不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行结束时间不能为空");
            Assert.hasValue(os[7], (osIndex + 1) + "行缴费时间不能为空");
            Assert.hasValue(os[8], (osIndex + 1) + "行缴费金额不能为空");

//

            String startTime = excelDoubleToDate(os[5].toString());
            String endTime = excelDoubleToDate(os[6].toString());
            String createTime = excelDoubleToDate(os[7].toString());
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(createTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");


            importRoomFee = new ImportRoomFee();
            importRoomFee.setFloorNum(os[0].toString());
            importRoomFee.setUnitNum(os[1].toString());
            importRoomFee.setRoomNum(os[2].toString());
            importRoomFee.setFeeName(os[3].toString());
            importRoomFee.setCycle(os[4].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setEndTime(endTime);
            importRoomFee.setCreateTime(createTime);
            importRoomFee.setAmount(os[8].toString());
            importRoomFee.setRemark(!StringUtil.isNullOrNone(os[9]) ? os[9].toString() : "");
            rooms.add(importRoomFee);
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
