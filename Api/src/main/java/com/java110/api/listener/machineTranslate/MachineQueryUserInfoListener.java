package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.OwnerDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.hardwareAdapation.MachineTranslateDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineQueryUserInfoListener")
public class MachineQueryUserInfoListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     *
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;

        outParam = new JSONObject();
        outParam.put("code", 0);
        outParam.put("message", "success");
        JSONArray data = null;
        Map<String, String> reqHeader = context.getRequestHeaders();

        HttpHeaders headers = new HttpHeaders();
        if (reqHeader == null || !reqHeader.containsKey("communityId") || StringUtils.isEmpty(reqHeader.get("communityId"))) {
            outParam.put("code", -1);
            outParam.put("message", "请求地址中未包含小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        for (String key : reqHeader.keySet()) {
            headers.add(key, reqHeader.get(key));
        }

        String communityId = reqHeader.get("communityId");

        //检查设备是否合法
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqHeader.get("MachineCode"));
        machineDto.setCommunityId(communityId);
        int machineCount = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);
        if (machineCount < 1) {
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + reqJson.getString("machineCode") + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        //检查是否存在该用户
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setOwnerId(reqJson.getString("faceid"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到相应业主信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (communityDtos == null || communityDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到相应小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到业主照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(communityId);
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            outParam.put("code", -1);
            outParam.put("message", "未找到业主照片，可能未录入照片");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        JSONObject dataObj = new JSONObject();
        dataObj.put("userid", ownerDtos.get(0).getOwnerId());
        dataObj.put("groupid", communityId);
        dataObj.put("group", communityDtos.get(0).getName());
        dataObj.put("name", ownerDtos.get(0).getName());
        dataObj.put("faceBase64", fileDtos.get(0).getContext()
                .replace("data:image/webp;base64,", "")
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", ""));
        dataObj.put("idNumber", ownerDtos.get(0).getOwnerId());
        dataObj.put("startTime", ownerDtos.get(0).getCreateTime().getTime());
        try {
            dataObj.put("endTime", DateUtil.getLastDate().getTime());
        } catch (ParseException e) {
            dataObj.put("endTime", 2145891661);
        }
        dataObj.put("remarks", "HC小区管理系统");
        dataObj.put("reserved", ownerDtos.get(0).getOwnerId());
        outParam.put("data", dataObj);

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(machineDto.getMachineCode());
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_HEARTBEAT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public IMachineTranslateInnerServiceSMO getMachineTranslateInnerServiceSMOImpl() {
        return machineTranslateInnerServiceSMOImpl;
    }

    public void setMachineTranslateInnerServiceSMOImpl(IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl) {
        this.machineTranslateInnerServiceSMOImpl = machineTranslateInnerServiceSMOImpl;
    }

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IFileRelInnerServiceSMO getFileRelInnerServiceSMOImpl() {
        return fileRelInnerServiceSMOImpl;
    }

    public void setFileRelInnerServiceSMOImpl(IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl) {
        this.fileRelInnerServiceSMOImpl = fileRelInnerServiceSMOImpl;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
