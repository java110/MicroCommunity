package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.applicationKey.IApplicationKeyBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.core.smo.common.ISmsInnerServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.msg.SmsDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeApplicationKeyConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Random;

/**
 * 钥匙申请
 * add by wuxw 2019-06-30
 */
@Java110Listener("applyApplicationKeyListener")
public class ApplyApplicationKeyListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyBMO applicationKeyBMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(reqJson, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(reqJson, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "machineIds", "必填，请填写设备信息");
        Assert.hasKeyAndValue(reqJson, "photos", "必填，未包含身份证信息");
        Assert.hasKeyAndValue(reqJson, "typeFlag", "必填，未包含密码类型");

        SmsDto smsDto = new SmsDto();
        smsDto.setTel(reqJson.getString("tel"));
        smsDto.setCode(reqJson.getString("msgCode"));
        smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

        if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(SendSmsFactory.SMS_SEND_SWITCH))) {
            throw new IllegalArgumentException(smsDto.getMsg());
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();


        JSONArray machineIds = reqJson.getJSONArray("machineIds");
        reqJson.put("pwd", getRandom());
        for (int machineIndex = 0; machineIndex < machineIds.size(); machineIndex++) {
            //添加单元信息
            reqJson.put("machineId", machineIds.getJSONObject(machineIndex).getString("machineId"));
            //reqJson.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));
            applicationKeyBMOImpl.addApplicationKey(reqJson, context);
            if (reqJson.containsKey("photos")) {
                JSONArray photos = reqJson.getJSONArray("photos");
                for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {

                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(photos.getJSONObject(photoIndex).getString("photo"));
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

                    reqJson.put("applicationKeyPhotoId", fileDto.getFileId());
                    reqJson.put("fileSaveName", fileName);

                    businesses.add(applicationKeyBMOImpl.addPhoto(reqJson, context));
                }
            }
        }

        applicationKeyBMOImpl.addMsg(reqJson, context);



        ResponseEntity<String> responseEntity = applicationKeyBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }



    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.APPLY_APPLICATIONKEY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }




    /**
     * 获取随机数
     *
     * @return
     */
    private static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }


    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
