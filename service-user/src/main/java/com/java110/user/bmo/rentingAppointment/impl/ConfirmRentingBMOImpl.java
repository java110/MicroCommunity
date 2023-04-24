package com.java110.user.bmo.rentingAppointment.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.rentingPool.RentingPoolFlowDto;
import com.java110.intf.user.IRentingAppointmentInnerServiceSMO;
import com.java110.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import com.java110.user.bmo.rentingAppointment.IConfirmRentingBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("confirmRentingBMOImpl")
public class ConfirmRentingBMOImpl implements IConfirmRentingBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;


    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> confirm(RentingAppointmentPo rentingAppointmentPo) {

        //查询 预约数据 校验
        RentingAppointmentDto rentingAppointmentDto = new RentingAppointmentDto();
        rentingAppointmentDto.setAppointmentId(rentingAppointmentPo.getAppointmentId());
        List<RentingAppointmentDto> rentingAppointmentDtos = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointments(rentingAppointmentDto);

        Assert.listOnlyOne(rentingAppointmentDtos, "未找到预约信息");

        //校验 房源信息
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRentingId(rentingAppointmentPo.getRentingId());
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        Assert.listOnlyOne(rentingPoolDtos, "未找到房源信息");

        //预约数据修改 租房成功
        rentingAppointmentPo.setState(RentingAppointmentDto.STATE_SUCCESS);
        rentingAppointmentPo.setRoomId(rentingPoolDtos.get(0).getRoomId());
        int flag = rentingAppointmentInnerServiceSMOImpl.updateRentingAppointment(rentingAppointmentPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        //房源状态修改 待支付
        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setRentingId(rentingPoolDtos.get(0).getRentingId());
        rentingPoolPo.setState(RentingPoolDto.STATE_TO_PAY);
        flag = rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改房屋状态失败");
        }

        //流程中插入租客信息
        RentingPoolFlowPo rentingPoolFlowPo = new RentingPoolFlowPo();
        rentingPoolFlowPo.setUserTel(rentingAppointmentDtos.get(0).getTenantTel());
        rentingPoolFlowPo.setUseName(rentingAppointmentDtos.get(0).getTenantName());
        rentingPoolFlowPo.setUserRole("2"); //租客
        rentingPoolFlowPo.setState(RentingPoolFlowDto.STATE_CONFIRM_RENTING);
        rentingPoolFlowPo.setRentingId(rentingAppointmentPo.getRentingId());
        rentingPoolFlowPo.setDealTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        rentingPoolFlowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        rentingPoolFlowPo.setContext("确认租房");
        rentingPoolFlowPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        flag = rentingPoolFlowInnerServiceSMOImpl.saveRentingPoolFlow(rentingPoolFlowPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改房屋状态失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
