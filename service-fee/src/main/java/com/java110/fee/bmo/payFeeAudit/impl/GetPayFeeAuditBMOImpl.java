package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.order.BusinessDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import com.java110.fee.bmo.payFeeAudit.IGetPayFeeAuditBMO;
import com.java110.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPayFeeAuditBMOImpl")
public class GetPayFeeAuditBMOImpl implements IGetPayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    /**
     * @param payFeeAuditDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PayFeeAuditDto payFeeAuditDto) {
        if (!refreshRoomPayObjId(payFeeAuditDto)) {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>("[]", HttpStatus.OK);
            return responseEntity;
        }
        if (!refreshCarPayObjId(payFeeAuditDto)) {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>("[]", HttpStatus.OK);
            return responseEntity;
        }
        int count = payFeeAuditInnerServiceSMOImpl.queryPayFeeAuditsCount(payFeeAuditDto);
        List<PayFeeAuditDto> payFeeAuditDtos = null;
        if (count > 0) {
            payFeeAuditDtos = payFeeAuditInnerServiceSMOImpl.queryPayFeeAudits(payFeeAuditDto);
            frashRoomAndStaff(payFeeAuditDtos);
        } else {
            payFeeAuditDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeAuditDto.getRow()), count, payFeeAuditDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    private boolean refreshCarPayObjId(PayFeeAuditDto payFeeAuditDto) {
        String payObjId = payFeeAuditDto.getPayerObjId();
        if (StringUtil.isEmpty(payObjId)) {
            return true;
        }

        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(payFeeAuditDto.getPayerObjId())) {
            return true;
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(payObjId);
        ownerCarDto.setCommunityId(payFeeAuditDto.getCommunityId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return false;
        }

        payFeeAuditDto.setPayerObjId(ownerCarDtos.get(0).getCarId());
        return true;
    }

    private boolean refreshRoomPayObjId(PayFeeAuditDto payFeeAuditDto) {
        String payObjId = payFeeAuditDto.getPayerObjId();
        if (StringUtil.isEmpty(payObjId)) {
            return true;
        }

        if (!payObjId.contains("-")) {
            return true;
        }

        String[] payObjIds = payObjId.split("-");

        if (payObjIds.length < 3) {
            throw new IllegalArgumentException("房屋编号格式错误，应该为 楼栋-单元-房屋");
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(payFeeAuditDto.getCommunityId());
        roomDto.setFloorNum(payObjIds[0]);
        roomDto.setUnitNum(payObjIds[1]);
        roomDto.setRoomNum(payObjIds[2]);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            return false;
        }

        payFeeAuditDto.setPayerObjId(roomDtos.get(0).getRoomId());
        return true;


    }

    private void frashRoomAndStaff(List<PayFeeAuditDto> payFeeAuditDtos) {

        if (payFeeAuditDtos == null || payFeeAuditDtos.size() < 1) {
            return;
        }

        List<FeeDto> feeDtos = BeanConvertUtil.covertBeanList(payFeeAuditDtos, FeeDto.class);
        computeFeeSMOImpl.freshFeeObjName(feeDtos);
        List<String> bIds = new ArrayList<>();
        for (PayFeeAuditDto payFeeAuditDto : payFeeAuditDtos) {
            for (FeeDto feeDto : feeDtos) {
                if (payFeeAuditDto.getFeeId().equals(feeDto.getFeeId())) {
                    payFeeAuditDto.setPayerObjName(feeDto.getPayerObjName());
                }
            }

            if (StringUtil.isEmpty(payFeeAuditDto.getbId()) || payFeeAuditDto.getbId().startsWith("-")) {
                continue;
            }

            bIds.add(payFeeAuditDto.getbId());
        }
        if (bIds.size() < 1) {
            return;
        }
        BusinessDto businessDto = new BusinessDto();
        businessDto.setbIds(bIds.toArray(new String[bIds.size()]));
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBId(businessDto);

        for (PayFeeAuditDto payFeeAuditDto : payFeeAuditDtos) {
            for (OrderDto orderDto : orderDtos) {
                if (StringUtil.isEmpty(payFeeAuditDto.getbId())) {
                    continue;
                }
                if (payFeeAuditDto.getbId().equals(orderDto.getbId())) {
                    payFeeAuditDto.setUserId(orderDto.getUserId());
                    payFeeAuditDto.setUserName(orderDto.getUserName());
                }
            }
        }
    }

}
