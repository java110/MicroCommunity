package com.java110.job.task.visiterToMachine;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.owner.VisitPo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 预约状态修改定时任务
 *
 * @author fqz
 * @date 2022-04-20
 */
@Component
public class EditRecordStateTemplate extends TaskSystemQuartz {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(EditRecordStateTemplate.class);

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        for (CommunityDto communityDto : communityDtos) {
            doEditRecordState(taskDto, communityDto);
        }
    }

    private void doEditRecordState(TaskDto taskDto, CommunityDto communityDto) throws ParseException {
        //查询访客记录信息
        VisitDto visitDto = new VisitDto();
        visitDto.setCommunityId(communityDto.getCommunityId());
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        if (visitDtos == null || visitDtos.size() < 1) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (VisitDto visit : visitDtos) {
            //获取访客离开时间
            String departureTime = visit.getDepartureTime();
            Date finishTime = format.parse(departureTime);
            Date date = new Date();
            if (finishTime.getTime() <= date.getTime()) { //如果访客离开时间小于当前时间，这条访客记录就失效
                VisitPo visitPo = new VisitPo();
                visitPo.setvId(visit.getvId());
                visitPo.setRecordState("1"); //预约状态 0表示有效 1表示无效
                visitInnerServiceSMOImpl.updateVisit(visitPo);
            }
        }
    }

}
