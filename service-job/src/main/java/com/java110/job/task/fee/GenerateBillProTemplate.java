package com.java110.job.task.fee;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.room.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.*;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.TaskTemplateException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 根据 pay_fee_rule 生成 pay_fee 账单
 * 只有小区设置了 按月生成费用 为ON是有效
 *
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateBillProTemplate extends TaskSystemQuartz {

    private static final String TASK_ATTR_VALUE_ONCE_MONTH = "005"; //一次性按月出账


    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;



    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                doRuleCreatePayFeeBill(taskDto, communityDto);
            } catch (Throwable e) {

                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + communityDto.getCommunityId(), e);
            }
        }

    }

    /**
     * 根据 pay_fee_rule 产生 账单
     *
     * @param taskDto
     * @param communityDto
     */
    private void doRuleCreatePayFeeBill(TaskDto taskDto, CommunityDto communityDto) {



        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.covertCommunityPayFee(communityDto.getCommunityId());
    }


}
