package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.applicationKey")
public class ApplicationKeyCmd extends Cmd {

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含房屋信息");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        //Assert.jsonObjectHaveKey(paramIn, "ownerTypeCd", "请求报文中未包含sex"); //这个不需要 这个直接写成钥匙申请，临时人员
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(reqJson, "idCard", "请求报文中未包含身份证号");
        Assert.jsonObjectHaveKey(reqJson, "ownerPhoto", "请求报文中未包含照片信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //根据房屋ID查询业主ID，自动生成成员ID
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtoList = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        Assert.listOnlyOne(ownerRoomRelDtoList, "根据房屋查询不到业主信息或查询到多条");
        reqJson.put("ownerId", ownerRoomRelDtoList.get(0).getOwnerId());


        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        reqJson.put("memberId", memberId);

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(reqJson);
        businessOwner.put("ownerTypeCd", "1004");//临时人员
        businessOwner.put("state", "1000");//待审核

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存业主失败");
        }

        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(reqJson.getString("ownerPhoto"));
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(reqJson.getString("communityId"));
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        reqJson.put("ownerPhotoId", fileDto.getFileId());
        reqJson.put("fileSaveName", fileName);

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "10000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", reqJson.getString("memberId"));
        businessUnit.put("fileRealName", reqJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", reqJson.getString("fileSaveName"));

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if (flag < 1) {
            throw new IllegalArgumentException("保存业主失败");
        }
    }
}
