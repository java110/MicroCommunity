package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.msg.SmsDto;
import com.java110.intf.common.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.message.MsgPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Java110Cmd(serviceCode = "applicationKey.applyApplicationKey")
public class ApplyApplicationKeyCmd extends Cmd{


    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(reqJson, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(reqJson, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "machineIds", "必填，请填写设备信息");
        Assert.hasKeyAndValue(reqJson, "photos", "必填，未包含身份证信息");
        Assert.hasKeyAndValue(reqJson, "typeFlag", "必填，未包含密码类型");

        SmsDto smsDto = new SmsDto();
        smsDto.setTel(reqJson.getString("tel"));
        smsDto.setCode(reqJson.getString("msgCode"));
        smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

        if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN,SendSmsFactory.SMS_SEND_SWITCH))) {
            throw new IllegalArgumentException(smsDto.getMsg());
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        JSONArray machineIds = reqJson.getJSONArray("machineIds");
        reqJson.put("pwd", getRandom());
        for (int machineIndex = 0; machineIndex < machineIds.size(); machineIndex++) {
            //添加单元信息
            reqJson.put("machineId", machineIds.getJSONObject(machineIndex).getString("machineId"));
            //reqJson.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));
            addApplicationKey(reqJson);
            if (reqJson.containsKey("photos")) {
                JSONArray photos = reqJson.getJSONArray("photos");
                for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {

                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(photos.getJSONObject(photoIndex).getString("photo"));
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

                    reqJson.put("applicationKeyPhotoId", fileDto.getFileId());
                    reqJson.put("fileSaveName", fileName);

                    addPhoto(reqJson);
                }
            }
        }

      addMsg(reqJson);
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);;
        }
        return result;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addApplicationKey(JSONObject paramInJson) {

        String applicationKeyId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId);
        paramInJson.put("applicationKeyId", applicationKeyId);
        //根据位置id 和 位置对象查询相应 设备ID
        if (!paramInJson.containsKey("machineId")) {
            MachineDto machineDto = new MachineDto();
            machineDto.setLocationObjId(paramInJson.getString("locationObjId"));
            machineDto.setLocationTypeCd(paramInJson.getString("locationTypeCd"));
            List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
            Assert.listOnlyOne(machineDtos, "该位置还没有相应的门禁设备");
            paramInJson.put("machineId", machineDtos.get(0).getMachineId());
        }

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        applicationKeyPo.setApplicationKeyId(applicationKeyId);
        applicationKeyPo.setState("10002");
        applicationKeyPo.setPwd(this.getRandom());
        if ("1100103".equals(paramInJson.getString("typeFlag"))) { // 临时访问密码,只设置成24小时
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 24);
            applicationKeyPo.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        }
        int flag = applicationKeyV1InnerServiceSMOImpl.saveApplicationKey(applicationKeyPo);
        if(flag < 1){
            throw new CmdException("申请钥匙失败");
        }
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson) {


        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        businessUnit.put("relTypeCd", "60000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("applicationKeyId"));
        businessUnit.put("fileRealName", paramInJson.getString("applicationKeyPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);


        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);

        if(flag < 1){
            throw new CmdException("申请钥匙失败");
        }
    }

    public void addMsg(JSONObject paramInJson) {
        MsgPo msgPo = new MsgPo();
        msgPo.setMsgId("-1");
        msgPo.setMsgType("10002");
        msgPo.setTitle("您有一条钥匙审核单");
        msgPo.setUrl("/admin.html#/pages/property/auditApplicationKey");
        msgPo.setViewObjId(paramInJson.getString("storeId"));
        msgPo.setViewTypeCd("30000");
        //super.insert(context, msgPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG);
    }
}
