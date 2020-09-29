package com.java110.user.bmo.rentingPool.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.user.bmo.rentingPool.IGetRentingPoolBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingPoolBMOImpl")
public class GetRentingPoolBMOImpl implements IGetRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    /**
     * @param rentingPoolDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingPoolDto rentingPoolDto) {


        int count = rentingPoolInnerServiceSMOImpl.queryRentingPoolsCount(rentingPoolDto);

        List<RentingPoolDto> rentingPoolDtos = null;
        if (count > 0) {
            rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

            //刷入房屋信息
            refreshRoomInfo(rentingPoolDtos);
        } else {
            rentingPoolDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingPoolDto.getRow()), count, rentingPoolDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshRoomInfo(List<RentingPoolDto> rentingPoolDtos) {
        List<String> roomIds = new ArrayList<>();

        List<String> rentingIds = new ArrayList<>();

        for (RentingPoolDto rentingPoolDto : rentingPoolDtos) {
            roomIds.add(rentingPoolDto.getRoomId());
            rentingIds.add(rentingPoolDto.getRentingId());
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(rentingPoolDtos.get(0).getCommunityId());

        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);


        for (RoomDto tmpRoomDto : roomDtos) {
            for (RentingPoolDto rentingPoolDto : rentingPoolDtos) {
                if (tmpRoomDto.getRoomId().equals(rentingPoolDto.getRoomId())) {
                    rentingPoolDto.setRoomName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    rentingPoolDto.setBuiltUpArea(tmpRoomDto.getBuiltUpArea());
                    rentingPoolDto.setApartmentName(tmpRoomDto.getApartmentName());
                }
            }
        }


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(rentingIds.toArray(new String[rentingIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        //刷入图片信息
        List<String> photoVos = null;
        String url = null;
        for (RentingPoolDto rentingPoolDto : rentingPoolDtos) {
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (rentingPoolDto.getRentingId().equals(tmpFileRelDto.getObjId())){
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=" + rentingPoolDto.getCommunityId();
                    photoVos.add(url);
                }
            }
            rentingPoolDto.setPhotos(photoVos);
        }
    }

}
