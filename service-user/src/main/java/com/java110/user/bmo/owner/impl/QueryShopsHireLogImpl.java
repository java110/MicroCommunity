package com.java110.user.bmo.owner.impl;

import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.user.bmo.owner.IQueryShopsHireLog;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryShopsHireLogImpl implements IQueryShopsHireLog {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(OwnerDto ownerDto) {

        int count = ownerInnerServiceSMOImpl.queryOwnerLogsCountByRoom(ownerDto);

        List<OwnerDto> ownerDtos = null;
        if (count > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerLogsByRoom(ownerDto);
        } else {
            ownerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) ownerDto.getRow()), count, ownerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }
}
