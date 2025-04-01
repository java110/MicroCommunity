package com.java110.community.cmd.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Java110Cmd(serviceCode = "repair.queryPhoneRepairs")
public class QueryPhoneRepairsCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    public static final String VIEW_LIST_STAFF_REPAIRS = "VIEW_LIST_STAFF_REPAIRS";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
    }

    /**
     * 报修已办
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);

        ownerRepairDto.setTel(userDtos.get(0).getTel());
        if (reqJson.containsKey("repairStates")) {
            String[] states = reqJson.getString("repairStates").split(",");
            ownerRepairDto.setStatess(states);
        } else {
            //Pc WEB维修已办
            String[] states = {RepairDto.STATE_BACK, RepairDto.STATE_TRANSFER, RepairDto.STATE_PAY, RepairDto.STATE_PAY_ERROR, RepairDto.STATE_APPRAISE, RepairDto.STATE_RETURN_VISIT, RepairDto.STATE_COMPLATE};
            ownerRepairDto.setStatess(states);
        }

        int count = repairInnerServiceSMOImpl.queryRepairsCount(ownerRepairDto);
        List<RepairDto> repairDtos;
        if (count > 0) {
            repairDtos = repairInnerServiceSMOImpl.queryRepairs(ownerRepairDto);
            computeRepairScore(repairDtos);
        } else {
            repairDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count, repairDtos);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算评分
     *
     * @param repairDtos
     */
    private void computeRepairScore(List<RepairDto> repairDtos) {
        if (ListUtil.isNull(repairDtos)) {
            return;
        }
        Date finishTime = null;
        String submitHour;
        Date timeout = null;
        for (RepairDto repairDto : repairDtos) {
            //获取综合评价得分
            String appraiseScoreNumber = repairDto.getAppraiseScore();
            Double appraiseScoreNum = 0.0;
            if (!StringUtil.isEmpty(appraiseScoreNumber)) {
                appraiseScoreNum = Double.parseDouble(appraiseScoreNumber);
            }
            int appraiseScore = (int) Math.ceil(appraiseScoreNum);
            //获取上门速度评分
            String doorSpeedScoreNumber = repairDto.getDoorSpeedScore();
            Double doorSpeedScoreNum = 0.0;
            if (!StringUtil.isEmpty(doorSpeedScoreNumber)) {
                doorSpeedScoreNum = Double.parseDouble(doorSpeedScoreNumber);
            }
            int doorSpeedScore = (int) Math.ceil(doorSpeedScoreNum);
            //获取维修员服务评分
            String repairmanServiceScoreNumber = repairDto.getRepairmanServiceScore();
            Double repairmanServiceScoreNum = 0.0;
            if (!StringUtil.isEmpty(repairmanServiceScoreNumber)) {
                repairmanServiceScoreNum = Double.parseDouble(repairmanServiceScoreNumber);
            }
            int repairmanServiceScore = (int) Math.ceil(repairmanServiceScoreNum);
            //取得平均分
            double averageNumber = (appraiseScoreNum + doorSpeedScoreNum + repairmanServiceScoreNum) / 3.0;
            BigDecimal averageNum = new BigDecimal(averageNumber);
            Double average = averageNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            repairDto.setAppraiseScore(String.valueOf(appraiseScore));
            repairDto.setDoorSpeedScore(String.valueOf(doorSpeedScore));
            repairDto.setRepairmanServiceScore(String.valueOf(repairmanServiceScore));
            repairDto.setAverage(String.valueOf(average));

            // 计算提单时长
            finishTime = DateUtil.getCurrentDate();
            repairDto.setTimeoutFlag("N");
            if (!StringUtil.isEmpty(repairDto.getFinishTime())) {
                finishTime = DateUtil.getDateFromStringA(repairDto.getFinishTime());
            } else {
                timeout = DateUtil.getDateFromStringA(repairDto.getTimeout());
                if (finishTime.getTime() > timeout.getTime()) {
                    repairDto.setStateName(repairDto.getStateName() + "(超时)");
                    repairDto.setTimeoutFlag("Y");
                }
            }
            submitHour = DateUtil.calculateTimeDifference(repairDto.getCreateTime(), finishTime);
            repairDto.setSubmitHours(submitHour);

        }
    }
}
