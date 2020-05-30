package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.po.advert.AdvertItemPo;
import com.java110.po.advert.AdvertPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;


import com.java110.core.annotation.Java110Listener;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAdvertListener")
public class SaveAdvertListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

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

        AdvertPo advertPo = BeanConvertUtil.covertBean(reqJson, AdvertPo.class);

        //保存广告信息
        super.insert(context, advertPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT);

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
        return ServiceCodeAdvertConstant.ADD_ADVERT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
