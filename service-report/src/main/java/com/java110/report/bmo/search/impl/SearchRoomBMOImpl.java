package com.java110.report.bmo.search.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.data.SearchDataDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.report.bmo.search.ISearchRoomBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchRoomBMOImpl implements ISearchRoomBMO {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<RoomDto> roomDtos = new ArrayList<>();

        // todo 根据房屋编号 查询房屋信息
        queryRoomByRoomNum(searchDataDto, roomDtos);

        // todo 根据 楼栋单元房屋 去查询房屋信息
        queryRoomByRoomName(searchDataDto, roomDtos);

        searchDataDto.setRooms(roomDtos);

        return searchDataDto;
    }

    private void queryRoomByRoomName(SearchDataDto searchDataDto, List<RoomDto> roomDtos) {
        if (!searchDataDto.getSearchValue().contains("-")) {
            return;
        }
        String[] values = searchDataDto.getSearchValue().split("-", 3);

        if (values.length != 3) {
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(values[0]);
        roomDto.setUnitNum(values[1]);
        roomDto.setRoomNum(values[2]);
        roomDto.setCommunityId(searchDataDto.getCommunityId());

        List<RoomDto> tmpRoomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        if (tmpRoomDtos == null || tmpRoomDtos.size() < 1) {
            return;
        }

        roomDtos.addAll(tmpRoomDtos);
    }

    /**
     * 根据房屋编号查询
     *
     * @param searchDataDto
     * @param roomDtos
     */
    private void queryRoomByRoomNum(SearchDataDto searchDataDto, List<RoomDto> roomDtos) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNumLike(searchDataDto.getSearchValue());
        roomDto.setCommunityId(searchDataDto.getCommunityId());
        List<RoomDto> tmpRoomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        if (tmpRoomDtos == null || tmpRoomDtos.size() < 1) {
            return;
        }
        roomDtos.addAll(tmpRoomDtos);
    }
}
