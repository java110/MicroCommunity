package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.saveOwner")
public class SaveOwnerCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含类型");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        //Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
        if (reqJson.containsKey("roomId")) {
            Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
            Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");
            Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
            Assert.hasLength(reqJson.getString("state"), "state不能为空");
            Assert.hasLength(reqJson.getString("storeId"), "storeId不能为空");
        }
        if (reqJson.containsKey("msgCode")) {
            SmsDto smsDto = new SmsDto();
            smsDto.setTel(reqJson.getString("link"));
            smsDto.setCode(reqJson.getString("msgCode"));
            smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);
            if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(SendSmsFactory.SMS_SEND_SWITCH))) {
                throw new IllegalArgumentException(smsDto.getMsg());
            }
        }
        //属性校验
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if ((!reqJson.containsKey("source") || !"BatchImport".equals(reqJson.getString("source"))) && "ON".equals(userValidate)) {
            //获取手机号(判断手机号是否重复)
            String link = reqJson.getString("link");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(link);
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
            Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");
            //获取身份证号(判断身份证号是否重复)
            String idCard = reqJson.getString("idCard");
            if (!StringUtil.isEmpty(idCard)) {
                OwnerDto owner = new OwnerDto();
                owner.setIdCard(idCard);
                owner.setCommunityId(reqJson.getString("communityId"));
                List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryAllOwners(owner);
                Assert.listIsNull(owners, "身份证号重复，请重新输入");
            }
        }
        //生成memberId
        generateMemberId(reqJson);
        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(reqJson);
        businessOwner.put("state", "2000");
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        if (reqJson.containsKey("age") && StringUtil.isEmpty(reqJson.getString("age"))) {
            ownerPo.setAge(null);
        }
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }
        //有房屋信息，则直接绑定房屋和 业主的关系
        if (reqJson.containsKey("roomId")) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.putAll(reqJson);
            businessUnit.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
            flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }
        if (reqJson.containsKey("ownerPhoto") && !StringUtils.isEmpty(reqJson.getString("ownerPhoto"))) {
            JSONObject businessUnit = new JSONObject();
            String _photo = reqJson.getString("ownerPhoto");
            if(_photo.length()> 512){
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(_photo);
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                _photo = fileInnerServiceSMOImpl.saveFile(fileDto);
            }
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "10000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", reqJson.getString("memberId"));
            businessUnit.put("fileRealName", _photo);
            businessUnit.put("fileSaveName", _photo);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }
        dealOwnerAttr(reqJson, cmdDataFlowContext);
    }

    /**
     * 生成小区楼ID
     *
     * @param paramObj 请求入参数据
     */
    private void generateMemberId(JSONObject paramObj) {
        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramObj.put("memberId", memberId);
        if (!paramObj.containsKey("ownerId") || OwnerDto.OWNER_TYPE_CD_OWNER.equals(paramObj.getString("ownerTypeCd"))) {
            paramObj.put("ownerId", memberId);
        }
    }

    private void dealOwnerAttr(JSONObject paramObj, ICmdDataFlowContext cmdDataFlowContext) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }

        int flag = 0;
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("memberId", paramObj.getString("memberId"));
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
            flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }

    }
}
