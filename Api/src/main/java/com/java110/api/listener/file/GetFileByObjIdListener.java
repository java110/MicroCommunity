package com.java110.api.listener.file;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.file.ApiFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("getFileByObjIdListener")
public class GetFileByObjIdListener extends AbstractServiceApiListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceConstant.GET_FILE_BY_OBJ_ID;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson,"objId", "未包含文件ID");
        Assert.hasKeyAndValue(reqJson,"fileTypeCd", "未包含文件类型");
        Assert.hasKeyAndValue(reqJson,"communityId", "未包含小区ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("objId"));
        fileRelDto.setRelTypeCd(reqJson.getString("fileTypeCd"));

        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        Assert.listOnlyOne(fileRelDtos,"存在多条数据或未存在数据");

        FileDto fileDto = BeanConvertUtil.covertBean(reqJson, FileDto.class);
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);

        Assert.listOnlyOne(fileDtos,"存在多个文件，数据错误" + reqJson.toJSONString());

        ApiFileVo fileVo = BeanConvertUtil.covertBean(fileDtos.get(0), ApiFileVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(fileVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

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
}
