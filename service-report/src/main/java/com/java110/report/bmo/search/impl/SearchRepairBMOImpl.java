package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.report.bmo.search.ISearchOwnerBMO;
import com.java110.report.bmo.search.ISearchRepairBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchRepairBMOImpl implements ISearchRepairBMO {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<RepairDto> repairDtos = new ArrayList<>();

        // todo 根据名称查询报修
        queryRepairByName(searchDataDto,repairDtos);

        // todo 根据手机号查询报修
        queryRepairByLink(searchDataDto,repairDtos);

        searchDataDto.setRepairs(repairDtos);

        return searchDataDto;
    }

    private void queryRepairByLink(SearchDataDto searchDataDto, List<RepairDto> repairDtos) {

        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return;
        }

        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(searchDataDto.getCommunityId());
        repairDto.setTel(searchDataDto.getTel());
        List<RepairDto> tmpRepairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        if(tmpRepairDtos == null || tmpRepairDtos.size() < 1){
            return ;
        }

        repairDtos.addAll(tmpRepairDtos);
    }

    private void queryRepairByName(SearchDataDto searchDataDto, List<RepairDto> repairDtos) {



        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(searchDataDto.getCommunityId());
        repairDto.setRepairNameLike(searchDataDto.getSearchValue());
        List<RepairDto> tmpRepairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        if(tmpRepairDtos == null || tmpRepairDtos.size() < 1){
            return ;
        }

        repairDtos.addAll(tmpRepairDtos);
    }
}
