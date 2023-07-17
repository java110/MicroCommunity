/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.smo.impl;


import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.user.dao.IUserQuestionAnswerV1ServiceDao;
import com.java110.intf.user.IUserQuestionAnswerV1InnerServiceSMO;
import com.java110.dto.user.UserQuestionAnswerDto;
import com.java110.po.user.UserQuestionAnswerPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-07-13 00:38:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class UserQuestionAnswerV1InnerServiceSMOImpl extends BaseServiceSMO implements IUserQuestionAnswerV1InnerServiceSMO {

    @Autowired
    private IUserQuestionAnswerV1ServiceDao userQuestionAnswerV1ServiceDaoImpl;


    @Override
    public int saveUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        int saveFlag = userQuestionAnswerV1ServiceDaoImpl.saveUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public int saveUserQuestionAnswers(@RequestBody List<UserQuestionAnswerPo> userQuestionAnswerPos) {
        Map info = new HashMap();
        info.put("userQuestionAnswerPos", userQuestionAnswerPos);
        int saveFlag = userQuestionAnswerV1ServiceDaoImpl.saveUserQuestionAnswers(info);
        return saveFlag;
    }


    @Override
    public int updateUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        int saveFlag = userQuestionAnswerV1ServiceDaoImpl.updateUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public int deleteUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        userQuestionAnswerPo.setStatusCd("1");
        int saveFlag = userQuestionAnswerV1ServiceDaoImpl.updateUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public List<UserQuestionAnswerDto> queryUserQuestionAnswers(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto) {

        //校验是否传了 分页信息

        int page = userQuestionAnswerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userQuestionAnswerDto.setPage((page - 1) * userQuestionAnswerDto.getRow());
        }

        List<UserQuestionAnswerDto> userQuestionAnswers = BeanConvertUtil.covertBeanList(userQuestionAnswerV1ServiceDaoImpl.getUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerDto)), UserQuestionAnswerDto.class);

        return userQuestionAnswers;
    }


    @Override
    public int queryUserQuestionAnswersCount(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto) {
        return userQuestionAnswerV1ServiceDaoImpl.queryUserQuestionAnswersCount(BeanConvertUtil.beanCovertMap(userQuestionAnswerDto));
    }

    @Override
    public List<QuestionAnswerDto> queryOwnerQuestionAnswers(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto) {
        List<QuestionAnswerDto> questionAnswerDtos = BeanConvertUtil.covertBeanList(
                userQuestionAnswerV1ServiceDaoImpl.queryOwnerQuestionAnswers(BeanConvertUtil.beanCovertMap(userQuestionAnswerDto)),
                QuestionAnswerDto.class);
        return questionAnswerDtos;
    }

}
