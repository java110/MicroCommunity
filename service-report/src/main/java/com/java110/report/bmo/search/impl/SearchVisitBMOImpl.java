package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.report.bmo.search.ISearchStaffBMO;
import com.java110.report.bmo.search.ISearchVisitBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchVisitBMOImpl implements ISearchVisitBMO {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {

        List<VisitDto> visitDtos = new ArrayList<>();

        // todo 查询访问
        queryVisitByName(searchDataDto,visitDtos);

        // todo 通过电话
        queryVisitByLink(searchDataDto,visitDtos);


        searchDataDto.setVisitDtos(visitDtos);

        return searchDataDto;
    }

    private void queryVisitByLink(SearchDataDto searchDataDto, List<VisitDto> visitDtos) {

        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return;
        }

        VisitDto visitDto = new VisitDto();
        visitDto.setPhoneNumber(searchDataDto.getTel());
        visitDto.setCommunityId(searchDataDto.getCommunityId());
        List<VisitDto> tmpVisitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);

        if(tmpVisitDtos == null || tmpVisitDtos.size()< 1){
            return ;
        }

        visitDtos.addAll(tmpVisitDtos);

    }

    private void queryVisitByName(SearchDataDto searchDataDto, List<VisitDto> visitDtos) {

        VisitDto visitDto = new VisitDto();
        visitDto.setvNameLike(searchDataDto.getSearchValue());
        visitDto.setCommunityId(searchDataDto.getCommunityId());
        List<VisitDto> tmpVisitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);

        if(tmpVisitDtos == null || tmpVisitDtos.size()< 1){
            return ;
        }

        visitDtos.addAll(tmpVisitDtos);
    }
}
