package com.java110.user.bmo.owner.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.user.bmo.owner.IComprehensiveQuery;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComprehensiveQueryImpl implements IComprehensiveQuery {

    public static final String SEARCH_TYPE_ROOM = "1"; //根据房屋来查
    public static final String SEARCH_TYPE_OWNER_NAME = "2"; //根据业主名称查询
    public static final String SEARCH_TYPE_OWNER_TEL = "3"; //根据业主手机号
    public static final String SEARCH_TYPE_OWNER_IDCARD = "4"; //根据业主身份证
    public static final String SEARCH_TYPE_OWNER_CAR = "5"; //根据业主车牌号
    public static final String SEARCH_TYPE_OWNER_MEMBER_NAME = "6"; //根据家庭成员名称
    public static final String SEARCH_TYPE_OWNER_MEMBER_TEL = "7"; //根据家庭成员电话
    public static final String SEARCH_TYPE_OWNER_MEMBER_IDCARD = "8"; //根据家庭成员身份证

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(String communityId, String searchValue, String searchType) {
        OwnerDto ownerDto = null;
        switch (searchType) {
            case SEARCH_TYPE_ROOM:
                ownerDto = queryByRoom(communityId, searchValue);
                break;
        }
        return ResultVo.createResponseEntity(1, 1, ownerDto);
    }

    /**
     * 根据房屋查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByRoom(String communityId, String searchValue) {

        if (!searchValue.contains("-")) {
            throw new IllegalArgumentException("查询内容格式错误，请输入 楼栋-单元-房屋 如 1-1-1");
        }

        String[] values = searchValue.split("-");

        if (values.length != 3) {
            throw new IllegalArgumentException("查询内容格式错误，请输入 楼栋-单元-房屋 如 1-1-1");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(values[0]);
        roomDto.setUnitNum(values[1]);
        roomDto.setRoomNum(values[2]);
        roomDto.setCommunityId(communityId);

        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "未找到房屋信息");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未找到业主信息");

        OwnerDto resOwnerDto = ownerDtos.get(0);

        resOwnerDto.setRooms(roomDtos);

        return resOwnerDto;
    }


}
