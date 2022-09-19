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
package com.java110.common.cmd.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.*;
import com.java110.po.advert.AdvertItemPo;
import com.java110.po.advert.AdvertPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：advert.updateAdvert
 * 请求路劲：/app/advert.UpdateAdvert
 * add by 吴学文 at 2022-06-19 10:07:43 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "advert.updateAdvert")
public class UpdateAdvertCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateAdvertCmd.class);

    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Autowired
    private IAdvertV1InnerServiceSMO advertV1InnerServiceSMOImpl;

    @Autowired
    private IAdvertItemV1InnerServiceSMO advertItemV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "advertId", "广告ID不能为空");
        Assert.hasKeyAndValue(reqJson, "adName", "必填，请填写广告名称");
        Assert.hasKeyAndValue(reqJson, "adTypeCd", "必填，请选择广告类型");
        Assert.hasKeyAndValue(reqJson, "classify", "必填，请选择广告分类");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，请选择投放位置");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，请填写具体位置");
        //Assert.hasKeyAndValue(reqJson, "state", "必填，请填写广告状态");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写播放顺序");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择投放时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
//        reqJson.put("communityId", "9999");
        if (!hasKeyAndValue(reqJson, "photos") && !hasKeyAndValue(reqJson, "vedioName")) {
            throw new IllegalArgumentException("请求报文中没有包含视频或图片");
        }
    }

    private boolean hasKeyAndValue(JSONObject paramIn, String key) {
        if (!paramIn.containsKey(key)) {
            return false;
        }
        if (StringUtil.isEmpty(paramIn.getString(key))) {
            return false;
        }
        return true;
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        AdvertDto advertDto = new AdvertDto();
        advertDto.setAdvertId(reqJson.getString("advertId"));
        advertDto.setCommunityId(reqJson.getString("communityId"));
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);
        Assert.listOnlyOne(advertDtos, "不存在该条广告 或存在多条数据");

        AdvertPo advert = BeanConvertUtil.covertBean(reqJson, AdvertPo.class);
        advert.setState(advertDtos.get(0).getState());
        int flag = advertV1InnerServiceSMOImpl.updateAdvert(advert);
        if(flag < 1){
            throw new CmdException("保存失败");
        }
        AdvertItemDto advertItemDto = new AdvertItemDto();
        advertItemDto.setAdvertId(reqJson.getString("advertId"));
        advertItemDto.setItemTypeCds(new String[]{"8888", "9999"});
        List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);
        //删除照片或视频
        for (AdvertItemDto tmpAdvertItemDto : advertItemDtos) {
            AdvertItemPo advertItemPo = BeanConvertUtil.covertBean(tmpAdvertItemDto, AdvertItemPo.class);
             flag = advertItemV1InnerServiceSMOImpl.deleteAdvertItem(advertItemPo);
            if(flag < 1){
                throw new CmdException("保存失败");
            }
        }

        //删除文件和广告的关系
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("advertId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        for (FileRelDto tmpFileRelDto : fileRelDtos) {
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(tmpFileRelDto, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
            if(flag < 1){
                throw new CmdException("保存失败");
            }
        }


        if (hasKeyAndValue(reqJson, "photos") && reqJson.getJSONArray("photos").size() > 0) {
            JSONArray photos = reqJson.getJSONArray("photos");
            for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
                addAdvertItemPhoto(reqJson, context, photos.getString(_photoIndex));
                addAdvertFileRel(reqJson, context, "40000");
            }
        } else {
            addAdvertItemVedio(reqJson, context);
            addAdvertFileRel(reqJson, context, "50000");
        }
    }

    public void addAdvertItemPhoto(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String photo) {
        String itemTypeCd = "";
        String url = "";
        if(photo.length()> 512) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(photo);
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(paramInJson.getString("communityId"));
            photo = fileInnerServiceSMOImpl.saveFile(fileDto);
        }
        paramInJson.put("fileSaveName", photo);
        paramInJson.put("advertPhotoId", photo);
        itemTypeCd = "8888";
        AdvertItemPo advertItemPo = new AdvertItemPo();
        advertItemPo.setAdvertId(paramInJson.getString("advertId"));
        advertItemPo.setAdvertItemId("-1");
        advertItemPo.setCommunityId(paramInJson.getString("communityId"));
        advertItemPo.setItemTypeCd(itemTypeCd);
        advertItemPo.setUrl(photo);
        advertItemPo.setSeq("1");
        int flag = advertItemV1InnerServiceSMOImpl.saveAdvertItem(advertItemPo);
        if(flag < 1){
            throw new CmdException("保存广告失败");
        }
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertFileRel(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String relTypeCd) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setRelTypeCd(relTypeCd);
        fileRelPo.setSaveWay("40000".equals(relTypeCd) ? "table" : "ftp");
        fileRelPo.setFileRelId("-1");
        fileRelPo.setObjId(paramInJson.getString("advertId"));
        fileRelPo.setFileRealName(paramInJson.getString("advertPhotoId"));
        fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if(flag < 1){
            throw new CmdException("保存广告失败");
        }
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertItemVedio(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        fileRelPo.setObjId(paramInJson.getString("advertId"));
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //50000 广告视频
        fileRelPo.setRelTypeCd("50000");
        fileRelPo.setFileRealName(paramInJson.getString("vedioName"));
        fileRelPo.setFileSaveName(paramInJson.getString("vedioName"));
        fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        /*String itemTypeCd = "";
        String url = "";
        itemTypeCd = "9999";
        url = paramInJson.getString("vedioName");
        paramInJson.put("advertPhotoId", url);
        AdvertItemPo advertItemPo = new AdvertItemPo();
        advertItemPo.setAdvertId(paramInJson.getString("advertId"));
        advertItemPo.setAdvertItemId("-1");
        advertItemPo.setCommunityId(paramInJson.getString("communityId"));
        advertItemPo.setItemTypeCd(itemTypeCd);
        advertItemPo.setUrl(url);
        advertItemPo.setSeq("1");
        super.insert(dataFlowContext, advertItemPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);*/
    }
}
