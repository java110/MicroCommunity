package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineCmdResult?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件执行结果上报
 */
@Java110Listener("machineCmdResultListener")
public class MachineCmdResultListener extends AbstractServiceApiListener {
    private final static Logger logger = LoggerFactory.getLogger(MachineCmdResultListener.class);

    public static final String FRONT_KAFKA_TOPIC = "webSentMessageTopic";

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

    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;
        ResultVo resultVo = null;

        Map<String, String> reqHeader = context.getRequestHeaders();

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
            tmpMtDto.setState(MachineGetTaskInfoListener.STATE_CMD_ERROR);
            tmpMtDto.setRemark(reqJson.getString("msg"));
            frontResultVo = new ResultVo(ResultVo.CODE_ERROR, reqJson.getString("msg"));
        } else {
            tmpMtDto.setState(MachineGetTaskInfoListener.STATE_CMD_SUCCESS);
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

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_CMD_RESULT;
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
