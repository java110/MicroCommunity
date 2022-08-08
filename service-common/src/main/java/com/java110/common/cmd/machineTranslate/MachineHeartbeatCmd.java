package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.core.event.service.api.ServiceDataFlowListener;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineHeartbeart")
public class MachineHeartbeatCmd extends BaseMachineCmd{

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
        Map<String, String> reqHeader = context.getReqHeaders();

        //设备通用校验
        //super.validateMachineHeader(event, reqJson);

        if (StringUtil.isEmpty(reqHeader.get("command"))) {
            reqHeader.put("command", "gettask");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Map<String, String> reqHeader = context.getReqHeaders();

        String command = reqHeader.get("command");
        String serviceCode = "";
        //校验报文格式信息, 不做判断 让下游判断
        /*if (!validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
            return;
        }*/
        //获取任务
        if ("gettask".equals(command)) {
            serviceCode = ServiceCodeMachineTranslateConstant.MACHINE_GET_TASK_INFO;
        } else if ("getface".equals(command)) {
            serviceCode = ServiceCodeMachineTranslateConstant.MACHINE_QUERY_USER_INFO;
        } else if ("record".equals(command)) {
            serviceCode = ServiceCodeMachineTranslateConstant.MACHINE_UPLOAD_FACE_LOG;
        } else if ("report".equals(command)) {
            //this.report(event, context, reqJson, reqHeader, headers);
            // return;
            serviceCode = ServiceCodeMachineTranslateConstant.MACHINE_CMD_RESULT;
        } else {
            JSONObject outParam = new JSONObject();
            outParam.put("code", -1);
            outParam.put("message", "当前不支持该命令" + command);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(outParam.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        //获取下游侦听处理类
//        List<ServiceDataFlowListener> listeners = ServiceDataFlowEventPublishing.getListeners(serviceCode, CommonConstant.HTTP_METHOD_POST);
//        Assert.listOnlyOne(listeners, "存在多个侦听来处理门禁对接，是不允许的");
//
//        //调用相应侦听来处理业务逻辑
//        ServiceDataFlowListener listener = listeners.get(0);
//        listener.soService(event);
    }
}
