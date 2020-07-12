package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.store.dao.IComplaintServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 投诉建议内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ComplaintInnerServiceSMOImpl extends BaseServiceSMO implements IComplaintInnerServiceSMO {

    @Autowired
    private IComplaintServiceDao complaintServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public List<ComplaintDto> queryComplaints(@RequestBody ComplaintDto complaintDto) {

        //校验是否传了 分页信息

        int page = complaintDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            complaintDto.setPage((page - 1) * complaintDto.getRow());
        }

        List<ComplaintDto> complaints = BeanConvertUtil.covertBeanList(complaintServiceDaoImpl.getComplaintInfo(BeanConvertUtil.beanCovertMap(complaintDto)), ComplaintDto.class);

        if(complaints == null || complaints.size() == 0){
            return complaints;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(complaintDto.getCommunityId());
        roomDto.setRoomIds(getRoomIds(complaints));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (ComplaintDto tmpComplainDto : complaints) {
            refreshRoomInfo(tmpComplainDto, roomDtos);
        }

        return complaints;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param complainDto 小区费用信息
     * @param roomDtos 用户列表
     */
    private void refreshRoomInfo(ComplaintDto complainDto, List<RoomDto> roomDtos) {
        for (RoomDto room : roomDtos) {
            if (room.getRoomId().equals(complainDto.getRoomId())) {
                //BeanConvertUtil.covertBean(room, complainDto);
                complainDto.setFloorNum(room.getFloorNum());
                complainDto.setRoomNum(room.getRoomNum());
                complainDto.setUnitNum(room.getUnitNum());
            }
        }
    }

    private String[] getRoomIds(List<ComplaintDto> complaints) {
        List<String> roomIds = new ArrayList<String>();
        for (ComplaintDto complaint : complaints) {
            roomIds.add(complaint.getRoomId());
        }

        return roomIds.toArray(new String[roomIds.size()]);
    }


    @Override
    public int queryComplaintsCount(@RequestBody ComplaintDto complaintDto) {
        return complaintServiceDaoImpl.queryComplaintsCount(BeanConvertUtil.beanCovertMap(complaintDto));
    }

    public IComplaintServiceDao getComplaintServiceDaoImpl() {
        return complaintServiceDaoImpl;
    }

    public void setComplaintServiceDaoImpl(IComplaintServiceDao complaintServiceDaoImpl) {
        this.complaintServiceDaoImpl = complaintServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
