package com.java110.front.components.file;


import com.java110.core.context.IPageData;
import com.java110.front.smo.file.IGetFileByObjIdSMO;
import com.java110.front.smo.file.IGetFileSMO;
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
        return getFileSMOImpl.getFile(pd);
    }

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<Object> fileByObjId(IPageData pd) throws IOException {
        return getFileByObjIdSMOImpl.getFileByObjId(pd);
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
