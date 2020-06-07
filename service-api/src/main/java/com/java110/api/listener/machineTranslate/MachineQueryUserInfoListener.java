package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IApplicationKeyMachineTranslateBMO;
import com.java110.api.bmo.machineTranslate.IOwnerMachineTranslateBMO;
import com.java110.api.bmo.machineTranslate.IStaffMachineTranslateBMO;
import com.java110.api.bmo.machineTranslate.IVisitMachineTranslateBMO;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IApplicationKeyInnerServiceSMO;
import com.java110.core.smo.common.IFileInnerServiceSMO;
import com.java110.core.smo.common.IFileRelInnerServiceSMO;
import com.java110.core.smo.common.IMachineInnerServiceSMO;
import com.java110.core.smo.common.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    public static final String TYPE_OWNER = "8899";//业主人脸
    public static final String TYPE_APPLICATION_KEY = "7788";//申请钥匙
    public static final String TYPE_VISIT = "6677";//访客人脸
    public static final String TYPE_STAFF = "5566";//员工人脸


    private static final String DEFAULT_DOMAIN = "YUNLUN";

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;


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


    @Autowired
    private IOwnerMachineTranslateBMO ownerMachineTranslateBMOImpl;

    @Autowired
    private IApplicationKeyMachineTranslateBMO applicationKeyMachineTranslateBMOImpl;

    @Autowired
    private IVisitMachineTranslateBMO visitMachineTranslateBMOImpl;

    @Autowired
    private IStaffMachineTranslateBMO staffMachineTranslateBMOImpl;

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

        ResultVo resultVo = null;
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
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (communityDtos == null || communityDtos.size() != 1) {

            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "未找到相应小区信息");

            responseEntity = new ResponseEntity<>(resultVo.toString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }


        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setObjId(reqJson.getString("faceid"));
        machineTranslateDto.setState("30000");//查询同步中
        List<MachineTranslateDto> machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);

        if (machineTranslateDtos == null || machineTranslateDtos.size() < 1) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "未找到相应人脸信息");
            responseEntity = new ResponseEntity<>(resultVo.toString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        MachineTranslateDto tmpMachineTranslateDto = machineTranslateDtos.get(0);

        reqJson.put("communityId", communityId);
        reqJson.put("communityName", communityDtos.get(0).getName());
        reqJson.put("machineCode", httpHeaders.get("machinecode").get(0));

        MachineUserResultDto machineUserResultDto = null;
        switch (tmpMachineTranslateDto.getTypeCd()) {
            case TYPE_OWNER:
                machineUserResultDto = ownerMachineTranslateBMOImpl.getPhotoInfo(reqJson);
                break;
            case TYPE_APPLICATION_KEY:
                machineUserResultDto = applicationKeyMachineTranslateBMOImpl.getPhotoInfo(reqJson);
            case TYPE_VISIT:
                machineUserResultDto = visitMachineTranslateBMOImpl.getPhotoInfo(reqJson);
            case TYPE_STAFF:
                machineUserResultDto = staffMachineTranslateBMOImpl.getPhotoInfo(reqJson);
            default:
                break;
        }

        //检查是否存在该用户
        if (machineUserResultDto == null) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "未找到相应人脸信息");
            responseEntity = new ResponseEntity<>(resultVo.toString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        resultVo = new ResultVo(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, machineUserResultDto);


        responseEntity = new ResponseEntity<>(JSONObject.toJSONString(resultVo), httpHeaders, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
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
