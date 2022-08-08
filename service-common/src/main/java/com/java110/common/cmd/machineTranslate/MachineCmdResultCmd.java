package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineCmdResult")
public class MachineCmdResultCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(MachineCmdResultCmd.class);


    public static final String FRONT_KAFKA_TOPIC = "webSentMessageTopic";
    public static final String STATE_NO_TRANSLATE = "10000";//待同步
    public static final String STATE_TRANSLATEED = "20000";//同步完成
    public static final String STATE_TRANSLATEING = "30000";//同步中
    public static final String STATE_CMD_SUCCESS = "40000";//命令执行成功
    public static final String STATE_CMD_ERROR = "50000";//命令执行失败

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

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResponseEntity<String> responseEntity = null;
        ResultVo resultVo = null;

        Map<String, String> reqHeader = context.getReqHeaders();

        HttpHeaders headers = new HttpHeaders();
        String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");
        if (StringUtil.isEmpty(communityId)) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "请求地址中未包含小区信息");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        if (!reqHeader.containsKey("machinecode") || StringUtils.isEmpty(reqHeader.get("machinecode"))) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "请求头中未包含设备编码");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }

        //String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");

        //检查设备是否合法
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqHeader.get("machinecode"));
        machineDto.setCommunityId(communityId);
        int machineCount = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);
        if (machineCount < 1) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "该设备【" + reqJson.getString("machinecode") + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        //outParam.put("data", outParam);

        if (!reqJson.containsKey("code")) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "请求报文格式错误 未包含code");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        //这里根据 code 修改命令执行结果
        int code = reqJson.getIntValue("code");
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineTranslateId(reqJson.getString("taskid"));
        tmpMtDto.setCommunityId(communityId);
        ResultVo frontResultVo = null;
        if (ResultVo.CODE_MACHINE_OK != code) {
            tmpMtDto.setState(STATE_CMD_ERROR);
            tmpMtDto.setRemark(reqJson.getString("msg"));
            frontResultVo = new ResultVo(ResultVo.CODE_ERROR, reqJson.getString("msg"));
        } else {
            tmpMtDto.setState(STATE_CMD_SUCCESS);
            frontResultVo = new ResultVo(ResultVo.CODE_OK, reqJson.getString("msg"));

        }
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);
        //写kafka消息
        try {
            KafkaFactory.sendKafkaMessage(FRONT_KAFKA_TOPIC, frontResultVo.toString());
        } catch (Exception e) {
            logger.error("通知 front失败", e);
        }
        resultVo = new ResultVo(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK);
        responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
