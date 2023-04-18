package com.java110.user.bmo.activitiesRule.impl;

import com.java110.dto.activities.ActivitiesRuleDto;
import com.java110.intf.user.IActivitiesRuleInnerServiceSMO;
import com.java110.user.bmo.activitiesRule.IGetActivitiesRuleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getActivitiesRuleBMOImpl")
public class GetActivitiesRuleBMOImpl implements IGetActivitiesRuleBMO {

    @Autowired
    private IActivitiesRuleInnerServiceSMO activitiesRuleInnerServiceSMOImpl;

    /**
     * @param activitiesRuleDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ActivitiesRuleDto activitiesRuleDto) {


        int count = activitiesRuleInnerServiceSMOImpl.queryActivitiesRulesCount(activitiesRuleDto);

        List<ActivitiesRuleDto> activitiesRuleDtos = null;
        if (count > 0) {
            activitiesRuleDtos = activitiesRuleInnerServiceSMOImpl.queryActivitiesRules(activitiesRuleDto);
        } else {
            activitiesRuleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) activitiesRuleDto.getRow()), count, activitiesRuleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
