package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserAttrDto;
import com.java110.intf.user.IUserAttrV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.user.UserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "user.saveUser")
public class SaveUserCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "name", "请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "address", "请求报文格式错误或未包含地址信息");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(reqJson, "levelCd", "请求报文格式错误或未包含员工角色");

        if (reqJson.containsKey("email") && !StringUtil.isEmpty(reqJson.getString("email"))) {
            Assert.isEmail(reqJson, "email", "不是有效的邮箱格式");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
//判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        String userId = "";
        if (!reqJson.containsKey("userId") || "-1".equals(reqJson.getString("userId"))) {
            userId = GenerateCodeFactory.getUserId();
            reqJson.put("userId", userId);
        }
        String staffDefaultPassword = reqJson.getString("password");
        if (StringUtil.isEmpty(staffDefaultPassword)) {
            //设置默认密码
            staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
            Assert.hasLength(staffDefaultPassword, "映射表中未设置默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        }

        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);
        reqJson.put("password", staffDefaultPassword);
        UserPo userPo = BeanConvertUtil.covertBean(reqJson, UserPo.class);
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("保存用户失败");
        }

        if (!StringUtil.isEmpty(reqJson.getString("extUserId"))) {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(reqJson.getString("userId"));
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_PROPERTY_USER_ID);
            userAttrPo.setValue(reqJson.getString("extUserId"));
            flag = userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
            if (flag < 1) {
                throw new CmdException("更新失败");
            }
        }
    }
}
