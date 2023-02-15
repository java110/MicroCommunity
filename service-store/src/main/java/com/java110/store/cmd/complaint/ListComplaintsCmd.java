package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.complaint.ApiComplaintDataVo;
import com.java110.vo.api.complaint.ApiComplaintVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "complaint.listComplaints")
public class ListComplaintsCmd extends Cmd {

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //如果根据业主ID查询转换为用手机号查询
        hasOwnerId(reqJson);

        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);
        String roomId = reqJson.getString("roomId");
        if (!StringUtil.isEmpty(roomId) && roomId.contains("-")) {
            String[] values = roomId.split("-", 3);
            if (values.length == 3) {
                RoomDto roomDto = new RoomDto();
                roomDto.setFloorNum(values[0]);
                roomDto.setUnitNum(values[1]);
                roomDto.setRoomNum(values[2]);
                roomDto.setCommunityId(reqJson.getString("communityId"));
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                Assert.listOnlyOne(roomDtos, "未找到房屋信息");
                complaintDto.setRoomId(roomDtos.get(0).getRoomId());
            }
        }
        int count = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        List<ApiComplaintDataVo> complaints = null;
        if (count > 0) {
            List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);
            complaintDtos = freshCurrentUser(complaintDtos);
            complaints = BeanConvertUtil.covertBeanList(complaintDtos, ApiComplaintDataVo.class);
            refreshPhotos(complaints);
        } else {
            complaints = new ArrayList<>();
        }
        ApiComplaintVo apiComplaintVo = new ApiComplaintVo();
        apiComplaintVo.setTotal(count);
        apiComplaintVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiComplaintVo.setComplaints(complaints);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiComplaintVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * //如果根据业主ID查询转换为用手机号查询
     * @param reqJson
     */
    private void hasOwnerId(JSONObject reqJson) {
        if(reqJson.containsKey("ownerId") && !StringUtil.isEmpty(reqJson.getString("ownerId"))){
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(reqJson.getString("ownerId"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if(ownerDtos != null && ownerDtos.size() > 0){
                reqJson.put("tel",ownerDtos.get(0).getLink());
            }
        }
    }

    private List<ComplaintDto> freshCurrentUser(List<ComplaintDto> complaintDtos) {
        List<ComplaintDto> tmpComplaintDtos = new ArrayList<>();
        for (ComplaintDto complaintDto : complaintDtos) {
            complaintDto = complaintUserInnerServiceSMOImpl.getTaskCurrentUser(complaintDto);
            tmpComplaintDtos.add(complaintDto);
        }
        return tmpComplaintDtos;
    }

    private void refreshPhotos(List<ApiComplaintDataVo> complaints) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        for (ApiComplaintDataVo complaintDataVo : complaints) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(complaintDataVo.getComplaintId());
            fileRelDto.setRelTypeCd("13000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName());
                photoVos.add(photoVo);
            }
            complaintDataVo.setPhotos(photoVos);
        }
    }
}
