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
package com.java110.community.cmd.propertyRightRegistrationDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IPropertyRightRegistrationDetailV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDetailDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：propertyRightRegistrationDetail.listPropertyRightRegistrationDetail
 * 请求路劲：/app/propertyRightRegistrationDetail.ListPropertyRightRegistrationDetail
 * add by 吴学文 at 2021-10-11 09:15:52 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "propertyRightRegistrationDetail.listPropertyRightRegistrationDetail")
public class ListPropertyRightRegistrationDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPropertyRightRegistrationDetailCmd.class);

    @Autowired
    private IPropertyRightRegistrationDetailV1InnerServiceSMO propertyRightRegistrationDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PropertyRightRegistrationDetailDto propertyRightRegistrationDetailDto = BeanConvertUtil.covertBean(reqJson, PropertyRightRegistrationDetailDto.class);

        int count = propertyRightRegistrationDetailV1InnerServiceSMOImpl.queryPropertyRightRegistrationDetailsCount(propertyRightRegistrationDetailDto);

        List<PropertyRightRegistrationDetailDto> propertyRightRegistrationDetailDtos = new ArrayList<>();

        if (count > 0) {
            List<PropertyRightRegistrationDetailDto> propertyRightRegistrationDetails = propertyRightRegistrationDetailV1InnerServiceSMOImpl.queryPropertyRightRegistrationDetails(propertyRightRegistrationDetailDto);
            for (PropertyRightRegistrationDetailDto propertyRightRegistrationDetail : propertyRightRegistrationDetails) {
                //获取材料类型
                String securities = propertyRightRegistrationDetail.getSecurities();
                String securitiesName = "";
                String idCardUrl = "";
                String housePurchaseUrl = "";
                String repairUrl = "";
                String deedTaxUrl = "";
                if (!StringUtil.isEmpty(securities) && securities.equals("001")) { //身份证
                    securitiesName = "身份证";
                    //查询房屋产权身份证图片
                    FileRelDto fileRelDto = new FileRelDto();
                    fileRelDto.setObjId(propertyRightRegistrationDetail.getPrrId());
                    fileRelDto.setRelTypeCd("60000"); //身份证图片
                    //查询文件表
                    List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                    if (fileRelDtos != null && fileRelDtos.size() > 0) {
                        for (FileRelDto fileRel : fileRelDtos) {
                            if (!StringUtil.isEmpty(fileRel.getFileRealName())) {  //身份证图片
                                if (!StringUtil.isEmpty(propertyRightRegistrationDetail.getIdCardUrl())) {
                                    idCardUrl = propertyRightRegistrationDetail.getIdCardUrl() + "," + fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setIdCardUrl(idCardUrl);
                                } else {
                                    idCardUrl = fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setIdCardUrl(idCardUrl);
                                }
                            }
                        }
                    }
                } else if (!StringUtil.isEmpty(securities) && securities.equals("002")) {
                    securitiesName = "购房合同";
                    //查询房屋产权购房合同图片
                    FileRelDto fileRelDto = new FileRelDto();
                    fileRelDto.setObjId(propertyRightRegistrationDetail.getPrrId());
                    fileRelDto.setRelTypeCd("25000"); //购房合同图片
                    //查询文件表
                    List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                    if (fileRelDtos != null && fileRelDtos.size() > 0) {
                        for (FileRelDto fileRel : fileRelDtos) {
                            if (!StringUtil.isEmpty(fileRel.getFileRealName())) { //购房合同图片
                                if (!StringUtil.isEmpty(propertyRightRegistrationDetail.getHousePurchaseUrl())) {
                                    housePurchaseUrl = propertyRightRegistrationDetail.getHousePurchaseUrl() + "," + fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setHousePurchaseUrl(housePurchaseUrl);
                                } else {
                                    housePurchaseUrl = fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setHousePurchaseUrl(housePurchaseUrl);
                                }
                            }
                        }
                    }
                } else if (!StringUtil.isEmpty(securities) && securities.equals("003")) {
                    securitiesName = "维修基金";
                    //查询房屋产权维修基金图片
                    FileRelDto fileRelDto = new FileRelDto();
                    fileRelDto.setObjId(propertyRightRegistrationDetail.getPrrId());
                    fileRelDto.setRelTypeCd("26000"); //维修基金图片
                    //查询文件表
                    List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                    if (fileRelDtos != null && fileRelDtos.size() > 0) {
                        for (FileRelDto fileRel : fileRelDtos) {
                            if (!StringUtil.isEmpty(fileRel.getFileRealName())) { //维修基金图片
                                if (!StringUtil.isEmpty(propertyRightRegistrationDetail.getRepairUrl())) {
                                    repairUrl = propertyRightRegistrationDetail.getRepairUrl() + "," + fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setRepairUrl(repairUrl);
                                } else {
                                    repairUrl = fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setRepairUrl(repairUrl);
                                }
                            }
                        }
                    }
                } else if (!StringUtil.isEmpty(securities) && securities.equals("004")) {
                    securitiesName = "契税";
                    //查询房屋产权契税图片
                    FileRelDto fileRelDto = new FileRelDto();
                    fileRelDto.setObjId(propertyRightRegistrationDetail.getPrrId());
                    fileRelDto.setRelTypeCd("27000"); //契税图片
                    //查询文件表
                    List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                    if (fileRelDtos != null && fileRelDtos.size() > 0) {
                        for (FileRelDto fileRel : fileRelDtos) {
                            if (!StringUtil.isEmpty(fileRel.getFileRealName())) { //契税图片
                                if (!StringUtil.isEmpty(propertyRightRegistrationDetail.getDeedTaxUrl())) {
                                    deedTaxUrl = propertyRightRegistrationDetail.getDeedTaxUrl() + "," + fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setDeedTaxUrl(deedTaxUrl);
                                } else {
                                    deedTaxUrl = fileRel.getFileRealName();
                                    propertyRightRegistrationDetail.setDeedTaxUrl(deedTaxUrl);
                                }
                            }
                        }
                    }
                }
                propertyRightRegistrationDetail.setSecuritiesName(securitiesName);
                propertyRightRegistrationDetail.setIdCardUrl(idCardUrl);
                propertyRightRegistrationDetail.setHousePurchaseUrl(housePurchaseUrl);
                propertyRightRegistrationDetail.setRepairUrl(repairUrl);
                propertyRightRegistrationDetail.setDeedTaxUrl(deedTaxUrl);
                propertyRightRegistrationDetailDtos.add(propertyRightRegistrationDetail);
            }
        } else {
            propertyRightRegistrationDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, propertyRightRegistrationDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
