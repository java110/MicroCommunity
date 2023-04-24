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
package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportCustom.ReportCustomComponentFooterDto;
import com.java110.intf.report.IReportCustomComponentFooterV1InnerServiceSMO;
import com.java110.po.reportCustomComponentFooter.ReportCustomComponentFooterPo;
import com.java110.report.dao.IReportCustomComponentFooterV1ServiceDao;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-11-14 01:32:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ReportCustomComponentFooterV1InnerServiceSMOImpl extends BaseServiceSMO implements IReportCustomComponentFooterV1InnerServiceSMO {

    @Autowired
    private IReportCustomComponentFooterV1ServiceDao reportCustomComponentFooterV1ServiceDaoImpl;


    @Override
    public int saveReportCustomComponentFooter(@RequestBody ReportCustomComponentFooterPo reportCustomComponentFooterPo) {
        int saveFlag = reportCustomComponentFooterV1ServiceDaoImpl.saveReportCustomComponentFooterInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentFooterPo));
        return saveFlag;
    }

    @Override
    public int updateReportCustomComponentFooter(@RequestBody ReportCustomComponentFooterPo reportCustomComponentFooterPo) {
        int saveFlag = reportCustomComponentFooterV1ServiceDaoImpl.updateReportCustomComponentFooterInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentFooterPo));
        return saveFlag;
    }

    @Override
    public int deleteReportCustomComponentFooter(@RequestBody ReportCustomComponentFooterPo reportCustomComponentFooterPo) {
        reportCustomComponentFooterPo.setStatusCd("1");
        int saveFlag = reportCustomComponentFooterV1ServiceDaoImpl.updateReportCustomComponentFooterInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentFooterPo));
        return saveFlag;
    }

    @Override
    public List<ReportCustomComponentFooterDto> queryReportCustomComponentFooters(@RequestBody ReportCustomComponentFooterDto reportCustomComponentFooterDto) {

        //校验是否传了 分页信息

        int page = reportCustomComponentFooterDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportCustomComponentFooterDto.setPage((page - 1) * reportCustomComponentFooterDto.getRow());
        }

        List<ReportCustomComponentFooterDto> reportCustomComponentFooters = BeanConvertUtil.covertBeanList(reportCustomComponentFooterV1ServiceDaoImpl.getReportCustomComponentFooterInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentFooterDto)), ReportCustomComponentFooterDto.class);

        desCode(reportCustomComponentFooters);
        return reportCustomComponentFooters;
    }


    private void desCode(List<ReportCustomComponentFooterDto> reportCustomComponentFooterDtos) {
        if (reportCustomComponentFooterDtos == null || reportCustomComponentFooterDtos.size() < 1) {
            return;
        }
        for (ReportCustomComponentFooterDto reportCustomComponentFooterDto : reportCustomComponentFooterDtos) {
            try {
                if (!StringUtil.isEmpty(reportCustomComponentFooterDto.getComponentSql())) {
                    reportCustomComponentFooterDto.setComponentSql(new String(Base64Convert.base64ToByte(reportCustomComponentFooterDto.getComponentSql()), "UTF-8"));
                }
                if (!StringUtil.isEmpty(reportCustomComponentFooterDto.getJavaScript())) {
                    reportCustomComponentFooterDto.setJavaScript(new String(Base64Convert.base64ToByte(reportCustomComponentFooterDto.getJavaScript()), "UTF-8"));
                }
            } catch (Exception e) {

            }
        }
    }


    @Override
    public int queryReportCustomComponentFootersCount(@RequestBody ReportCustomComponentFooterDto reportCustomComponentFooterDto) {
        return reportCustomComponentFooterV1ServiceDaoImpl.queryReportCustomComponentFootersCount(BeanConvertUtil.beanCovertMap(reportCustomComponentFooterDto));
    }

}
