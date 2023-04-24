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
package com.java110.community.cmd.propertyRightRegistration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDto;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDetailDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IPropertyRightRegistrationDetailV1InnerServiceSMO;
import com.java110.intf.community.IPropertyRightRegistrationV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.propertyRightRegistration.PropertyRightRegistrationPo;
import com.java110.po.propertyRightRegistrationDetail.PropertyRightRegistrationDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：propertyRightRegistration.savePropertyRightRegistration
 * 请求路劲：/app/propertyRightRegistration.SavePropertyRightRegistration
 * add by 吴学文 at 2021-10-09 10:34:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "propertyRightRegistration.savePropertyRightRegistration")
public class SavePropertyRightRegistrationCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SavePropertyRightRegistrationCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    public static final String CODE_PREFIX_DETAIL_ID = "11";

    @Autowired
    private IPropertyRightRegistrationV1InnerServiceSMO propertyRightRegistrationV1InnerServiceSMOImpl;

    @Autowired
    private IPropertyRightRegistrationDetailV1InnerServiceSMO propertyRightRegistrationDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "link", "请求报文中未包含link");
        Assert.hasKeyAndValue(reqJson, "idCard", "请求报文中未包含idCard");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //获取身份证照片信息
        if (reqJson.containsKey("idCardPhotos") && !StringUtil.isEmpty(reqJson.getString("idCardPhotos"))) {
            String idCardPhotos = reqJson.getString("idCardPhotos");
            JSONArray objects = JSONArray.parseArray(idCardPhotos);
            if (objects.size() != 2 || idCardPhotos.equals("[]")) {
                throw new CmdException("请上传身份证正反两张图片！");
            }
        }
        //获取购房合同图片信息
        if (reqJson.containsKey("housePurchasePhotos") && !StringUtil.isEmpty(reqJson.getString("housePurchasePhotos"))) {
            String housePurchasePhotos = reqJson.getString("housePurchasePhotos");
            if (housePurchasePhotos.length() < 1 || housePurchasePhotos.equals("[]")) {
                throw new CmdException("购房合同图片不能为空！");
            }
        }
        //获取维修基金标识
        if (reqJson.containsKey("isTrue") && reqJson.getString("isTrue").equals("true")) {
            String repairPhotos = reqJson.getString("repairPhotos");
            if (repairPhotos.length() < 1 || repairPhotos.equals("[]")) {
                throw new CmdException("维修基金图片不能为空！");
            }
        }
        //获取契税标识
        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("0")) {
            String deedTaxPhotos = reqJson.getString("deedTaxPhotos");
            if (deedTaxPhotos.length() < 1 || deedTaxPhotos.equals("[]")) {
                throw new CmdException("契税证明图片不能为空！");
            }
        }
        PropertyRightRegistrationDto propertyRightRegistrationDto = new PropertyRightRegistrationDto();
        propertyRightRegistrationDto.setRoomId(reqJson.getString("roomId"));
        //根据房屋查询产权登记表
        List<PropertyRightRegistrationDto> propertyRightRegistrationDtos = propertyRightRegistrationV1InnerServiceSMOImpl.queryPropertyRightRegistrations(propertyRightRegistrationDto);
        if (propertyRightRegistrationDtos != null && propertyRightRegistrationDtos.size() > 0) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该房屋已经申请过房屋产权！");
            cmdDataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        PropertyRightRegistrationPo propertyRightRegistrationPo = BeanConvertUtil.covertBean(reqJson, PropertyRightRegistrationPo.class);
        propertyRightRegistrationPo.setPrrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        propertyRightRegistrationPo.setCreateUser("-1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        propertyRightRegistrationPo.setCreateTime(simpleDateFormat.format(new Date()));
        propertyRightRegistrationPo.setStatusCd("1");
        int flag = propertyRightRegistrationV1InnerServiceSMOImpl.savePropertyRightRegistration(propertyRightRegistrationPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(propertyRightRegistrationPo.getPrrId());
        //table表示表存储 ftp表示ftp文件存储
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //身份证图片上传
        List<String> idCardPhotos = propertyRightRegistrationPo.getIdCardPhotos();
        if (idCardPhotos != null && idCardPhotos.size() > 0) {
            //60000表示身份证图片
            fileRelPo.setRelTypeCd("60000");
            for (String photo : idCardPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.ID_CARD); //身份证
            propertyRightRegistrationDetailPo.setIsTrue("-1"); //未缴费
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        //购房合同照片上传
        List<String> housePurchasePhotos = propertyRightRegistrationPo.getHousePurchasePhotos();
        if (housePurchasePhotos != null && housePurchasePhotos.size() > 0) {
            //25000表示购房合同图片
            fileRelPo.setRelTypeCd("25000");
            for (String photo : housePurchasePhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.HOUSE_PURCHASE); //购房合同
            propertyRightRegistrationDetailPo.setIsTrue("-1"); //未缴费
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        //维修基金图片上传
        List<String> repairPhotos = propertyRightRegistrationPo.getRepairPhotos();
        if (repairPhotos != null && repairPhotos.size() > 0) {
            //26000表示维修基金图片
            fileRelPo.setRelTypeCd("26000");
            for (String photo : repairPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.REPAIR_MONEY); //维修基金
            propertyRightRegistrationDetailPo.setIsTrue("true");
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        //契税证明图片上传
        List<String> deedTaxPhotos = propertyRightRegistrationPo.getDeedTaxPhotos();
        if (deedTaxPhotos != null && deedTaxPhotos.size() > 0) {
            //27000表示契税图片
            fileRelPo.setRelTypeCd("27000");
            for (String photo : deedTaxPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.DEED_TAX); //契税
            propertyRightRegistrationDetailPo.setIsTrue("true");
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        if (reqJson.containsKey("isTrue") && reqJson.getString("isTrue").equals("false")) { //维修基金未缴费，此时向产权详情表新增一条数据，缴费状态为否
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.REPAIR_MONEY); //维修基金
            propertyRightRegistrationDetailPo.setIsTrue("false");
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("1")) { //契税未缴费，此时向产权详情表新增一条数据，缴费状态为否
            PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = new PropertyRightRegistrationDetailPo();
            propertyRightRegistrationDetailPo.setPrrdId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_DETAIL_ID));
            propertyRightRegistrationDetailPo.setPrrId(propertyRightRegistrationPo.getPrrId());
            propertyRightRegistrationDetailPo.setSecurities(PropertyRightRegistrationDetailDto.DEED_TAX); //契税
            propertyRightRegistrationDetailPo.setIsTrue("false");
            propertyRightRegistrationDetailPo.setCreateTime(simpleDateFormat.format(new Date()));
            propertyRightRegistrationDetailPo.setCreateUser("-1");
            propertyRightRegistrationDetailV1InnerServiceSMOImpl.savePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
