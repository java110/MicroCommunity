package com.java110.front.smo.assetImport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.assetImport.IImportRoomFeeSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("importRoomFeeSMOImpl")
public class ImportRoomFeeSMOImpl extends BaseComponentSMO implements IImportRoomFeeSMO {
    private final static Logger logger = LoggerFactory.getLogger(ImportRoomFeeSMOImpl.class);

    private static final int DEFAULT_ADD_FEE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = ImportExcelUtils.createWorkbook(uploadFile);  //工作簿

            List<ImportRoomFee> rooms = new ArrayList<ImportRoomFee>();

            //获取楼信息
            getRooms(workbook, rooms);
            // 保存数据
            return dealExcelData(pd, rooms, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
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

        JSONObject data = JSONObject.parseObject(pd.getReqData());
        data.put("importFeeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        data.put("storeId", result.getStoreId());
        data.put("userId", result.getUserId());

        List<ImportRoomFee> tmpImportRoomFees = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < roomFees.size(); roomIndex++) {

            tmpImportRoomFees.add(roomFees.get(roomIndex));

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
     * @param tmpImportRoomFee
     */
    private void createRoomFee(IPageData pd, List<ImportRoomFee> tmpImportRoomFee, JSONObject data) {

        JSONArray importRoomFees = JSONArray.parseArray(JSONObject.toJSONString(tmpImportRoomFee));
        data.put("importRoomFees", importRoomFees);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/feeApi/importRoomFees";

        this.callCenterService(restTemplate, pd, data.toJSONString(), apiUrl, HttpMethod.POST);
    }


    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms(Workbook workbook, List<ImportRoomFee> rooms) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "房屋信息");
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
            Assert.hasValue(os[1], (osIndex + 1) + "行单元编号不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行房屋编号不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行费用名称不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行开始时间不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行结束时间不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行费用不能为空");

            Assert.isDate(os[4].toString(), DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(os[5].toString(), DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");

            importRoomFee = new ImportRoomFee();
            importRoomFee.setFloorNum(os[0].toString());
            importRoomFee.setUnitNum(os[1].toString());
            importRoomFee.setRoomNum(os[2].toString());
            importRoomFee.setFeeName(os[3].toString());
            importRoomFee.setStartTime(os[4].toString());
            importRoomFee.setEndTime(os[5].toString());
            importRoomFee.setAmount(os[6].toString());
            rooms.add(importRoomFee);
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
