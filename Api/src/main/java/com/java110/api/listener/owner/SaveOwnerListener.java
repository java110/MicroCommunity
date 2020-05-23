package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.factory.SendSmsFactory;
import com.java110.core.smo.common.ISmsInnerServiceSMO;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.msg.SmsDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.activation.FileDataSource;
import javax.xml.ws.soap.Addressing;
import java.util.List;

/**
 * @ClassName SaveOwnerListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("saveOwnerListener")
public class SaveOwnerListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_OWNER;
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

        //生成memberId
        generateMemberId(paramObj);

        //添加小区楼
        businesses.add(ownerBMOImpl.addOwner(paramObj));

        if ("1001".equals(paramObj.getString("ownerTypeCd"))) {
            //小区楼添加到小区中
            businesses.add(ownerBMOImpl.addCommunityMember(paramObj));
        }

        //有房屋信息，则直接绑定房屋和 业主的关系
        if (paramObj.containsKey("roomId")) {
            //添加单元信息
            businesses.add(ownerBMOImpl.sellRoom(paramObj, dataFlowContext));

            //添加物业费用信息
            businesses.add(ownerBMOImpl.addPropertyFee(paramObj, dataFlowContext));
        }
        if (paramObj.containsKey("ownerPhoto") && !StringUtils.isEmpty(paramObj.getString("ownerPhoto"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(paramObj.getString("ownerPhoto"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(paramObj.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            paramObj.put("ownerPhotoId", fileDto.getFileId());
            paramObj.put("fileSaveName", fileName);

            businesses.add(ownerBMOImpl.addOwnerPhoto(paramObj, dataFlowContext));

        }

        /*if ("ON".equals(MappingCache.getValue("SAVE_MACHINE_TRANSLATE_FLAG"))) {
            addMachineTranslate(paramObj, dataFlowContext);
        }*/




        ResponseEntity<String> responseEntity = ownerBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 生成小区楼ID
     *
     * @param paramObj 请求入参数据
     */
    private void generateMemberId(JSONObject paramObj) {
        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramObj.put("memberId", memberId);
        if (!paramObj.containsKey("ownerId") || "1001".equals(paramObj.getString("ownerTypeCd"))) {
            paramObj.put("ownerId", memberId);
        }
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
        Assert.jsonObjectHaveKey(paramIn, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(paramIn, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(paramIn, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(paramIn, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(paramIn, "ownerTypeCd", "请求报文中未包含类型");
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId");
        //Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");

        JSONObject paramObj = JSONObject.parseObject(paramIn);

        if (paramObj.containsKey("roomId")) {

            Assert.jsonObjectHaveKey(paramObj, "state", "请求报文中未包含state节点");
            Assert.jsonObjectHaveKey(paramObj, "storeId", "请求报文中未包含storeId节点");

            Assert.hasLength(paramObj.getString("roomId"), "roomId不能为空");
            Assert.hasLength(paramObj.getString("state"), "state不能为空");
            Assert.hasLength(paramObj.getString("storeId"), "storeId不能为空");
        }

        if(paramObj.containsKey("msgCode")){
            SmsDto smsDto = new SmsDto();
            smsDto.setTel(paramObj.getString("link"));
            smsDto.setCode(paramObj.getString("msgCode"));
            smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

            if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(SendSmsFactory.SMS_SEND_SWITCH))) {
                throw new IllegalArgumentException(smsDto.getMsg());
            }
        }
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
}
