package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.doc.annotation.*;
import com.java110.dto.account.AccountDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.account.AccountPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.user.UserPo;
import com.java110.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存 业主
 */
@Java110CmdDoc(title = "添加业主",
        description = "第三方系统，比如招商系统同步业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.saveOwner",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.saveOwner",
        seq = 9
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "name", length = 64, remark = "业主名称"),
        @Java110ParamDoc(name = "link", length = 11, remark = "业主手机号"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "业主身份证号"),
        @Java110ParamDoc(name = "address", length = 512, remark = "地址"),
        @Java110ParamDoc(name = "personType", length = 12, remark = "人员类型 P 个人 C 公司"),
        @Java110ParamDoc(name = "personRole", length = 12, remark = "人员角色 1 业主 2 租客 3 家庭成员 4 公司员工 99 其他"),
        @Java110ParamDoc(name = "sex", length = 12, remark = "性别 男 1 女 0"),
        @Java110ParamDoc(name = "remark", length = 512, remark = "备注"),
        @Java110ParamDoc(name = "ownerId", length = 30, remark = "业主 时 填写-1 家庭成员时填写业主ID"),
        @Java110ParamDoc(name = "ownerPhoto", length = -1, remark = "业主人脸 用于同步门禁 人脸开门"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\n" +
                "\t\"name\": \"王王\",\n" +
                "\t\"link\": \"18909718888\",\n" +
                "\t\"address\": \"张三\",\n" +
                "\t\"sex\": \"0\",\n" +
                "\t\"ownerTypeCd\": \"1001\",\n" +
                "\t\"remark\": \"\",\n" +
                "\t\"ownerId\": -1,\n" +
                "\t\"ownerPhoto\": \"\",\n" +
                "\t\"personType\": \"P\",\n" +
                "\t\"personRole\": \"1\",\n" +
                "\t\"idCard\": \"\",\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
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

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IGeneratorOwnerUserBMO generatorOwnerUserBMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(reqJson, "personType", "请求报文中未包含personType");
        Assert.jsonObjectHaveKey(reqJson, "personRole", "请求报文中未包含personRole");

        if (reqJson.containsKey("ownerPhotoUrl")) {
            String ownerPhotoUrl = reqJson.getString("ownerPhotoUrl");
            if (!StringUtil.isEmpty(ownerPhotoUrl) && ownerPhotoUrl.length() > 500) {
                throw new CmdException("图片地址太长");
            }
        }

        //属性校验
        Assert.judgeAttrValue(reqJson);

        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if (!"ON".equals(userValidate)) {
            return;
        }
        //获取手机号(判断手机号是否重复)
        String link = reqJson.getString("link");
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
        Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);

        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson, OwnerPo.class);
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setMemberId(memberId);
        ownerPo.setOwnerId(memberId);
        ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerPo.setAge("0");
        ownerPo.setUserId(userId);

        String ownerPhotoUrl = reqJson.getString("ownerPhotoUrl");


        ownerPo.setFaceUrl(ownerPhotoUrl);
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }

        dealOwnerAttr(reqJson, memberId, cmdDataFlowContext);


        //todo 生成登录账号
        generatorOwnerUserBMOImpl.generator(ownerPo);
    }


    private void dealOwnerAttr(JSONObject paramObj, String memberId, ICmdDataFlowContext cmdDataFlowContext) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (ListUtil.isNull(attrs)) {
            return;
        }

        int flag = 0;
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("memberId", memberId);
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
            flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }

    }
}
