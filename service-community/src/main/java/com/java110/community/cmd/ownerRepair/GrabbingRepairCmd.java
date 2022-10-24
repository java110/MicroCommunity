package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.grabbingRepair")
public class GrabbingRepairCmd extends Cmd {

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMO;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(维修师傅未处理最大单数)
    public static final String REPAIR_NUMBER = "REPAIR_NUMBER";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
//        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户ID");
//        Assert.hasKeyAndValue(reqJson, "userName", "未包含用户名称");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        Assert.hasLength(userId, "员工不存在");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "未查询到用户信息");

        reqJson.put("userId", userId);
        reqJson.put("userName", userDtos.get(0).getName());

        int flag = 0;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("repairId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            //获取当前处理员工id
            String staffId = reqJson.getString("userId");
            /*RepairUserDto repairUser = new RepairUserDto();
            repairUser.setStaffId(staffId);
            repairUser.setState("10001"); //处理中
            int i = repairUserInnerServiceSMOImpl.queryRepairUsersCount(repairUser);*/
            RepairDto repair = new RepairDto();
            repair.setStaffId(staffId);
            repair.setCommunityId(reqJson.getString("communityId"));
            int i = repairInnerServiceSMOImpl.queryStaffRepairsCount(repair);
            //取出开关映射的值(维修师傅未处理最大单数)
            String repairNumber = MappingCache.getValue(DOMAIN_COMMON, REPAIR_NUMBER);
            if (i >= Integer.parseInt(repairNumber)) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您有超过" + Integer.parseInt(repairNumber) + "条未处理的订单急需处理，请处理完成后再进行抢单！");
                context.setResponseEntity(responseEntity);
                return;
            }
            RepairDto repairDtoData = new RepairDto();
            repairDtoData.setRepairId(reqJson.getString("repairId"));
            repairDtoData.setCommunityId(reqJson.getString("communityId"));
            List<RepairDto> repairDtoList = repairInnerServiceSMOImpl.queryRepairs(repairDtoData);
            if (repairDtoList != null && repairDtoList.size() != 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "未找到工单信息或找到多条！");
                context.setResponseEntity(responseEntity);
                return;
            }
            //获取报修类型
            String repairType = repairDtoList.get(0).getRepairType();
            RepairTypeUserDto repairTypeUser = new RepairTypeUserDto();
            repairTypeUser.setStaffId(staffId);
            repairTypeUser.setRepairType(repairType);
            //查询工单设置表
            List<RepairTypeUserDto> repairTypeUserDtos = repairTypeUserInnerServiceSMO.queryRepairTypeUsers(repairTypeUser);
            if (repairTypeUserDtos != null && repairTypeUserDtos.size() != 1) { //报修类型设置未添加改操作的员工！
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "对不起，您还没权限进行此操作，请联系管理员处理！");
                context.setResponseEntity(responseEntity);
                return;
            }
            //获取维修师傅状态
            String staffState = repairTypeUserDtos.get(0).getState();
            if (!StringUtil.isEmpty(staffState) && staffState.equals("8888")) { //离线状态
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "员工处于离线状态，无法进行操作！");
                context.setResponseEntity(responseEntity);
                return;
            }
            RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
            repairTypeUserDto.setCommunityId(reqJson.getString("communityId"));
            repairTypeUserDto.setRepairType(repairType);
            repairTypeUserDto.setStaffId(reqJson.getString("userId"));
            int count = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsersCount(repairTypeUserDto);
            if (count < 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您没有权限抢该类型报修单！");
                context.setResponseEntity(responseEntity);
                return;
            }
            //获取报修id
            String repairId = reqJson.getString("repairId");
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(repairId);
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            if (repairDtos == null || repairDtos.size() < 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "数据错误！");
                context.setResponseEntity(responseEntity);
            } else {
                //获取状态
                String state = repairDtos.get(0).getState();
                if (state.equals("1000")) {   //1000表示未派单
                    RepairUserDto repairUserDto = new RepairUserDto();
                    repairUserDto.setRepairId(repairId);
                    repairUserDto.setCommunityId(reqJson.getString("communityId"));
                    repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
                    List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
                    Assert.listOnlyOne(repairUserDtos, "未找到开始节点或找到多条");
                    userId = reqJson.getString("userId");
                    String userName = reqJson.getString("userName");
                    RepairUserPo repairUserPo = new RepairUserPo();
                    repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
                    repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                    repairUserPo.setState(RepairUserDto.STATE_DOING);
                    repairUserPo.setRepairId(reqJson.getString("repairId"));
                    repairUserPo.setStaffId(userId);
                    repairUserPo.setStaffName(userName);
                    repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
                    repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
                    repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
                    repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
                    repairUserPo.setContext("");
                    repairUserPo.setCommunityId(reqJson.getString("communityId"));
                    flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
                    if (flag < 1) {
                        throw new CmdException("修改用户失败");
                    }
                    modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_TAKING);
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
                    context.setResponseEntity(responseEntity);
                } else if (state.equals("1100")) {   //1100表示接单
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "该订单处于接单状态，无法进行抢单！");
                    context.setResponseEntity(responseEntity);
                } else {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "状态异常！");
                    context.setResponseEntity(responseEntity);
                }
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    public void modifyBusinessRepairDispatch(JSONObject paramInJson, String state) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", state);
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
    }
}
