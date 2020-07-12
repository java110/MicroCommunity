package com.java110.common.bmo.appraise.impl;

import com.java110.common.bmo.appraise.IGetAppraiseBMO;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.intf.common.IAppraiseInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GetAppraiseBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/12 23:08
 * @Version 1.0
 * add by wuxw 2020/7/12
 **/
@Service("getAppraiseBMOImpl")
public class GetAppraiseBMOImpl implements IGetAppraiseBMO {

    @Autowired
    private IAppraiseInnerServiceSMO appraiseInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> getAppraise(AppraiseDto appraiseDto) {
        int count = appraiseInnerServiceSMOImpl.queryAppraisesCount(appraiseDto);

        List<AppraiseDto> appraiseDtos = null;

        if (count > 0) {
            appraiseDtos = appraiseInnerServiceSMOImpl.queryAppraises(appraiseDto);
        } else {
            appraiseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) appraiseDto.getRow()), count, appraiseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }
}
