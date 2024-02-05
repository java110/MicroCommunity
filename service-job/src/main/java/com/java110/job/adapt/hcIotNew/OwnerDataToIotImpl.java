package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineTranslateErrorDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.po.machine.MachineTranslateErrorPo;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerDataToIotImpl implements IOwnerDataToIot {

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    @Override
    public void sendOwnerData(OwnerDto ownerDto) {
        JSONObject paramIn = new JSONObject();
        paramIn.put("communityId", ownerDto.getCommunityId());
        paramIn.put("memberId", ownerDto.getMemberId());
        paramIn.put("ownerId", ownerDto.getOwnerId());
        paramIn.put("name", ownerDto.getName());
        paramIn.put("ownerTypeCd", ownerDto.getOwnerTypeCd());
        paramIn.put("idCard", ownerDto.getIdCard());
        paramIn.put("link", ownerDto.getLink());
        paramIn.put("ownerPhoto", getOwnerPhoto(ownerDto));
        paramIn.put("cardNumber", getCardNumber(ownerDto));


        //todo 查询业主房屋
        getOwnerRoom(paramIn, ownerDto);

        //todo 查询业主车辆
        getOwnerCars(paramIn, ownerDto);

        ResultVo resultVo = sendIotImpl.post("/iot/api/owner.addOwnerApi", paramIn);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            saveTranslateLog(ownerDto.getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_FACE,
                    ownerDto.getMemberId(), ownerDto.getName(),
                    MachineTranslateDto.STATE_ERROR, resultVo.getMsg());
            return;
        }

        saveTranslateLog(ownerDto.getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_FACE,
                ownerDto.getMemberId(), ownerDto.getName(),
                MachineTranslateDto.STATE_SUCCESS, resultVo.getMsg());


    }

    private String getCardNumber(OwnerDto ownerDto) {

        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberId(ownerDto.getMemberId());
        ownerAttrDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        if (ListUtil.isNull(ownerAttrDtos)) {
            return "";
        }

        return OwnerAttrDto.getValue(ownerAttrDtos, OwnerAttrDto.SPEC_CD_ACCESS_CONTROL_KEY);

    }


    /**
     * 存储交互 记录
     *
     * @param communityId
     */
    public void saveTranslateLog(String communityId, String cmd, String objId, String objName, String state, String remark) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode("-1");
        machineTranslateDto.setMachineId("-1");
        machineTranslateDto.setObjId(objId);
        machineTranslateDto.setObjName(objName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_OWNER);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }


    /**
     * 查询业主车辆
     *
     * @param paramIn
     * @param ownerDto
     */
    private void getOwnerCars(JSONObject paramIn, OwnerDto ownerDto) {

        /**
         * carMemberId
         * carId
         * carNum
         * paId
         * psId
         * paNum
         * psNum
         * carTypeCd
         * startTime
         * endTime
         * leaseType
         */
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setOwnerId(ownerDto.getOwnerId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            return;
        }
        JSONArray cars = new JSONArray();

        JSONObject car = null;
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            car = new JSONObject();
            car.put("carMemberId", tmpOwnerCarDto.getMemberId());
            car.put("carId", tmpOwnerCarDto.getCarId());
            car.put("carNum", tmpOwnerCarDto.getCarNum());
            car.put("paId", tmpOwnerCarDto.getPaId());
            car.put("psId", tmpOwnerCarDto.getPsId());
            car.put("paNum", tmpOwnerCarDto.getAreaNum());
            car.put("psNum", tmpOwnerCarDto.getNum());
            car.put("carTypeCd", tmpOwnerCarDto.getCarTypeCd());
            car.put("startTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getStartTime()));
            car.put("endTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getEndTime()));
            car.put("leaseType", tmpOwnerCarDto.getLeaseType());
            cars.add(car);
        }

        paramIn.put("cars", cars);

    }

    /**
     * 查询业主房屋
     *
     * @param paramIn
     * @param ownerDto
     */
    private void getOwnerRoom(JSONObject paramIn, OwnerDto ownerDto) {

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDto.getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }

        List<String> roomIds = new ArrayList<>();

        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(ownerDto.getCommunityId());
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        JSONArray rooms = new JSONArray();

        JSONObject room = null;
        for (RoomDto tmpRoomDto : roomDtos) {
            room = new JSONObject();
            room.put("roomId", tmpRoomDto.getRoomId());
            room.put("floorId", tmpRoomDto.getFloorId());
            room.put("unitId", tmpRoomDto.getUnitId());
            room.put("floorNum", tmpRoomDto.getFloorNum());
            room.put("unitNum", tmpRoomDto.getUnitNum());
            room.put("roomNum", tmpRoomDto.getRoomNum());
            rooms.add(room);
        }
        paramIn.put("rooms", rooms);

    }

    private String getOwnerPhoto(OwnerDto ownerDto) {


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(ownerDto.getMemberId());
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (ListUtil.isNull(fileRelDtos)) {
            return "";
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(ownerDto.getCommunityId());
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (ListUtil.isNull(fileDtos)) {
            return "";
        }

        return fileDtos.get(0).getContext();
    }


}
