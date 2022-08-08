package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.CarResultDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineQueryUserInfo")
public class MachineQueryUserInfoCmd extends BaseMachineCmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    /**
     *
     private static Logger logger = LoggerFactory.getLogger(MachineQueryUserInfoListener.class);

     public static final String TYPE_OWNER = "8899";//业主人脸
     public static final String TYPE_APPLICATION_KEY = "7788";//申请钥匙
     public static final String TYPE_VISIT = "6677";//访客人脸
     public static final String TYPE_STAFF = "5566";//员工人脸
     public static final String TYPE_OWNER_CAR = "4455";//业主车辆


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
     private RestTemplate outRestTemplate;

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

     @Autowired
     private IOwnerCarMachineTranslateBMO ownerCarMachineTranslateBMOImpl;


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
        CarResultDto carResultDto = null;
        switch (tmpMachineTranslateDto.getTypeCd()) {
            case TYPE_OWNER:
                machineUserResultDto = ownerMachineTranslateBMOImpl.getPhotoInfo(reqJson);
                break;
            case TYPE_APPLICATION_KEY:
                machineUserResultDto = applicationKeyMachineTranslateBMOImpl.getPhotoInfo(reqJson);
                break;
            case TYPE_VISIT:
                machineUserResultDto = visitMachineTranslateBMOImpl.getPhotoInfo(reqJson);
                break;
            case TYPE_STAFF:
                machineUserResultDto = staffMachineTranslateBMOImpl.getPhotoInfo(reqJson);
                break;
            case TYPE_OWNER_CAR:
                carResultDto = ownerCarMachineTranslateBMOImpl.getInfo(reqJson);
            default:
                break;
        }

        if (carResultDto != null) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, carResultDto);
            responseEntity = new ResponseEntity<>(JSONObject.toJSONString(resultVo), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
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
     */
}
