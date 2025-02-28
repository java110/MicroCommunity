package com.java110.fee.cmd.receipt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "receipt.queryAdminFeeReceipt")
public class QueryAdminFeeReceiptCmd extends Cmd {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeReceiptDto feeReceiptDto = BeanConvertUtil.covertBean(reqJson, FeeReceiptDto.class);


        if (!StringUtil.isEmpty(feeReceiptDto.getQstartTime())) {
            feeReceiptDto.setQstartTime(feeReceiptDto.getQstartTime() + " 00:00:00");
        }
        if (!StringUtil.isEmpty(feeReceiptDto.getQendTime())) {
            feeReceiptDto.setQendTime(feeReceiptDto.getQendTime() + " 23:59:59");
        }

        int count = feeReceiptInnerServiceSMOImpl.queryFeeReceiptsCount(feeReceiptDto);

        List<FeeReceiptDto> feeReceiptDtos = null;
        if (count > 0) {
            feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceipts(feeReceiptDto);
            for (FeeReceiptDto feeReceipt : feeReceiptDtos) {
                feeReceipt.setStoreName(feeReceiptDto.getStoreName());
            }
            //输入房屋信息
            freshRoomInfo(feeReceiptDtos);
        } else {
            feeReceiptDtos = new ArrayList<>();
        }


        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDto.getRow()), count, feeReceiptDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }


    /**
     * 刷入房屋信息
     *
     * @param feeReceiptDtos
     */
    private void freshRoomInfo(List<FeeReceiptDto> feeReceiptDtos) {

        for (FeeReceiptDto feeReceiptDto : feeReceiptDtos) {
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeReceiptDto.getObjType())) {
                feeReceiptDto.setRoomName(feeReceiptDto.getObjName());
                feeReceiptDto.setCarNum("-");
                continue;
            }

            doFreshRoomInfo(feeReceiptDto);
        }

    }

    /**
     * 车位信息刷入房屋信息
     *
     * @param feeReceiptDto
     */
    private void doFreshRoomInfo(FeeReceiptDto feeReceiptDto) {
        feeReceiptDto.setCarNum(feeReceiptDto.getObjName());
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(feeReceiptDto.getObjId());
        ownerCarDto.setCommunityId(feeReceiptDto.getCommunityId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            feeReceiptDto.setRoomName("-");
            return;
        }
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());

        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            feeReceiptDto.setRoomName("-");
            return;
        }

        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(feeReceiptDto.getCommunityId());
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String roomName = "";
        for (RoomDto tRoomDto : roomDtos) {
            roomName += (tRoomDto.getFloorNum() + "-" + tRoomDto.getUnitNum() + "-" + tRoomDto.getRoomNum() + "/");
        }

        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        feeReceiptDto.setRoomName(roomName);
    }

}
