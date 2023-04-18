package com.java110.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.MachineAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IMachineAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.machineAuth.MachineAuthPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 添加员工门禁授权
 *
 * @author fqz
 * @date 2022-10-29
 */
@Java110Cmd(serviceCode = "machineAuth.saveMachineAuth")
public class SaveMachineAuth extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(SaveMachineAuth.class);

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.jsonObjectHaveKey(reqJson, "personId", "请求报文中未包含员工ID");
        Assert.jsonObjectHaveKey(reqJson, "machineId", "请求报文中未包含设备ID");
        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setMachineId(reqJson.getString("machineId"));
        machineAuthDto.setPersonId(reqJson.getString("personId"));
        List<MachineAuthDto> machineAuthDtos = machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (machineAuthDtos != null && machineAuthDtos.size() > 0) {
            for (MachineAuthDto machineAuth : machineAuthDtos) {
                //获取结束时间
                Date endTime = simpleDateFormat.parse(machineAuth.getEndTime());
                //开始时间
                Date newStartTime = simpleDateFormat.parse(reqJson.getString("startTime"));
                int i = newStartTime.compareTo(endTime);
                if (i < 0) { //上次结束时间大于本次开始时间
                    throw new IllegalArgumentException("该时间段内该员工已经授权过该设备，无法再次进行授权！");
                }
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(reqJson, MachineAuthPo.class);
        if (!StringUtil.isEmpty(reqJson.getString("personId"))) {
            UserDto userDto = new UserDto();
            userDto.setUserId(reqJson.getString("personId"));
            List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
            Assert.listOnlyOne(users, "查询用户错误！");
            machineAuthPo.setPersonName(users.get(0).getName());
        } else {
            machineAuthPo.setPersonName("-1");
        }
        machineAuthPo.setAuthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_authId));
        machineAuthPo.setPersonType("1001"); //员工
        machineAuthPo.setState("10301"); //待审核 10301  审核通过 10302  审核不通过 10303
        machineAuthPo.setbId("-1");
        int flag = machineAuthInnerServiceSMOImpl.saveMachineAuth(machineAuthPo);
        if (flag < 1) {
            throw new CmdException("添加员工门禁授权失败");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
