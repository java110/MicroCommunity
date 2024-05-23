package com.java110.boot.components.assetImport;

import com.java110.boot.smo.assetExport.IExportOwnerCarSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加应用组件
 *
 * @author fqz
 * @date 2021-12-21
 */
@Component("importOwnerCar")
public class ImportOwnerCarComponent {

    private final static Logger logger = LoggerFactory.getLogger(ImportOwnerCarComponent.class);

    @Autowired
    private IExportOwnerCarSMO exportOwnerCarSMOImpl;



    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<Object> exportData(IPageData pd) throws Exception {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), AppDto.WEB_APP_ID, pd.getHeaders());
        return exportOwnerCarSMOImpl.exportExcelData(newPd);
    }

}
