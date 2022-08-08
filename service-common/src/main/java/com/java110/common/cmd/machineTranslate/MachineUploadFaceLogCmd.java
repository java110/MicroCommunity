package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.smo.impl.MachineRecordV1InnerServiceSMOImpl;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineUploadFaceLog")
public class MachineUploadFaceLogCmd extends BaseMachineCmd {


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

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private MachineRecordV1InnerServiceSMOImpl machineRecordV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
        super.validateMachineHeader(event, reqJson);
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        if (reqJson.containsKey("userID")) {
            Assert.hasKeyAndValue(reqJson, "userID", "必填，请填写用户信息"); // 为了兼容锐目
        } else {
            Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
        }
        // Assert.hasKeyAndValue(reqJson, "openTypeCd", "必填，请选择开门方式");
        Assert.hasKeyAndValue(reqJson, "photo", "必填，请填写用户照片信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResponseEntity<String> responseEntity = null;
        JSONObject outParam = null;

        try {
            Map<String, String> reqHeader = context.getReqHeaders();
            //判断是否是心跳类过来的
            if (!super.validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
                return;
            }
            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            JSONArray data = null;
            reqJson.put("communityId", reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId"));
            HttpHeaders httpHeaders = super.getHeader(context);

            reqJson.put("fileId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));

            //添加单元信息
            addMachineRecord(reqJson);
            //保存文件信息
            savePhoto(reqJson);

            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            outParam.put("data", data);
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        } catch (Exception e) {
            outParam.put("code", -1);
            outParam.put("message", e.getMessage());
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        }
    }

    public void savePhoto(JSONObject reqJson) {
        FileDto fileDto = new FileDto();
        fileDto.setCommunityId(reqJson.getString("communityId"));
        fileDto.setFileId(reqJson.getString("fileId"));
        fileDto.setFileName(reqJson.getString("fileId"));
        fileDto.setContext(reqJson.getString("photo"));
        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        businessUnit.put("relTypeCd", reqJson.getString("relTypeCd"));
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", reqJson.getString("machineRecordId"));
        businessUnit.put("fileRealName", reqJson.getString("fileId"));
        businessUnit.put("fileSaveName", fileName);
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if(flag <1){
            throw new CmdException("保存记录失败");
        }
    }

    public void addMachineRecord(JSONObject paramInJson) {

        if (!paramInJson.containsKey("openTypeCd")) {
            paramInJson.put("openTypeCd", "1000");
        }

        if (!paramInJson.containsKey("recordTypeCd")) {
            paramInJson.put("openTypeCd", "8888");
        }
        paramInJson.put("fileTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));

        paramInJson.put("machineRecordId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));

        String objId = paramInJson.getString("userId");
        //这里objId 可能是 业主ID 也可能是钥匙ID
        //先根据业主ID去查询
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(paramInJson.getString("communityId"));
        ownerDto.setMemberId(objId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            Assert.listOnlyOne(ownerDtos, "根据业主ID查询到多条记录");
            paramInJson.put("name", ownerDtos.get(0).getName());
            paramInJson.put("tel", ownerDtos.get(0).getLink());
            paramInJson.put("idCard", ownerDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "60000");
        } else { //钥匙申请ID
            ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
            applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
            applicationKeyDto.setApplicationKeyId(objId);
            List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

            Assert.listOnlyOne(applicationKeyDtos, "根据钥匙ID未查询到记录或查询到多条记录");

            paramInJson.put("name", applicationKeyDtos.get(0).getName());
            paramInJson.put("tel", applicationKeyDtos.get(0).getTel());
            paramInJson.put("idCard", applicationKeyDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "60000");

        }

        //计算 应收金额
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(paramInJson, MachineRecordPo.class);
        int flag = machineRecordV1InnerServiceSMOImpl.saveMachineRecord(machineRecordPo);
        if(flag <1){
            throw new CmdException("保存记录失败");
        }
    }
}
