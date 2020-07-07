package com.java110.common.service.appraise;

import com.java110.common.dao.IAppraiseServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.intf.common.appraise.ISaveAppraiseService;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaveAppraiseServiceImpl implements ISaveAppraiseService {

    @Autowired
    IAppraiseServiceDao appraiseServiceDaoImpl;

    /**
     * 校验入参
     *
     * @param appraiseDto
     */
    private void validate(AppraiseDto appraiseDto) {

        Assert.hasKeyAndValue(appraiseDto, "appraiseScore", "未包含评分");
        Assert.hasKeyAndValue(appraiseDto, "appraiseType", "未包含评价类型");
        Assert.hasKeyAndValue(appraiseDto, "context", "未包含评价内容");
        Assert.hasKeyAndValue(appraiseDto, "appraiseUserId", "未包含评价者");
        Assert.hasKeyAndValue(appraiseDto, "appraiseUserName", "未包含评价者名称");
        Assert.hasKeyAndValue(appraiseDto, "objType", "未包含评价对象类型");
        Assert.hasKeyAndValue(appraiseDto, "objId", "未包含评价对象ID");

    }


    @Override
    @Java110Transactional
    public AppraiseDto saveAppraise(@RequestBody AppraiseDto appraiseDto) {
        validate(appraiseDto);
        if (StringUtil.isEmpty(appraiseDto.getAppraiseId())|| appraiseDto.getAppraiseId().startsWith("-")) {
            appraiseDto.setAppraiseId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appraiseId));
        }

        if (StringUtil.isEmpty(appraiseDto.getParentAppraiseId())) {
            appraiseDto.setParentAppraiseId("-1");
        }
        int saveFlag = appraiseServiceDaoImpl.saveAppraise(BeanConvertUtil.beanCovertMap(appraiseDto));
        if (saveFlag > 0) {
            return appraiseDto;
        }
        return null;
    }
}
