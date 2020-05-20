package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.dto.hardwareAdapation.MachineTranslateDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.exception.ConfigDataException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineQueryUserInfoListener")
public class MachineQueryUserInfoListener extends BaseMachineListener {

    private static Logger logger = LoggerFactory.getLogger(MachineQueryUserInfoListener.class);


    private static final String DEFAULT_DOMAIN = "YUNLUN";

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

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
    private RestTemplate restTemplateNoLoadBalanced;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validateMachineHeader(event, reqJson);
        Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        //判断是否是心跳类过来的
        if (!super.validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
            return;
        }

        outParam = new JSONObject();
        outParam.put("code", 0);
        outParam.put("message", "success");
        JSONArray data = null;
        String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");
        HttpHeaders httpHeaders = super.getHeader(context);
        //检查是否存在该用户
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setMemberId(reqJson.getString("faceid"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos != null && ownerDtos.size() == 1) {
            getOwnerPhoto(communityId, outParam, context, httpHeaders, reqJson, ownerDtos.get(0));
            return;
        }
        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setCommunityId(communityId);
        applicationKeyDto.setApplicationKeyId(reqJson.getString("faceid"));
        List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

        if (applicationKeyDtos == null || applicationKeyDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到相应人脸信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        //查询钥匙人脸
        getApplicationKeyPhoto(communityId, outParam, context, httpHeaders, reqJson, applicationKeyDtos.get(0));


    }

    private void getApplicationKeyPhoto(String communityId, JSONObject outParam,
                                        DataFlowContext context,
                                        HttpHeaders httpHeaders, JSONObject reqJson, ApplicationKeyDto applicationKeyDto) {
        Map<String, String> reqHeader = context.getRequestHeaders();
        ResponseEntity<String> responseEntity = null;
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (communityDtos == null || communityDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到相应小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("30000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到钥匙照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(communityId);
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        JSONObject dataObj = new JSONObject();
        dataObj.put("userid", applicationKeyDto.getApplicationKeyId());
        dataObj.put("groupid", communityId);
        dataObj.put("group", communityDtos.get(0).getName());
        dataObj.put("name", applicationKeyDto.getName());
        dataObj.put("faceBase64", "data:image/jpeg;base64," + fileDtos.get(0).getContext()
                .replace("data:image/webp;base64,", "")
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", ""));
        dataObj.put("idNumber", applicationKeyDto.getIdCard());
        dataObj.put("startTime", applicationKeyDto.getCreateTime().getTime());
        try {
            dataObj.put("endTime", DateUtil.getLastDate().getTime());
        } catch (ParseException e) {
            dataObj.put("endTime", 2145891661);
        }
        dataObj.put("remarks", "HC小区管理系统");
        dataObj.put("reserved", applicationKeyDto.getApplicationKeyId());


        //特殊处理是否获取特征值
        if ("ON".equals(MappingCache.getValue(DEFAULT_DOMAIN, "getFeature"))) {
            getFeature(dataObj);
        }
        outParam.put("data", dataObj);
        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqHeader.get("machinecode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(applicationKeyDto.getApplicationKeyId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void getOwnerPhoto(String communityId, JSONObject outParam,
                               DataFlowContext context,
                               HttpHeaders httpHeaders, JSONObject reqJson, OwnerDto ownerDto) {
        Map<String, String> reqHeader = context.getRequestHeaders();
        ResponseEntity<String> responseEntity = null;
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (communityDtos == null || communityDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到相应小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到业主照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(communityId);
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到业主照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        JSONObject dataObj = new JSONObject();
        dataObj.put("userid", ownerDto.getMemberId());
        dataObj.put("groupid", communityId);
        dataObj.put("group", communityDtos.get(0).getName());
        dataObj.put("name", ownerDto.getName());
        String tmpImg = fileDtos.get(0).getContext();
        dataObj.put("faceBase64", "data:image/jpeg;base64," + tmpImg.substring(tmpImg.indexOf("base64,") + 7));
        dataObj.put("idNumber", ownerDto.getIdCard());
        dataObj.put("startTime", ownerDto.getCreateTime().getTime());
        try {
            dataObj.put("endTime", DateUtil.getLastDate().getTime());
        } catch (ParseException e) {
            dataObj.put("endTime", 2145891661);
        }
        dataObj.put("remarks", "HC小区管理系统");
        dataObj.put("reserved", ownerDto.getMemberId());


        //特殊处理是否获取特征值
        if ("ON".equals(MappingCache.getValue(DEFAULT_DOMAIN, "getFeature"))) {
            getFeature(dataObj);
        }
        outParam.put("data", dataObj);
        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqHeader.get("machinecode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(ownerDto.getMemberId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 调用第三方平台获取照片特征
     *
     * @param dataObj
     */
    private void getFeature(JSONObject dataObj) {

        logger.debug("开始获取照片特征");

        String photo = dataObj.getString("faceBase64");

        photo = photo
                .replace("data:image/webp;base64,", "")
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "");

        //从redis 中获取token，没有则重新获取
        String token = getToken();
        logger.debug("获取到的token为：" + token);

        String url = MappingCache.getValue(DEFAULT_DOMAIN, "hc_feature_url");
        if (StringUtil.isEmpty(url)) {
            throw new ConfigDataException(199, "未配置hc_feature_url参数");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", token);
        headers.add("Content-Type", "application/json");
        JSONObject paramIn = new JSONObject();
        paramIn.put("image", photo);
        paramIn.put("imageType", "BASE64");
        HttpEntity httpEntity = new HttpEntity(paramIn.toJSONString(), headers);
        ResponseEntity<String> responseEntity = restTemplateNoLoadBalanced.exchange(url, HttpMethod.POST, httpEntity, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("查询特征token失败" + responseEntity.toString());
        }

        String outBody = responseEntity.getBody();

        JSONObject outObj = JSONObject.parseObject(outBody);
        if (!outObj.containsKey("code")) {
            throw new RuntimeException("查询特征失败" + outBody);
        }

        if (!"0".equals(outObj.getString("code"))) {
            throw new RuntimeException("查询特征失败" + outBody);
        }

        dataObj.put("faceBase64", outObj.getString("feature"));
        logger.debug("获取到的数据dataObj为：" + dataObj.toJSONString());

    }

    /**
     * 获取token
     *
     * @return
     */
    private String getToken() {
        String token = CommonCache.getValue("hc_getFeature_token");
        if (!StringUtil.isEmpty(token)) {
            return token;
        }

        String url = MappingCache.getValue(DEFAULT_DOMAIN, "hc_token_url");
        if (StringUtil.isEmpty(url)) {
            throw new ConfigDataException(199, "未配置hc_token_url参数");
        }

        url += ("?clientId=" + MappingCache.getValue(DEFAULT_DOMAIN, "hc_clientId"));
        url += ("&clientSecret=" + MappingCache.getValue(DEFAULT_DOMAIN, "hc_clientSecret"));

        String resultInfo = restTemplateNoLoadBalanced.getForObject(url, String.class);

        logger.debug("调用 获取特征token接口：url = " + url + "返回报文：" + resultInfo);

        JSONObject resultObj = JSONObject.parseObject(resultInfo);

        if (!resultObj.containsKey("code")) {
            throw new RuntimeException("查询特征token失败" + resultInfo);
        }

        if (!"0".equals(resultObj.getString("code"))) {
            throw new RuntimeException("查询特征token失败" + resultInfo);

        }

        token = resultObj.getString("accessToken");
        int expiresIn = resultObj.getInteger("expiresIn");
        CommonCache.setValue("hc_getFeature_token", token, (expiresIn / 1000) - 10);//减去10秒 防止网络超时 时间不一致问题


        return token;

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_QUERY_USER_INFO;
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

    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
    }
}
