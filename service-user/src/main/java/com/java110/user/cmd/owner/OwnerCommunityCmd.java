package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务注册功能迁移
 */
@Java110Cmd(serviceCode = "owner.ownerCommunity")
public class OwnerCommunityCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(OwnerCommunityCmd.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "link", "未包含联系电话");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("link"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }

        List<String> communityIds = new ArrayList<>();
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            communityIds.add(tmpOwnerDto.getCommunityId());
            tmpOwnerDto.setAppUserName(tmpOwnerDto.getName());
        }

        CommunityDto communityDto = new CommunityDto();
        communityDto.setState("1100");
        communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if(communityDtos == null || communityDtos.size()<1){
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return ;
        }
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (!tmpCommunityDto.getCommunityId().equals(tmpOwnerDto.getCommunityId())) {
                    continue;
                }
                tmpOwnerDto.setCommunityName(tmpCommunityDto.getName());
                tmpOwnerDto.setsCommunityTel(tmpCommunityDto.getTel());
                tmpOwnerDto.setCommunityQrCode(tmpCommunityDto.getQrCode());
            }
        }

        //在判断 是否在 owner_app_user 表中
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("link"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        for (OwnerDto tmpOwnerDto : ownerDtos) {
            if (hasOwnerAppUser(tmpOwnerDto, ownerAppUserDtos)) {
                continue;
            }
            addOwnerAppUser(tmpOwnerDto, ownerAppUserDtos.get(0));
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(ownerDtos));

    }

    /**
     * @param ownerDto
     * @param ownerAppUserDtos
     * @return
     */
    private boolean hasOwnerAppUser(OwnerDto ownerDto, List<OwnerAppUserDto> ownerAppUserDtos) {

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            return false;
        }

        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            if (ownerDto.getLink().equals(ownerAppUserDto.getLink())
                    && ownerDto.getMemberId().equals(ownerAppUserDto.getMemberId())) {
                return true;
            }
        }

        return false;
    }

    private void addOwnerAppUser(OwnerDto ownerDto, OwnerAppUserDto ownerAppUserDto) {

        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUserDto, OwnerAppUserPo.class);
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId(ownerDto.getMemberId());
        ownerAppUserPo.setCommunityName(ownerDto.getCommunityName());
        ownerAppUserPo.setCommunityId(ownerDto.getCommunityId());
        ownerAppUserPo.setAppUserName(ownerDto.getName());
        ownerAppUserPo.setIdCard(ownerDto.getIdCard());
        int flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("添加用户业主关系失败");
        }
    }

}
