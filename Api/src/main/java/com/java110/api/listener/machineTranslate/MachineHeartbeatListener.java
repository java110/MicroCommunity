package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.event.service.api.ServiceDataFlowListener;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件心跳检测类
 */
@Java110Listener("machineHeartbeatListener")
public class MachineHeartbeatListener extends BaseMachineListener {

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
     * {
     * "machineCode":"test-MachineCode",
     * "devGroup":"default",
     * "name":"dev1",
     * "authCode":"ab2324f12ca2312b213133bfac",
     * "ip":"192.168.100.33",
     * "mac":"00:00:00:00","remarks":"test",
     * "faceNum":0,
     * "lastOnTime":15328329,
     * "statCode":1,
     * "deviceType":1,
     * "versionCode":114
     * }
     *
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
       /* Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备编码");
        //Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "authCode", "请求报文中未包含设备鉴权码");*/
        DataFlowContext context = event.getDataFlowContext();
        Map<String, String> reqHeader = context.getRequestHeaders();

        //设备通用校验
        //super.validateMachineHeader(event, reqJson);

        if (StringUtil.isEmpty(reqHeader.get("command"))) {
            reqHeader.put("command", "gettask");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        Map<String, String> reqHeader = context.getRequestHeaders();

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
        List<ServiceDataFlowListener> listeners = ServiceDataFlowEventPublishing.getListeners(serviceCode, CommonConstant.HTTP_METHOD_POST);
        Assert.listOnlyOne(listeners, "存在多个侦听来处理门禁对接，是不允许的");

        //调用相应侦听来处理业务逻辑
        ServiceDataFlowListener listener = listeners.get(0);
        listener.soService(event);


    }

    /**
     * 执行结果上报
     *
     * @param event
     * @param context
     * @param reqJson
     * @param reqHeader
     * @param headers
     */
    private void report(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, Map<String, String> reqHeader, HttpHeaders headers) {
        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;

        outParam = new JSONObject();
        outParam.put("code", 0);
        outParam.put("message", "success");
        JSONArray data = null;
        String communityId = reqHeader.get("communityId");
        outParam.put("data", data);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_HEARTBEAT;
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
