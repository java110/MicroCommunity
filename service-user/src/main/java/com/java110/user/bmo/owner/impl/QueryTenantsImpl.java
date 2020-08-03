package com.java110.user.bmo.owner.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.user.bmo.owner.IQueryTenants;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryTenantsImpl implements IQueryTenants {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(JSONObject reqJson) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("code"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        JSONArray data = new JSONArray();
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            dealOwner(reqJson, tmpOwnerDto, data);
        }


        return ResultVo.createResponseEntity(1,data.size(),data);
    }

    /**
     * 处理业主信息
     *
     * @param reqJson
     * @param tmpOwnerDto
     * @param data
     */
    private void dealOwner(JSONObject reqJson, OwnerDto tmpOwnerDto, JSONArray data) {

        JSONObject dataObj = new JSONObject();
        dataObj.put("name", tmpOwnerDto.getName());
        dataObj.put("gender", tmpOwnerDto.getSex());
        dataObj.put("certificationType", "1");//身份证
        dataObj.put("certificationCode", tmpOwnerDto.getIdCard());
        dataObj.put("birthday", "");
        dataObj.put("contact", tmpOwnerDto.getLink());
        dataObj.put("tenantsType", tmpOwnerDto.getOwnerTypeCd());

        dataObj.put("tenantsPhoto", getOwnerPhoto(tmpOwnerDto));
        dataObj.put("accessTypt", "0");
        dataObj.put("accessInfo", "");

        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(tmpOwnerDto.getOwnerTypeCd())) {
            OwnerDto memberOwnerDto = new OwnerDto();
            memberOwnerDto.setOwnerId(tmpOwnerDto.getOwnerId());
            memberOwnerDto.setCommunityId(reqJson.getString("code"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(memberOwnerDto);

            if (ownerDtos != null && ownerDtos.size() > 0) {
                dataObj.put("ownerName", ownerDtos.get(0).getName());
                dataObj.put("ownerCertificationType", "1");
                dataObj.put("ownerCertificationCode", ownerDtos.get(0).getIdCard());
            }
        } else {
            dataObj.put("ownerName", tmpOwnerDto.getName());
            dataObj.put("ownerCertificationType", "1");
            dataObj.put("ownerCertificationCode", tmpOwnerDto.getIdCard());
        }


        dataObj.put("weixin", "");
        dataObj.put("qq", "");
        dataObj.put("email", "");
        dataObj.put("update_time", tmpOwnerDto.getCreateTime());
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(tmpOwnerDto.getOwnerId());

        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            dataObj.put("floor", "");
            dataObj.put("building", "");
            dataObj.put("houses", "");
            data.add(dataObj);
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
        roomDto.setCommunityId(reqJson.getString("code"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            dataObj.put("floor", "");
            dataObj.put("building", "");
            dataObj.put("houses", "");
            data.add(dataObj);
            return;
        }
        String floor = "";
        String building = "";
        String houses = "";
        for (RoomDto tmpRoomDto : roomDtos) {
            floor += (tmpRoomDto.getLayer() + ",");
            building += (tmpRoomDto.getFloorNum() + ",");
            houses += (tmpRoomDto.getRoomNum() + ",");
        }
        dataObj.put("floor", floor);
        dataObj.put("building", building);
        dataObj.put("houses", houses);
        data.add(dataObj);


    }

    private String getOwnerPhoto(OwnerDto ownerDto) {
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(ownerDto.getMemberId());
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return "";
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(ownerDto.getCommunityId());
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            return "";
        }

        return fileDtos.get(0).getContext();
    }
}
