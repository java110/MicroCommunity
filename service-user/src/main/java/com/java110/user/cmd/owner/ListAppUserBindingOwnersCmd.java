package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.workflowDataFile.WorkflowDataFileDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "owner.listAppUserBindingOwners")
public class ListAppUserBindingOwnersCmd extends Cmd {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Map<String, String> headers = context.getReqHeaders();

        if (!reqJson.containsKey("page")) {
            reqJson.put("page", 1);
        }
        if (!reqJson.containsKey("row")) {
            reqJson.put("row", 10);
        }

        if (!reqJson.containsKey("userId") && headers.containsKey("userid")) {
            reqJson.put("userId", headers.get("userid"));
        }

        //根据 openId 查询的
        if(reqJson.containsKey("openId") && !StringUtil.isEmpty(reqJson.getString("openId"))){
            reqJson.put("userId", "");
        }

        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);


        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);


        List<OwnerAppUserDto> ownerAppUserDtos = null;
        if (count > 0) {
            ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
            refreshCommunityArea(ownerAppUserDtos);
        } else {
            ownerAppUserDtos = new ArrayList<>();
        }
        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerAppUserDtos));

    }

    /**
     * 刷入小区地区
     *
     * @param ownerAppUserDtos
     */
    private void refreshCommunityArea(List<OwnerAppUserDto> ownerAppUserDtos) {
        String[] communityIds = getCommunityIds(ownerAppUserDtos);
        if (communityIds == null || communityIds.length < 1) {
            return;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (CommunityDto tmpCommunityDto : communityDtos) {
            for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
                if (ownerAppUserDto.getCommunityId().equals(tmpCommunityDto.getCommunityId())) {
                    ownerAppUserDto.setAreaCode(tmpCommunityDto.getAreaCode());
                    ownerAppUserDto.setAreaName(tmpCommunityDto.getAreaName());
                    ownerAppUserDto.setParentAreaCode(tmpCommunityDto.getParentAreaCode());
                    ownerAppUserDto.setParentAreaName(tmpCommunityDto.getParentAreaName());
                    ownerAppUserDto.setCommunityQrCode(tmpCommunityDto.getQrCode());
                }
            }
        }

        List<String> ownerIds = new ArrayList<>();
        for(OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos){
            if(StringUtil.isEmpty(ownerAppUserDto.getMemberId()) || "-1".equals(ownerAppUserDto.getMemberId())){
                continue;
            }
            ownerIds.add(ownerAppUserDto.getMemberId());
        }

        if(ownerIds.size() < 1){
            return;
        }


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if(fileRelDtos == null || fileRelDtos.size() < 1){
            return ;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for(OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos){
            for(FileRelDto tmpFileRelDto : fileRelDtos){
                if(!ownerAppUserDto.getMemberId().equals(tmpFileRelDto.getObjId())){
                    continue;
                }
                ownerAppUserDto.setHeadImgUrl(imgUrl +tmpFileRelDto.getFileSaveName() );
            }
        }
    }

    /**
     * 获取批量userIdsaveOwner
     *
     * @param ownerAppUserDtos 业主绑定信息
     * @return 批量userIds 信息
     */
    private String[] getCommunityIds(List<OwnerAppUserDto> ownerAppUserDtos) {
        List<String> communityIds = new ArrayList<String>();
        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            if (StringUtil.isEmpty(ownerAppUserDto.getCommunityId()) || "-1".equals(ownerAppUserDto.getCommunityId())) {
                continue;
            }
            communityIds.add(ownerAppUserDto.getCommunityId());
        }

        return communityIds.toArray(new String[communityIds.size()]);
    }
}
