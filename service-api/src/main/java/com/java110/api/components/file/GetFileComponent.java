package com.java110.api.components.file;


import com.java110.core.context.IPageData;
import com.java110.api.smo.file.IGetFileByObjIdSMO;
import com.java110.api.smo.file.IGetFileSMO;
import com.java110.core.context.PageData;
import com.java110.dto.app.AppDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("getFile")
public class GetFileComponent {

    @Autowired
    private IGetFileSMO getFileSMOImpl;

    @Autowired
    private IGetFileByObjIdSMO getFileByObjIdSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<Object> file(IPageData pd) throws IOException {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(),
                pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(),
                "", pd.getSessionId(), AppDto.WEB_APP_ID,pd.getHeaders());
        return getFileSMOImpl.getFile(newPd);
    }

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<Object> fileByObjId(IPageData pd) throws IOException {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(),
                pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(),
                "", pd.getSessionId(), AppDto.WEB_APP_ID,pd.getHeaders());
        return getFileByObjIdSMOImpl.getFileByObjId(newPd);
    }

    public IGetFileByObjIdSMO getGetFileByObjIdSMOImpl() {
        return getFileByObjIdSMOImpl;
    }

    public void setGetFileByObjIdSMOImpl(IGetFileByObjIdSMO getFileByObjIdSMOImpl) {
        this.getFileByObjIdSMOImpl = getFileByObjIdSMOImpl;
    }

    public IGetFileSMO getGetFileSMOImpl() {
        return getFileSMOImpl;
    }

    public void setGetFileSMOImpl(IGetFileSMO getFileSMOImpl) {
        this.getFileSMOImpl = getFileSMOImpl;
    }
}
