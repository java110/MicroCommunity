package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IFileInnerServiceSMO;
import com.java110.core.smo.common.IFileRelInnerServiceSMO;
import com.java110.core.smo.common.IMachineInnerServiceSMO;
import com.java110.core.smo.common.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.user.IOwnerInnerServiceSMO;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.vo.MachineTaskVo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineGetTaskInfoListener")
public class MachineGetTaskInfoListener extends BaseMachineListener {

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

        ResultVo resultVo = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        //判断是否是心跳类过来的
        if (!super.validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
            return;
        }
        List<MachineTaskVo> machineTaskVos = null;
        String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");
        HttpHeaders httpHeaders = super.getHeader(context);
        //查询删除的业主信息
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineCode(reqJson.getString("machineCode"));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setState(STATE_NO_TRANSLATE);
        machineTranslateDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        List<MachineTranslateDto> machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);
        if (machineTranslateDtos != null && machineTranslateDtos.size() >= 0) {
            machineTaskVos = new ArrayList<>();
            for (MachineTranslateDto tmpM : machineTranslateDtos) {
                MachineTaskVo machineTaskVo = new MachineTaskVo(tmpM.getMachineCmd(), tmpM.getMachineTranslateId(), tmpM.getObjId());
                machineTaskVos.add(machineTaskVo);
                MachineTranslateDto tmpMtDto = new MachineTranslateDto();
                tmpMtDto.setMachineTranslateId(tmpM.getMachineTranslateId());
                tmpMtDto.setCommunityId(tmpM.getCommunityId());
                tmpMtDto.setState(STATE_TRANSLATEING);
                machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);
            }
        }
        resultVo = new ResultVo(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, machineTaskVos);
        responseEntity = new ResponseEntity<>(resultVo.toString(), httpHeaders, HttpStatus.OK);
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
