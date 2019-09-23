package com.java110.web.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.web.controller.CallComponentController;
import com.java110.web.smo.app.IAddAppSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加应用组件
 */
@Component("assetImport")
public class AssetImportComponent {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);

    @Autowired
    private IAddAppSMO addAppSMOImpl;

    /**
     * 添加应用数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd){

        logger.debug("请求参数为：%s", pd.getReqData());
        return new ResponseEntity<String>("成功", HttpStatus.OK);
    }

    public IAddAppSMO getAddAppSMOImpl() {
        return addAppSMOImpl;
    }

    public void setAddAppSMOImpl(IAddAppSMO addAppSMOImpl) {
        this.addAppSMOImpl = addAppSMOImpl;
    }
}
