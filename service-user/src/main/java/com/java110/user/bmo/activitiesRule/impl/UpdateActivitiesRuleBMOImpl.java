package com.java110.user.bmo.activitiesRule.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IActivitiesRuleInnerServiceSMO;
import com.java110.po.activitiesRule.ActivitiesRulePo;
import com.java110.user.bmo.activitiesRule.IUpdateActivitiesRuleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateActivitiesRuleBMOImpl")
public class UpdateActivitiesRuleBMOImpl implements IUpdateActivitiesRuleBMO {

    @Autowired
    private IActivitiesRuleInnerServiceSMO activitiesRuleInnerServiceSMOImpl;

    /**
     * @param activitiesRulePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ActivitiesRulePo activitiesRulePo) {

        int flag = activitiesRuleInnerServiceSMOImpl.updateActivitiesRule(activitiesRulePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
