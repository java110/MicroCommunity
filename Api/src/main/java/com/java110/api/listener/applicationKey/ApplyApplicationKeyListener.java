package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
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
            reqJson.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));
            businesses.add(addApplicationKey(reqJson, context));
            if (reqJson.containsKey("photos")) {
                JSONArray photos = reqJson.getJSONArray("photos");
                for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {

                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(photos.getJSONObject(photoIndex).getString("photo"));
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    if (fileInnerServiceSMOImpl.saveFile(fileDto) < 1) {
                        throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件出错");
                    }
                    reqJson.put("applicationKeyPhotoId", fileDto.getFileId());

                    businesses.add(addPhoto(reqJson, context));
                }
            }
        }

        businesses.add(addMsg(reqJson, context));


        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "60000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("applicationKeyId"));
        businessUnit.put("fileRealName", paramInJson.getString("applicationKeyPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("applicationKeyPhotoId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
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
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLICATION_KEY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessApplicationKey = new JSONObject();
        businessApplicationKey.putAll(paramInJson);
        businessApplicationKey.put("applicationKeyId", paramInJson.getString("applicationKeyId"));
        businessApplicationKey.put("state", "10002");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessApplicationKey", businessApplicationKey);
        return business;
    }


    private Object addMsg(JSONObject paramInJson, DataFlowContext context) {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd("390001200002");
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "小区存在零个或多个物业");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessMsg = new JSONObject();
        //businessApplicationKey.putAll(paramInJson);
        businessMsg.put("msgId", "-1");
        businessMsg.put("msgType", "10002");
        businessMsg.put("title", "您有一条钥匙审核单");
        businessMsg.put("url", "/flow/auditApplicationKeyFlow");
        businessMsg.put("viewTypeCd", "30000");
        businessMsg.put("viewObjId", communityMemberDtos.get(0).getMemberId());
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessMsg", businessMsg);
        return business;
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
