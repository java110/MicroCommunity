package com.java110.common.cmd.file;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@Java110Cmd(serviceCode = "file.saveFile")
public class SaveFileCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "fileName", "必填，请填写文件名称");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写文件内容");
        Assert.hasKeyAndValue(reqJson, "suffix", "必填，请填写文件扩展");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FileDto fileDto = BeanConvertUtil.covertBean(reqJson, FileDto.class);

        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));

        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

        JSONObject outParam = new JSONObject();
        outParam.put("fileId", fileName);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(outParam.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
