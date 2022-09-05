package com.java110.api.components.uploadFile;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.file.IAddFileSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加活动组件
 */
@Component("uploadFile")
public class UploadFileComponent {

    @Autowired
    private IAddFileSMO addFileSMOImpl;

    /**
     * 上传图片
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> uploadImage(IPageData pd, MultipartFile uploadFile) throws Exception {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("suffix", "jpeg");
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), paramIn.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), pd.getAppId(), pd.getHeaders());
        return addFileSMOImpl.saveFile(newPd, uploadFile);
    }

    /**
     * 上传图片
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> uploadPhotoImage(IPageData pd) throws Exception {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("suffix", "jpeg");
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), paramIn.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), pd.getAppId(), pd.getHeaders());
        return addFileSMOImpl.savePhotoFile(newPd);
    }

}
