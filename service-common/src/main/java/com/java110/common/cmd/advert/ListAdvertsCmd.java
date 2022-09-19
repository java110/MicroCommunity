package com.java110.common.cmd.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IAdvertInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.advert.ApiAdvertDataVo;
import com.java110.vo.api.advert.ApiAdvertVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "查询广告",
        description = "主要用于给业主端物业员工端发送广告",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/advert.listAdverts",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "advert.listAdverts"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page",type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11,remark = "行数"),
        @Java110ParamDoc(name = "advertId", length = 30, remark = "广告编号 "),
        @Java110ParamDoc(name = "adTypeCd", length = 12, remark = "投放位置 2000 业主首页 4000 员工首页"),

})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "adverts", type = "Object", length = 250, defaultValue = "-", remark = "数据"),
                @Java110ParamDoc(parentNodeName = "adverts",name = "advertId", type = "String", length = 250,  remark = "广告编号"),
                @Java110ParamDoc(parentNodeName = "adverts",name = "adName", type = "String", length = 250,  remark = "广告名称"),
                @Java110ParamDoc(parentNodeName = "adverts",name = "startTime", length = 12, remark = "开始时间  "),
                @Java110ParamDoc(parentNodeName = "adverts",name = "endTime", length = 12, remark = "结束时间 "),

        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/advert.listAdverts?adName=&adTypeCd=&classify=&state=&locationTypeCd=&page=1&row=10",
        resBody="{\"adverts\":[{\"adName\":\"123456\",\"adTypeCd\":\"20000\",\"advertId\":\"962022091922110006\",\"advertType\":\"2\",\"classify\":\"9006\",\"classifyName\":\"互联网\",\"communityId\":\"9999\",\"createTime\":\"2022-09-19 11:26:47\",\"endTime\":\"2023-03-24 11:55:00\",\"locationObjId\":\"-1\",\"locationObjName\":\"员工首页\",\"locationTypeCd\":\"4000\",\"pageUrl\":\"http://taobao.com\",\"seq\":\"2\",\"startTime\":\"2022-09-19 11:25:40\",\"state\":\"1000\",\"stateName\":\"未审核\",\"viewType\":\"8888\"}],\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)
@Java110Cmd(serviceCode = "advert.listAdverts")
public class ListAdvertsCmd extends Cmd {


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
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        AdvertDto advertDto = BeanConvertUtil.covertBean(reqJson, AdvertDto.class);
        int count = advertInnerServiceSMOImpl.queryAdvertsCount(advertDto);
        List<ApiAdvertDataVo> adverts = null;
        if (count > 0) {
            List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);
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
        //批量处理楼栋信息
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
     * 获取批量楼栋
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
        for (AdvertDto advertDto : tmpAdvertDtos) {
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorId(advertDto.getLocationObjId());
            floorDto.setCommunityId(advertDto.getCommunityId());
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            if (floorDtos == null || floorDtos.size() < 1) {
                return;
            }
            if (advertDto.getLocationObjId().equals(floorDtos.get(0).getFloorId())) {
                advertDto.setLocationObjName(floorDtos.get(0).getFloorNum() + "栋");
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
                }
            }
        }
    }

}
