package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.advert.IAdvertBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.advert.IAdvertInnerServiceSMO;
import com.java110.core.smo.advert.IAdvertItemInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.po.advert.AdvertItemPo;
import com.java110.po.advert.AdvertPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeAdvertConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存发布广告侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateAdvertListener")
public class UpdateAdvertListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAdvertBMO advertBMOImpl;
    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

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
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        AdvertDto advertDto = new AdvertDto();
        advertDto.setAdvertId(reqJson.getString("advertId"));
        advertDto.setCommunityId(reqJson.getString("communityId"));
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        Assert.listOnlyOne(advertDtos, "不存在该条广告 或存在多条数据");

        AdvertPo advertPo = BeanConvertUtil.covertBean(reqJson, AdvertPo.class);
        advertPo.setState(advertDtos.get(0).getState());
        super.update(context, advertPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ADVERT);

        AdvertItemDto advertItemDto = new AdvertItemDto();
        advertItemDto.setAdvertId(reqJson.getString("advertId"));
        advertItemDto.setItemTypeCds(new String[]{"8888", "9999"});
        List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);

        //删除照片或视频
        for (AdvertItemDto tmpAdvertItemDto : advertItemDtos) {
            AdvertItemPo advertItemPo = BeanConvertUtil.covertBean(tmpAdvertItemDto, AdvertItemPo.class);
            super.delete(context, advertItemPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ADVERT_ITEM);
        }

        //删除文件和 广告的关系
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("advertId"));
        fileRelDto.setRelTypeCds(new String[]{"40000", "50000"});
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        for (FileRelDto tmpFileRelDto : fileRelDtos) {
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(tmpFileRelDto, FileRelPo.class);
            super.delete(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FILE_REL);
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

    public void addAdvertItemPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext, String photo) {

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

        AdvertItemPo advertItemPo = new AdvertItemPo();
        advertItemPo.setAdvertId(paramInJson.getString("advertId"));
        advertItemPo.setAdvertItemId("-1");
        advertItemPo.setCommunityId(paramInJson.getString("communityId"));
        advertItemPo.setItemTypeCd(itemTypeCd);
        advertItemPo.setUrl(url);
        advertItemPo.setSeq("1");
        super.insert(dataFlowContext, advertItemPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertFileRel(JSONObject paramInJson, DataFlowContext dataFlowContext, String relTypeCd) {

        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setRelTypeCd(relTypeCd);
        fileRelPo.setSaveWay("40000".equals(relTypeCd) ? "table" : "ftp");
        fileRelPo.setFileRelId("-1");
        fileRelPo.setObjId(paramInJson.getString("advertId"));
        fileRelPo.setFileSaveName(paramInJson.getString("advertPhotoId"));
        fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertItemVedio(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        String itemTypeCd = "";
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
        super.insert(dataFlowContext, advertItemPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeAdvertConstant.UPDATE_ADVERT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAdvertInnerServiceSMO getAdvertInnerServiceSMOImpl() {
        return advertInnerServiceSMOImpl;
    }

    public void setAdvertInnerServiceSMOImpl(IAdvertInnerServiceSMO advertInnerServiceSMOImpl) {
        this.advertInnerServiceSMOImpl = advertInnerServiceSMOImpl;
    }

    public IAdvertItemInnerServiceSMO getAdvertItemInnerServiceSMOImpl() {
        return advertItemInnerServiceSMOImpl;
    }

    public void setAdvertItemInnerServiceSMOImpl(IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl) {
        this.advertItemInnerServiceSMOImpl = advertItemInnerServiceSMOImpl;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
