package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.intf.user.IUserQuestionAnswerValueInnerServiceSMO;
import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import com.java110.user.dao.IUserQuestionAnswerValueServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 答卷答案内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserQuestionAnswerValueInnerServiceSMOImpl extends BaseServiceSMO implements IUserQuestionAnswerValueInnerServiceSMO {

    @Autowired
    private IUserQuestionAnswerValueServiceDao userQuestionAnswerValueServiceDaoImpl;


    @Override
    public int saveUserQuestionAnswerValue(@RequestBody List<UserQuestionAnswerValuePo> userQuestionAnswerValuePos) {
        int saveFlag = 1;
        List<Map> fees = new ArrayList<>();
        for (UserQuestionAnswerValuePo payFeePo : userQuestionAnswerValuePos) {
            fees.add(BeanConvertUtil.beanCovertMap(payFeePo));
        }

        Map info = new HashMap();
        info.put("userQuestionAnswerValuePos", fees);
        userQuestionAnswerValueServiceDaoImpl.saveUserQuestionAnswerValueInfo(info);
        return saveFlag;
    }

    @Override
    public int updateUserQuestionAnswerValue(@RequestBody UserQuestionAnswerValuePo userQuestionAnswerValuePo) {
        int saveFlag = 1;
        userQuestionAnswerValueServiceDaoImpl.updateUserQuestionAnswerValueInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerValuePo));
        return saveFlag;
    }

    @Override
    public int deleteUserQuestionAnswerValue(@RequestBody UserQuestionAnswerValuePo userQuestionAnswerValuePo) {
        int saveFlag = 1;
        userQuestionAnswerValuePo.setStatusCd("1");
        userQuestionAnswerValueServiceDaoImpl.updateUserQuestionAnswerValueInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerValuePo));
        return saveFlag;
    }

    @Override
    public List<UserQuestionAnswerValueDto> queryUserQuestionAnswerValues(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto) {

        //校验是否传了 分页信息

        int page = userQuestionAnswerValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userQuestionAnswerValueDto.setPage((page - 1) * userQuestionAnswerValueDto.getRow());
        }

        List<UserQuestionAnswerValueDto> userQuestionAnswerValues = BeanConvertUtil.covertBeanList(userQuestionAnswerValueServiceDaoImpl.getUserQuestionAnswerValueInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerValueDto)), UserQuestionAnswerValueDto.class);

        return userQuestionAnswerValues;
    }


    @Override
    public int queryUserQuestionAnswerValuesCount(@RequestBody UserQuestionAnswerValueDto userQuestionAnswerValueDto) {
        return userQuestionAnswerValueServiceDaoImpl.queryUserQuestionAnswerValuesCount(BeanConvertUtil.beanCovertMap(userQuestionAnswerValueDto));
    }

    public IUserQuestionAnswerValueServiceDao getUserQuestionAnswerValueServiceDaoImpl() {
        return userQuestionAnswerValueServiceDaoImpl;
    }

    public void setUserQuestionAnswerValueServiceDaoImpl(IUserQuestionAnswerValueServiceDao userQuestionAnswerValueServiceDaoImpl) {
        this.userQuestionAnswerValueServiceDaoImpl = userQuestionAnswerValueServiceDaoImpl;
    }
}
