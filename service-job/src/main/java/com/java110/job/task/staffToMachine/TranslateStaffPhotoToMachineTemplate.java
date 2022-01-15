package com.java110.job.task.staffToMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.user.UserDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TransalateOwnerPhotoToMachine
 * @Description TODO 传输员工图片到门禁 任务
 * @Author wuxw
 * @Date 2020/6/3 20:59
 * @Version 1.0
 * add by wuxw 2020/6/3
 **/
@Component
    public class TranslateStaffPhotoToMachineTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(TranslateStaffPhotoToMachineTemplate.class);

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        logger.debug("任务在执行" + taskDto.toString());

        OrderDto orderDto = new OrderDto();
        String[] businessTypeCds = new String[]{"100100030001","100100040001"};
        orderDto.setBusinessTypeCds(businessTypeCds);
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBusinessType(orderDto);

        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));

                UserDto userDto = new UserDto();
                userDto.setbId(tmpOrderDto.getbId());
                List<UserDto> userDtos = userInnerServiceSMOImpl.getStaffs(userDto);

                if (userDtos == null || userDtos.size() < 1) {
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }
                dealData(tmpOrderDto, userDtos.get(0));
                //刷新 状态为C1
                orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                logger.debug("处理订单结束" + JSONObject.toJSONString(tmpOrderDto));

            } catch (Exception e) {
                logger.error("执行订单任务失败", e);
            }
        }

    }

    /**
     * 将员工数据同步给所有该小区设备
     *
     * @param tmpOrderDto
     * @param userDto
     */
    private void dealData(OrderDto tmpOrderDto, UserDto userDto) {

        List<String> communityIds = new ArrayList<>();

        if ("9999".equals(userDto.getBelongCommunityId())) {
            CommunityMemberDto communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setMemberId(userDto.getStoreId());
            communityMemberDto.setAuditStatusCd("1100");//审核通过的
            List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

            if (communityMemberDtos == null || communityMemberDtos.size() < 1) {
                return;
            }

            for (CommunityMemberDto tmpCommunityMemberDto : communityMemberDtos) {
                communityIds.add(tmpCommunityMemberDto.getCommunityId());
            }
        } else {
            OrgCommunityDto orgCommunityDto = new OrgCommunityDto();
            orgCommunityDto.setOrgId(userDto.getParentOrgId());
            orgCommunityDto.setStoreId(userDto.getStoreId());
            List<OrgCommunityDto> orgCommunityDtos = orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto);

            if (orgCommunityDtos == null || orgCommunityDtos.size() < 1) {
                return;
            }

            for (OrgCommunityDto tmpOrgCommunityDto : orgCommunityDtos) {
                communityIds.add(tmpOrgCommunityDto.getCommunityId());
            }
        }

        for (String communityId : communityIds) {
            //根据小区ID查询现有设备
            MachineDto machineDto = new MachineDto();
            machineDto.setCommunityId(communityId);
            List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);


            for (MachineDto tmpMachineDto : machineDtos) {
                if (!"9997".equals(tmpMachineDto.getMachineTypeCd())) {
                    continue;
                }
                saveMachineTranslate(tmpOrderDto, tmpMachineDto, userDto);
            }

        }

    }


    private void saveMachineTranslate(OrderDto tmpOrderDto, MachineDto tmpMachineDto, UserDto userDto) {
        //       MachineTranslateDto machineTranslateDto = new MachineTranslateDto();


//        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
//        machineTranslateDto.setObjId(userDto.getUserId());
//        machineTranslateDto.setStatusCd("0");
//
//        int count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);
//        if (count > 0) {
//            updateMachineTranslate(tmpMachineDto, userDto);
//            return;
//        }
   //     Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        MachineTranslateDto tmpMachineTranslateDto = new MachineTranslateDto();
        tmpMachineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        tmpMachineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        tmpMachineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        tmpMachineTranslateDto.setTypeCd("5566"); //访客人脸
        tmpMachineTranslateDto.setObjId(userDto.getUserId());
        tmpMachineTranslateDto.setObjName(userDto.getName());
        tmpMachineTranslateDto.setState("10000");
        tmpMachineTranslateDto.setCommunityId(tmpMachineDto.getCommunityId());
        tmpMachineTranslateDto.setbId("-1");
        tmpMachineTranslateDto.setObjBId(tmpOrderDto.getbId());
        tmpMachineTranslateDto.setMachineCmd("101");//增加更新人脸
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(tmpMachineTranslateDto);

    }

    private void updateMachineTranslate(MachineDto tmpMachineDto, UserDto userDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setObjId(userDto.getUserId());
        machineTranslateDto.setState("10000");
        machineTranslateDto.setCommunityId(tmpMachineDto.getCommunityId());
        machineTranslateDto.setUpdateTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(machineTranslateDto);

    }
}
