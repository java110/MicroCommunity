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
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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
        repair.setState(RepairDto.STATE_APPRAISE); //待评价
        repair.setReturnVisitFlag("001,003"); //回访标识 001 都不回访 002 已评价不回访 003 都回访
        repair.setPage(1);
        repair.setRow(100);
        //查询都回访待评价状态数据
        List<RepairDto> repairs = repairInnerServiceSMOImpl.queryRepairs(repair);
        for (RepairDto repairDto : repairs) {
            //超过了配置时间变为待回访状态处理
            doFinishRepairAppraise(repairDto);
        }
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityDto.getCommunityId());
        repairDto.setStatess(new String[]{RepairDto.STATE_RETURN_VISIT, RepairDto.STATE_APPRAISE}); //待回访 待评价
        repairDto.setReturnVisitFlag("001,002"); //回访标识 001 都不回访 002 已评价不回访 003 都回访
        repairDto.setPage(1);
        repairDto.setRow(100);
        //查询状态为待评价、待回访状态的，且不需要回访的
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
        if ("001".equals(tmpRepairDto.getReturnVisitFlag()) && RepairDto.STATE_RETURN_VISIT.equals(tmpRepairDto.getState())) { //状态为待回访且都不回访时
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
            repairPoolPo.setState(RepairDto.STATE_COMPLATE); //办理完成
            repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            //状态为待回访，且都不回访时，直接变为办理完成状态
            repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(tmpRepairDto.getRepairId());
            //查询报修信息
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            if (repairDtos == null || repairDtos.size() != 1) {
                return;
            }
            //查询报修工单信息
            RepairSettingDto repairSettingDto = new RepairSettingDto();
            repairSettingDto.setSettingId(repairDtos.get(0).getRepairType());
            List<RepairSettingDto> repairSettingDtos = repairSettingInnerServiceSMO.queryRepairSettings(repairSettingDto);
            if (repairSettingDtos == null || repairSettingDtos.size() != 1) {
                return;
            }
            //获取报修区域
            String publicArea = repairSettingDtos.get(0).getPublicArea(); //是否为公共区域 T是 F否
            //获取报修渠道
            String repairChannel = repairDtos.get(0).getRepairChannel(); //报修渠道  D员工代客报修  T电话报修 Z业主自主保修
            //获取维修类型
            String maintenanceType = repairDtos.get(0).getMaintenanceType(); //维修类型  1001有偿服务  1002无偿服务 1003需要用料 1004无需用料
            if (publicArea.equals("T") && !repairChannel.equals("Z")) { //如果是公共区域，且不是业主自主报修，就把工单状态变为结单状态
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE); //结单
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else if (publicArea.equals("F") && maintenanceType.equals("1002") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是无偿的，并且不是业主自主报修，工单状态就变为结单状态
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE); //结单
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else if (publicArea.equals("F") && maintenanceType.equals("1001") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是有偿的，并且不是业主自主报修，工单状态就变为已支付
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE); //已支付
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            } else { //如果是业主自主报修，工单状态就变为评价完成
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH); //评价完成
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
            }
            return;
        }
        if ("002".equals(tmpRepairDto.getReturnVisitFlag())) { //回访标识 001 都不回访 002 已评价不回访 003 都回访
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(tmpRepairDto.getRepairId());
            //查询报修信息
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            if (repairDtos == null || repairDtos.size() != 1) {
                return;
            }
            //查询报修工单信息
            RepairSettingDto repairSettingDto = new RepairSettingDto();
            repairSettingDto.setSettingId(repairDtos.get(0).getRepairType());
            List<RepairSettingDto> repairSettingDtos = repairSettingInnerServiceSMO.queryRepairSettings(repairSettingDto);
            if (repairSettingDtos == null || repairSettingDtos.size() != 1) {
                return;
            }
            //获取报修区域
            String publicArea = repairSettingDtos.get(0).getPublicArea();
            //获取报修渠道
            String repairChannel = repairDtos.get(0).getRepairChannel();
            //获取维修类型
            String maintenanceType = repairDtos.get(0).getMaintenanceType();
            if (publicArea.equals("T") && !repairChannel.equals("Z")) {  //如果是公共区域，且不是业主自主报修，工单类型就变为结单状态
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE); //结单
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE); //办理完成
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else if (publicArea.equals("F") && maintenanceType.equals("1002") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是无偿的，并且不是业主自主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_CLOSE); //结单
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE); //办理完成
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else if (publicArea.equals("F") && maintenanceType.equals("1001") && !repairChannel.equals("Z")) {  //如果不是公共区域，且是有偿的，并且不是业主自主报修
                RepairUserDto repairUserDto = new RepairUserDto();
                repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE); //已支付
                List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                    return;
                }
                saveRepairUser(repairUserDtoList, tmpRepairDto);
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE); //办理完成
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            } else { //如果是业主自主报修
                if (RepairDto.STATE_APPRAISE.equals(tmpRepairDto.getState())) { //业主未评价(待评价状态)
                    RepairUserDto repairUserDto = new RepairUserDto();
                    repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                    repairUserDto.setState(RepairUserDto.STATE_EVALUATE); //待评价
                    List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                    if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                        return;
                    }
                    saveRepairUserOfOwner(repairUserDtoList, tmpRepairDto);
                } else if (RepairDto.STATE_RETURN_VISIT.equals(tmpRepairDto.getState())) { //业主已评价(待回访状态)
                    RepairUserDto repairUserDto = new RepairUserDto();
                    repairUserDto.setRepairId(tmpRepairDto.getRepairId());
                    repairUserDto.setState(RepairUserDto.STATE_FINISH); //评价完成
                    List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
                    if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                        return;
                    }
                    saveRepairUser(repairUserDtoList, tmpRepairDto);
                }
                //改变repair_pool状态
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(tmpRepairDto.getRepairId());
                repairPoolPo.setState(RepairDto.STATE_COMPLATE); //办理完成
                repairPoolPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
                repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
            }
            return;
        }
    }

    /**
     * 待评价状态超过48小时跑成待回访状态
     *
     * @param tmpRepairDto
     */
    private void doFinishRepairAppraise(RepairDto tmpRepairDto) {
        //如果是待评价都回访状态，判断是否已超过配置时间(系统dev下配置的时间)未评价
        if (RepairDto.STATE_APPRAISE.equals(tmpRepairDto.getState())) {
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(tmpRepairDto.getRepairId());
            repairUserDto.setState(RepairUserDto.STATE_EVALUATE);
            List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
            if (repairUserDtoList == null || repairUserDtoList.size() != 1) {
                return;
            }
            //获取评价开始时间
            Date startTime = repairUserDtoList.get(0).getStartTime();
            //获取当前时间
            Date nowTime = new Date();
            //默认48小时
            Integer autoEvaluateHour = 48;
            if (StringUtil.isEmpty(MappingCache.getValue(MappingConstant.REPAIR_DOMAIN, "autoEvaluateHour"))) {
                autoEvaluateHour = Integer.valueOf(MappingCache.getValue(MappingConstant.REPAIR_DOMAIN, "autoEvaluateHour"));
            }
            if ((nowTime.getTime() - startTime.getTime()) > autoEvaluateHour * 1000 * 60 * 60) { //如果评价开始时间距离当前时间超过了配置时间，就变为待回访状态
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
