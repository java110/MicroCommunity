package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.*;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "applicationKey.updateApplicationKey")
public class UpdateApplicationKeyCmd extends Cmd{


    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(reqJson, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(reqJson, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，位置不能为空");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，未选择位置对象");
        Assert.hasKeyAndValue(reqJson, "typeFlag", "必填，未选择钥匙类型");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        //添加钥匙信息
        updateApplicationKey(reqJson);

        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("applicationKeyPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            editApplicationKeyPhoto(reqJson);

        }
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editApplicationKeyPhoto(JSONObject paramInJson) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("30000");
        fileRelDto.setObjId(paramInJson.getString("applicationKeyId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId( GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            fileRelPo.setRelTypeCd("3000");
            fileRelPo.setSaveWay("table");
            fileRelPo.setObjId(paramInJson.getString("applicationKeyId"));
            fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
            fileRelPo.setFileRealName(paramInJson.getString("applicationKeyPhotoId"));
            int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存开门记录失败");
            }
            return;
        }

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(fileRelDtos.get(0), FileRelPo.class);
        fileRelPo.setFileRealName(paramInJson.getString("fileSaveName"));
        fileRelPo.setFileSaveName(paramInJson.getString("applicationKeyPhotoId"));
        int flag = fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);
        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
    }


    /**
     * 添加钥匙申请信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateApplicationKey(JSONObject paramInJson) {
        //根据位置id 和 位置对象查询相应 设备ID
        MachineDto machineDto = new MachineDto();
        machineDto.setLocationObjId(paramInJson.getString("locationObjId"));
        machineDto.setLocationTypeCd(paramInJson.getString("locationTypeCd"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        Assert.listOnlyOne(machineDtos, "该位置还没有相应的门禁设备");
        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setApplicationKeyId(paramInJson.getString("applicationKeyId"));
        applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
        List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);
        Assert.listOnlyOne(applicationKeyDtos, "未找到申请记录或找到多条记录");

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        applicationKeyPo.setMachineId(machineDtos.get(0).getMachineId());
        applicationKeyPo.setState(applicationKeyDtos.get(0).getState());
        if (!paramInJson.containsKey("pwd") || StringUtil.isEmpty(paramInJson.getString("pwd"))) {
            applicationKeyPo.setPwd(applicationKeyDtos.get(0).getPwd());
        }
        if ("1100103".equals(paramInJson.getString("typeFlag"))) { // 临时访问密码,只设置成24小时
            applicationKeyPo.setEndTime(applicationKeyDtos.get(0).getEndTime());
        }
        applicationKeyPo.setTypeFlag(applicationKeyDtos.get(0).getTypeFlag());

        int flag = applicationKeyV1InnerServiceSMOImpl.updateApplicationKey(applicationKeyPo);
        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
    }
}
