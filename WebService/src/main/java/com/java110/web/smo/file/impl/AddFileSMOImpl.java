package com.java110.web.smo.file.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.file.IAddFileSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addFileSMOImpl")
public class AddFileSMOImpl extends BaseComponentSMO implements IAddFileSMO {

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public ResponseEntity<String> saveFile(IPageData pd, MultipartFile uploadFile) throws IOException {

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if (uploadFile.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("上传文件超过两兆");
        }

        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(paramIn, "suffix", "必填，请填写文件类型");
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.SAVE_FILE);

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        InputStream is = uploadFile.getInputStream();
        String fileContext = Base64Convert.ioToBase64(is);
        paramIn.put("file", fileContext);
        paramIn.put("fileName", uploadFile.getOriginalFilename());


        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/file.saveFile" ;


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);
        return responseEntity;

    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
