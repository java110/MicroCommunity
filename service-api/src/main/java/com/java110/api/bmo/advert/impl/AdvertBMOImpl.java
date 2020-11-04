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
package com.java110.api.bmo.advert.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.advert.IAdvertBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAdvertInnerServiceSMO;
import com.java110.intf.common.IAdvertItemInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("advertBMOImpl")
public class AdvertBMOImpl extends ApiBaseBMO implements IAdvertBMO {

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;
    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteAdvert(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ADVERT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvert = new JSONObject();
        businessAdvert.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvert", businessAdvert);
        return business;
    }


    public JSONObject addAdvertItemPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext, String photo) {

        String itemTypeCd = "";
        String url = "";

        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(photo);
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(paramInJson.getString("communityId"));
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        paramInJson.put("fileSaveName", fileName);
        paramInJson.put("advertPhotoId", fileDto.getFileId());
        itemTypeCd = "8888";
        url = fileDto.getFileId();


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvertItem = new JSONObject();
        businessAdvertItem.put("advertId", paramInJson.getString("advertId"));
        businessAdvertItem.put("itemTypeCd", itemTypeCd);
        businessAdvertItem.put("url", url);
        businessAdvertItem.put("seq", 1);
        businessAdvertItem.put("advertItemId", "-1");
        businessAdvertItem.put("communityId", paramInJson.getString("communityId"));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvertItem", businessAdvertItem);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addAdvertItemVedio(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        String itemTypeCd = "";
        String url = "";

        itemTypeCd = "9999";
        url = paramInJson.getString("vedioName");
        paramInJson.put("advertPhotoId", url);

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvertItem = new JSONObject();
        businessAdvertItem.put("advertId", paramInJson.getString("advertId"));
        businessAdvertItem.put("itemTypeCd", itemTypeCd);
        businessAdvertItem.put("url", url);
        businessAdvertItem.put("seq", 1);
        businessAdvertItem.put("advertItemId", "-1");
        businessAdvertItem.put("communityId", paramInJson.getString("communityId"));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvertItem", businessAdvertItem);
        return business;
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addAdvertFileRel(JSONObject paramInJson, DataFlowContext dataFlowContext, String relTypeCd) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", relTypeCd);
        businessUnit.put("saveWay", "40000".equals(relTypeCd) ? "table" : "ftp");
        businessUnit.put("objId", paramInJson.getString("advertId"));
        businessUnit.put("fileRealName", paramInJson.getString("advertPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addAdvert(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        String advertId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_advertId);
        paramInJson.put("advertId", advertId);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvert = new JSONObject();
        businessAdvert.putAll(paramInJson);
        businessAdvert.put("advertId", advertId);
        businessAdvert.put("state", "1000");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvert", businessAdvert);
        return business;
    }

    /**
     * 删除所有的照片或视频信息
     *
     * @param advertItemDto
     * @param context
     * @return
     */
    public JSONObject delAdvertItemPhotoOrVideo(AdvertItemDto advertItemDto, DataFlowContext context) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ADVERT_ITEM);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvertItem = new JSONObject();
        businessAdvertItem.put("advertItemId", advertItemDto.getAdvertItemId());
        businessAdvertItem.put("communityId", advertItemDto.getCommunityId());
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvertItem", businessAdvertItem);
        return business;
    }


    /**
     * 删除广告文件关系
     *
     * @param fileRelDto      接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject delAdvertFileRel(FileRelDto fileRelDto, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", fileRelDto.getFileRelId());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);
        return business;
    }

    /**
     * 添加发布广告信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateAdvert(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AdvertDto advertDto = new AdvertDto();
        advertDto.setAdvertId(paramInJson.getString("advertId"));
        advertDto.setCommunityId(paramInJson.getString("communityId"));
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        Assert.listOnlyOne(advertDtos, "不存在该条广告 或存在多条数据");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ADVERT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessAdvert = new JSONObject();
        businessAdvert.putAll(paramInJson);
        businessAdvert.put("state", advertDtos.get(0).getState());
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessAdvert", businessAdvert);
        return business;
    }
}
