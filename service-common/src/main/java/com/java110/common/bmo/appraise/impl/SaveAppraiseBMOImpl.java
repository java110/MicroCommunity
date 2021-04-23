package com.java110.common.bmo.appraise.impl;

import com.java110.common.bmo.appraise.ISaveAppraiseBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAppraiseInnerServiceSMO;
import com.java110.po.appraise.AppraisePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAppraiseBMOImpl")
public class SaveAppraiseBMOImpl implements ISaveAppraiseBMO {

    @Autowired
    private IAppraiseInnerServiceSMO appraiseInnerServiceSMOImpl;

    /**
     * 校验入参
     *
     * @param appraisePo
     */
    private void validate(AppraisePo appraisePo) {

        Assert.hasKeyAndValue(appraisePo, "appraiseScore", "未包含评分");
        Assert.hasKeyAndValue(appraisePo, "appraiseType", "未包含评价类型");
        Assert.hasKeyAndValue(appraisePo, "context", "未包含评价内容");
        Assert.hasKeyAndValue(appraisePo, "appraiseUserId", "未包含评价者");
        Assert.hasKeyAndValue(appraisePo, "appraiseUserName", "未包含评价者名称");
        Assert.hasKeyAndValue(appraisePo, "objType", "未包含评价对象类型");
        Assert.hasKeyAndValue(appraisePo, "objId", "未包含评价对象ID");

    }


    @Override
    @Java110Transactional
    public ResponseEntity<String> saveAppraise(AppraisePo appraisePo) {
        validate(appraisePo);
        if (StringUtil.isEmpty(appraisePo.getAppraiseId()) || appraisePo.getAppraiseId().startsWith("-")) {
            appraisePo.setAppraiseId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appraiseId));
        }

        if (StringUtil.isEmpty(appraisePo.getParentAppraiseId())) {
            appraisePo.setParentAppraiseId("-1");
        }
        int saveFlag = appraiseInnerServiceSMOImpl.saveAppraise(appraisePo);
        if (saveFlag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }
}
