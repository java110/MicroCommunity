package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IApplicationKeyV1InnerServiceSMO;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Java110Cmd(serviceCode = "applicationKey.applyVisitorApplicationKey")
public class ApplyVisitorApplicationKeyCmd extends Cmd {

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ResponseEntity<String> responseEntity = null;
        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setIdCard(reqJson.getString("idCard"));
        applicationKeyDto.setCommunityId(reqJson.getString("communityId"));
        applicationKeyDto.setState("10001"); //审核万彩城
        applicationKeyDto.setTypeFlag("1100103"); //固定密码
        applicationKeyDto.setEndTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        List<ApplicationKeyDto> applicationKeys = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

        if (applicationKeys != null && applicationKeys.size() > 0) {
            JSONObject resObj = new JSONObject();
            resObj.put("pwd", applicationKeys.get(0).getPwd());
            responseEntity = new ResponseEntity<>(resObj.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);

            return;
        }


        applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setIdCard(reqJson.getString("idCard"));
        applicationKeyDto.setCommunityId(reqJson.getString("communityId"));
        applicationKeyDto.setState("10001");
        applicationKeys = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

        if (applicationKeys == null || applicationKeys.size() < 1) {
            throw new IllegalArgumentException("还没有住户密码，不能申请访客密码");
        }

        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DAY_OF_MONTH, 1);
        String endTime = DateUtil.getFormatTimeString(cl.getTime(), DateUtil.DATE_FORMATE_STRING_A);
        String pwd = getRandom();

        for (ApplicationKeyDto tmpApplicationKey : applicationKeys) {
            //添加单元信息
            reqJson.putAll(BeanConvertUtil.beanCovertMap(tmpApplicationKey));

            reqJson.put("endTime", endTime);
            reqJson.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));
            reqJson.put("pwd", pwd);
            addApplicationVisitKey(reqJson);
        }


        JSONObject resObj = new JSONObject();
        resObj.put("pwd", reqJson.getString("pwd"));
        resObj.put("endTime", endTime);

        responseEntity = new ResponseEntity<>(resObj.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addApplicationVisitKey(JSONObject paramInJson) {

        //查询 是否住户密码已经审核完

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        applicationKeyPo.setApplicationKeyId(paramInJson.getString("applicationKeyId"));
        applicationKeyPo.setState("10001");
        applicationKeyPo.setTypeFlag("1100103");
        applicationKeyPo.setStartTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        int flag = applicationKeyV1InnerServiceSMOImpl.saveApplicationKey(applicationKeyPo);
        if (flag < 1) {
            throw new CmdException("申请钥匙失败");
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);
            ;
        }
        return result;
    }
}
