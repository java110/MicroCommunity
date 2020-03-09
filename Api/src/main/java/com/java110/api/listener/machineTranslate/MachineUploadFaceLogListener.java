package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineUploadFaceLogListener")
public class MachineUploadFaceLogListener extends BaseMachineListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateBMO machineTranslateBMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
        super.validateMachineHeader(event, reqJson);
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        if (reqJson.containsKey("userID")) {
            Assert.hasKeyAndValue(reqJson, "userID", "必填，请填写用户信息"); // 为了兼容锐目
        } else {
            Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
        }
        // Assert.hasKeyAndValue(reqJson, "openTypeCd", "必填，请选择开门方式");
        Assert.hasKeyAndValue(reqJson, "photo", "必填，请填写用户照片信息");
        //Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        ResponseEntity<String> responseEntity = null;
        JSONObject outParam = null;

        try {
            Map<String, String> reqHeader = context.getRequestHeaders();
            //判断是否是心跳类过来的
            if (!super.validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
                return;
            }
            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            JSONArray data = null;
            reqJson.put("communityId", reqHeader.get("communityId"));
            HttpHeaders httpHeaders = super.getHeader(context);

            HttpHeaders header = new HttpHeaders();
            context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
            JSONArray businesses = new JSONArray();

            AppService service = event.getAppService();
            reqJson.put("fileId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));

            //添加单元信息
            businesses.add(addMachineRecord(reqJson, context));
            //保存文件信息
            businesses.add(savePhoto(reqJson, context));



            responseEntity = machineTranslateBMOImpl.callService(context, service.getServiceCode(), businesses);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "上报记录失败" + responseEntity);
            }
            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            outParam.put("data", data);
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        } catch (Exception e) {
            outParam.put("code", -1);
            outParam.put("message", e.getMessage());
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        }
    }

    /**
     * 保存照片
     *
     * @param reqJson
     * @param context
     */
    private JSONObject savePhoto(JSONObject reqJson, DataFlowContext context) {


        FileDto fileDto = new FileDto();
        fileDto.setCommunityId(reqJson.getString("communityId"));
        fileDto.setFileId(reqJson.getString("fileId"));
        fileDto.setFileName(reqJson.getString("fileId"));
        fileDto.setContext(reqJson.getString("photo"));
        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", reqJson.getString("relTypeCd"));
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", reqJson.getString("userId"));
        businessUnit.put("fileRealName", reqJson.getString("fileId"));
        businessUnit.put("fileSaveName", fileName);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        if (!paramInJson.containsKey("openTypeCd")) {
            paramInJson.put("openTypeCd", "1000");
        }

        if (!paramInJson.containsKey("recordTypeCd")) {
            paramInJson.put("openTypeCd", "8888");
        }
        paramInJson.put("fileTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));

        String objId = paramInJson.getString("userId");
        //这里objId 可能是 业主ID 也可能是钥匙ID
        //先根据业主ID去查询
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(paramInJson.getString("communityId"));
        ownerDto.setMemberId(objId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            Assert.listOnlyOne(ownerDtos, "根据业主ID查询到多条记录");
            paramInJson.put("name", ownerDtos.get(0).getName());
            paramInJson.put("tel", ownerDtos.get(0).getLink());
            paramInJson.put("idCard", ownerDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "10000");
        } else { //钥匙申请ID
            ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
            applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
            applicationKeyDto.setApplicationKeyId(objId);
            List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

            Assert.listOnlyOne(applicationKeyDtos, "根据钥匙ID未查询到记录或查询到多条记录");

            paramInJson.put("name", applicationKeyDtos.get(0).getName());
            paramInJson.put("tel", applicationKeyDtos.get(0).getTel());
            paramInJson.put("idCard", applicationKeyDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "30000");

        }


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_RECORD);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessMachineRecord = new JSONObject();
        businessMachineRecord.putAll(paramInJson);
        businessMachineRecord.put("machineRecordId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessMachineRecord", businessMachineRecord);
        return business;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_UPLOAD_FACE_LOG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public IMachineTranslateInnerServiceSMO getMachineTranslateInnerServiceSMOImpl() {
        return machineTranslateInnerServiceSMOImpl;
    }

    public void setMachineTranslateInnerServiceSMOImpl(IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl) {
        this.machineTranslateInnerServiceSMOImpl = machineTranslateInnerServiceSMOImpl;
    }

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IFileRelInnerServiceSMO getFileRelInnerServiceSMOImpl() {
        return fileRelInnerServiceSMOImpl;
    }

    public void setFileRelInnerServiceSMOImpl(IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl) {
        this.fileRelInnerServiceSMOImpl = fileRelInnerServiceSMOImpl;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
