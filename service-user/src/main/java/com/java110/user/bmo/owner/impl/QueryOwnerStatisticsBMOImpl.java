package com.java110.user.bmo.owner.impl;

import com.java110.dto.owner.OwnerDto;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.user.bmo.owner.IQueryOwnerStatisticsBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryOwnerStatisticsBMOImpl implements IQueryOwnerStatisticsBMO {

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
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Override
    public List<OwnerDto> query(List<OwnerDto> ownerDtos) {

        if(ownerDtos == null || ownerDtos.size() < 1){
            return ownerDtos;
        }

        //这里限制行数，以免影响系统性能
        if(ownerDtos.size() > MAX_LINE_COUNT){
            return ownerDtos;
        }
        List<String> ownerIds = new ArrayList<>();
        List<String> ownerTels = new ArrayList<>();
        for(OwnerDto ownerDto : ownerDtos){
            ownerIds.add(ownerDto.getOwnerId());
            ownerTels.add(ownerDto.getLink());
        }


        // 查询 房屋数量
        queryRoomCount(ownerIds,ownerDtos);

        // 查询 家庭成员数
        queryOwnerMemberCount(ownerIds,ownerDtos);

        // 查询 车辆数
        queryCarCount(ownerIds,ownerDtos);

        // 查询 投诉数
        queryComplaintCount(ownerTels,ownerDtos);

        // 查询 报修数
        queryRepairCount(ownerTels,ownerDtos);

        // 查询业主欠费
        queryOwnerOweFee(ownerIds,ownerDtos);

        // 查询业主合同
        queryOwnerContractCount(ownerIds,ownerDtos);

        return ownerDtos;
    }

    private void queryOwnerContractCount(List<String> ownerIds, List<OwnerDto> ownerDtos) {
        Map info = new HashMap();
        info.put("communityId",ownerDtos.get(0).getCommunityId());
        info.put("ownerIds",ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> contractsCount = contractInnerServiceSMOImpl.queryContractsByOwnerIds(info);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : contractsCount) {
                if(ownerDto.getOwnerId().equals(count.get("ownerId"))){
                    ownerDto.setContractCount(count.get("contractCount").toString());
                }
            }
        }
    }

    /**
     * 查询业主欠费
     * @param ownerIds
     * @param ownerDtos
     */
    private void queryOwnerOweFee(List<String> ownerIds, List<OwnerDto> ownerDtos) {
        Map info = new HashMap();
        info.put("communityId",ownerDtos.get(0).getCommunityId());
        info.put("ownerIds",ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> repairCounts = reportOweFeeInnerServiceSMOImpl.queryOweFeesByOwnerIds(info);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : repairCounts) {
                if(ownerDto.getOwnerId().equals(count.get("ownerId"))){
                    ownerDto.setOweFee(count.get("oweFee").toString());
                }
            }
        }
    }


    /**
     * 查询业主投诉数
     * @param ownerTels
     * @param ownerDtos
     */
    private void queryRepairCount(List<String> ownerTels, List<OwnerDto> ownerDtos) {


        Map info = new HashMap();
        info.put("communityId",ownerDtos.get(0).getCommunityId());
        info.put("ownerTels",ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> repairCounts = repairPoolV1InnerServiceSMOImpl.queryRepairCountByOwnerTels(info);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : repairCounts) {
                if(ownerDto.getLink().equals(count.get("ownerTel"))){
                    ownerDto.setRepairCount(count.get("repairCount").toString());
                }
            }
        }
    }

    /**
     * 查询业主投诉数
     * @param ownerTels
     * @param ownerDtos
     */
    private void queryComplaintCount(List<String> ownerTels, List<OwnerDto> ownerDtos) {


        Map info = new HashMap();
        info.put("communityId",ownerDtos.get(0).getCommunityId());
        info.put("ownerTels",ownerTels.toArray(new String[ownerTels.size()]));
        List<Map> complaintCounts = complaintV1InnerServiceSMOImpl.queryComplaintCountByOwnerTels(info);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : complaintCounts) {
                if(ownerDto.getLink().equals(count.get("ownerTel"))){
                    ownerDto.setComplaintCount(count.get("complaintCount").toString());
                }
            }
        }
    }

    /**
     * 查询业主车辆数
     * @param ownerIds
     * @param ownerDtos
     */
    private void queryCarCount(List<String> ownerIds, List<OwnerDto> ownerDtos) {

        List<Map> memberCounts = ownerCarV1InnerServiceSMOImpl.queryOwnerCarCountByOwnerIds(ownerIds);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : memberCounts) {
                if(ownerDto.getOwnerId().equals(count.get("ownerId"))){
                    ownerDto.setCarCount(count.get("carCount").toString());
                }
            }
        }
    }

    /**
     * 查询 业主成员数
     * @param ownerIds
     * @param ownerDtos
     */
    private void queryOwnerMemberCount(List<String> ownerIds, List<OwnerDto> ownerDtos) {

        List<Map> memberCounts = ownerV1InnerServiceSMOImpl.queryOwnerMembersCount(ownerIds);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : memberCounts) {
                if(ownerDto.getOwnerId().equals(count.get("ownerId"))){
                    ownerDto.setMemberCount(count.get("memberCount").toString());
                }
            }
        }
    }

    /**
     * 查询 房屋数量
     * @param ownerIds
     * @param ownerDtos
     */
    private void queryRoomCount(List<String> ownerIds, List<OwnerDto> ownerDtos) {

        //查询业主房屋数
        List<Map> ownerRoomCounts = ownerRoomRelV1InnerServiceSMOImpl.queryRoomCountByOwnerIds(ownerIds);

        for(OwnerDto ownerDto : ownerDtos) {
            for (Map count : ownerRoomCounts) {
                if(!StringUtil.isEmpty(ownerDto.getOwnerId()) && !StringUtil.isEmpty(count.get("ownerId").toString()))
                if(ownerDto.getOwnerId().equals(count.get("ownerId").toString())){
                    ownerDto.setRoomCount(count.get("roomCount").toString());
                }
            }
        }
    }
}
