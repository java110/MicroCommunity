package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.report.bmo.search.ISearchCarBMO;
import com.java110.report.bmo.search.ISearchRoomBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchCarBMOImpl implements ISearchCarBMO {

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<OwnerCarDto> ownerCarDtos = new ArrayList<>();

        queryCarsByCarNum(searchDataDto, ownerCarDtos);

        searchDataDto.setCars(ownerCarDtos);

        return searchDataDto;
    }

    private void queryCarsByCarNum(SearchDataDto searchDataDto, List<OwnerCarDto> ownerCarDtos) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNumLike(searchDataDto.getSearchValue());
        ownerCarDto.setCommunityId(searchDataDto.getCommunityId());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        List<OwnerCarDto> tmpOwnerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (tmpOwnerCarDtos == null || tmpOwnerCarDtos.size() < 1) {
            return;
        }

        ownerCarDtos.addAll(tmpOwnerCarDtos);
    }
}
