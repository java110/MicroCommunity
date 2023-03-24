package com.java110.community.cmd.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.community.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.notice.ApiNoticeDataVo;
import com.java110.vo.api.notice.ApiNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 查询公告 功能
 * 请求地址为/app/login.pcUserLogin
 */

@Java110CmdDoc(title = "查询公告",
        description = "提供外系统查询物业系统公告",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/notice.listNotices",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "notice.listNotices",
        seq = 21
)
//入参要求
@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page",type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 30, remark = "行数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
                @Java110ParamDoc(parentNodeName = "token",name = "abc", type = "String", remark = "临时票据"),

        }
)

@Java110ExampleDoc(
        reqBody="http://localhost:3000/app/notice.listNotices?title=&noticeTypeCd=&state=&noticeId=&startTime=&endTime=&page=1&row=10&communityId=2022110264250009",
        resBody="{\"notices\":[],\"page\":0,\"records\":0,\"rows\":0,\"total\":0}"
)

@Java110Cmd(serviceCode = "notice.listNotices")
public class ListNoticesCmd extends Cmd {

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
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        reqJson.remove("userId");
        NoticeDto noticeDto = BeanConvertUtil.covertBean(reqJson, NoticeDto.class);
        if (!StringUtil.isEmpty("clientType") && "H5".equals(reqJson.get("clientType"))) {
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            noticeDto.setStartTime(df.format(day));
            noticeDto.setEndTime(df.format(day));
        }

        // 多类型同时查询
        if (!StringUtil.isEmpty(noticeDto.getNoticeTypeCd()) && noticeDto.getNoticeTypeCd().contains(",")) {
            noticeDto.setNoticeTypeCds(noticeDto.getNoticeTypeCd().split(","));
            noticeDto.setNoticeTypeCd("");
        }

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

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void refreshNotice(List<ApiNoticeDataVo> notices) {

        refreshWechats(notices);
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
    private void refreshWechats(List<ApiNoticeDataVo> notices) {

        for (ApiNoticeDataVo noticeDto : notices) {
            if (NoticeDto.OBJ_TYPE_ALL.equals(noticeDto.getObjType())) {
                noticeDto.setObjName("关注用户");
            }
        }
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
