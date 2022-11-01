package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户登录 功能
 * 请求地址为/app/login.pcUserLogin
 */

@Java110CmdDoc(title = "门禁开门记录上传",
        description = "主要用于物联网 开门记录上传使用",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/machine/openDoorLog",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "machine.openDoorLog"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "userId", length = 30, defaultValue = "-1", remark = "用户ID，如果没有填写-1"),
        @Java110ParamDoc(name = "userName", type = "String", length = 64, remark = "用户 名称"),
        @Java110ParamDoc(name = "machineCode", type = "String", length = 64, remark = "设备编号"),
        @Java110ParamDoc(name = "openTypeCd", type = "String", length = 64, remark = "开门类型 1000 人脸"),
        @Java110ParamDoc(name = "similar", type = "String", length = 64, remark = "相似度 0-1 之间"),
        @Java110ParamDoc(name = "dateTime", type = "String", length = 64, remark = "开门时间 YYYY-MM-DD hh24:mi:ss"),
        @Java110ParamDoc(name = "extCommunityId", type = "String", length = 64, remark = "小区ID"),
        @Java110ParamDoc(name = "recordTypeCd", type = "String", length = 64, remark = "8888 开门记录"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'userId':'123123','userName':'admin','machineCode':'sss','openTypeCd':'1000','similar':'0.9','dateTime':'2022-10-22 10:10:10','extCommunityId':'123123','recordTypeCd':'8888'}",
        resBody = "{'code':0,'msg':'成功'}"
)
/**
 * 门禁开门 物联网 触发调用 cmd
 */
@Java110Cmd(serviceCode = "/machine/openDoorLog")
public class OpenDoorLogCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "userName", "未包含用户名称");
        Assert.hasKeyAndValue(reqJson, "machineCode", "未包含设备编码");
        Assert.hasKeyAndValue(reqJson, "openTypeCd", "未包含开门方式");
        Assert.hasKeyAndValue(reqJson, "similar", "未包含开门相似度");
        //Assert.hasKeyAndValue(reqJson, "photo", "未包含抓拍照片");
        Assert.hasKeyAndValue(reqJson, "dateTime", "未包含开门时间");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "recordTypeCd", "未包含记录类型");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);
        machineRecordDto.setCommunityId(reqJson.getString("extCommunityId"));
        machineRecordDto.setName(reqJson.getString("userName"));
        if (reqJson.containsKey("idNumber")) {
            machineRecordDto.setIdCard(reqJson.getString("idNumber"));
        } else {
            machineRecordDto.setIdCard("-1");
        }
        if (reqJson.containsKey("tel")) {
            machineRecordDto.setTel(reqJson.getString("tel"));
        } else {
            machineRecordDto.setTel("-1");
        }

        //根据用户ID查询手机号嘛
        addTelByUserId(reqJson, machineRecordDto);

        machineRecordDto.setMachineRecordId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));
        if (!StringUtil.isEmpty(machineRecordDto.getPhoto())) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(machineRecordDto.getPhoto());
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(machineRecordDto.getCommunityId());
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            fileRelPo.setRelTypeCd("60000");
            fileRelPo.setSaveWay("table");
            fileRelPo.setObjId(machineRecordDto.getMachineRecordId());
            fileRelPo.setFileRealName(fileName);
            fileRelPo.setFileSaveName(fileName);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);

            machineRecordDto.setFileId(fileDto.getFileId());
            machineRecordDto.setFileTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        }

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(machineRecordDto.getMachineCode());
        machineDto.setCommunityId(machineRecordDto.getCommunityId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

        machineRecordDto.setMachineId(machineDtos.get(0).getMachineId());
        List<MachineRecordPo> machineRecordPos = new ArrayList<>();
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(machineRecordDto, MachineRecordPo.class);
        machineRecordPos.add(machineRecordPo);

        int count = machineRecordInnerServiceSMOImpl.saveMachineRecords(machineRecordPos);

        if (count < 1) {
            context.setResponseEntity(ResultVo.error("上传记录失败"));
        }
//        //传送databus
//        dataBusInnerServiceSMOImpl.customExchange(CustomBusinessDatabusDto.getInstance(
//                BusinessTypeConstant.BUSINESS_TYPE_DATABUS_SEND_OPEN_LOG, BeanConvertUtil.beanCovertJson(machineRecordPo)));
        context.setResponseEntity(ResultVo.success());
    }

    private void addTelByUserId(JSONObject reqJson, MachineRecordDto machineRecordDto) {

        if (!reqJson.containsKey("userId") || "-1".equals(reqJson.getString("userId"))) {
            return;
        }

        if(!"-1".equals(machineRecordDto.getTel())){
            return ;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        if(userDtos != null && userDtos.size()> 0){
            machineRecordDto.setTel(userDtos.get(0).getTel());
            return ;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("userId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if(ownerDtos == null || ownerDtos.size()<1){
            return ;
        }
        machineRecordDto.setTel(ownerDtos.get(0).getLink());
    }
}
