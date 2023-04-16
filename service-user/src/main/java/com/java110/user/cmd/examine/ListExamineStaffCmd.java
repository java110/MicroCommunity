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
package com.java110.user.cmd.examine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.examineProject.ExamineStaffIntroductionDto;
import com.java110.dto.examineProject.ExamineStaffProjectDto;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IExamineStaffIntroductionV1InnerServiceSMO;
import com.java110.intf.user.IExamineStaffProjectV1InnerServiceSMO;
import com.java110.intf.user.IExamineStaffV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.examineProject.ExamineStaffDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：examineStaff.listExamineStaff
 * 请求路劲：/app/examineStaff.ListExamineStaff
 * add by 吴学文 at 2023-03-07 15:57:29 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "examine.listExamineStaff")
public class ListExamineStaffCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListExamineStaffCmd.class);
    @Autowired
    private IExamineStaffV1InnerServiceSMO examineStaffV1InnerServiceSMOImpl;

    @Autowired
    private IExamineStaffProjectV1InnerServiceSMO examineStaffProjectV1InnerServiceSMOImpl;

    @Autowired
    private IExamineStaffIntroductionV1InnerServiceSMO examineStaffIntroductionV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ExamineStaffDto examineStaffDto = BeanConvertUtil.covertBean(reqJson, ExamineStaffDto.class);

        int count = examineStaffV1InnerServiceSMOImpl.queryExamineStaffsCount(examineStaffDto);

        List<ExamineStaffDto> examineStaffDtos = null;

        if (count > 0) {
            examineStaffDtos = examineStaffV1InnerServiceSMOImpl.queryExamineStaffs(examineStaffDto);

            freshStaffProjects(examineStaffDtos);
            updatePhone(examineStaffDtos);
            //刷入简介
            updateIntroduction(examineStaffDtos);
        } else {
            examineStaffDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, examineStaffDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 刷入简介
     * @param examineStaffDtos
     */
    private void updateIntroduction(List<ExamineStaffDto> examineStaffDtos) {

        if(examineStaffDtos == null || examineStaffDtos.size() != 1){
            return ;
        }

        ExamineStaffIntroductionDto examineStaffIntroductionDto = new ExamineStaffIntroductionDto();
        examineStaffIntroductionDto.setStaffId(examineStaffDtos.get(0).getStaffId());
        examineStaffIntroductionDto.setCommunityId(examineStaffDtos.get(0).getCommunityId());
        List<ExamineStaffIntroductionDto> introductionDtos = examineStaffIntroductionV1InnerServiceSMOImpl.queryExamineStaffIntroductions(examineStaffIntroductionDto);

        if(introductionDtos == null || introductionDtos.size() < 1){
            return ;
        }

        examineStaffDtos.get(0).setIntroduction(introductionDtos.get(0).getIntroduction());

    }

    private boolean updatePhone(List<ExamineStaffDto> examineStaffDtos) {
        if (examineStaffDtos.size() > 100) {
            return true;
        }

        List<String> esIds = new ArrayList<>();

        for (ExamineStaffDto tmpExamineStaffDto : examineStaffDtos) {
            esIds.add(tmpExamineStaffDto.getEsId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        //fileRelDto.setObjId(owners.get(0).getMemberId());
        fileRelDto.setObjIds(esIds.toArray(new String[esIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return true;
        }

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");

        for (ExamineStaffDto tmpExamineStaffDto : examineStaffDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!tmpExamineStaffDto.getEsId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }

                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    tmpExamineStaffDto.setHeaderImg(tmpFileRelDto.getFileSaveName());
                } else {
                    tmpExamineStaffDto.setHeaderImg(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }

        return false;
    }

    /**
     *
     * @param examineStaffDtos
     */
    private void freshStaffProjects(List<ExamineStaffDto> examineStaffDtos) {

        if(examineStaffDtos == null || examineStaffDtos.size() < 1){
            return ;
        }

        List<String> esIds = new ArrayList<>();
        for(ExamineStaffDto tmpExamineStaffDto : examineStaffDtos){
            esIds.add(tmpExamineStaffDto.getEsId());
        }


        ExamineStaffProjectDto examineStaffProjectDto = new ExamineStaffProjectDto();

        examineStaffProjectDto.setEsIds(esIds.toArray(new String[esIds.size()]));

       List<ExamineStaffProjectDto> examineStaffProjectDtos =  examineStaffProjectV1InnerServiceSMOImpl.queryExamineStaffProjects(examineStaffProjectDto);

       List<ExamineStaffProjectDto> staffProjectDtos = null;
        for(ExamineStaffDto tmpExamineStaffDto : examineStaffDtos){
            staffProjectDtos = new ArrayList<>();
            for(ExamineStaffProjectDto tmpExamineStaffProjectDto: examineStaffProjectDtos){
                if(tmpExamineStaffProjectDto.getEsId().equals(tmpExamineStaffDto.getEsId())){
                    staffProjectDtos.add(tmpExamineStaffProjectDto);
                }
            }

            tmpExamineStaffDto.setProjects(staffProjectDtos);
        }
    }
}
