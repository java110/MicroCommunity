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
import com.java110.dto.hardwareAdapation.MachineTranslateDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.constant.StatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineGetTaskInfoListener")
public class MachineGetTaskInfoListener extends BaseMachineListener {

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
        super.validateMachineHeader(event, reqJson);
        //Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
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
        String communityId = reqHeader.get("communityId");
        HttpHeaders httpHeaders = super.getHeader(context);
        //查询删除的业主信息
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineCode(reqJson.getString("machineCode"));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        List<MachineTranslateDto> machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);
        //如果有失效数据，则告诉设备删除
        if (machineTranslateDtos != null && machineTranslateDtos.size() >= 0) {
            data = new JSONArray();
            for (MachineTranslateDto tmpM : machineTranslateDtos) {
                JSONObject tmpData = new JSONObject();
                tmpData.put("taskcmd", 102);
                tmpData.put("taskId", UUID.randomUUID().toString().replace("-", ""));
                tmpData.put("taskinfo", tmpM.getObjId());
                data.add(tmpData);
            }
        }

        //查询待同步的业主数据
        machineTranslateDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        machineTranslateDto.setState("10000");
        //鉴权码先不做判断，后期判断
        machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);
        if (machineTranslateDtos == null || machineTranslateDtos.size() == 0) {

            outParam.put("data", data);
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        if (data == null) {
            data = new JSONArray();
        }
        JSONObject tmpData = null;
        for (MachineTranslateDto tmpMachineTranslate : machineTranslateDtos) {
            tmpData = new JSONObject();
            tmpData.put("taskcmd", 101);
            tmpData.put("taskId", UUID.randomUUID().toString().replace("-", ""));
            tmpData.put("taskinfo", tmpMachineTranslate.getObjId());
            data.add(tmpData);
            //将 设备 待同步 改为同步中
            MachineTranslateDto tmpMtDto = new MachineTranslateDto();
            tmpMtDto.setMachineCode(machineTranslateDto.getMachineCode());
            tmpMtDto.setCommunityId(machineTranslateDto.getCommunityId());
            tmpMtDto.setObjId(tmpMachineTranslate.getObjId());
            tmpMtDto.setState("30000");
            machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);
        }


        outParam.put("data", data);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_GET_TASK_INFO;
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
