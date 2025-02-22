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
import com.java110.dto.account.AccountDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.account.AccountPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
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
        seq = 10)

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
        })

@Java110ExampleDoc(
        reqBody = "{\n" +
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
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
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

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.judgeAttrValue(reqJson);
        //获取手机号(判断手机号是否重复)
        String link = reqJson.getString("link");
        if (!StringUtil.isEmpty(link) && link.contains("*")) {
            reqJson.put("link", "");
        }
        //获取身份证号(判断身份证号是否重复)
        String idCard = reqJson.getString("idCard");
        if (!StringUtil.isEmpty(idCard) && idCard.contains("*")) {
            reqJson.put("idCard", "");
        }

        if (reqJson.containsKey("ownerPhotoUrl")) {
            String ownerPhotoUrl = reqJson.getString("ownerPhotoUrl");
            if (!StringUtil.isEmpty(ownerPhotoUrl) && ownerPhotoUrl.length() > 500) {
                throw new CmdException("图片地址太长");
            }
            if (!StringUtil.isEmpty(ownerPhotoUrl)) {
                reqJson.put("faceUrl", ownerPhotoUrl);
            }
        }


        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if (!"ON".equals(userValidate)) {
            return;
        }

        OwnerDto curOwner = new OwnerDto();
        curOwner.setMemberId(reqJson.getString("memberId"));

        List<OwnerDto> curOwners = ownerInnerServiceSMOImpl.queryOwners(curOwner);
        Assert.listOnlyOne(curOwners, "未查询到业主信息或查询到多条");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
        if (ownerDtos != null && ownerDtos.size() > 1) {
            throw new IllegalArgumentException("手机号重复，请重新输入");
        } else if (ownerDtos != null && ownerDtos.size() == 1) {
            for (OwnerDto owner : ownerDtos) {
                if (!reqJson.getString("memberId").equals(owner.getMemberId())) {
                    throw new IllegalArgumentException("手机号重复，请重新输入");
                }
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        //todo 修改 业主信息
        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson, OwnerPo.class);
        int flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("修改业主失败");
        }

        //todo 修改账户
        updateAccount(reqJson);

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (ListUtil.isNull(attrs)) {
            return;
        }
        JSONObject attr = null;
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
        //todo 如果 业主做了绑定则修改绑定的手机号
        updateOwnerAppUser(reqJson);
    }

    public void updateAccount(JSONObject paramInJson) {

        //todo 判断业主手机号和账户手机号是否相同，不相同修改账户手机号
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(paramInJson.getString("memberId"));
        accountDto.setPartId(paramInJson.getString("communityId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (ListUtil.isNull(accountDtos)) {
            return;
        }
        //查询更新后的业主信息
        OwnerDto owner = new OwnerDto();
        owner.setMemberId(paramInJson.getString("memberId"));
        List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwnerMembers(owner);
        Assert.listOnlyOne(owners, "未查询到业主信息或查询到多条");
        if (!accountDtos.get(0).getLink().equals(owners.get(0).getLink()) || !accountDtos.get(0).getAcctName().equals(owners.get(0).getName())) {
            AccountPo accountPo = new AccountPo();
            accountPo.setAcctName(owners.get(0).getName());
            accountPo.setoLink(owners.get(0).getLink());
            accountPo.setAcctId(accountDtos.get(0).getAcctId());
            accountInnerServiceSMOImpl.updateAccount(accountPo);
        }
    }

    /**
     * 如果 业主做了绑定则修改绑定的手机号
     *
     * @param reqJson
     */
    private void updateOwnerAppUser(JSONObject reqJson) {
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));
        //todo 查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)) {
            return;
        }
        for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
            ownerAppUserPo.setLink(reqJson.getString("link"));
            ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
            if (StringUtil.isEmpty(ownerAppUser.getUserId())) {
                continue;
            }
            if (ownerAppUser.getUserId().startsWith("-")) {
                continue;
            }
            // todo 修改用户信息
            UserPo userPo = new UserPo();
            userPo.setUserId(ownerAppUserDtos.get(0).getUserId());
            userPo.setTel(reqJson.getString("link"));
            userV1InnerServiceSMOImpl.updateUser(userPo);
        }
    }
}
