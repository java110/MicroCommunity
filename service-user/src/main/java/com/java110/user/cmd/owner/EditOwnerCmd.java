package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110CmdDoc(title = "修改业主",
        description = "第三方系统，比如招商系统同步业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.editOwner",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.editOwner",
        seq = 10
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "name", length = 64, remark = "业主名称"),
        @Java110ParamDoc(name = "link", length = 11, remark = "业主手机号"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "业主身份证号"),
        @Java110ParamDoc(name = "address", length = 512, remark = "地址"),
        @Java110ParamDoc(name = "sex", length = 12, remark = "性别 男 1 女 0"),
        @Java110ParamDoc(name = "ownerTypeCd", length = 12, remark = "业主类型 1001 业主 2002 家庭成员 家庭成员 需要传业主的ownerId"),
        @Java110ParamDoc(name = "remark", length = 512, remark = "备注"),
        @Java110ParamDoc(name = "memberId", length = 30, remark = "业主ID"),
        @Java110ParamDoc(name = "ownerPhoto", length = -1, remark = "业主人脸 用于同步门禁 人脸开门"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\n" +
                "\t\"name\": \"王王\",\n" +
                "\t\"age\": \"\",\n" +
                "\t\"link\": \"18909718888\",\n" +
                "\t\"address\": \"张三\",\n" +
                "\t\"sex\": \"0\",\n" +
                "\t\"ownerTypeCd\": \"1001\",\n" +
                "\t\"remark\": \"\",\n" +
                "\t\"memberId\": 123123123,\n" +
                "\t\"ownerPhoto\": \"\",\n" +
                "\t\"idCard\": \"\",\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody="{\"code\":0,\"msg\":\"成功\"}"
)
@Java110Cmd(serviceCode = "owner.editOwner")
public class EditOwnerCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
//        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含ownerTypeCd");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        // Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
        Assert.judgeAttrValue(reqJson);

        //获取手机号(判断手机号是否重复)
        String link = reqJson.getString("link");
        if (!StringUtil.isEmpty(link) && link.contains("*")) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(reqJson.getString("ownerId"));
            //业主
            ownerDto.setOwnerTypeCd("1001");
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
            link = ownerDtos.get(0).getLink();
            reqJson.put("link", link);
        }
        //获取身份证号(判断身份证号是否重复)
        String idCard = reqJson.getString("idCard");

        if (!StringUtil.isEmpty(idCard) && idCard.contains("*")) {
            OwnerDto owner = new OwnerDto();
            owner.setOwnerId(reqJson.getString("ownerId"));
            //业主
            owner.setOwnerTypeCd("1001");
            List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwners(owner);
            Assert.listOnlyOne(owners, "查询业主信息错误！");
            idCard = owners.get(0).getIdCard();
            reqJson.put("idCard", idCard);
        }

        String userValidate = MappingCache.getValue("USER_VALIDATE");

        if (!"ON".equals(userValidate)) {
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
        if (ownerDtos != null && ownerDtos.size() > 1) {
            throw new IllegalArgumentException("手机号重复，请重新输入");
        } else if (ownerDtos != null && ownerDtos.size() == 1) {
            for (OwnerDto owner : ownerDtos) {
                if ((!StringUtil.isEmpty(reqJson.getString("ownerId")) && !owner.getOwnerId().equals(reqJson.getString("ownerId"))) || (!StringUtil.isEmpty(reqJson.getString("memberId")) && !owner.getMemberId().equals(reqJson.getString("memberId")))) {
                    throw new IllegalArgumentException("手机号重复，请重新输入");
                }
            }
        }
        if (!StringUtil.isEmpty(idCard)) {
            OwnerDto owner = new OwnerDto();
            owner.setIdCard(idCard);
            owner.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryAllOwners(owner);
            if (owners != null && owners.size() > 1) {
                throw new IllegalArgumentException("身份证号重复，请重新输入");
            } else if (owners != null && owners.size() == 1) {
                for (OwnerDto ownerDto1 : owners) {
                    if ((!StringUtil.isEmpty(reqJson.getString("ownerId")) && !ownerDto1.getOwnerId().equals(reqJson.getString("ownerId"))) || (!StringUtil.isEmpty(reqJson.getString("memberId")) && !ownerDto1.getMemberId().equals(reqJson.getString("memberId")))) {
                        throw new IllegalArgumentException("身份证号重复，请重新输入");
                    }
                }
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("ownerId") || OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
            reqJson.put("ownerId", reqJson.getString("memberId"));
        }

        //这里注释 因为 有国外的手机号 不是11位
//        if (link.length() != 11) {
//            throw new IllegalArgumentException("手机号输入不正确！");
//        }
        if (reqJson.containsKey("ownerPhoto") && !StringUtils.isEmpty(reqJson.getString("ownerPhoto"))) {
            editOwnerPhoto(reqJson);
        }
        editOwner(reqJson);
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("memberId", reqJson.getString("memberId"));
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || StringUtil.isEmpty(attr.getString("attrId")) || attr.getString("attrId").startsWith("-")) {
                attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
                flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
                if (flag < 1) {
                    throw new CmdException("添加业主属性失败");
                }
                continue;
            }
            OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
            flag = ownerAttrInnerServiceSMOImpl.updateOwnerAttrInfoInstance(ownerAttrPo);
            if (flag < 1) {
                throw new CmdException("修改业主属性失败");
            }
        }
    }

    public void editOwner(JSONObject paramInJson) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(paramInJson.getString("memberId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未查询到业主信息或查询到多条");

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(BeanConvertUtil.beanCovertMap(ownerDtos.get(0)));
        businessOwner.putAll(paramInJson);

        if (paramInJson.containsKey("wxPhoto")) {
            businessOwner.put("link", paramInJson.getString("wxPhoto"));
        }
        businessOwner.put("state", ownerDtos.get(0).getState());
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        if (StringUtil.isEmpty(ownerPo.getIdCard())) {
            ownerPo.setAge(null);
        }
        int flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("修改业主失败");
        }
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(paramInJson.getString("ownerId"));

        //查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
            for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
                OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
                ownerAppUserPo.setLink(paramInJson.getString("link"));
                ownerAppUserPo.setIdCard(paramInJson.getString("idCard"));
                flag = ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
                if (flag < 1) {
                    throw new CmdException("修改业主失败");
                }
            }
        }
    }

    public void editOwnerPhoto(JSONObject paramInJson) {

        String _photo = paramInJson.getString("ownerPhoto");
        if(_photo.length()> 512){
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(_photo);
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(paramInJson.getString("communityId"));
            _photo = fileInnerServiceSMOImpl.saveFile(fileDto);
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("10000");
        fileRelDto.setObjId(paramInJson.getString("memberId"));
        int flag = 0;
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "10000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", paramInJson.getString("memberId"));
            businessUnit.put("fileRealName", _photo);
            businessUnit.put("fileSaveName", _photo);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存文件失败");
            }
            return;
        }

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
        businessUnit.put("fileRealName", _photo);
        businessUnit.put("fileSaveName", _photo);
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        flag = fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);
        if (flag < 1) {
            throw new CmdException("保存文件失败");
        }
    }
}
