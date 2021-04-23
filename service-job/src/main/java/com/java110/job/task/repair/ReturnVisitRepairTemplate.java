package com.java110.job.task.repair;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairSettingInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairSettingInnerServiceSMO repairSettingInnerServiceSMO;

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
        RepairDto repair = new RepairDto();
        repair.setCommunityId(communityDto.getCommunityId());
        repair.setState(RepairDto.STATE_APPRAISE);
        repair.setReturnVisitFlag("001,003");
        repair.setPage(1);
        repair.setRow(100);
        //查询都回访待价状态数据
        List<RepairDto> repairs = repairInnerServiceSMOImpl.queryRepairs(repair);
        for (RepairDto repairDto : repairs) {
            doFinishRepairAppraise(repairDto);
        }
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityDto.getCommunityId());
        repairDto.setStatess(new String[]{RepairDto.STATE_RETURN_VISIT, RepairDto.STATE_APPRAISE});
        repairDto.setReturnVisitFlag("001,002");
        repairDto.setPage(1);
        repairDto.setRow(100);
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
        if ("001".equals(tmpRepairDto.getReturnVisitFlag()) && RepairDto.STATE_RETURN_VISIT.equals(tmpRepairDto.getState())) {
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
            repairPoolPo.setState(RepairDto.STATE_COMPLATE);
            repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(tmpRepairDto.getRepairId());
            //查询报修信息
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            Assert.listOnlyOne(repairDtos, "报修信息错误");
            //查询报修工单信息
            RepairSettingDto repairSettingDto = new RepairSettingDto();
            repairSettingDto.setSettingId(repairDtos.get(0).getRepairType());
            List<RepairSettingDto> repairSettingDtos = repairSettingInnerServiceSMO.queryRepairSettings(repairSettingDto);
            Assert.listOnlyOne(repairDtos, "报修工单信息错误");
            //获取报修区域
            String publicArea = repairSettingDtos.get(0).getPublicArea();
            //获取报修渠道
            String repairChannel = repairDtos.get(0).getRepairChannel();
            //获取维修类型
            String maintenanceType = repairDtos.get(0).getMaintenanceType();
            if (publicArea.equals("T") && !repairChannel.equals("Z")) {  //如果是公共区域，且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else if (publicArea.equals("F") && maintenanceType.equals("1002") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是无偿的，并且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else if (publicArea.equals("F") && maintenanceType.equals("1001") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是有偿的，并且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else { //如果是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            }
            return;
        }
        if ("002".equals(tmpRepairDto.getReturnVisitFlag())) {
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(tmpRepairDto.getRepairId());
            //查询报修信息
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            Assert.listOnlyOne(repairDtos, "报修信息错误");
            //查询报修工单信息
            RepairSettingDto repairSettingDto = new RepairSettingDto();
            repairSettingDto.setSettingId(repairDtos.get(0).getRepairType());
            List<RepairSettingDto> repairSettingDtos = repairSettingInnerServiceSMO.queryRepairSettings(repairSettingDto);
            Assert.listOnlyOne(repairDtos, "报修工单信息错误");
            //获取报修区域
            String publicArea = repairSettingDtos.get(0).getPublicArea();
            //获取报修渠道0
            String repairChannel = repairDtos.get(0).getRepairChannel();
            //获取维修类型
            String maintenanceType = repairDtos.get(0).getMaintenanceType();
            if (publicArea.equals("T") && !repairChannel.equals("Z")) {  //如果是公共区域，且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE);
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else if (publicArea.equals("F") && maintenanceType.equals("1002") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是无偿的，并且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE);
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else if (publicArea.equals("F") && maintenanceType.equals("1001") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是有偿的，并且不是业主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                Assert.listOnlyOne(repairUserDtoList, "信息错误");
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE);
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else { //如果是业主报修
                if (RepairDto.STATE_APPRAISE.equals(tmpRepairDto.getState())) { //业主未评价
                    RepairUserDto repairUserDto = new RepairUserDto();
                    repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                    repairUserDto.setState(RepairUserDto.STATE_EVALUATE);
                    List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                    Assert.listOnlyOne(repairUserDtoList, "业主没有待评价的记录");
                    saveRepairUserOfOwner(repairUserDtoList, tmpRepairDto);
                } else if (RepairDto.STATE_RETURN_VISIT.equals(tmpRepairDto.getState())) { //业主已评价
                    RepairUserDto repairUserDto = new RepairUserDto();
                    repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                    repairUserDto.setState(RepairUserDto.STATE_FINISH);
                    List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                    Assert.listOnlyOne(repairUserDtoList, "业主没有已评价的记录");
                    saveRepairUser(repairUserDtoList, tmpRepairDto);
                }
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE);
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            }
            return;
        }
    }

    /**
     * 待评价状态超过48小时泡成待回访状态
     *
     * @param tmpRepairDto
     */
    private void doFinishRepairAppraise(RepairDto tmpRepairDto) {
        //如果是待评价状态，判断是否已超过两天未评价
        if (RepairDto.STATE_APPRAISE.equals(tmpRepairDto.getState())) {
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(tmpRepairDto.getRepairId());
            repairUserDto.setState(RepairUserDto.STATE_EVALUATE);
            List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
            Assert.listOnlyOne(repairUserDtoList, "信息错误");
            //获取评价开始时间
            Date startTime = repairUserDtoList.get(0).getStartTime();
            //获取当前时间
            Date nowTime = new Date();
            //判断时间是否超过了两天
            if ((nowTime.getTime() - startTime.getTime()) > 2 * 1000 * 60 * 60 * 24) {
                //超过两天未评价，状态变为待回访状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_RETURN_VISIT);
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
                //r_repair_user状态变为已评价状态
                RepairUserPo repairUserPo = new RepairUserPo();
                repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
                repairUserPo.setState(RepairUserDto.STATE_FINISH);
                repairUserPo.setStaffName("定时任务处理");
                repairUserPo.setContext("已评价");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                repairUserPo.setEndTime(format.format(nowTime));
                repairUserInnerServiceSMO.updateRepairUser(repairUserPo);
            }
        }
    }

    //定时任务跑完新增维修派单状态
    public void saveRepairUser(List<RepairUserDto> repairUserDtoList, RepairDto tmpRepairDto) {
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        repairUserPo.setStartTime(format.format(repairUserDtoList.get(0).getEndTime()));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_FINISH_VISIT);
        repairUserPo.setContext("已回访");
        repairUserPo.setCommunityId(tmpRepairDto.getCommunityId());
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairId(tmpRepairDto.getRepairId());
        repairUserPo.setStaffId("-1");
        repairUserPo.setbId("-1");
        repairUserPo.setStaffName("定时任务处理");
        repairUserPo.setPreStaffId(repairUserDtoList.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtoList.get(0).getStaffName());
        repairUserPo.setPreRuId(repairUserDtoList.get(0).getRuId());
        repairUserPo.setRepairEvent("auditUser");
        repairUserInnerServiceSMO.saveRepairUser(repairUserPo);
    }

    //业主端已评价不回访时修改并新增维修派单状态
    public void saveRepairUserOfOwner(List<RepairUserDto> repairUserDtoList, RepairDto tmpRepairDto) {
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
        repairUserPo.setState(RepairUserDto.STATE_FINISH);
        repairUserPo.setStaffName("定时任务处理");
        repairUserPo.setContext("已评价");
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        //修改报修派单待评价状态，变为评价完成
        repairUserInnerServiceSMO.updateRepairUser(repairUserPo);
        RepairUserPo repairUser = new RepairUserPo();
        repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUser.setStartTime(repairUserPo.getEndTime());
        repairUser.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUser.setState(RepairUserDto.STATE_FINISH_VISIT);
        repairUser.setContext("已回访");
        repairUser.setCommunityId(tmpRepairDto.getCommunityId());
        repairUser.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUser.setRepairId(tmpRepairDto.getRepairId());
        repairUser.setStaffId("-1");
        repairUser.setbId("-1");
        repairUser.setStaffName("定时任务处理");
        repairUser.setPreStaffId(repairUserDtoList.get(0).getStaffId());
        repairUser.setPreStaffName(repairUserDtoList.get(0).getStaffName());
        repairUser.setPreRuId(repairUserDtoList.get(0).getRuId());
        repairUser.setRepairEvent("auditUser");
        repairUserInnerServiceSMO.saveRepairUser(repairUser);
    }
}
