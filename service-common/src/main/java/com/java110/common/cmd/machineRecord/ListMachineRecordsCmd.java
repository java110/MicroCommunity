package com.java110.common.cmd.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machineRecord.ApiMachineRecordDataVo;
import com.java110.vo.api.machineRecord.ApiMachineRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "machineRecord.listMachineRecords")
public class ListMachineRecordsCmd extends Cmd {

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);

        int count = machineRecordInnerServiceSMOImpl.queryMachineRecordsCount(machineRecordDto);

        List<ApiMachineRecordDataVo> machineRecords = null;

        if (count > 0) {
            machineRecords = BeanConvertUtil.covertBeanList(machineRecordInnerServiceSMOImpl.queryMachineRecords(machineRecordDto), ApiMachineRecordDataVo.class);
            freshFaceUrl(machineRecords);
        } else {
            machineRecords = new ArrayList<>();
        }

        ApiMachineRecordVo apiMachineRecordVo = new ApiMachineRecordVo();

        apiMachineRecordVo.setTotal(count);
        apiMachineRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineRecordVo.setMachineRecords(machineRecords);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void freshFaceUrl(List<ApiMachineRecordDataVo> machineRecords) {

        List<String> recordIds = new ArrayList<>();

        for (ApiMachineRecordDataVo tmpOwnerDto : machineRecords) {
            recordIds.add(tmpOwnerDto.getMachineRecordId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        //fileRelDto.setObjId(owners.get(0).getMemberId());
        fileRelDto.setObjIds(recordIds.toArray(new String[recordIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return ;
        }

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");

        for (ApiMachineRecordDataVo tmpOwnerDto : machineRecords) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!tmpOwnerDto.getMachineRecordId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }

                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    tmpOwnerDto.setFaceUrl(tmpFileRelDto.getFileSaveName());
                } else {
                    tmpOwnerDto.setFaceUrl(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
    }
}
