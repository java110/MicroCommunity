package com.java110.api.components.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.file.IAddFileSMO;
import com.java110.api.smo.notice.IAddNoticeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加公告组件
 */
@Component("addNoticeView")
public class AddNoticeViewComponent {

    @Autowired
    private IAddNoticeSMO addNoticeSMOImpl;

    @Autowired
    private IAddFileSMO addFileSMOImpl;

    /**
     * 添加公告数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addNoticeSMOImpl.saveNotice(pd);
    }

    /**
     * 上传图片
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> uploadImage(IPageData pd, MultipartFile uploadFile) throws Exception {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("suffix", "jpeg");
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(),pd.getToken(), paramIn.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(),
                "", pd.getSessionId(),pd.getAppId(),pd.getHeaders());
        return addFileSMOImpl.saveFile(newPd, uploadFile);
    }

    public IAddNoticeSMO getAddNoticeSMOImpl() {
        return addNoticeSMOImpl;
    }

    public void setAddNoticeSMOImpl(IAddNoticeSMO addNoticeSMOImpl) {
        this.addNoticeSMOImpl = addNoticeSMOImpl;
    }

    public IAddFileSMO getAddFileSMOImpl() {
        return addFileSMOImpl;
    }

    public void setAddFileSMOImpl(IAddFileSMO addFileSMOImpl) {
        this.addFileSMOImpl = addFileSMOImpl;
    }
}
