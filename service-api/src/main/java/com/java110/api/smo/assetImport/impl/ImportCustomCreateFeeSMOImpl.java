package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetImport.IImportCustomCreateFeeSMO;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.assetImport.ImportCustomCreateFeeDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
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
@Service("importCustomCreateFeeSMOImpl")
public class ImportCustomCreateFeeSMOImpl extends DefaultAbstractComponentSMO implements IImportCustomCreateFeeSMO {
    private final static Logger logger = LoggerFactory.getLogger(ImportCustomCreateFeeSMOImpl.class);

    private static final int DEFAULT_ADD_FEE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> importCustomExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
            Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");

            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿


            List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = new ArrayList<ImportCustomCreateFeeDto>();
            //获取楼信息
            getImportCustomCreateFeeDtos(workbook, importCustomCreateFeeDtos, result);
            // 保存数据
            return dealExcelData(pd, importCustomCreateFeeDtos, result);

        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 处理ExcelData数据
     *
     * @param importCustomCreateFeeDtos 房屋费用
     */
    private ResponseEntity<String> dealExcelData(IPageData pd,
                                                 List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos,
                                                 ComponentValidateResult result) {
        ResponseEntity<String> responseEntity = null;
        //保存单元信息 和 楼栋信息
        if (importCustomCreateFeeDtos.size() < 1) {
            throw new IllegalArgumentException("没有数据需要处理");
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("successCount", 0);
        paramOut.put("errorCount", 0);
        //生成批次
        String batchId = generatorBatch(result.getCommunityId(), result.getUserId());

        List<ImportCustomCreateFeeDto> tmpImportCustomCreateFeeDtos = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < importCustomCreateFeeDtos.size(); roomIndex++) {
            tmpImportCustomCreateFeeDtos.add(importCustomCreateFeeDtos.get(roomIndex));
            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {
                // 处理房屋费用
                doImportRoomCreateFee(tmpImportCustomCreateFeeDtos, batchId, result);
                doImportCarCreateFee(tmpImportCustomCreateFeeDtos, batchId, result);

                tmpImportCustomCreateFeeDtos = new ArrayList<>();
            }
        }
        if (tmpImportCustomCreateFeeDtos != null && tmpImportCustomCreateFeeDtos.size() > 0) {

            doImportRoomCreateFee(tmpImportCustomCreateFeeDtos, batchId, result);
            doImportCarCreateFee(tmpImportCustomCreateFeeDtos, batchId, result);

        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "成功");
    }

    /**
     * 创建房屋费用
     *
     * @param importCustomCreateFeeDtos
     * @param batchId
     */
    private void doImportCarCreateFee(List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos, String batchId, ComponentValidateResult result) {
        int successCount = 0;
        int errorCount = 0;
        List<ImportCustomCreateFeeDto> cars = new ArrayList<>();

        for (ImportCustomCreateFeeDto importCustomCreateFeeDto : importCustomCreateFeeDtos) {
            if (!ImportCustomCreateFeeDto.TYPE_CAR.equals(importCustomCreateFeeDto.getObjType())) {
                continue;
            }
            importCustomCreateFeeDto.setCarNum(importCustomCreateFeeDto.getObjName());

            cars.add(importCustomCreateFeeDto);
        }

        if (cars.size() < 1) {
            return;
        }

        importCustomCreateFeeDtos = ownerCarInnerServiceSMOImpl.freshCarIdsByImportCustomCreateFee(cars);

        doCreateFeeAndAttrs(importCustomCreateFeeDtos, batchId, result, successCount, errorCount);
    }


    /**
     * 创建房屋费用
     *
     * @param importCustomCreateFeeDtos
     * @param batchId
     */
    private void doImportRoomCreateFee(List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos, String batchId, ComponentValidateResult result) {
        int successCount = 0;
        int errorCount = 0;
        List<ImportCustomCreateFeeDto> rooms = new ArrayList<>();
        String[] objNames;
        for (ImportCustomCreateFeeDto importCustomCreateFeeDto : importCustomCreateFeeDtos) {
            if (!ImportCustomCreateFeeDto.TYPE_ROOM.equals(importCustomCreateFeeDto.getObjType())) {
                continue;
            }
            objNames = importCustomCreateFeeDto.getObjName().split("-");
            if (objNames.length != 3) {
                continue;
            }
            importCustomCreateFeeDto.setFloorNum(objNames[0]);
            importCustomCreateFeeDto.setUnitNum(objNames[1]);
            importCustomCreateFeeDto.setRoomNum(objNames[2]);
            rooms.add(importCustomCreateFeeDto);
        }

        if (rooms.size() < 1) {
            return;
        }

        importCustomCreateFeeDtos = roomInnerServiceSMOImpl.freshRoomIdsByImportCustomCreateFee(rooms);
        List<String> roomIds = new ArrayList<>();
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            roomIds.add(importRoomFee.getPayObjId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importCustomCreateFeeDtos.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (importRoomFee.getPayObjId().equals(tmpOwnerDto.getRoomId())) {
                    importRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    importRoomFee.setOwnerName(tmpOwnerDto.getName());
                    importRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }
        doCreateFeeAndAttrs(importCustomCreateFeeDtos, batchId, result, successCount, errorCount);
    }


    private void doCreateFeeAndAttrs(List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos, String batchId, ComponentValidateResult result, int successCount, int errorCount) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            if (StringUtil.isEmpty(importRoomFee.getPayObjId())) {
                errorCount++;
                continue;
            }
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setCommunityId(importRoomFee.getCommunityId());
            feeConfigDto.setConfigId(importRoomFee.getConfigId());
            List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
            if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
                continue;
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importRoomFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(importRoomFee.getCommunityId());
            payFeePo.setConfigId(importRoomFee.getConfigId());
            payFeePo.setPayerObjId(importRoomFee.getPayObjId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setUserId(result.getUserId());
            payFeePo.setIncomeObjId(result.getStoreId());
            payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
            payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
            payFeePo.setAmount("-1");
            payFeePo.setBatchId(batchId);
            payFeePo.setEndTime(importRoomFee.getStartTime());
            payFeePo.setStartTime(importRoomFee.getCreateTime());

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(feeConfigDtos.get(0).getFeeName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importRoomFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importRoomFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importRoomFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importRoomFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }
        }

        if (payFeePos.size() < 1) {
            return;
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
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


            importRoomFee = new ImportCustomCreateFeeDto();
            importRoomFee.setObjName(os[0].toString());
            importRoomFee.setObjType(os[1].toString());
            importRoomFee.setConfigId(os[2].toString());
            importRoomFee.setConfigName(os[3].toString());
            importRoomFee.setStartTime(startTime);
            importRoomFee.setCreateTime(createTime);
            importRoomFee.setCommunityId(result.getCommunityId());
            importCustomCreateFeeDtos.add(importRoomFee);
        }
    }

    /**
     * 生成批次号
     *
     * @param userId
     */
    private String generatorBatch(String communityId, String userId) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(communityId);
        payFeeBatchPo.setCreateUserId(userId);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        return payFeeBatchPo.getBatchId();
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
