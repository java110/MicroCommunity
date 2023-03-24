package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
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
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "修改业主或者家庭成员人脸",
        description = "主要用于第三方系统 调用此接口修改人脸信息，同步门禁，进出小区或者楼栋",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.uploadOwnerPhoto",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.uploadOwnerPhoto",
        seq = 13
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "memberId", length = 30, remark = "人员编号，可以通过 查询业主 接口获取到"),
        @Java110ParamDoc(name = "photo", length = -1, remark = "业主人脸 用于同步门禁 人脸开门 base64"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\n" +
                "\t\"memberId\": 123123123,\n" +
                "\t\"ownerPhoto\": \"\",\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody="{\"code\":0,\"msg\":\"成功\"}"
)
@Java110Cmd(serviceCode = "owner.uploadOwnerPhoto")
public class UploadOwnerPhotoCmd extends Cmd {


    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;
    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "photo", "请求报文中未包含photo");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("ownerPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            editOwnerPhoto(reqJson);

        }
        //添加小区楼
        editOwner(reqJson);
    }

    public void editOwnerPhoto(JSONObject paramInJson) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("10000");
        fileRelDto.setObjId(paramInJson.getString("memberId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "10000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", paramInJson.getString("memberId"));
            businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
            businessUnit.put("fileSaveName", paramInJson.getString("ownerPhotoId"));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            return;
        }

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
        businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);


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
        int flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
        if(flag < 1){
            throw new CmdException("修改业主");
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
                ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
            }
        }
    }

}
