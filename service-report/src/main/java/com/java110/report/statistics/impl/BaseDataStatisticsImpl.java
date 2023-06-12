package com.java110.report.statistics.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基础数据统计类
 */
@Service
public class BaseDataStatisticsImpl implements IBaseDataStatistics {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    /**
     * 查询全部房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setOwnerName(queryStatisticsDto.getOwnerName());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setLink(queryStatisticsDto.getLink());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return baseDataStatisticsInnerServiceSMOImpl.getRoomCount(roomDto);
    }

    /**
     * 查询房屋信息
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<RoomDto> getRoomInfo(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setPage(queryStatisticsDto.getPage());
        roomDto.setRow(queryStatisticsDto.getRow());
        roomDto.setOwnerName(queryStatisticsDto.getOwnerName());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setLink(queryStatisticsDto.getLink());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return baseDataStatisticsInnerServiceSMOImpl.getRoomInfo(roomDto);
    }


    /**
     * 查询空闲房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setState(RoomDto.STATE_FREE);
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }

    @Override
    public long getOwnerCount(QueryStatisticsDto queryStatisticsDto) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setNameLike(queryStatisticsDto.getOwnerName());
        ownerDto.setLink(queryStatisticsDto.getLink());
        ownerDto.setCommunityId(queryStatisticsDto.getCommunityId());
        return ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);
    }

    @Override
    public List<OwnerDto> getOwnerInfo(QueryStatisticsDto queryStatisticsDto) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setNameLike(queryStatisticsDto.getOwnerName());
        ownerDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerDto.setLink(queryStatisticsDto.getLink());
        ownerDto.setPage(queryStatisticsDto.getPage());
        ownerDto.setRow(queryStatisticsDto.getRow());
        return ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
    }

    @Override
    public long getContractCount(QueryStatisticsDto queryStatisticsDto) {
        ContractDto contractDto = new ContractDto();
        contractDto.setContractNameLike(queryStatisticsDto.getObjName());
        contractDto.setStoreId(queryStatisticsDto.getStoreId());
        contractDto.setbLink(queryStatisticsDto.getLink());
        contractDto.setPartyBLike(queryStatisticsDto.getOwnerName());
        return contractInnerServiceSMOImpl.queryContractsCount(contractDto);
    }

    @Override
    public List<ContractDto> getContract(QueryStatisticsDto queryStatisticsDto) {
        ContractDto contractDto = new ContractDto();
        contractDto.setStoreId(queryStatisticsDto.getStoreId());
        contractDto.setbLink(queryStatisticsDto.getLink());
        contractDto.setContractNameLike(queryStatisticsDto.getObjName());
        contractDto.setPartyBLike(queryStatisticsDto.getOwnerName());
        contractDto.setPage(queryStatisticsDto.getPage());
        contractDto.setRow(queryStatisticsDto.getRow());
        return contractInnerServiceSMOImpl.queryContracts(contractDto);
    }

    @Override
    public long getCarCount(QueryStatisticsDto queryStatisticsDto) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerCarDto.setCarNumLike(queryStatisticsDto.getObjName());
        ownerCarDto.setOwnerName(queryStatisticsDto.getOwnerName());
        ownerCarDto.setLink(queryStatisticsDto.getLink());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        return ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
    }

    @Override
    public List<OwnerCarDto> getCar(QueryStatisticsDto queryStatisticsDto) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerCarDto.setCarNumLike(queryStatisticsDto.getObjName());
        ownerCarDto.setOwnerName(queryStatisticsDto.getOwnerName());
        ownerCarDto.setLink(queryStatisticsDto.getLink());
        ownerCarDto.setPage(queryStatisticsDto.getPage());
        ownerCarDto.setRow(queryStatisticsDto.getRow());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        return ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
    }


    /**
     * roomNum 拆分 查询房屋信息
     *
     * @param queryStatisticsDto
     * @param roomDto
     */
    private void addRoomNumCondition(QueryStatisticsDto queryStatisticsDto, RoomDto roomDto) {
        if (StringUtil.isEmpty(queryStatisticsDto.getObjName())) {
            return;
        }
        if (!queryStatisticsDto.getObjName().contains("-")) {
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
            return;
        }
        String[] objNames = queryStatisticsDto.getObjName().split("-");
        if (objNames.length == 2) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum("0");
            roomDto.setRoomNum(objNames[1]);
            return;
        }
        objNames = queryStatisticsDto.getObjName().split("-", 3);
        if (objNames.length == 3) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum(objNames[1]);
            roomDto.setRoomNum(objNames[2]);
        }

    }
}
