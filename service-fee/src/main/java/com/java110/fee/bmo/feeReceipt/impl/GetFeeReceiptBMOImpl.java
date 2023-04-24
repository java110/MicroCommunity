package com.java110.fee.bmo.feeReceipt.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.fee.bmo.feeReceipt.IGetFeeReceiptBMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeReceiptBMOImpl")
public class GetFeeReceiptBMOImpl implements IGetFeeReceiptBMO {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * @param feeReceiptDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeReceiptDto feeReceiptDto) {


        int count = feeReceiptInnerServiceSMOImpl.queryFeeReceiptsCount(feeReceiptDto);

        List<FeeReceiptDto> feeReceiptDtos = null;
        List<FeeReceiptDto> feeReceiptList = new ArrayList<>();
        if (count > 0) {
            feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceipts(feeReceiptDto);
            for (FeeReceiptDto feeReceipt : feeReceiptDtos) {
                feeReceipt.setStoreName(feeReceiptDto.getStoreName());
                feeReceiptList.add(feeReceipt);
            }
            //输入房屋信息
            freshRoomInfo(feeReceiptDtos);
        } else {
            feeReceiptDtos = new ArrayList<>();
            feeReceiptList.addAll(feeReceiptDtos);
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDto.getRow()), count, feeReceiptList);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
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

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            feeReceiptDto.setRoomName("-");
            return;
        }
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());

        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
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
            roomName += (tRoomDto.getFloorNum() + "栋" + tRoomDto.getUnitNum() + "单元" + tRoomDto.getRoomNum() + "室" + "/");
        }

        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        feeReceiptDto.setRoomName(roomName);
    }


    @Override
    public ResponseEntity<String> gets(FeeReceiptDtoNew feeReceiptDtonew) {
        FeeReceiptDto feeReceiptDto = new FeeReceiptDto();
        feeReceiptDto.setPage(feeReceiptDtonew.getPage());
        feeReceiptDto.setRow(feeReceiptDtonew.getRow());
        feeReceiptDto.setCommunityId(feeReceiptDtonew.getCommunityId());
        feeReceiptDto.setReceiptId(feeReceiptDtonew.getReceiptId());
        feeReceiptDto.setObjType(feeReceiptDto.getObjType());
        feeReceiptDto.setObjName(feeReceiptDto.getObjName());
        int count = feeReceiptInnerServiceSMOImpl.queryFeeReceiptsCount(feeReceiptDto);

        List<FeeReceiptDtoNew> feeReceiptDtos = null;
        if (count > 0) {
            feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceiptsNew(feeReceiptDtonew);
        } else {
            feeReceiptDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDto.getRow()), count, feeReceiptDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }
}
