package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.report.bmo.search.ISearchCarBMO;
import com.java110.report.bmo.search.ISearchOwnerBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchOwnerBMOImpl implements ISearchOwnerBMO {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<OwnerDto> ownerDtos = new ArrayList<>();

        //todo 根据名称模糊
        queryOwnerByName(searchDataDto,ownerDtos);

        //todo 根据手机号查询
        queryOwnerByLink(searchDataDto,ownerDtos);



        searchDataDto.setOwners(ownerDtos);

        return searchDataDto;
    }

    private void queryOwnerByLink(SearchDataDto searchDataDto, List<OwnerDto> ownerDtos) {

        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return ;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(searchDataDto.getTel());
        ownerDto.setCommunityId(searchDataDto.getCommunityId());
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> tmpOwnerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if(tmpOwnerDtos == null || tmpOwnerDtos.size() < 1){
            return ;
        }

        ownerDtos.addAll(tmpOwnerDtos);
    }

    private void queryOwnerByName(SearchDataDto searchDataDto, List<OwnerDto> ownerDtos) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setNameLike(searchDataDto.getSearchValue());
        ownerDto.setCommunityId(searchDataDto.getCommunityId());
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> tmpOwnerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if(tmpOwnerDtos == null || tmpOwnerDtos.size() < 1){
            return ;
        }

        ownerDtos.addAll(tmpOwnerDtos);
    }
}
