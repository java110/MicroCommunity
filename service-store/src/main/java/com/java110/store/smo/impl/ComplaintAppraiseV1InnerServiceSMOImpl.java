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
package com.java110.store.smo.impl;


import com.java110.store.dao.IComplaintAppraiseV1ServiceDao;
import com.java110.intf.store.IComplaintAppraiseV1InnerServiceSMO;
import com.java110.dto.complaint.ComplaintAppraiseDto;
import com.java110.po.complaint.ComplaintAppraisePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2024-02-21 13:04:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ComplaintAppraiseV1InnerServiceSMOImpl extends BaseServiceSMO implements IComplaintAppraiseV1InnerServiceSMO {

    @Autowired
    private IComplaintAppraiseV1ServiceDao complaintAppraiseV1ServiceDaoImpl;


    @Override
    public int saveComplaintAppraise(@RequestBody  ComplaintAppraisePo complaintAppraisePo) {
        int saveFlag = complaintAppraiseV1ServiceDaoImpl.saveComplaintAppraiseInfo(BeanConvertUtil.beanCovertMap(complaintAppraisePo));
        return saveFlag;
    }

     @Override
    public int updateComplaintAppraise(@RequestBody  ComplaintAppraisePo complaintAppraisePo) {
        int saveFlag = complaintAppraiseV1ServiceDaoImpl.updateComplaintAppraiseInfo(BeanConvertUtil.beanCovertMap(complaintAppraisePo));
        return saveFlag;
    }

     @Override
    public int deleteComplaintAppraise(@RequestBody  ComplaintAppraisePo complaintAppraisePo) {
       complaintAppraisePo.setStatusCd("1");
       int saveFlag = complaintAppraiseV1ServiceDaoImpl.updateComplaintAppraiseInfo(BeanConvertUtil.beanCovertMap(complaintAppraisePo));
       return saveFlag;
    }

    @Override
    public List<ComplaintAppraiseDto> queryComplaintAppraises(@RequestBody  ComplaintAppraiseDto complaintAppraiseDto) {

        //校验是否传了 分页信息

        int page = complaintAppraiseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            complaintAppraiseDto.setPage((page - 1) * complaintAppraiseDto.getRow());
        }

        List<ComplaintAppraiseDto> complaintAppraises = BeanConvertUtil.covertBeanList(complaintAppraiseV1ServiceDaoImpl.getComplaintAppraiseInfo(BeanConvertUtil.beanCovertMap(complaintAppraiseDto)), ComplaintAppraiseDto.class);

        return complaintAppraises;
    }


    @Override
    public int queryComplaintAppraisesCount(@RequestBody ComplaintAppraiseDto complaintAppraiseDto) {
        return complaintAppraiseV1ServiceDaoImpl.queryComplaintAppraisesCount(BeanConvertUtil.beanCovertMap(complaintAppraiseDto));    }

}
