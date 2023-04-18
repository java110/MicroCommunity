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
package com.java110.common.smo.impl;


import com.java110.common.dao.IItemReleaseTypeV1ServiceDao;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.intf.common.IItemReleaseTypeV1InnerServiceSMO;
import com.java110.dto.itemRelease.ItemReleaseTypeDto;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.po.itemReleaseType.ItemReleaseTypePo;
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
 * add by 吴学文 at 2023-01-11 15:33:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ItemReleaseTypeV1InnerServiceSMOImpl extends BaseServiceSMO implements IItemReleaseTypeV1InnerServiceSMO {

    @Autowired
    private IItemReleaseTypeV1ServiceDao itemReleaseTypeV1ServiceDaoImpl;
    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Override
    public int saveItemReleaseType(@RequestBody  ItemReleaseTypePo itemReleaseTypePo) {
        int saveFlag = itemReleaseTypeV1ServiceDaoImpl.saveItemReleaseTypeInfo(BeanConvertUtil.beanCovertMap(itemReleaseTypePo));
        return saveFlag;
    }

     @Override
    public int updateItemReleaseType(@RequestBody  ItemReleaseTypePo itemReleaseTypePo) {
        int saveFlag = itemReleaseTypeV1ServiceDaoImpl.updateItemReleaseTypeInfo(BeanConvertUtil.beanCovertMap(itemReleaseTypePo));
        return saveFlag;
    }

     @Override
    public int deleteItemReleaseType(@RequestBody  ItemReleaseTypePo itemReleaseTypePo) {
       itemReleaseTypePo.setStatusCd("1");
       int saveFlag = itemReleaseTypeV1ServiceDaoImpl.updateItemReleaseTypeInfo(BeanConvertUtil.beanCovertMap(itemReleaseTypePo));
       return saveFlag;
    }

    @Override
    public List<ItemReleaseTypeDto> queryItemReleaseTypes(@RequestBody  ItemReleaseTypeDto itemReleaseTypeDto) {

        //校验是否传了 分页信息

        int page = itemReleaseTypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            itemReleaseTypeDto.setPage((page - 1) * itemReleaseTypeDto.getRow());
        }

        List<ItemReleaseTypeDto> itemReleaseTypes = BeanConvertUtil.covertBeanList(itemReleaseTypeV1ServiceDaoImpl.getItemReleaseTypeInfo(BeanConvertUtil.beanCovertMap(itemReleaseTypeDto)), ItemReleaseTypeDto.class);
        refreshWorkflow(itemReleaseTypes);
        return itemReleaseTypes;
    }
    /**
     * 查询工作流信息
     *
     * @param itemReleaseTypeDtos
     */
    private void refreshWorkflow(List<ItemReleaseTypeDto> itemReleaseTypeDtos) {
        if(itemReleaseTypeDtos == null || itemReleaseTypeDtos.size()< 1){
            return ;
        }
        List<String> flowIds = new ArrayList<>();
        for (ItemReleaseTypeDto itemReleaseTypeDto : itemReleaseTypeDtos) {
            flowIds.add(itemReleaseTypeDto.getFlowId());
        }

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setFlowIds(flowIds.toArray(new String[flowIds.size()]));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        for (ItemReleaseTypeDto itemReleaseTypeDto : itemReleaseTypeDtos) {
            for (OaWorkflowDto tmpOaWorkflowDto : oaWorkflowDtos) {
                if (itemReleaseTypeDto.getFlowId().equals(tmpOaWorkflowDto.getFlowId())) {
                    BeanConvertUtil.covertBean(tmpOaWorkflowDto, itemReleaseTypeDto);
                }
            }
        }
    }



    @Override
    public int queryItemReleaseTypesCount(@RequestBody ItemReleaseTypeDto itemReleaseTypeDto) {
        return itemReleaseTypeV1ServiceDaoImpl.queryItemReleaseTypesCount(BeanConvertUtil.beanCovertMap(itemReleaseTypeDto));    }

}
