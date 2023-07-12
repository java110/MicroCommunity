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


import com.java110.user.dao.IQuestionAnswerTitleRelV1ServiceDao;
import com.java110.intf.user.IQuestionAnswerTitleRelV1InnerServiceSMO;
import com.java110.dto.questionAnswerTitleRel.QuestionAnswerTitleRelDto;
import com.java110.po.questionAnswerTitleRel.QuestionAnswerTitleRelPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-07-13 00:07:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class QuestionAnswerTitleRelV1InnerServiceSMOImpl extends BaseServiceSMO implements IQuestionAnswerTitleRelV1InnerServiceSMO {

    @Autowired
    private IQuestionAnswerTitleRelV1ServiceDao questionAnswerTitleRelV1ServiceDaoImpl;


    @Override
    public int saveQuestionAnswerTitleRel(@RequestBody  QuestionAnswerTitleRelPo questionAnswerTitleRelPo) {
        int saveFlag = questionAnswerTitleRelV1ServiceDaoImpl.saveQuestionAnswerTitleRelInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleRelPo));
        return saveFlag;
    }

     @Override
    public int updateQuestionAnswerTitleRel(@RequestBody  QuestionAnswerTitleRelPo questionAnswerTitleRelPo) {
        int saveFlag = questionAnswerTitleRelV1ServiceDaoImpl.updateQuestionAnswerTitleRelInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleRelPo));
        return saveFlag;
    }

     @Override
    public int deleteQuestionAnswerTitleRel(@RequestBody  QuestionAnswerTitleRelPo questionAnswerTitleRelPo) {
       questionAnswerTitleRelPo.setStatusCd("1");
       int saveFlag = questionAnswerTitleRelV1ServiceDaoImpl.updateQuestionAnswerTitleRelInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleRelPo));
       return saveFlag;
    }

    @Override
    public List<QuestionAnswerTitleRelDto> queryQuestionAnswerTitleRels(@RequestBody  QuestionAnswerTitleRelDto questionAnswerTitleRelDto) {

        //校验是否传了 分页信息

        int page = questionAnswerTitleRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            questionAnswerTitleRelDto.setPage((page - 1) * questionAnswerTitleRelDto.getRow());
        }

        List<QuestionAnswerTitleRelDto> questionAnswerTitleRels = BeanConvertUtil.covertBeanList(questionAnswerTitleRelV1ServiceDaoImpl.getQuestionAnswerTitleRelInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleRelDto)), QuestionAnswerTitleRelDto.class);

        return questionAnswerTitleRels;
    }


    @Override
    public int queryQuestionAnswerTitleRelsCount(@RequestBody QuestionAnswerTitleRelDto questionAnswerTitleRelDto) {
        return questionAnswerTitleRelV1ServiceDaoImpl.queryQuestionAnswerTitleRelsCount(BeanConvertUtil.beanCovertMap(questionAnswerTitleRelDto));    }

}
