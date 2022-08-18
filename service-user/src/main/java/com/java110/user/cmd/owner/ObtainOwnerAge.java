package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Java110Cmd(serviceCode = "owner.obtainAge")
public class ObtainOwnerAge extends Cmd {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "idCard", "请求报文中未包含身份证号码");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        //属性校验
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException, ParseException {
        //获取身份证号码
        String idCard = reqJson.getString("idCard");
        String year = null;
        String month = null;
        String day = null;
        Date birthDay = null;
        String msg = null;
        OwnerDto ownerDto = new OwnerDto();
        //正则匹配身份证号是否是正确的，15位或者17位数字+数字/x/X
        if (idCard.matches("^\\d{15}|\\d{17}[\\dxX]$")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            year = idCard.substring(6, 10);
            month = idCard.substring(10, 12);
            day = idCard.substring(12, 14);
            birthDay = simpleDateFormat.parse(year + "-" + month + "-" + day);
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
                msg = "业主出生日期晚于当前时间";
                ownerDto.setMsg(msg);
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(ownerDto), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
            }
            int yearNow = cal.get(Calendar.YEAR); //当前年份
            int monthNow = cal.get(Calendar.MONTH) + 1; //当前月份
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
            int age = yearNow - Integer.parseInt(year); //计算整岁
            if (monthNow <= Integer.parseInt(month)) {
                if (monthNow == Integer.parseInt(month)) {
                    if (dayOfMonthNow < Integer.parseInt(day)) { //当前日期在生日之前，年龄减一岁
                        age--;
                    }
                } else { //当前月份在生日之前，年龄减一岁
                    age--;
                }
            }
            ownerDto.setAge(String.valueOf(age));
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(ownerDto), HttpStatus.OK);
            cmdDataFlowContext.setResponseEntity(responseEntity);
        }
    }
}
