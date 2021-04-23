package com.java110.job.task.repair;

import com.java110.core.factory.CallApiServiceFactory;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: MicroCommunity
 * @description: 定时任务 轮训派单
 * @author: wuxw
 * @create: 2020-06-15 13:35
 **/
@Component
public class TrainingRepairTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(TrainingRepairTemplate.class);

    private static final int EXPIRE_IN = 7200;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                trainingRepair(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    /**
     * 轮训派单
     *
     * @param taskDto
     * @param communityDto
     */
    private void trainingRepair(TaskDto taskDto, CommunityDto communityDto) {
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityDto.getCommunityId());
        repairDto.setState(RepairDto.STATE_WAIT);
        repairDto.setRepairWay(RepairDto.REPAIR_WAY_TRAINING);
        //查询需要程序轮训派单 订单
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        for (RepairDto tmpRepairDto : repairDtos) {
            RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
            repairTypeUserDto.setCommunityId(communityDto.getCommunityId());
            repairTypeUserDto.setRepairType(tmpRepairDto.getRepairType());
            repairTypeUserDto.setState(RepairTypeUserDto.STATE_ONLINE); //派单给在线员工
            List<RepairTypeUserDto> repairTypeUserDtos = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsers(repairTypeUserDto);
            //没有设置 处理师傅 无法派单
            if (repairTypeUserDtos == null || repairTypeUserDtos.size() < 1) {
                continue;
            }
            doTrainingRepair(tmpRepairDto, repairTypeUserDtos);

        }

    }

    /**
     * 将改订单 派给下面 师傅
     *
     * @param tmpRepairDto
     * @param repairTypeUserDtos
     */
    private void doTrainingRepair(RepairDto tmpRepairDto, List<RepairTypeUserDto> repairTypeUserDtos) {

        if (repairTypeUserDtos.size() == 1) {
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setUserId(repairTypeUserDtos.get(0).getStaffId());
            repairUserDto.setUserName(repairTypeUserDtos.get(0).getStaffName());
            repairUserDto.setCommunityId(repairTypeUserDtos.get(0).getCommunityId());
            repairUserDto.setRepairId(tmpRepairDto.getRepairId());
            CallApiServiceFactory.postForApi(AppDto.WEB_APP_ID, repairUserDto, ServiceCodeRepairDispatchStepConstant.BINDING_GRABBING_REPAIR, RepairUserDto.class);
            return;
        }

        String sizeStr = CommonCache.getValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-size");
        if (StringUtil.isEmpty(sizeStr)) {
            CommonCache.setValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-size", repairTypeUserDtos.size() + "", EXPIRE_IN);
            sizeStr = repairTypeUserDtos.size() + "";
        }

        int size = Integer.parseInt(sizeStr);
        //总数不一致从新设置 随机判断就好
        if (size != repairTypeUserDtos.size()) {
            size = repairTypeUserDtos.size();
            CommonCache.setValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-size", size + "", EXPIRE_IN);
            int index = (int) (Math.random() * repairTypeUserDtos.size());
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setUserId(repairTypeUserDtos.get(index).getStaffId());
            repairUserDto.setUserName(repairTypeUserDtos.get(index).getStaffName());
            repairUserDto.setCommunityId(repairTypeUserDtos.get(index).getCommunityId());
            repairUserDto.setRepairId(tmpRepairDto.getRepairId());
            CallApiServiceFactory.postForApi(AppDto.WEB_APP_ID, repairUserDto, ServiceCodeRepairDispatchStepConstant.BINDING_GRABBING_REPAIR, RepairUserDto.class);
            return;
        }


        String repairIndexStr = CommonCache.getValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-index");
        if (StringUtil.isEmpty(repairIndexStr)) {
            CommonCache.setValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-index", "0", EXPIRE_IN);
            repairIndexStr = "0";
        }

        int repairIndex = Integer.parseInt(repairIndexStr);
        if (repairIndex < size - 1) {
            repairIndex += 1;
        } else {
            repairIndex = 0;
        }

        CommonCache.setValue(tmpRepairDto.getRepairType() + "-" + tmpRepairDto.getCommunityId() + "-staff-index", repairIndex + "", EXPIRE_IN);

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setUserId(repairTypeUserDtos.get(repairIndex).getStaffId());
        repairUserDto.setUserName(repairTypeUserDtos.get(repairIndex).getStaffName());
        repairUserDto.setCommunityId(repairTypeUserDtos.get(repairIndex).getCommunityId());
        repairUserDto.setRepairId(tmpRepairDto.getRepairId());
        CallApiServiceFactory.postForApi(AppDto.WEB_APP_ID, repairUserDto, ServiceCodeRepairDispatchStepConstant.BINDING_GRABBING_REPAIR, RepairUserDto.class);


    }


}
