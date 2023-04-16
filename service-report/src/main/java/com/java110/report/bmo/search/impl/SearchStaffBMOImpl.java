package com.java110.report.bmo.search.impl;

import com.java110.dto.data.SearchDataDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.report.bmo.search.ISearchRoomBMO;
import com.java110.report.bmo.search.ISearchStaffBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchStaffBMOImpl implements ISearchStaffBMO {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {

        List<UserDto> userDtos = new ArrayList<>();

        //todo 根据名称
        queryStaffByName(searchDataDto, userDtos);

        //todo 根据手机号
        queryStaffByLink(searchDataDto, userDtos);

        searchDataDto.setStaffs(userDtos);

        return searchDataDto;
    }

    private void queryStaffByLink(SearchDataDto searchDataDto, List<UserDto> userDtos) {
        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return;
        }
        UserDto userDto = new UserDto();
        userDto.setTel(searchDataDto.getTel());
        userDto.setStoreId(searchDataDto.getStoreId());
        List<UserDto> tmpUserDtos = userInnerServiceSMOImpl.getStaffs(userDto);
        if(tmpUserDtos == null || tmpUserDtos.size()< 1){
            return ;
        }
        userDtos.addAll(tmpUserDtos);
    }

    private void queryStaffByName(SearchDataDto searchDataDto, List<UserDto> userDtos) {

        UserDto userDto = new UserDto();
        userDto.setNameLike(searchDataDto.getSearchValue());
        userDto.setStoreId(searchDataDto.getStoreId());
        List<UserDto> tmpUserDtos = userInnerServiceSMOImpl.getStaffs(userDto);
        if(tmpUserDtos == null || tmpUserDtos.size()< 1){
            return ;
        }
        userDtos.addAll(tmpUserDtos);
    }
}
