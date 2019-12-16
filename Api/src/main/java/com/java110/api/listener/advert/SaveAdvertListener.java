package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;


import com.java110.core.annotation.Java110Listener;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAdvertListener")
public class SaveAdvertListener extends AbstractServiceApiListener {

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

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(addAdvert(reqJson, context));
        if (hasKeyAndValue(reqJson, "photos") && reqJson.getJSONArray("photos").size() > 0) {
            JSONArray photos = reqJson.getJSONArray("photos");
            for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
                businesses.add(addAdvertItemPhoto(reqJson, context, photos.getString(_photoIndex)));
                businesses.add(addAdvertFileRel(reqJson, context, "40000"));
            }

        } else {
            businesses.add(addAdvertItemVedio(reqJson, context));
            businesses.add(addAdvertFileRel(reqJson, context, "50000"));
        }

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
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


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addAdvert(JSONObject paramInJson, DataFlowContext dataFlowContext) {

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

    private JSONObject addAdvertItemPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext, String photo) {

        String itemTypeCd = "";
        String url = "";

        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(photo);
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(paramInJson.getString("communityId"));
        if (fileInnerServiceSMOImpl.saveFile(fileDto) < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件出错");
        }
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
    private JSONObject addAdvertItemVedio(JSONObject paramInJson, DataFlowContext dataFlowContext) {

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
    private JSONObject addAdvertFileRel(JSONObject paramInJson, DataFlowContext dataFlowContext, String relTypeCd) {


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
        businessUnit.put("fileSaveName", paramInJson.getString("advertPhotoId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
