package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetImport.IAssetImportSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.intf.job.IUserImportDataV1InnerServiceSMO;
import com.java110.po.log.AssetImportLogDetailPo;
import com.java110.po.log.AssetImportLogPo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import com.java110.api.importData.IImportDataCleaningAdapt;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("assetImportSMOImpl")
public class AssetImportSMOImpl extends DefaultAbstractComponentSMO implements IAssetImportSMO {
    private final static Logger logger = LoggerFactory.getLogger(AssetImportSMOImpl.class);

    /**
     * 导入最大行数
     */
    public static final int MAX_LINE = 2000;

    /**
     * 导入最大行数
     */
    public static final int DEFAULT_ROWS = 200;


    @Autowired
    private RestTemplate restTemplate;


    private IImportDataCleaningAdapt importDataCleaningAdapt;

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    @Autowired
    private IUserImportDataV1InnerServiceSMO userImportDataV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("userId", pd.getUserId());
        Assert.hasKeyAndValue(paramIn, "importAdapt", "未包含模板");
        String importAdapt = paramIn.getString("importAdapt") + "DataCleaning";

        importDataCleaningAdapt = ApplicationContextFactory.getBean(importAdapt, IImportDataCleaningAdapt.class);

        if (importDataCleaningAdapt == null) {
            throw new IllegalArgumentException("适配器没有实现" + importAdapt);
        }
        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
            paramIn.put("storeId", result.getStoreId());

            Workbook workbook = null;  //工作簿
            //工作表
            String[] headers = null;   //表头信息
            workbook = ImportExcelUtils.createWorkbook(uploadFile);

            // todo 适配器封装数据
            List datas = importDataCleaningAdapt.analysisExcel(workbook, paramIn, result);

            if (datas == null || datas.size() > MAX_LINE) {
                throw new IllegalArgumentException("数据为空，或者数据行数大于" + MAX_LINE);
            }

            // 保存数据
            return saveLogAndImportData(pd, datas, result, paramIn.getString("importAdapt"));
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 处理ExcelData数据
     *
     * @param datas 数据
     */
    private ResponseEntity<String> saveLogAndImportData(IPageData pd,
                                                 List datas,
                                                 ComponentValidateResult result, String logType) {
        ResponseEntity<String> responseEntity = null;

        String logId = GenerateCodeFactory.getGeneratorId("10");

        AssetImportLogPo assetImportLogPo = new AssetImportLogPo();
        assetImportLogPo.setCommunityId(result.getCommunityId());
        assetImportLogPo.setLogId(logId);
        assetImportLogPo.setLogType(logType);
        assetImportLogPo.setErrorCount("0");
        assetImportLogPo.setSuccessCount("0");
        assetImportLogInnerServiceSMOImpl.saveAssetImportLog(assetImportLogPo);

        List<AssetImportLogDetailPo> assetImportLogDetailPos = new ArrayList<>();
        AssetImportLogDetailPo assetImportLogDetailPo = null;
        int flag = 0;
        Calendar createTimeCal = Calendar.getInstance();
        for (Object data : datas) {
            createTimeCal.add(Calendar.SECOND,1);
            assetImportLogDetailPo = new AssetImportLogDetailPo();
            assetImportLogDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
            assetImportLogDetailPo.setLogId(logId);
            assetImportLogDetailPo.setState(AssetImportLogDetailDto.STATE_WAIT_IMPORT);
            assetImportLogDetailPo.setMessage("待导入");
            assetImportLogDetailPo.setCommunityId(result.getCommunityId());
            assetImportLogDetailPo.setContent(JSONObject.toJSONString(data));
            assetImportLogDetailPo.setCreateTime(DateUtil.getFormatTimeStringA(createTimeCal.getTime()));
            assetImportLogDetailPos.add(assetImportLogDetailPo);
            if (assetImportLogDetailPos.size() > DEFAULT_ROWS) {
                flag = assetImportLogDetailInnerServiceSMOImpl.saveAssetImportLogDetails(assetImportLogDetailPos);
                if (flag < 1) {
                    throw new IllegalArgumentException("保存失败");
                }
                assetImportLogDetailPos = new ArrayList<>();
            }
        }

        if (assetImportLogDetailPos.size() > 0) {
            flag = assetImportLogDetailInnerServiceSMOImpl.saveAssetImportLogDetails(assetImportLogDetailPos);
            if (flag < 1) {
                throw new IllegalArgumentException("保存失败");
            }
        }

        // todo 调用 导入队列开始导入
        flag = userImportDataV1InnerServiceSMOImpl.importExcelData(assetImportLogPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(assetImportLogPo);
        }
        return ResultVo.error("导入失败");
    }



}
