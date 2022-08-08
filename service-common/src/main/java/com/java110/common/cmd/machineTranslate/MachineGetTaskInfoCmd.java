package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.CmdException;
import com.java110.vo.MachineTaskVo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineGetTaskInfo")
public class MachineGetTaskInfoCmd extends BaseMachineCmd {


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
        super.validateMachineHeader(event, reqJson);
        //Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResultVo resultVo = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getReqHeaders();
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
                tmpMtDto.setMachineCode(tmpM.getMachineCode());
                tmpMtDto.setState(STATE_TRANSLATEING);
                machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);
            }
        }
        resultVo = new ResultVo(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, machineTaskVos);
        responseEntity = new ResponseEntity<>(resultVo.toString(), httpHeaders, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
