package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.entity.center.AppService;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.StoreUserRelConstant;
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

/**
 * 添加员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("addStaffServiceListener")
public class AddStaffServiceListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(AddStaffServiceListener.class);

    @Autowired
    private IUserBMO userBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_ADD;
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
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //获取数据上下文对象
        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "storeTypeCd", "请求参数中未包含storeTypeCd 节点，请确认");
        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        String userId = "";
        String oldUserId = "";
        String relCd = reqJson.getString("relCd");//员工 组织 岗位
        if (!reqJson.containsKey("userId") || "-1".equals(reqJson.getString("userId"))) {
            //将userId 强制写成-1
            oldUserId = "-1";
            userId = GenerateCodeFactory.getUserId();
            reqJson.put("userId", userId);
            //添加用户
            userBMOImpl.addUser(reqJson, context);
        }
        reqJson.put("userId", userId);
        reqJson.put("relCd", "-1".equals(oldUserId) ? StoreUserRelConstant.REL_COMMON : StoreUserRelConstant.REL_ADMIN);
        userBMOImpl.addStaff(reqJson, context);
        //重写 员工岗位
        reqJson.put("relCd", relCd);
        userBMOImpl.addStaffOrg(reqJson, context);
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", "-1");
            businessUnit.put("relTypeCd", "12000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", userId);
            businessUnit.put("fileRealName", fileDto.getFileId());
            businessUnit.put("fileSaveName", fileName);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        }
        commit(context);
        //如果不成功直接返回
        if (context.getResponseEntity().getStatusCode() != HttpStatus.OK) {
            return;
        }
    }

    /**
     * 用户赋权
     *
     * @return
     */
    private void privilegeUserDefault(DataFlowContext dataFlowContext, JSONObject paramObj) {
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        if (appService == null) {
            responseEntity = new ResponseEntity<String>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        userBMOImpl.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId", paramObj.getString("userId"));
        paramInObj.put("storeTypeCd", paramObj.getString("storeTypeCd"));
        paramInObj.put("storeId", paramObj.getString("storeId"));
        paramInObj.put("userFlag", "staff");
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }
    }
}
