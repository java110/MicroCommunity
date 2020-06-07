package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.common.IFileInnerServiceSMO;
import com.java110.core.smo.common.IFileRelInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.entity.center.AppService;
import com.java110.po.file.FileRelPo;
import com.java110.po.user.UserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("modifyStaffServiceListener")
public class ModifyStaffServiceListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(ModifyStaffServiceListener.class);

    @Autowired
    private IUserBMO userBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_MODIFY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "name", "请求参数中未包含name 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求参数中未包含tel 节点，请确认");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setRelTypeCd("12000");
            fileRelDto.setObjId(reqJson.getString("userId"));
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos == null || fileRelDtos.size() == 0) {
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-1");
                businessUnit.put("relTypeCd", "12000");
                businessUnit.put("saveWay", "table");
                businessUnit.put("objId", reqJson.getString("userId"));
                businessUnit.put("fileRealName", fileDto.getFileId());
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            } else {
                JSONObject businessUnit = new JSONObject();
                businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
                businessUnit.put("fileRealName", fileDto.getFileId());
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                super.update(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FILE_REL);
            }
        }
        modifyStaff(reqJson, context);
    }


    private void modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext) {
        UserPo userPo = BeanConvertUtil.covertBean(builderStaffInfo(paramObj, dataFlowContext), UserPo.class);
        super.update(dataFlowContext, userPo, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //首先根据员工ID查询员工信息，根据员工信息修改相应的数据
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        if (appService == null) {
            throw new ListenerExecuteException(1999, "当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        }
        String requestUrl = appService.getUrl() + "?userId=" + paramObj.getString("userId");
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        dataFlowContext.getRequestHeaders().put("REQUEST_URL", requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        userInfo.putAll(paramObj);

        return userInfo;
    }

}
