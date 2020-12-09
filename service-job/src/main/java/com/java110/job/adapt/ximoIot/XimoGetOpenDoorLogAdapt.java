/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt.ximoIot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImageUtils;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @desc add by 吴学文 12:28
 */
@Service("ximoGetOpenDoorLogAdapt")
public class XimoGetOpenDoorLogAdapt extends TaskSystemQuartz {

    private static Date preDate = null;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            queryOpenDoorLog(taskDto, communityDto);
        }
    }

    private void queryOpenDoorLog(TaskDto taskDto, CommunityDto communityDto) {

        if (preDate == null) {
            preDate = new Date();
        }

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("extCommunityUuid", communityDto.getCommunityId());
        postParameters.add("startDateTime", DateUtil.getFormatTimeString(preDate, DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(preDate);
        calendar.add(Calendar.SECOND, XimoIotConstant.DEFAULT_LOG_TIME);
        postParameters.add("endDateTime", DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        postParameters.add("currPage", 1);
        postParameters.add("pageSize", 200);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(XimoIotConstant.UPDATE_MACHINE_URL, HttpMethod.GET, httpEntity, String.class);

        if (responseEntity.getStatusCode() != null) {
            throw new IllegalArgumentException("获取token失败" + responseEntity.getBody());
        }
        JSONObject tokenObj = JSONObject.parseObject(responseEntity.getBody());

        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
            throw new IllegalArgumentException("获取token失败" + responseEntity.getBody());
        }

        preDate = calendar.getTime();

        JSONArray list = tokenObj.getJSONObject("data").getJSONArray("list");
        MachineDto tMachineDto = null;
        List<MachineRecordPo> machineRecordPos = new ArrayList<>();
        for (int dataIndex = 0; dataIndex < list.size(); dataIndex++) {
            saveMachineOpenDoor(list.getJSONObject(dataIndex), communityDto, machineRecordPos);
        }

        machineRecordInnerServiceSMOImpl.saveMachineRecords(machineRecordPos);
        logger.debug("调用吸墨信息：" + responseEntity);
    }

    private void saveMachineOpenDoor(JSONObject jsonObject, CommunityDto communityDto, List<MachineRecordPo> machineRecordPos) {

        String machineCode = jsonObject.getString("devSn");
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityDto.getCommunityId());
        machineDto.setMachineCode(machineCode);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        //设备不存在
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }

        String ownerId = "";
        String tel = "";
        String idCard = "";

        if (jsonObject.containsKey("empUuid")) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(jsonObject.getString("empUuid"));
            ownerDto.setCommunityId(communityDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

            if (ownerDtos != null && ownerDtos.size() > 0) {
                ownerId = ownerDtos.get(0).getOwnerId();
                tel = ownerDtos.get(0).getLink();
                idCard = ownerDtos.get(0).getIdCard();
            }
        }

        machineDto = machineDtos.get(0);

        MachineRecordPo machineRecordPo = new MachineRecordPo();
        machineRecordPo.setCommunityId(communityDto.getCommunityId());
        machineRecordPo.setMachineCode(machineDto.getMachineCode());
        machineRecordPo.setMachineId(machineDto.getMachineId());
        machineRecordPo.setMachineRecordId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));
        machineRecordPo.setOpenTypeCd("21".equals(jsonObject.getString("eventType")) ? "1000" : "2000");
        machineRecordPo.setName(jsonObject.getString("empName"));

        machineRecordPo.setTel(tel);
        machineRecordPo.setIdCard(idCard);
        machineRecordPo.setRecordTypeCd("8888");
        machineRecordPo.setFileTime(jsonObject.getString("eventTime"));

        if (!jsonObject.containsKey("captureImage") || !jsonObject.getString("captureImage").startsWith("http")) {
            return;
        }

        String img = ImageUtils.getBase64ByImgUrl(jsonObject.getString("captureImage"));
        String fileId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id);

        FileDto fileDto = new FileDto();
        fileDto.setCommunityId(communityDto.getCommunityId());
        fileDto.setFileId(fileId);
        fileDto.setFileName(fileId);
        fileDto.setContext(img);
        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        businessUnit.put("relTypeCd", "60000");
        businessUnit.put("saveWay", "ftp");
        businessUnit.put("objId", machineRecordPo.getMachineRecordId());
        businessUnit.put("fileRealName", fileId);
        businessUnit.put("fileSaveName", fileName);
        fileRelInnerServiceSMOImpl.saveFileRel(BeanConvertUtil.covertBean(businessUnit, FileRelPo.class));
        machineRecordPo.setFileId(fileId);

    }
}
