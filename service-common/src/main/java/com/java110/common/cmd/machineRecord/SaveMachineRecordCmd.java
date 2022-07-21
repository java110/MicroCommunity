package com.java110.common.cmd.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineRecordV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Java110Cmd(serviceCode = "machineRecord.saveMachineRecord")
public class SaveMachineRecordCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IMachineRecordV1InnerServiceSMO machineRecordV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请选择用户名称");
        Assert.hasKeyAndValue(reqJson, "openTypeCd", "必填，请选择开门方式");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写用户手机号");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证");

        Map<String, String> headers = context.getReqHeaders();
        Assert.hasKeyAndValue(headers, "communityid", "必填，请填写小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Map<String, String> headers = context.getReqHeaders();
        String communityId = headers.get("communityid");

        int flag = 0;

        String machineRecordId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId);
        reqJson.put("machineRecordId", machineRecordId);
        JSONObject businessMachineRecord = new JSONObject();
        businessMachineRecord.putAll(reqJson);
        businessMachineRecord.put("machineRecordId", machineRecordId);
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(businessMachineRecord, MachineRecordPo.class);
        flag = machineRecordV1InnerServiceSMOImpl.saveMachineRecord(machineRecordPo);
        if (flag < 1) {
            throw new CmdException("保存记录异常");
        }
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(communityId);
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "60000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", reqJson.getString("machineRecordId"));
            businessUnit.put("fileRealName", reqJson.getString("photoId"));
            businessUnit.put("fileSaveName", reqJson.getString("fileSaveName"));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);

            if (flag < 1) {
                throw new CmdException("保存图片异常");
            }
        }
    }
}
