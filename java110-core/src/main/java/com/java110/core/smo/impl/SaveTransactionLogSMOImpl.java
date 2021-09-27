package com.java110.core.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.smo.ISaveTransactionLogSMO;
import com.java110.dto.app.AppDto;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.utils.constant.ServiceConstant;
import com.sun.deploy.association.utility.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName SaveTransactionLogSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:43
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
@Service
public class SaveTransactionLogSMOImpl implements ISaveTransactionLogSMO {

    @Autowired(required = false)
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Override
    @Async
    public void saveLog(TransactionLogPo transactionLogPo) {
        transactionLogInnerServiceSMOImpl.saveTransactionLog(transactionLogPo);
    }

    @Override
    @Async
    public void saveAssetImportLog(AssetImportLogDto assetImportLogDto) {
        String apiUrl =  "http://api-service/api/assetImportLog/saveAssetImportLog";
        IPageData newPd = PageData.newInstance().builder("-1", "批量日志", "", "",
                "", "", apiUrl, "",
                AppDto.WEB_APP_ID);

        CallApiServiceFactory.callCenterService(restTemplate, newPd, JSONObject.toJSONString(assetImportLogDto), apiUrl, HttpMethod.POST);
    }
}
