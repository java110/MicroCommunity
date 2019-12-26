package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.file.FileDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName ApplicationKeyListener
 * @Description 钥匙申请类
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("applicationKeyListener")
public class ApplicationKeyListener extends AbstractServiceApiDataFlowListener {


    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(ApplicationKeyListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_APPLICATION_KEY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        //添加小区楼
        businesses.add(addMember(paramObj));


        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(paramObj.getString("ownerPhoto"));
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(paramObj.getString("communityId"));
        if (fileInnerServiceSMOImpl.saveFile(fileDto) < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件出错");
        }
        paramObj.put("ownerPhotoId", fileDto.getFileId());

        businesses.add(addOwnerPhoto(paramObj, dataFlowContext));


        /*if ("ON".equals(MappingCache.getValue("SAVE_MACHINE_TRANSLATE_FLAG"))) {
            addMachineTranslate(paramObj, dataFlowContext);
        }*/

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());


        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private JSONObject addMember(JSONObject paramInJson) {

        //根据房屋ID查询业主ID，自动生成成员ID
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(paramInJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtoList = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        Assert.listOnlyOne(ownerRoomRelDtoList, "根据房屋查询不到业主信息或查询到多条");
        paramInJson.put("ownerId", ownerRoomRelDtoList.get(0).getOwnerId());


        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramInJson.put("memberId", memberId);


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(paramInJson);
        businessOwner.put("ownerTypeCd", "1004");//临时人员
        businessOwner.put("state", "1000");//待审核
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwner", businessOwner);

        return business;
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "10000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("memberId"));
        businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("ownerPhotoId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
    }


    /**
     * 数据校验
     * <p>
     * name:'',
     * age:'',
     * link:'',
     * sex:'',
     * remark:''
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(paramIn, "roomId", "请求报文中未包含房屋信息");
        Assert.jsonObjectHaveKey(paramIn, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(paramIn, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(paramIn, "sex", "请求报文中未包含sex");
        //Assert.jsonObjectHaveKey(paramIn, "ownerTypeCd", "请求报文中未包含sex"); //这个不需要 这个直接写成钥匙申请，临时人员
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
        Assert.jsonObjectHaveKey(paramIn, "ownerPhoto", "请求报文中未包含照片信息");
       /* Assert.jsonObjectHaveKey(paramIn, "startTime", "请求报文中未包含开始时间"); 这块打算放在业主属性表中
        Assert.jsonObjectHaveKey(paramIn, "endTime", "请求报文中未包含结束时间");*/
    }


    @Override
    public int getOrder() {
        return 0;
    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }

    public IOwnerRoomRelInnerServiceSMO getOwnerRoomRelInnerServiceSMOImpl() {
        return ownerRoomRelInnerServiceSMOImpl;
    }

    public void setOwnerRoomRelInnerServiceSMOImpl(IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl) {
        this.ownerRoomRelInnerServiceSMOImpl = ownerRoomRelInnerServiceSMOImpl;
    }
}
