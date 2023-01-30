package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "user.userSendSms")
public class UserSendSmsCmd extends Cmd {

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    public static final String ID_CARD_SWITCH = "ID_CARD_SWITCH";

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");

        if (!ValidatorUtil.isMobile(reqJson.getString("tel"))) {
            throw new IllegalArgumentException("手机号格式错误");
        }

        //校验是否有有效的验证码
        String smsCode = CommonCache.getValue(reqJson.getString("tel") + SendSmsFactory.VALIDATE_CODE);

        if (!StringUtil.isEmpty(smsCode) && smsCode.contains("-")) {
            long oldTime = Long.parseLong(smsCode.substring(smsCode.indexOf("-"), smsCode.length()));
            long nowTime = new Date().getTime();
            if (nowTime - oldTime < 1000 * 60 * 2) {
                throw new IllegalArgumentException("请不要重复发送验证码");
            }
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String tel = reqJson.getString("tel");
        String captchaType = reqJson.getString("captchaType");
        if(!StringUtil.isEmpty(captchaType) && "ownerBinding".equals(captchaType)){
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            ownerDto.setName(reqJson.getString("appUserName"));
            ownerDto.setLink(reqJson.getString("tel"));

            //取出开关映射的值
            String val = MappingCache.getValue(DOMAIN_COMMON, ID_CARD_SWITCH);
            //取出身份证
            String idCardErrorMsg ="";
            String idCard = reqJson.getString("idCard");
            if ("1".equals(val) && !StringUtil.isEmpty(idCard)) {
                ownerDto.setIdCard(idCard);
                idCardErrorMsg="或者身份证号";
            }
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
            Assert.listOnlyOne(ownerDtos, "填写业主信息错误，请确认，预留业主姓名、手机号"+idCardErrorMsg+"信息是否正确！");
        }
        //校验是否传了 分页信息
        String msgCode = SendSmsFactory.generateMessageCode(6);
        SmsDto smsDto = new SmsDto();
        smsDto.setTel(tel);
        smsDto.setCode(msgCode);
        if ("ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN,SendSmsFactory.SMS_SEND_SWITCH))) {
            smsDto = smsInnerServiceSMOImpl.send(smsDto);
        } else {
            CommonCache.setValue(smsDto.getTel() + SendSmsFactory.VALIDATE_CODE, smsDto.getCode().toLowerCase() + "-" + new Date().getTime(), CommonCache.defaultExpireTime);
            smsDto.setSuccess(true);
            smsDto.setMsg("您的验证码为" + msgCode);
        }
        ResponseEntity<String> sendMessageResult = new ResponseEntity<String>(smsDto.getMsg(), smsDto.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        context.setResponseEntity(sendMessageResult);

    }
}
