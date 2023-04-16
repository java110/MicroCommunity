package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.report.bmo.search.ISearchOwnerBMO;
import com.java110.report.bmo.search.ISearchOwnerMemberBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchOwnerMemberBMOImpl implements ISearchOwnerMemberBMO {
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<OwnerDto> ownerDtos = new ArrayList<>();

        //todo 根据名称模糊
        queryOwnerMemberByName(searchDataDto,ownerDtos);

        //todo 根据手机号查询
        queryOwnerMemberByLink(searchDataDto,ownerDtos);

        searchDataDto.setOwnerMembers(ownerDtos);

        return searchDataDto;
    }

    private void queryOwnerMemberByLink(SearchDataDto searchDataDto, List<OwnerDto> ownerDtos) {

        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return ;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(searchDataDto.getTel());
        ownerDto.setCommunityId(searchDataDto.getCommunityId());
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER,OwnerDto.OWNER_TYPE_CD_RENTING});
        List<OwnerDto> tmpOwnerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if(tmpOwnerDtos == null || tmpOwnerDtos.size() < 1){
            return ;
        }

        ownerDtos.addAll(tmpOwnerDtos);
    }

    private void queryOwnerMemberByName(SearchDataDto searchDataDto, List<OwnerDto> ownerDtos) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setNameLike(searchDataDto.getSearchValue());
        ownerDto.setCommunityId(searchDataDto.getCommunityId());
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER,OwnerDto.OWNER_TYPE_CD_RENTING,OwnerDto.OWNER_TYPE_CD_TEMP,OwnerDto.OWNER_TYPE_CD_OTHER});
        List<OwnerDto> tmpOwnerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if(tmpOwnerDtos == null || tmpOwnerDtos.size() < 1){
            return ;
        }

        ownerDtos.addAll(tmpOwnerDtos);
    }
}
