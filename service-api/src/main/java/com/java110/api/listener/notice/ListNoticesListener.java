package com.java110.api.listener.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.community.*;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.notice.ApiNoticeDataVo;
import com.java110.vo.api.notice.ApiNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listNoticesListener")
public class ListNoticesListener extends AbstractServiceApiListener {

    @Autowired
    private INoticeInnerServiceSMO noticeInnerServiceSMOImpl;

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
        return ServiceCodeConstant.SERVICE_CODE_LIST_NOTICES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public INoticeInnerServiceSMO getNoticeInnerServiceSMOImpl() {
        return noticeInnerServiceSMOImpl;
    }

    public void setNoticeInnerServiceSMOImpl(INoticeInnerServiceSMO noticeInnerServiceSMOImpl) {
        this.noticeInnerServiceSMOImpl = noticeInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        NoticeDto noticeDto = BeanConvertUtil.covertBean(reqJson, NoticeDto.class);

        int count = noticeInnerServiceSMOImpl.queryNoticesCount(noticeDto);

        List<ApiNoticeDataVo> notices = null;

        if (count > 0) {
            notices = BeanConvertUtil.covertBeanList(noticeInnerServiceSMOImpl.queryNotices(noticeDto), ApiNoticeDataVo.class);
            refreshNotice(notices);
        } else {
            notices = new ArrayList<>();
        }

        ApiNoticeVo apiNoticeVo = new ApiNoticeVo();

        apiNoticeVo.setTotal(count);
        apiNoticeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiNoticeVo.setNotices(notices);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiNoticeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void refreshNotice(List<ApiNoticeDataVo> notices) {

        //批量处理 小区
        refreshCommunitys(notices);

        //刷新楼栋
        refreshFloors(notices);

        //批量处理单元信息
        refreshUnits(notices);

        //批量处理 房屋信息
        refreshRooms(notices);


    }

    /**
     * 获取批量小区
     *
     * @param notices 设备信息
     * @return 批量userIds 信息
     */
    private void refreshCommunitys(List<ApiNoticeDataVo> notices) {
        List<String> communityIds = new ArrayList<String>();
        List<ApiNoticeDataVo> tmpNoticeDtos = new ArrayList<>();
        for (ApiNoticeDataVo noticeDto : notices) {

            if (NoticeDto.OBJ_TYPE_COMMUNITY.equals(noticeDto.getObjType())
            ) {
                communityIds.add(noticeDto.getObjId());
                tmpNoticeDtos.add(noticeDto);
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

        for (ApiNoticeDataVo noticeDto : tmpNoticeDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (noticeDto.getObjId().equals(tmpCommunityDto.getCommunityId())) {
                    noticeDto.setObjName(tmpCommunityDto.getName());
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param notices 设备信息
     * @return 批量userIds 信息
     */
    private void refreshFloors(List<ApiNoticeDataVo> notices) {
        List<String> floorIds = new ArrayList<String>();
        List<ApiNoticeDataVo> tmpNoticeDtos = new ArrayList<>();
        for (ApiNoticeDataVo noticeDto : notices) {
            if (NoticeDto.OBJ_TYPE_UNIT.equals(noticeDto.getObjType())) {
                floorIds.add(noticeDto.getObjId());
                tmpNoticeDtos.add(noticeDto);
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

        for (ApiNoticeDataVo noticeDto : tmpNoticeDtos) {
            for (FloorDto tmpFloorDto : floorDtos) {
                if (noticeDto.getObjId().equals(tmpFloorDto.getFloorId())) {
                    noticeDto.setObjName(tmpFloorDto.getFloorNum() + "栋");
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param notices 设备信息
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<ApiNoticeDataVo> notices) {
        List<String> unitIds = new ArrayList<String>();
        List<ApiNoticeDataVo> tmpNoticeDtos = new ArrayList<>();
        for (ApiNoticeDataVo noticeDto : notices) {
            if (NoticeDto.OBJ_TYPE_UNIT.equals(noticeDto.getObjType())) {
                unitIds.add(noticeDto.getObjId());
                tmpNoticeDtos.add(noticeDto);
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

        for (ApiNoticeDataVo noticeDto : tmpNoticeDtos) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (noticeDto.getObjId().equals(tmpUnitDto.getUnitId())) {
                    noticeDto.setObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param notices 设备信息
     * @return 批量userIds 信息
     */
    private void refreshRooms(List<ApiNoticeDataVo> notices) {
        List<String> roomIds = new ArrayList<String>();
        List<ApiNoticeDataVo> tmpNoticeDtos = new ArrayList<>();
        for (ApiNoticeDataVo noticeDto : notices) {
            if (NoticeDto.OBJ_TYPE_ROOM.equals(noticeDto.getObjType())) {
                if ("3000".equals(noticeDto.getObjType())) {
                    roomIds.add(noticeDto.getObjId());
                    tmpNoticeDtos.add(noticeDto);
                }
            }
            if (roomIds.size() < 1) {
                return;
            }
            String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

            RoomDto roomDto = new RoomDto();
            roomDto.setRoomIds(tmpRoomIds);
            roomDto.setCommunityId(notices.get(0).getCommunityId());
            //根据 userId 查询用户信息
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            for (ApiNoticeDataVo tmpNoticeDto : tmpNoticeDtos) {
                for (RoomDto tmpRoomDto : roomDtos) {
                    if (tmpNoticeDto.getObjId().equals(tmpRoomDto.getRoomId())) {
                        tmpNoticeDto.setObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    }
                }
            }
        }
    }
}
