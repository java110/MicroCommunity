package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.advert.IAdvertInnerServiceSMO;
import com.java110.core.smo.advert.IAdvertItemInnerServiceSMO;
import com.java110.dto.advert.AdvertDto;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAdvertConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 查询广告信息
 */
@Java110Listener("listAdvertPhotoListener")
public class ListAdvertPhotoListener extends AbstractServiceApiListener {

    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;



    @Override
    public String getServiceCode() {
        return ServiceCodeAdvertConstant.LIST_ADVERT_PHOTO;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
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

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;


        JSONArray advertPhoto = new JSONArray();


        //如果是大门 则只获取小区的广告

        getCommunityAdvert(reqJson.getString("communityId"), advertPhoto);

        responseEntity = new ResponseEntity<String>(advertPhoto.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private boolean getCommunityAdvert(String communityId, JSONArray advertPhotoAndVideos) {
        AdvertDto advertDto = new AdvertDto();
        advertDto.setCommunityId(communityId);
        advertDto.setLocationObjId(communityId);
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhotoAndVideos);
            return true;
        }

        return false;
    }

    /**
     * 查询广告照片
     *
     * @param advertDtos
     * @param advertPhotoAndVideos
     */
    private void getAdvertItem(List<AdvertDto> advertDtos, JSONArray advertPhotoAndVideos) {
        JSONObject photoAndVideo = null;

        for (AdvertDto advertDto : advertDtos) {

            AdvertItemDto advertItemDto = new AdvertItemDto();
            advertItemDto.setAdvertId(advertDto.getAdvertId());
            advertItemDto.setCommunityId(advertDto.getCommunityId());
            List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);

            for (AdvertItemDto tmpAdvertItemDto : advertItemDtos) {
                //照片
                if ("8888".equals(tmpAdvertItemDto.getItemTypeCd())) {
                    photoAndVideo = new JSONObject();
                    photoAndVideo.put("suffix", "JPEG");
                    photoAndVideo.put("url", "/callComponent/download/getFile/file?fileId=" + tmpAdvertItemDto.getUrl() + "&communityId=" + advertDto.getCommunityId());
                    photoAndVideo.put("seq", tmpAdvertItemDto.getSeq());
                    advertPhotoAndVideos.add(photoAndVideo);
                }
            }
        }

    }

    public IAdvertItemInnerServiceSMO getAdvertItemInnerServiceSMOImpl() {
        return advertItemInnerServiceSMOImpl;
    }

    public void setAdvertItemInnerServiceSMOImpl(IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl) {
        this.advertItemInnerServiceSMOImpl = advertItemInnerServiceSMOImpl;
    }

}
