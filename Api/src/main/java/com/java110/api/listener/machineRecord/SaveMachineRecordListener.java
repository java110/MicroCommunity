package com.java110.api.listener.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineRecord.IMachineRecordBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineRecordConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMachineRecordListener")
public class SaveMachineRecordListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IMachineRecordBMO machineRecordBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请选择用户名称");
        Assert.hasKeyAndValue(reqJson, "openTypeCd", "必填，请选择开门方式");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写用户手机号");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证");

        Map<String, String> headers = event.getDataFlowContext().getRequestHeaders();
        Assert.hasKeyAndValue(headers, "communityid", "必填，请填写小区ID");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        Map<String, String> headers = event.getDataFlowContext().getRequestHeaders();
        String communityId = headers.get("communityid");

        machineRecordBMOImpl.addMachineRecord(reqJson, context);

        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(communityId);
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            machineRecordBMOImpl.addPhoto(reqJson, context);

        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineRecordConstant.ADD_MACHINERECORD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
