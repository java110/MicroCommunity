package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.bmo.ownerAttr.IOwnerAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.msg.SmsDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName SaveOwnerListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("saveOwnerListener")
public class SaveOwnerListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private IOwnerAttrBMO ownerAttrBMOImpl;

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
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含类型");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        //Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");


        if (reqJson.containsKey("roomId")) {

            Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
            Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");

            Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
            Assert.hasLength(reqJson.getString("state"), "state不能为空");
            Assert.hasLength(reqJson.getString("storeId"), "storeId不能为空");
        }

        if (reqJson.containsKey("msgCode")) {
            SmsDto smsDto = new SmsDto();
            smsDto.setTel(reqJson.getString("link"));
            smsDto.setCode(reqJson.getString("msgCode"));
            smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

            if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(SendSmsFactory.SMS_SEND_SWITCH))) {
                throw new IllegalArgumentException(smsDto.getMsg());
            }
        }
        //属性校验
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        //生成memberId
        generateMemberId(reqJson);

        //添加小区楼
        ownerBMOImpl.addOwner(reqJson, context);

//        if ("1001".equals(reqJson.getString("ownerTypeCd"))) {
//            //小区楼添加到小区中
//            ownerBMOImpl.addCommunityMember(reqJson, context);
//        }

        //有房屋信息，则直接绑定房屋和 业主的关系
        if (reqJson.containsKey("roomId")) {
            //添加单元信息
            ownerBMOImpl.sellRoom(reqJson, context);

            //添加物业费用信息
            //ownerBMOImpl.addPropertyFee(reqJson, context);
        }
        if (reqJson.containsKey("ownerPhoto") && !StringUtils.isEmpty(reqJson.getString("ownerPhoto"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("ownerPhoto"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("ownerPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            ownerBMOImpl.addOwnerPhoto(reqJson, context);
        }

        dealOwnerAttr(reqJson, context);

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

    private void dealOwnerAttr(JSONObject paramObj, DataFlowContext context) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("memberId", paramObj.getString("memberId"));
            ownerAttrBMOImpl.addOwnerAttr(attr, context);
        }

    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
