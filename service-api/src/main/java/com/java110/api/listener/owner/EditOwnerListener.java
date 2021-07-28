package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.bmo.ownerAttr.IOwnerAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName EditOwnerListener
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:19
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/
@Java110Listener("editOwnerListener")
public class EditOwnerListener extends AbstractServiceApiPlusListener {

    private static Logger logger = LoggerFactory.getLogger(EditOwnerListener.class);

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private IOwnerAttrBMO ownerAttrBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EDIT_OWNER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含ownerTypeCd");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        // Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        if (!reqJson.containsKey("ownerId") || "1001".equals(reqJson.getString("ownerTypeCd"))) {
            reqJson.put("ownerId", reqJson.getString("memberId"));
        }
        //获取手机号(判断手机号是否重复)
        String link = reqJson.getString("link");
        if (link.length() != 11) {
            throw new IllegalArgumentException("手机号输入不正确！");
        }
        if (!StringUtil.isEmpty(link) && link.contains("*")) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(reqJson.getString("ownerId"));
            //业主
            ownerDto.setOwnerTypeCd("1001");
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
            link = ownerDtos.get(0).getLink();
            reqJson.put("link", link);
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
        if (ownerDtos != null && ownerDtos.size() > 1) {
            throw new IllegalArgumentException("手机号重复，请重新输入");
        } else if (ownerDtos != null && ownerDtos.size() == 1) {
            for (OwnerDto owner : ownerDtos) {
                if ((!StringUtil.isEmpty(reqJson.getString("ownerId")) && !owner.getOwnerId().equals(reqJson.getString("ownerId"))) || (!StringUtil.isEmpty(reqJson.getString("memberId")) && !owner.getMemberId().equals(reqJson.getString("memberId")))) {
                    throw new IllegalArgumentException("手机号重复，请重新输入");
                }
            }
        }
        //获取身份证号(判断身份证号是否重复)
        String idCard = reqJson.getString("idCard");
        if (!StringUtil.isEmpty(idCard) && idCard.contains("*")) {
            OwnerDto owner = new OwnerDto();
            owner.setOwnerId(reqJson.getString("ownerId"));
            //业主
            owner.setOwnerTypeCd("1001");
            List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwners(owner);
            Assert.listOnlyOne(owners, "查询业主信息错误！");
            idCard = owners.get(0).getIdCard();
            reqJson.put("idCard", idCard);
        }
        if (!StringUtil.isEmpty(idCard)) {
            OwnerDto owner = new OwnerDto();
            owner.setIdCard(idCard);
            owner.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryAllOwners(owner);
            if (owners != null && owners.size() > 1) {
                throw new IllegalArgumentException("身份证号重复，请重新输入");
            } else if (owners != null && owners.size() == 1) {
                for (OwnerDto ownerDto1 : owners) {
                    if ((!StringUtil.isEmpty(reqJson.getString("ownerId")) && !ownerDto1.getOwnerId().equals(reqJson.getString("ownerId"))) || (!StringUtil.isEmpty(reqJson.getString("memberId")) && !ownerDto1.getMemberId().equals(reqJson.getString("memberId")))) {
                        throw new IllegalArgumentException("身份证号重复，请重新输入");
                    }
                }
            }
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
            ownerBMOImpl.editOwnerPhoto(reqJson, context);
        }
        ownerBMOImpl.editOwner(reqJson, context);
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("memberId", reqJson.getString("memberId"));
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                ownerAttrBMOImpl.addOwnerAttr(attr, context);
                continue;
            }
            ownerAttrBMOImpl.updateOwnerAttr(attr, context);
        }
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

    public IFileRelInnerServiceSMO getFileRelInnerServiceSMOImpl() {
        return fileRelInnerServiceSMOImpl;
    }

    public void setFileRelInnerServiceSMOImpl(IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl) {
        this.fileRelInnerServiceSMOImpl = fileRelInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }
}
