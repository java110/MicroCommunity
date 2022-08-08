package com.java110.common.cmd.file;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.file.ApiFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "file.getFile")
public class GetFileCmd extends Cmd {
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "fileId", "未包含文件ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setFileRealName(reqJson.getString("fileId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        FileDto fileDto = new FileDto();
        if(fileRelDtos == null || fileRelDtos.size() < 1) {
            fileDto.setFileSaveName(reqJson.getString("fileId"));
        }else {
            fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        }
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);

        Assert.listOnlyOne(fileDtos, "存在多个文件，数据错误" + reqJson.toJSONString());

        ApiFileVo fileVo = BeanConvertUtil.covertBean(fileDtos.get(0), ApiFileVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(fileVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
