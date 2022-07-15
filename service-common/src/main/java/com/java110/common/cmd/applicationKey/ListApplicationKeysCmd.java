package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.applicationKey.ApiApplicationKeyDataVo;
import com.java110.vo.api.applicationKey.ApiApplicationKeyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "applicationKey.listApplicationKeys")
public class ListApplicationKeysCmd extends Cmd{

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ApplicationKeyDto applicationKeyDto = BeanConvertUtil.covertBean(reqJson, ApplicationKeyDto.class);

        int count = applicationKeyInnerServiceSMOImpl.queryApplicationKeysCount(applicationKeyDto);

        List<ApiApplicationKeyDataVo> applicationKeys = null;

        if (count > 0) {
            List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);
            // 刷新 位置信息
            refreshMachines(applicationKeyDtos);
            applicationKeys = BeanConvertUtil.covertBeanList(applicationKeyDtos, ApiApplicationKeyDataVo.class);
        } else {
            applicationKeys = new ArrayList<>();
        }

        ApiApplicationKeyVo apiApplicationKeyVo = new ApiApplicationKeyVo();

        apiApplicationKeyVo.setTotal(count);
        apiApplicationKeyVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiApplicationKeyVo.setApplicationKeys(applicationKeys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiApplicationKeyVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }


    private void refreshMachines(List<ApplicationKeyDto> applicationKeyDtos) {

        //批量处理 小区
        refreshCommunitys(applicationKeyDtos);

        //批量处理单元信息
        refreshUnits(applicationKeyDtos);

        //批量处理 房屋信息
        refreshRooms(applicationKeyDtos);

    }

    /**
     * 获取批量小区
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshCommunitys(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> communityIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if (!"2000".equals(applicationKeyDto.getLocationTypeCd())
                    && !"3000".equals(applicationKeyDto.getLocationTypeCd())
                    ) {
                communityIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
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

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpCommunityDto.getCommunityId())) {
                    applicationKeyDto.setLocationObjName(tmpCommunityDto.getName() + " " + applicationKeyDto.getLocationTypeName());
                }
            }
        }
    }


    /**
     * 获取批量单元
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> unitIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if ("2000".equals(applicationKeyDto.getLocationTypeCd())) {
                unitIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
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

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpUnitDto.getUnitId())) {
                    applicationKeyDto.setLocationObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                    BeanConvertUtil.covertBean(tmpUnitDto, applicationKeyDto);
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshRooms(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> roomIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if ("3000".equals(applicationKeyDto.getLocationTypeCd())) {
                roomIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
            }
        }
        if (roomIds.size() < 1) {
            return;
        }
        String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(tmpRoomIds);
        roomDto.setCommunityId(applicationKeyDtos.get(0).getCommunityId());
        //根据 userId 查询用户信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (RoomDto tmpRoomDto : roomDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpRoomDto.getRoomId())) {
                    applicationKeyDto.setLocationObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    BeanConvertUtil.covertBean(tmpRoomDto, applicationKeyDto);
                }
            }
        }
    }
}
