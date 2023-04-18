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
package com.java110.community.smo.impl;


import com.java110.community.dao.IVisitSettingV1ServiceDao;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.po.visitSetting.VisitSettingPo;
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
 * add by 吴学文 at 2023-01-18 14:43:29 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class VisitSettingV1InnerServiceSMOImpl extends BaseServiceSMO implements IVisitSettingV1InnerServiceSMO {

    @Autowired
    private IVisitSettingV1ServiceDao visitSettingV1ServiceDaoImpl;
    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Override
    public int saveVisitSetting(@RequestBody  VisitSettingPo visitSettingPo) {
        int saveFlag = visitSettingV1ServiceDaoImpl.saveVisitSettingInfo(BeanConvertUtil.beanCovertMap(visitSettingPo));
        return saveFlag;
    }

     @Override
    public int updateVisitSetting(@RequestBody  VisitSettingPo visitSettingPo) {
        int saveFlag = visitSettingV1ServiceDaoImpl.updateVisitSettingInfo(BeanConvertUtil.beanCovertMap(visitSettingPo));
        return saveFlag;
    }

     @Override
    public int deleteVisitSetting(@RequestBody  VisitSettingPo visitSettingPo) {
       visitSettingPo.setStatusCd("1");
       int saveFlag = visitSettingV1ServiceDaoImpl.updateVisitSettingInfo(BeanConvertUtil.beanCovertMap(visitSettingPo));
       return saveFlag;
    }

    @Override
    public List<VisitSettingDto> queryVisitSettings(@RequestBody  VisitSettingDto visitSettingDto) {

        //校验是否传了 分页信息

        int page = visitSettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            visitSettingDto.setPage((page - 1) * visitSettingDto.getRow());
        }

        List<VisitSettingDto> visitSettings = BeanConvertUtil.covertBeanList(visitSettingV1ServiceDaoImpl.getVisitSettingInfo(BeanConvertUtil.beanCovertMap(visitSettingDto)), VisitSettingDto.class);
        refreshWorkflow(visitSettings);
        return visitSettings;
    }

    /**
     * 查询工作流信息
     *
     * @param visitSettings
     */
    private void refreshWorkflow(List<VisitSettingDto> visitSettings) {
        if(visitSettings == null || visitSettings.size()< 1){
            return ;
        }
        List<String> flowIds = new ArrayList<>();
        for (VisitSettingDto visitSettingDto : visitSettings) {
            flowIds.add(visitSettingDto.getFlowId());
        }

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setFlowIds(flowIds.toArray(new String[flowIds.size()]));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        for (VisitSettingDto visitSettingDto : visitSettings) {
            for (OaWorkflowDto tmpOaWorkflowDto : oaWorkflowDtos) {
                if (visitSettingDto.getFlowId().equals(tmpOaWorkflowDto.getFlowId())) {
                    BeanConvertUtil.covertBean(tmpOaWorkflowDto, visitSettingDto);
                }
            }
        }
    }


    @Override
    public int queryVisitSettingsCount(@RequestBody VisitSettingDto visitSettingDto) {
        return visitSettingV1ServiceDaoImpl.queryVisitSettingsCount(BeanConvertUtil.beanCovertMap(visitSettingDto));    }

}
