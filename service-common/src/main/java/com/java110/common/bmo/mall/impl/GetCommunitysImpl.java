package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.area.AreaDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getCommunitysImpl")
public class GetCommunitysImpl implements IMallCommonApiBmo {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "page", "未包含page");
        Assert.hasKeyAndValue(reqJson, "row", "未包含row");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
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
        ResponseEntity<String> responseEntity =ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count, communitys);

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
        if (ListUtil.isNull(areaCodes)) {
            return;
        }
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
