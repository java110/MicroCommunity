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
import com.java110.dto.reportCustom.ReportCustomComponentDto;
import com.java110.intf.report.IReportCustomComponentV1InnerServiceSMO;
import com.java110.po.reportCustomComponent.ReportCustomComponentPo;
import com.java110.report.dao.IReportCustomComponentV1ServiceDao;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-11-09 13:18:41 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ReportCustomComponentV1InnerServiceSMOImpl extends BaseServiceSMO implements IReportCustomComponentV1InnerServiceSMO {

    @Autowired
    private IReportCustomComponentV1ServiceDao reportCustomComponentV1ServiceDaoImpl;


    @Override
    public int saveReportCustomComponent(@RequestBody ReportCustomComponentPo reportCustomComponentPo) {
        int saveFlag = reportCustomComponentV1ServiceDaoImpl.saveReportCustomComponentInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentPo));
        return saveFlag;
    }

    @Override
    public int updateReportCustomComponent(@RequestBody ReportCustomComponentPo reportCustomComponentPo) {
        int saveFlag = reportCustomComponentV1ServiceDaoImpl.updateReportCustomComponentInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentPo));
        return saveFlag;
    }

    @Override
    public int deleteReportCustomComponent(@RequestBody ReportCustomComponentPo reportCustomComponentPo) {
        reportCustomComponentPo.setStatusCd("1");
        int saveFlag = reportCustomComponentV1ServiceDaoImpl.updateReportCustomComponentInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentPo));
        return saveFlag;
    }

    @Override
    public List<ReportCustomComponentDto> queryReportCustomComponents(@RequestBody ReportCustomComponentDto reportCustomComponentDto) {

        //校验是否传了 分页信息

        int page = reportCustomComponentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportCustomComponentDto.setPage((page - 1) * reportCustomComponentDto.getRow());
        }

        List<ReportCustomComponentDto> reportCustomComponents = BeanConvertUtil.covertBeanList(reportCustomComponentV1ServiceDaoImpl.getReportCustomComponentInfo(BeanConvertUtil.beanCovertMap(reportCustomComponentDto)), ReportCustomComponentDto.class);
        desCode(reportCustomComponents);
        return reportCustomComponents;
    }


    @Override
    public int queryReportCustomComponentsCount(@RequestBody ReportCustomComponentDto reportCustomComponentDto) {
        return reportCustomComponentV1ServiceDaoImpl.queryReportCustomComponentsCount(BeanConvertUtil.beanCovertMap(reportCustomComponentDto));
    }

    private void desCode(List<ReportCustomComponentDto> reportCustomComponentDtos) {
        if (reportCustomComponentDtos == null || reportCustomComponentDtos.size() < 1) {
            return;
        }
        for (ReportCustomComponentDto reportCustomComponentDto : reportCustomComponentDtos) {
            try {
                if (!StringUtil.isEmpty(reportCustomComponentDto.getComponentSql())) {
                    reportCustomComponentDto.setComponentSql(new String(Base64Convert.base64ToByte(reportCustomComponentDto.getComponentSql()), "UTF-8"));
                }
                if (!StringUtil.isEmpty(reportCustomComponentDto.getJavaScript())) {
                    reportCustomComponentDto.setJavaScript(new String(Base64Convert.base64ToByte(reportCustomComponentDto.getJavaScript()), "UTF-8"));
                }
            } catch (Exception e) {
            }
        }
    }

}
