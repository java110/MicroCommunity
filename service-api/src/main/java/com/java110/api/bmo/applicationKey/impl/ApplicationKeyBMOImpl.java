package com.java110.api.bmo.applicationKey.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.applicationKey.IApplicationKeyBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.po.message.MsgPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @ClassName AppBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 19:59
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("applicationKeyBMOImpl")
public class ApplicationKeyBMOImpl extends ApiBaseBMO implements IApplicationKeyBMO {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;
    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        super.delete(dataFlowContext, applicationKeyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_APPLICATION_KEY);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId("-1");
        fileRelPo.setRelTypeCd("30000");
        fileRelPo.setSaveWay("table");
        fileRelPo.setObjId(paramInJson.getString("applicationKeyId"));
        fileRelPo.setFileRealName(paramInJson.getString("applicationKeyPhotoId"));
        fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }

    /**
     * `msg_type` varchar(12) not null comment '消息类型 10001 公告，10002 钥匙审核，10003 小区入驻审核，10004 小区添加审核',
     * `title` varchar(30) NOT NULL COMMENT '消息标题',
     * `url` varchar(100) NOT NULL COMMENT '消息路径',
     * `view_type_cd` varchar(100) NOT NULL COMMENT '受众类型，10000 系统内消息，20000 小区内消息， 30000 商户内， 40000 员工ID（userId）',
     * `view_obj_id` varchar(64) NOT NULL COMMENT '对象ID',
     * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
     *
     * @param paramInJson
     * @param context
     * @return
     */

    public void addMsg(JSONObject paramInJson, DataFlowContext context) {
        MsgPo msgPo = new MsgPo();
        msgPo.setMsgId("-1");
        msgPo.setMsgType("10002");
        msgPo.setTitle("您有一条钥匙审核单");
        msgPo.setUrl("/admin.html#/pages/property/auditApplicationKey");
        msgPo.setViewObjId(paramInJson.getString("storeId"));
        msgPo.setViewTypeCd("30000");
        super.insert(context, msgPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void editApplicationKeyPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("30000");
        fileRelDto.setObjId(paramInJson.getString("applicationKeyId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId("-1");
            fileRelPo.setRelTypeCd("3000");
            fileRelPo.setSaveWay("table");
            fileRelPo.setObjId(paramInJson.getString("applicationKeyId"));
            fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
            fileRelPo.setFileRealName(paramInJson.getString("applicationKeyPhotoId"));
            super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            return;
        }

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(fileRelDtos.get(0), FileRelPo.class);
        fileRelPo.setFileRealName(paramInJson.getString("applicationKeyPhotoId"));
        fileRelPo.setFileSaveName(paramInJson.getString("applicationKeyPhotoId"));
        super.update(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FILE_REL);
    }

    /**
     * 添加钥匙申请信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {
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

        super.update(dataFlowContext, applicationKeyPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_APPLICATION_KEY);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {

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
        super.insert(dataFlowContext, applicationKeyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLICATION_KEY);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addApplicationVisitKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        //查询 是否住户密码已经审核完

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        applicationKeyPo.setApplicationKeyId(paramInJson.getString("applicationKeyId"));
        applicationKeyPo.setState("10001");
        applicationKeyPo.setTypeFlag("1100103");
        applicationKeyPo.setStartTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        super.insert(dataFlowContext, applicationKeyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLICATION_KEY);
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);;
        }
        return result;
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "60000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("applicationKeyId"));
        businessUnit.put("fileRealName", paramInJson.getString("applicationKeyPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);

        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);

    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        //paramInJson.put("fileTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        paramInJson.put("name", "匿名");
        paramInJson.put("tel", "");
        paramInJson.put("idCard", "");
        paramInJson.put("openTypeCd", "2000");
        paramInJson.put("machineRecordId", "-1");
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(paramInJson, MachineRecordPo.class);
        super.insert(dataFlowContext, machineRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_RECORD);
    }

    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addMember(JSONObject paramInJson, DataFlowContext context) {

        //根据房屋ID查询业主ID，自动生成成员ID
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(paramInJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtoList = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        Assert.listOnlyOne(ownerRoomRelDtoList, "根据房屋查询不到业主信息或查询到多条");
        paramInJson.put("ownerId", ownerRoomRelDtoList.get(0).getOwnerId());


        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramInJson.put("memberId", memberId);

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(paramInJson);
        businessOwner.put("ownerTypeCd", "1004");//临时人员
        businessOwner.put("state", "1000");//待审核

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);

        super.insert(context, ownerPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO);

    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerKeyPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "10000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("memberId"));
        businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);

        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);

    }

}
