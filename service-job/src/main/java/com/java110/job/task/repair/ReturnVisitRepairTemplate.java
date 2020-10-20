package com.java110.job.task.repair;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.owner.RepairPoolPo;
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
public class ReturnVisitRepairTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(ReturnVisitRepairTemplate.class);

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
                returnVisitRepair(taskDto, communityDto);
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
    private void returnVisitRepair(TaskDto taskDto, CommunityDto communityDto) {
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityDto.getCommunityId());
        repairDto.setStatess(new String[]{RepairDto.STATE_RETURN_VISIT, RepairDto.STATE_APPRAISE});
        repairDto.setReturnVisitFlag("001,002");
        repairDto.setPage(1);
        repairDto.setRow(1000);
        //查询需要程序轮训派单 订单
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        for (RepairDto tmpRepairDto : repairDtos) {

            doReturnVisitRepair(tmpRepairDto);

        }

    }

    /**
     * 将改订单 派给下面 师傅
     *
     * @param tmpRepairDto
     */
    private void doReturnVisitRepair(RepairDto tmpRepairDto) {

        if ("001".equals(tmpRepairDto.getReturnVisitFlag())) {
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
            repairPoolPo.setState(RepairDto.STATE_COMPLATE);
            repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            return;
        }

        if ("002".equals(tmpRepairDto.getReturnVisitFlag()) && RepairDto.STATE_APPRAISE.equals(tmpRepairDto.getState())) {
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
            repairPoolPo.setState(RepairDto.STATE_COMPLATE);
            repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            return;
        }


    }


}
