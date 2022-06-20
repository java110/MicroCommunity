package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.area.AreaDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "community.listCommunitys")
public class ListCommunitysCmd extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);

        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<ApiCommunityDataVo> communitys = null;
        ApiCommunityDataVo apiCommunityDataVo = null;
        if (count > 0) {
            communitys = new ArrayList<>();
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            for (CommunityDto tmpCommunityDto : communityDtos) {
                apiCommunityDataVo = BeanConvertUtil.covertBean(tmpCommunityDto, ApiCommunityDataVo.class);
                apiCommunityDataVo.setCommunityAttrDtos(tmpCommunityDto.getCommunityAttrDtos());
                communitys.add(apiCommunityDataVo);
            }
            refreshCommunityCity(communitys);
        } else {
            communitys = new ArrayList<>();
        }
        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();
        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新cityName
     *
     * @param communitys
     */
    private void refreshCommunityCity(List<ApiCommunityDataVo> communitys) {
        List<String> areaCodes = new ArrayList<>();
        for (ApiCommunityDataVo communityDataVo : communitys) {
            areaCodes.add(communityDataVo.getCityCode());
        }
        if (areaCodes.size() > 0) {
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaCodes(areaCodes.toArray(new String[areaCodes.size()]));
            List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getProvCityArea(areaDto);
            for (ApiCommunityDataVo communityDataVo : communitys) {
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
}
