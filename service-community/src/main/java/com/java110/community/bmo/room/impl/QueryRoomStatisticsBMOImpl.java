package com.java110.community.bmo.room.impl;

import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryRoomStatisticsBMOImpl implements IQueryRoomStatisticsBMO {

    public static final int MAX_LINE_COUNT = 30;

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

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public List<RoomDto> query(List<RoomDto> roomDtos) {

        if (ListUtil.isNull(roomDtos)) {
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
            if (!StringUtil.isEmpty(roomDto.getOwnerId())) {
                ownerIds.add(roomDto.getOwnerId());
            }
            if (!StringUtil.isEmpty(roomDto.getOwnerTel())) {
                ownerTels.add(roomDto.getOwnerTel());
            }

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


    @Override
    public List<RoomDto> querySimple(List<RoomDto> roomDtos) {
        if (ListUtil.isNull(roomDtos)) {
            return roomDtos;
        }

        //这里限制行数，以免影响系统性能
        if (roomDtos.size() > MAX_LINE_COUNT) {
            return roomDtos;
        }
        List<String> roomIds = new ArrayList<>();

        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        // 查询业主信息
        queryRoomOwner(roomIds, roomDtos);
        List<String> ownerIds = new ArrayList<>();
        List<String> ownerTels = new ArrayList<>();
        List<String> communityIds = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            if (!StringUtil.isEmpty(roomDto.getOwnerId())) {
                ownerIds.add(roomDto.getOwnerId());
            }
            if (!StringUtil.isEmpty(roomDto.getOwnerTel())) {
                ownerTels.add(roomDto.getOwnerTel());
            }
            communityIds.add(roomDto.getCommunityId());
        }

        // 查询小区名称
        queryCommunityName(communityIds,roomDtos);

        // 查询 家庭成员数
        queryOwnerMemberCount(ownerIds, roomDtos);

        // 查询 车辆数
        queryCarCount(ownerIds, roomDtos);

        // 查询 房屋数量
        queryRoomCount(ownerIds, roomDtos);

        // 查询房屋 合同
        queryRoomContract(roomIds, roomDtos);

        return roomDtos;
    }

    private void queryCommunityName(List<String> communityIds, List<RoomDto> roomDtos) {
        if(ListUtil.isNull(communityIds)){
            return ;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        if(ListUtil.isNull(communityDtos)){
            return;
        }
        for (RoomDto tmpRoomDto : roomDtos) {
            for (CommunityDto tCommunityDto : communityDtos) {
                if (!tmpRoomDto.getCommunityId().equals(tCommunityDto.getCommunityId())) {
                    continue;
                }
                tmpRoomDto.setCommunityName(tCommunityDto.getName());
            }
        }
    }


    private void queryRoomOwner(List<String> roomIds, List<RoomDto> roomDtos) {
        if (ListUtil.isNull(roomDtos)) {
            return;
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }
        for (RoomDto tmpRoomDto : roomDtos) {
            for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
                if (!tmpRoomDto.getRoomId().equals(tOwnerRoomRelDto.getRoomId())) {
                    continue;
                }
                tmpRoomDto.setOwnerId(tOwnerRoomRelDto.getOwnerId());
                tmpRoomDto.setOwnerName(tOwnerRoomRelDto.getOwnerName());
                tmpRoomDto.setOwnerTel(tOwnerRoomRelDto.getLink());
                tmpRoomDto.setStartTime(tOwnerRoomRelDto.getStartTime());
                tmpRoomDto.setEndTime(tOwnerRoomRelDto.getEndTime());
                tmpRoomDto.setLink(tOwnerRoomRelDto.getLink());
            }
        }
    }

    @Override
    public List<RoomDto> queryRoomOweFee(List<RoomDto> roomDtos) {
        if (ListUtil.isNull(roomDtos)) {
            return roomDtos;
        }
        List<String> roomIds = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        // 查询房屋 合同
        queryRoomOweFee(roomIds, roomDtos);
        return roomDtos;
    }


    /**
     * 查询
     *
     * @param roomIds
     * @param roomDtos
     */
    private void queryRoomContract(List<String> roomIds, List<RoomDto> roomDtos) {
        if (ListUtil.isNull(roomDtos)) {
            return;
        }
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("roomIds", roomIds.toArray(new String[roomIds.size()]));
        List<Map> repairCounts = contractRoomInnerServiceSMOImpl.queryContractByRoomIds(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (!StringUtil.isEmpty(roomDto.getRoomId()) && !StringUtil.isEmpty(count.get("roomId").toString())) {
                    if (roomDto.getRoomId().equals(count.get("roomId").toString())) {
                        roomDto.setContractCount(count.get("contractCount").toString());
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void queryRoomOweFee(List<String> roomIds, List<RoomDto> roomDtos) {
        if (ListUtil.isNull(roomIds)) {
            return;
        }
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("roomIds", roomIds.toArray(new String[roomIds.size()]));
        List<Map> repairCounts = reportOweFeeInnerServiceSMOImpl.queryOweFeesByRoomIds(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (StringUtil.isEmpty(roomDto.getRoomId())) {
                    continue;
                }
                if (StringUtil.isEmpty(count.get("roomId").toString())) {
                    continue;
                }
                if (roomDto.getRoomId().equals(count.get("roomId").toString())) {
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
        if (ownerIds == null || ownerIds.size() < 1) {
            return;
        }
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerIds", ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> repairCounts = reportOweFeeInnerServiceSMOImpl.queryOweFeesByOwnerIds(info);

        for (RoomDto roomDto : roomDtos) {
            if (StringUtil.isEmpty(roomDto.getOwnerId())) {
                continue;
            }
            for (Map count : repairCounts) {
                if (!StringUtil.isEmpty(roomDto.getOwnerId()) && !StringUtil.isEmpty(count.get("ownerId").toString())) {
                    if (roomDto.getOwnerId().equals(count.get("ownerId").toString())) {
                        roomDto.setOweFee(count.get("oweFee").toString());
                    }
                } else {
                    continue;
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

        if (ownerTels == null || ownerTels.size() < 1) {
            return;
        }
        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerTels", ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> repairCounts = repairPoolV1InnerServiceSMOImpl.queryRepairCountByOwnerTels(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : repairCounts) {
                if (!StringUtil.isEmpty(roomDto.getLink()) && !StringUtil.isEmpty(count.get("ownerTel").toString())) {
                    if (roomDto.getLink().equals(count.get("ownerTel").toString())) {
                        roomDto.setRepairCount(count.get("repairCount").toString());
                    }
                } else {
                    continue;
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
        if (ListUtil.isNull(ownerTels)) {
            return;
        }

        Map info = new HashMap();
        info.put("communityId", roomDtos.get(0).getCommunityId());
        info.put("ownerTels", ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> complaintCounts = complaintV1InnerServiceSMOImpl.queryComplaintCountByOwnerTels(info);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : complaintCounts) {
                if (!StringUtil.isEmpty(roomDto.getLink()) && !StringUtil.isEmpty(count.get("ownerTel").toString())) {
                    if (roomDto.getLink().equals(count.get("ownerTel").toString())) {
                        roomDto.setComplaintCount(count.get("complaintCount").toString());
                    }
                } else {
                    continue;
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
        if (ownerIds == null || ownerIds.size() < 1) {
            return;
        }

        List<Map> memberCounts = ownerCarV1InnerServiceSMOImpl.queryOwnerCarCountByOwnerIds(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            if (StringUtil.isEmpty(roomDto.getOwnerId())) {
                continue;
            }
            for (Map count : memberCounts) {
                if (!StringUtil.isEmpty(roomDto.getOwnerId()) && !StringUtil.isEmpty(count.get("ownerId").toString())) {
                    if (roomDto.getOwnerId().equals(count.get("ownerId").toString())) {
                        roomDto.setCarCount(count.get("carCount").toString());
                    }
                } else {
                    continue;
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
        if (ListUtil.isNull(ownerIds)) {
            return;
        }

        List<Map> memberCounts = ownerV1InnerServiceSMOImpl.queryOwnerMembersCount(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            if (StringUtil.isEmpty(roomDto.getOwnerId())) {
                continue;
            }
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

        if (ownerIds == null || ownerIds.size() < 1) {
            return;
        }

        //查询业主房屋数
        List<Map> ownerRoomCounts = ownerRoomRelV1InnerServiceSMOImpl.queryRoomCountByOwnerIds(ownerIds);

        for (RoomDto roomDto : roomDtos) {
            for (Map count : ownerRoomCounts) {
                if (StringUtil.isEmpty(roomDto.getOwnerId()) || StringUtil.isEmpty(count.get("ownerId").toString())) {
                    continue;
                }
                if (roomDto.getOwnerId().equals(count.get("ownerId").toString())) {
                    roomDto.setRoomCount(count.get("roomCount").toString());
                }
            }
        }
    }
}
