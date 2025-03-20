package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.area.AreaDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 运营查询小区
 */
@Java110Cmd(serviceCode = "community.listAdminCommunitys")
public class ListAdminCommunitysCmd extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);

        String staffId = CmdContextUtils.getUserId(context);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }

        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<CommunityDto> communityDtos = null;
        CommunityDto apiCommunityDataVo = null;
        if (count > 0) {
            communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            refreshCommunityCity(communityDtos);
        } else {
            communityDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communityDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新cityName
     *
     * @param communitys
     */
    private void refreshCommunityCity(List<CommunityDto> communitys) {
        List<String> areaCodes = new ArrayList<>();
        for (CommunityDto communityDataVo : communitys) {
            areaCodes.add(communityDataVo.getCityCode());
        }
        if (ListUtil.isNull(areaCodes)) {
            return;
        }
        AreaDto areaDto = new AreaDto();
        areaDto.setAreaCodes(areaCodes.toArray(new String[areaCodes.size()]));
        List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getProvCityArea(areaDto);
        for (CommunityDto communityDataVo : communitys) {
            for (AreaDto tmpAreaDto : areaDtos) {
                if (communityDataVo.getCityCode().equals(tmpAreaDto.getAreaCode())) {
                    communityDataVo.setCityName(tmpAreaDto.getProvName() + tmpAreaDto.getCityName() + tmpAreaDto.getAreaName());
                    break;
                }
                communityDataVo.setCityName("未知");
            }
        }
    }

}
