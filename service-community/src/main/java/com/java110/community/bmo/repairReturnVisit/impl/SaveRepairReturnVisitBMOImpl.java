package com.java110.community.bmo.repairReturnVisit.impl;

import com.java110.community.bmo.repairReturnVisit.ISaveRepairReturnVisitBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.repair.RepairReturnVisitPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("saveRepairReturnVisitBMOImpl")
public class SaveRepairReturnVisitBMOImpl implements ISaveRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param repairReturnVisitPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RepairReturnVisitPo repairReturnVisitPo) {

        repairReturnVisitPo.setVisitId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_visitId));
        int flag = repairReturnVisitInnerServiceSMOImpl.saveRepairReturnVisit(repairReturnVisitPo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairReturnVisitPo.getRepairId());
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "报修信息为空");
        //获取报修渠道
        String repairChannel = repairDtos.get(0).getRepairChannel();
        //获取维修类型
        String maintenanceType = repairDtos.get(0).getMaintenanceType();
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(repairReturnVisitPo.getRepairId());
        repairUserDto.setCommunityId(repairReturnVisitPo.getCommunityId());
//        if (repairChannel.equals("Z")) { //如果是业主端报修，就查询是否有已评价状态的
//            repairUserDto.setState(RepairUserDto.STATE_FINISH);
//        } else if (!StringUtil.isEmpty(maintenanceType) && maintenanceType.equals("1001")) { //如果不是业主端报修，且是有偿的，就查询已支付状态的
//            repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
//        } else { //其他的查询结单状态的
//            repairUserDto.setState(RepairUserDto.STATE_CLOSE);
//        }

        repairUserDto.setStates(new String[]{RepairUserDto.STATE_FINISH, RepairUserDto.STATE_FINISH_PAY_FEE, RepairUserDto.STATE_CLOSE});
        //查询报修派单状态
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);

        if (repairUserDtos == null || repairUserDtos.size() < 1) {
            throw new IllegalArgumentException("未查询到 接单 待支付 或者 评价完成的工单，不能回访");
        }
        repairUserDto = repairUserDtos.get(repairUserDtos.size() - 1);
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = repairUserDto.getEndTime();
        repairUserPo.setStartTime(format.format(endTime));
        repairUserPo.setState(RepairUserDto.STATE_FINISH_VISIT);
        repairUserPo.setContext(repairReturnVisitPo.getContext());
        repairUserPo.setCommunityId(repairReturnVisitPo.getCommunityId());
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairId(repairReturnVisitPo.getRepairId());
        repairUserPo.setStaffId(repairReturnVisitPo.getVisitPersonId());
        repairUserPo.setStaffName(repairReturnVisitPo.getVisitPersonName());
        repairUserPo.setPreStaffId(repairUserDto.getStaffId());
        repairUserPo.setPreStaffName(repairUserDto.getStaffName());
        repairUserPo.setPreRuId(repairUserDto.getRuId());
        repairUserPo.setRepairEvent("auditUser");
        repairUserPo.setbId("-1");
        repairUserInnerServiceSMOImpl.saveRepairUser(repairUserPo);

        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(repairReturnVisitPo.getRepairId());
        repairPoolPo.setState(RepairDto.STATE_COMPLATE);
        repairPoolPo.setCommunityId(repairReturnVisitPo.getCommunityId());
        repairPoolPo.setStatusCd("0");
        flag = repairInnerServiceSMOImpl.updateRepair(repairPoolPo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
