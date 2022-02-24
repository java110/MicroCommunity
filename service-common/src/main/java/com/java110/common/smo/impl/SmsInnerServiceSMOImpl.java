package com.java110.common.smo.impl;


import com.java110.common.dao.IMsgServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.SendSmsFactory;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.msg.SmsDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 消息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class SmsInnerServiceSMOImpl extends BaseServiceSMO implements ISmsInnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(SmsInnerServiceSMOImpl.class);


    @Autowired
    private IMsgServiceDao msgServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public SmsDto send(@RequestBody SmsDto smsDto) {

        try {

            SendSmsFactory.sendSms(smsDto.getTel(), smsDto.getCode());

            //将验证码存入Redis中
            CommonCache.setValue(smsDto.getTel() + SendSmsFactory.VALIDATE_CODE, smsDto.getCode().toLowerCase() + "-" + new Date().getTime(), CommonCache.defaultExpireTime);

            smsDto.setSuccess(true);
            smsDto.setMsg("成功");
        } catch (Exception e) {
            logger.error("发送短信验证码失败", e);
            smsDto.setSuccess(false);
            smsDto.setMsg(e.getMessage());
        }
        return smsDto;

    }


    @Override
    public SmsDto validateCode(@RequestBody SmsDto smsDto) {

        //校验是否有有效的验证码
        String smsCode = CommonCache.getValue(smsDto.getTel() + SendSmsFactory.VALIDATE_CODE);

        if (!StringUtil.isEmpty(smsCode) && smsCode.contains("-")) {
            smsCode = smsCode.substring(0, smsCode.indexOf("-"));
        }

        if (smsDto.getCode().equals(smsCode)) {
            smsDto.setSuccess(true);
            smsDto.setMsg("校验成功");
            return smsDto;
        }
        smsDto.setSuccess(false);
        smsDto.setMsg("校验码不存在或不正确");
        return smsDto;
    }

    public IMsgServiceDao getMsgServiceDaoImpl() {
        return msgServiceDaoImpl;
    }

    public void setMsgServiceDaoImpl(IMsgServiceDao msgServiceDaoImpl) {
        this.msgServiceDaoImpl = msgServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
