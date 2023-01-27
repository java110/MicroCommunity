package com.java110.community.bmo.room.impl;

import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IComplaintV1InnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryRoomStatisticsBMOImpl implements IQueryRoomStatisticsBMO {

    public static final int MAX_LINE_COUNT = 15;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Override
    public List<RoomDto> query(List<RoomDto> roomDtos) {

        if (roomDtos == null || roomDtos.size() < 1) {
            return roomDtos;
        }

        //这里限制行数，以免影响系统性能
        if (roomDtos.size() > MAX_LINE_COUNT) {
            return roomDtos;
        }
        List<String> roomIds = new ArrayList<>();
        List<String> ownerIds = new ArrayList<>();
        List<String> ownerTels = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            ownerIds.add(roomDto.getOwnerId());
            ownerTels.add(roomDto.getOwnerTel());
            roomIds.add(roomDto.getRoomId());
        }

        // 查询 家庭成员数
        queryOwnerMemberCount(ownerIds, roomDtos);

        // 查询 车辆数
        queryCarCount(ownerIds, roomDtos);

        // 查询 房屋数量
        queryRoomCount(ownerIds, roomDtos);

        // 查询 投诉数
        queryComplaintCount(ownerTels, roomDtos);

        // 查询 报修数
        queryRepairCount(ownerTels, roomDtos);

        // 查询房屋欠费
        queryRoomOweFee(roomIds, roomDtos);

        // 查询业主欠费
        queryOwnerOweFee(ownerIds, roomDtos);

        // 查询房屋 合同
        queryRoomContract(roomIds, roomDtos);

        return roomDtos;
    }

    /**
     * 查询
     *
     * @param roomIds
     * @param roomDtos
     */
    private void queryRoomContract(List<String> roomIds, List<RoomDto> roomDtos) {
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("roomIds", roomIds.toArray(new String[roomIds.size()]));
        List<Map> repairCounts = contractRoomInnerServiceSMOImpl.queryContractByRoomIds(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (roomDto.getRoomId().equals(count.get("roomId"))) {
                    roomDto.setContractCount(count.get("contractCount").toString());
                }
            }
        }
    }

    private void queryRoomOweFee(List<String> roomIds, List<RoomDto> roomDtos) {
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("roomIds", roomIds.toArray(new String[roomIds.size()]));
        List<Map> repairCounts = reportOweFeeInnerServiceSMOImpl.queryOweFeesByRoomIds(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (roomDto.getRoomId().equals(count.get("roomId"))) {
                    roomDto.setRoomOweFee(count.get("oweFee").toString());
                }
            }
        }
    }

    /**
     * 查询业主欠费
     *
     * @param ownerIds
     * @param roomDtos
     */
    private void queryOwnerOweFee(List<String> ownerIds, List<RoomDto> roomDtos) {
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerIds", ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> repairCounts = reportOweFeeInnerServiceSMOImpl.queryOweFeesByOwnerIds(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (roomDto.getOwnerId().equals(count.get("ownerId"))) {
                    roomDto.setOweFee(count.get("oweFee").toString());
                }
            }
        }
    }


    /**
     * 查询业主投诉数
     *
     * @param ownerTels
     * @param roomDtos
     */
    private void queryRepairCount(List<String> ownerTels, List<RoomDto> roomDtos) {


        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerTels", ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> repairCounts = repairPoolV1InnerServiceSMOImpl.queryRepairCountByOwnerTels(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (roomDto.getLink().equals(count.get("ownerTel"))) {
                    roomDto.setRepairCount(count.get("repairCount").toString());
                }
            }
        }
    }

    /**
     * 查询业主投诉数
     *
     * @param ownerTels
     * @param roomDtos
     */
    private void queryComplaintCount(List<String> ownerTels, List<RoomDto> roomDtos) {


        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerTels", ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> complaintCounts = complaintV1InnerServiceSMOImpl.queryComplaintCountByOwnerTels(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : complaintCounts) {
                if (roomDto.getLink().equals(count.get("ownerTel"))) {
                    roomDto.setComplaintCount(count.get("complaintCount").toString());
                }
            }
        }
    }

    /**
     * 查询业主车辆数
     *
     * @param ownerIds
     * @param roomDtos
     */
    private void queryCarCount(List<String> ownerIds, List<RoomDto> roomDtos) {

        List<Map> memberCounts = ownerCarV1InnerServiceSMOImpl.queryOwnerCarCountByOwnerIds(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : memberCounts) {
                if (roomDto.getOwnerId().equals(count.get("ownerId"))) {
                    roomDto.setCarCount(count.get("carCount").toString());
                }
            }
        }
    }

    /**
     * 查询 业主成员数
     *
     * @param ownerIds
     * @param roomDtos
     */
    private void queryOwnerMemberCount(List<String> ownerIds, List<RoomDto> roomDtos) {

        List<Map> memberCounts = ownerV1InnerServiceSMOImpl.queryOwnerMembersCount(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : memberCounts) {
                if (roomDto.getOwnerId().equals(count.get("ownerId"))) {
                    roomDto.setMemberCount(count.get("memberCount").toString());
                }
            }
        }
    }

    /**
     * 查询 房屋数量
     *
     * @param ownerIds
     * @param roomDtos
     */
    private void queryRoomCount(List<String> ownerIds, List<RoomDto> roomDtos) {

        //查询业主房屋数
        List<Map> ownerRoomCounts = ownerRoomRelV1InnerServiceSMOImpl.queryRoomCountByOwnerIds(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : ownerRoomCounts) {
                if (roomDto.getOwnerId().equals(count.get("ownerId"))) {
                    roomDto.setRoomCount(count.get("roomCount").toString());
                }
            }
        }
    }
}
