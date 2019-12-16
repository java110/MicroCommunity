package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.advert.IAdvertInnerServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.core.smo.unit.IUnitInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAdvertConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.advert.ApiAdvertDataVo;
import com.java110.vo.api.advert.ApiAdvertVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAdvertsListener")
public class ListAdvertsListener extends AbstractServiceApiListener {

    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAdvertConstant.LIST_ADVERTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAdvertInnerServiceSMO getAdvertInnerServiceSMOImpl() {
        return advertInnerServiceSMOImpl;
    }

    public void setAdvertInnerServiceSMOImpl(IAdvertInnerServiceSMO advertInnerServiceSMOImpl) {
        this.advertInnerServiceSMOImpl = advertInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AdvertDto advertDto = BeanConvertUtil.covertBean(reqJson, AdvertDto.class);

        int count = advertInnerServiceSMOImpl.queryAdvertsCount(advertDto);

        List<ApiAdvertDataVo> adverts = null;

        if (count > 0) {
            List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);
            refreshAdvert(advertDtos);
            adverts = BeanConvertUtil.covertBeanList(advertDtos, ApiAdvertDataVo.class);
        } else {
            adverts = new ArrayList<>();
        }

        ApiAdvertVo apiAdvertVo = new ApiAdvertVo();

        apiAdvertVo.setTotal(count);
        apiAdvertVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAdvertVo.setAdverts(adverts);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAdvertVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }


    private void refreshAdvert(List<AdvertDto> advertDtos) {

        //批量处理 小区
        refreshCommunitys(advertDtos);


        //批量处理单元信息
        refreshFloors(advertDtos);

        //批量处理单元信息
        refreshUnits(advertDtos);

        //批量处理 房屋信息
        refreshRooms(advertDtos);

    }

    /**
     * 获取批量小区
     *
     * @param advertDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshCommunitys(List<AdvertDto> advertDtos) {
        List<String> communityIds = new ArrayList<String>();
        List<AdvertDto> tmpAdvertDtos = new ArrayList<>();
        for (AdvertDto advertDto : advertDtos) {

            if ("1000".equals(advertDto.getLocationTypeCd())) {
                communityIds.add(advertDto.getLocationObjId());
                tmpAdvertDtos.add(advertDto);
            }
        }

        if (communityIds.size() < 1) {
            return;
        }
        String[] tmpCommunityIds = communityIds.toArray(new String[communityIds.size()]);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(tmpCommunityIds);
        //根据 userId 查询用户信息
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (AdvertDto advertDto : tmpAdvertDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (advertDto.getLocationObjId().equals(tmpCommunityDto.getCommunityId())) {
                    advertDto.setLocationObjName(tmpCommunityDto.getName());
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param adverts 设备信息
     * @return 批量userIds 信息
     */
    private void refreshFloors(List<AdvertDto> adverts) {
        List<String> floorIds = new ArrayList<String>();
        List<AdvertDto> tmpAdvertDtos = new ArrayList<>();
        for (AdvertDto advertDto : adverts) {

            if ("4000".equals(advertDto.getLocationTypeCd())) {
                floorIds.add(advertDto.getLocationObjId());
                tmpAdvertDtos.add(advertDto);
            }
        }

        if (floorIds.size() < 1) {
            return;
        }
        String[] tmpFloorIds = floorIds.toArray(new String[floorIds.size()]);

        FloorDto floorDto = new FloorDto();
        floorDto.setFloorIds(tmpFloorIds);
        //根据 userId 查询用户信息
        List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);

        for (AdvertDto advertDto : tmpAdvertDtos) {
            for (FloorDto tmpFloorDto : floorDtos) {
                if (advertDto.getLocationObjId().equals(tmpFloorDto.getFloorId())) {
                    advertDto.setLocationObjName(tmpFloorDto.getFloorNum() + "栋");
                    BeanConvertUtil.covertBean(tmpFloorDto, advertDto);
                }
            }
        }
    }


    /**
     * 获取批量单元
     *
     * @param adverts 设备信息
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<AdvertDto> adverts) {
        List<String> unitIds = new ArrayList<String>();
        List<AdvertDto> tmpAdvertDtos = new ArrayList<>();
        for (AdvertDto advertDto : adverts) {

            if ("2000".equals(advertDto.getLocationTypeCd())) {
                unitIds.add(advertDto.getLocationObjId());
                tmpAdvertDtos.add(advertDto);
            }
        }

        if (unitIds.size() < 1) {
            return;
        }
        String[] tmpUnitIds = unitIds.toArray(new String[unitIds.size()]);

        FloorAndUnitDto floorAndUnitDto = new FloorAndUnitDto();
        floorAndUnitDto.setUnitIds(tmpUnitIds);
        //根据 userId 查询用户信息
        List<FloorAndUnitDto> unitDtos = unitInnerServiceSMOImpl.getFloorAndUnitInfo(floorAndUnitDto);

        for (AdvertDto advertDto : tmpAdvertDtos) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (advertDto.getLocationObjId().equals(tmpUnitDto.getUnitId())) {
                    advertDto.setLocationObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                    BeanConvertUtil.covertBean(tmpUnitDto, advertDto);
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param adverts 设备信息
     * @return 批量userIds 信息
     */
    private void refreshRooms(List<AdvertDto> adverts) {
        List<String> roomIds = new ArrayList<String>();
        List<AdvertDto> tmpAdvertDtos = new ArrayList<>();
        for (AdvertDto advertDto : adverts) {

            if ("3000".equals(advertDto.getLocationTypeCd())) {
                roomIds.add(advertDto.getLocationObjId());
                tmpAdvertDtos.add(advertDto);
            }
        }
        if (roomIds.size() < 1) {
            return;
        }
        String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(tmpRoomIds);
        roomDto.setCommunityId(adverts.get(0).getCommunityId());
        //根据 userId 查询用户信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (AdvertDto advertDto : tmpAdvertDtos) {
            for (RoomDto tmpRoomDto : roomDtos) {
                if (advertDto.getLocationObjId().equals(tmpRoomDto.getRoomId())) {
                    advertDto.setLocationObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    BeanConvertUtil.covertBean(tmpRoomDto, advertDto);
                }
            }
        }
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
