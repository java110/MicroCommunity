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
package com.java110.job.adapt.hcIot.asyn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineTranslateErrorDto;
import com.java110.intf.common.IMachineAttrInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.job.adapt.hcIot.IotConstant;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.machineTranslateError.MachineTranslateErrorPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * IOT信息异步同步处理实现类
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 11:55
 */
//@Service
public class IotSendKafkaImpl implements IIotSendAsyn {
    private static final Logger logger = LoggerFactory.getLogger(IotSendKafkaImpl.class);

    private static final String DEFAULT_MACHINE_CODE = "-";
    private static final String DEFAULT_MACHINE_ID = "-";

    public static final String DEFAULT_TOPIC = "java110-hc-xiaoqu";

    public static final String IOT_TOPIC = "IOT_TOPIC";


    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineAttrInnerServiceSMO machineAttrInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;

    private String createReqParam(String url, JSONObject param) {
        JSONObject data = new JSONObject();
        data.put("appId", IotConstant.getAppId());
        data.put("action", url);
        data.put("data", param);
        data.put("sign", "");
        return data.toJSONString();
    }

    private void sendKafkaMessage(String url, JSONObject param) throws Exception {
        String data = createReqParam(url, param);

        // 根据小区 topic
        String topic = MappingCache.getValue(IOT_TOPIC, param.getString("extCommunityId"));

        topic = StringUtil.isEmpty(topic) ? DEFAULT_TOPIC : topic;

        KafkaFactory.sendKafkaMessage(topic, data);

    }

    @Override
    public void addCommunity(JSONObject postParameters) throws Exception {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_COMMUNITY,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID, "extCommunityId", "name", MachineTranslateDto.TYPE_COMMUNITY);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_COMMUNITY_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            logger.error("修改小区异常", e);
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), "", IotConstant.ADD_COMMUNITY_URL);
        } finally {
            saveTranslateLog(machineTranslateDto);
        }
    }

    @Override
    @Async
    public void editCommunity(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_UPDATE_COMMUNITY,
                DEFAULT_MACHINE_CODE, DEFAULT_MACHINE_ID, "extCommunityId", "name", MachineTranslateDto.TYPE_COMMUNITY);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_COMMUNITY_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            logger.error("修改小区异常", e);
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_COMMUNITY_URL);

        } finally {
            saveTranslateLog(machineTranslateDto);
        }
    }

    @Override
    @Async
    public void deleteCommunity(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_COMMUNITY,
                DEFAULT_MACHINE_CODE, DEFAULT_MACHINE_ID,
                "extCommunityId", "name", MachineTranslateDto.TYPE_COMMUNITY);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_COMMUNITY_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            logger.error("删除小区异常", e);
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_COMMUNITY_URL);
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    /**
     * 添加设备
     *
     * @param postParameters
     * @param ownerDtos
     */
    @Override
    @Async
    public void addMachine(JSONObject postParameters, List<JSONObject> ownerDtos) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_ADD_MACHINE,
                postParameters.getString("machineCode"), postParameters.getString("extMachineId"),
                "extMachineId", "machineName", MachineTranslateDto.TYPE_MACHINE);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_MACHINE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_MACHINE_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }

        MachineDto machinePo = new MachineDto();
        machinePo.setMachineId(postParameters.getString("extMachineId"));
        machinePo.setCommunityId(postParameters.getString("extCommunityId"));
        machinePo.setState("1700");
        machineInnerServiceSMOImpl.updateMachineState(machinePo);

        for (JSONObject owner : ownerDtos) {
            addOwner(owner);
        }
    }

    @Override
    @Async
    public void updateMachine(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_UPDATE_MACHINE,
                postParameters.getString("machineCode"),
                postParameters.getString("extMachineId"),
                "extMachineId", "machineName", MachineTranslateDto.TYPE_MACHINE);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_MACHINE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_MACHINE_URL);
        } finally {
            saveTranslateLog(machineTranslateDto);

        }

        MachineDto machinePo = new MachineDto();
        machinePo.setMachineId(postParameters.getString("extMachineId"));
        machinePo.setCommunityId(postParameters.getString("extCommunityId"));
        machinePo.setState("1700");
        machineInnerServiceSMOImpl.updateMachineState(machinePo);
    }

    @Override
    @Async
    public void deleteMachine(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_DELETE_MACHINE,
                postParameters.getString("machineCode"),
                postParameters.getString("extMachineId"),
                "extMachineId",
                "machineName",
                MachineTranslateDto.TYPE_MACHINE);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_MACHINE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_MACHINE_URL);
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addOwner(JSONObject postParameters) {

        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_ADD_OWNER_FACE,
                postParameters.getString("machineCode"),
                postParameters.getString("extMachineId"),
                "userId",
                "name",
                MachineTranslateDto.TYPE_OWNER);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_OWNER, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_OWNER);
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void sendUpdateOwner(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_UPDATE_OWNER_FACE,
                postParameters.getString("machineCode"),
                postParameters.getString("extMachineId"),
                "userId", "name",
                MachineTranslateDto.TYPE_OWNER);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.EDIT_OWNER, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), "", IotConstant.EDIT_OWNER);

        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void sendDeleteOwner(JSONObject postParameters) {

        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_OWNER_FACE,
                postParameters.getString("machineCode"),
                postParameters.getString("extMachineId"),
                "userId",
                "name",
                MachineTranslateDto.TYPE_OWNER);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_OWNER, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), "", IotConstant.DELETE_OWNER);


        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    /**
     * 重试
     *
     * @param reqJson
     */
    @Override
    @Async
    public void reSendIot(JSONObject reqJson) {
        MachineTranslateErrorDto machineTranslateErrorDto = new MachineTranslateErrorDto();
        machineTranslateErrorDto.setCommunityId(reqJson.getString("communityId"));
        machineTranslateErrorDto.setMachineTranslateId(reqJson.getString("machineTranslateId"));
        List<MachineTranslateErrorDto> machineTranslateErrorDtos
                = machineTranslateErrorInnerServiceSMOImpl.queryMachineTranslateErrors(machineTranslateErrorDto);

        if (machineTranslateErrorDtos == null || machineTranslateErrorDtos.size() < 1) {
            return;
        }


//        HttpEntity httpEntity = new HttpEntity(machineTranslateErrorDtos.get(0).getReqBody(), getHeaders());
//        ResponseEntity<String> responseEntity
//                = outRestTemplate.exchange(machineTranslateErrorDtos.get(0).getReqPath(), HttpMethod.POST, httpEntity, String.class);
//        JSONObject tokenObj = JSONObject.parseObject(responseEntity.getBody());
//        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
//        machineTranslateDto.setMachineTranslateId(reqJson.getString("machineTranslateId"));
//        machineTranslateDto.setCommunityId(reqJson.getString("communityId"));
//        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
//        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
//            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
//            machineTranslateDto.setRemark(tokenObj.getString("msg"));
//        } else {
//            machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
//            machineTranslateDto.setRemark("同步物联网系统成功");
//        }
//        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(machineTranslateDto);
    }

    @Override
    @Async
    public void addParkingArea(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_ADD_PARKING_AREA,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extPaId",
                "num",
                MachineTranslateDto.TYPE_PARKING_AREA);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_PARKING_AREA_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), "", IotConstant.ADD_PARKING_AREA_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void updateParkingArea(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_UPDATE_PARKING_AREA,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extPaId",
                "num",
                MachineTranslateDto.TYPE_PARKING_AREA);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_PARKING_AREA_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_PARKING_AREA_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void deleteParkingArea(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_DELETE_PARKING_AREA,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extPaId",
                "num",
                MachineTranslateDto.TYPE_PARKING_AREA);
        ResponseEntity<String> responseEntity = null;
        String url = IotConstant.getUrl(IotConstant.DELETE_PARKING_AREA_URL);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_PARKING_AREA_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_PARKING_AREA_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addOwnerCar(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_OWNER_CAR,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extCarId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_OWNER_CAR_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_OWNER_CAR_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addVisit(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_VISIT,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extCarId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_VISIT_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_OWNER_CAR_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void updateOwnerCar(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_UPDATE_OWNER_CAR,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extCarId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_OWNER_CAR_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_OWNER_CAR_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void deleteOwnerCar(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters, MachineTranslateDto.CMD_DELETE_OWNER_CAR,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extCarId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_OWNER_CAR_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_OWNER_CAR_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addCarBlackWhite(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_CAR_BLACK_WHITE,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extBwId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_CAR_BLACK_WHITE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_CAR_BLACK_WHITE_URL);
            return;
        } finally {
            saveTranslateLog(machineTranslateDto);
        }
    }

    @Override
    public void updateCarBlackWhite(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_UPDATE_CAR_BLACK_WHITE,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extBwId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_CAR_BLACK_WHITE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_CAR_BLACK_WHITE_URL);
            return;
        } finally {
            saveTranslateLog(machineTranslateDto);
        }
    }

    @Override
    @Async
    public void deleteCarBlackWhite(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_CAR_BLACK_WHITE,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extBwId",
                "carNum",
                MachineTranslateDto.TYPE_OWNER_CAR);
        ResponseEntity<String> responseEntity = null;
        String url = IotConstant.getUrl(IotConstant.DELETE_CAR_BLACK_WHITE_URL);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_CAR_BLACK_WHITE_URL, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_CAR_BLACK_WHITE_URL);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }


    @Override
    @Async
    public void addTempCarFeeConfig(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_TEAM_CAR_FEE_CONFIG,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extConfigId",
                "feeName",
                MachineTranslateDto.TYPE_TEAM_CAR_FEE_CONFIG);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_TEAM_CAR_FEE_CONFIG, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_TEAM_CAR_FEE_CONFIG);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void updateTempCarFeeConfig(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_UPDATE_TEAM_CAR_FEE_CONFIG,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extConfigId",
                "feeName",
                MachineTranslateDto.TYPE_TEAM_CAR_FEE_CONFIG);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_TEAM_CAR_FEE_CONFIG, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_TEAM_CAR_FEE_CONFIG);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void deleteTempCarFeeConfig(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_TEAM_CAR_FEE_CONFIG,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extBwId",
                "carNum",
                MachineTranslateDto.TYPE_TEAM_CAR_FEE_CONFIG);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_TEAM_CAR_FEE_CONFIG, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");
        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_TEAM_CAR_FEE_CONFIG);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addAttendance(JSONObject postParameters, List<JSONObject> staffs) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_ATTENDANCE_CLASSES,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extClassesId",
                "classesName",
                MachineTranslateDto.TYPE_ATTENDANCE);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.ADD_ATTENDANCE_CLASSES, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");

        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_ATTENDANCE_CLASSES);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void addAttendanceStaff(JSONObject postParameters, List<JSONObject> staffs) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_ADD_ATTENDANCE_CLASSES,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extClassesId",
                "classesName",
                MachineTranslateDto.TYPE_ATTENDANCE);
        ResponseEntity<String> responseEntity = null;
        try {
            for (JSONObject staff : staffs) {
                staff.put("taskId", machineTranslateDto.getMachineTranslateId());
            }
            sendKafkaMessage(IotConstant.ADD_ATTENDANCE_CLASSES_STAFFS, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");

        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.ADD_ATTENDANCE_CLASSES_STAFFS);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    public void updateAttendanceStaff(JSONObject postParameters, List<JSONObject> staffs) {

    }


    @Override
    @Async
    public void deleteAttendanceStaff(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_ATTENDANCE_CLASSES,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extClassesId",
                "classesName",
                MachineTranslateDto.TYPE_ATTENDANCE);
        ResponseEntity<String> responseEntity = null;
        String url = IotConstant.getUrl(IotConstant.DELETE_ATTENDANCE_CLASSES_STAFFS);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_ATTENDANCE_CLASSES_STAFFS, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");

        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_ATTENDANCE_CLASSES_STAFFS);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    public void addParkingAreaText(JSONObject postParameters) {

    }

    @Override
    public void addParkingBox(JSONObject postParameters) {

    }

    @Override
    public void deleteParkingBox(JSONObject postParameters) {

    }

    @Override
    public void addParkingCouponCar(JSONObject postParameters) {

    }

    @Override
    public void deleteParkingCouponCar(JSONObject postParameters) {

    }

    @Override
    @Async
    public void updateAttendance(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_UPDATE_ATTENDANCE_CLASSES,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extClassesId",
                "classesName",
                MachineTranslateDto.TYPE_ATTENDANCE);
        ResponseEntity<String> responseEntity = null;
        String url = IotConstant.getUrl(IotConstant.UPDATE_ATTENDANCE_CLASSES);
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.UPDATE_ATTENDANCE_CLASSES, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");

        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.UPDATE_ATTENDANCE_CLASSES);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    @Override
    @Async
    public void deleteAttendance(JSONObject postParameters) {
        MachineTranslateDto machineTranslateDto = getMachineTranslateDto(postParameters,
                MachineTranslateDto.CMD_DELETE_ATTENDANCE_CLASSES,
                DEFAULT_MACHINE_CODE,
                DEFAULT_MACHINE_ID,
                "extClassesId",
                "classesName",
                MachineTranslateDto.TYPE_ATTENDANCE);
        ResponseEntity<String> responseEntity = null;
        try {
            postParameters.put("taskId", machineTranslateDto.getMachineTranslateId());
            sendKafkaMessage(IotConstant.DELETE_ATTENDANCE_CLASSES, postParameters);
            machineTranslateDto.setState(MachineTranslateDto.STATE_DOING);
            machineTranslateDto.setRemark("正在同步");

        } catch (Exception e) {
            machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
            machineTranslateDto.setRemark(e.getLocalizedMessage());
            //保存 失败报文
            saveTranslateError(machineTranslateDto, postParameters.toJSONString(), responseEntity != null ? responseEntity.getBody() : "", IotConstant.DELETE_ATTENDANCE_CLASSES);

            return;
        } finally {
            saveTranslateLog(machineTranslateDto);

        }
    }

    private MachineTranslateDto getMachineTranslateDto(JSONObject postParameters, String cmd,
                                                       String machineCode,
                                                       String machineId,
                                                       String objId,
                                                       String objName,
                                                       String typeCd) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(postParameters.getString("extCommunityId"));
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode(machineCode);
        machineTranslateDto.setMachineId(machineId);
        machineTranslateDto.setObjId(postParameters.getString(objId));
        machineTranslateDto.setObjName(postParameters.getString(objName));
        machineTranslateDto.setTypeCd(typeCd);
        machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
        machineTranslateDto.setRemark("同步物联网系统成功");
        return machineTranslateDto;
    }


    /**
     * 存储交互 记录
     *
     * @param machineTranslateDto
     */
    public void saveTranslateLog(MachineTranslateDto machineTranslateDto) {
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }

    public void saveTranslateError(MachineTranslateDto machineTranslateDto, String reqJson, String resJson, String url) {
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        MachineTranslateErrorPo machineTranslateErrorPo = new MachineTranslateErrorPo();
        machineTranslateErrorPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        machineTranslateErrorPo.setCommunityId(machineTranslateDto.getCommunityId());
        machineTranslateErrorPo.setMachineTranslateId(machineTranslateDto.getMachineTranslateId());
        machineTranslateErrorPo.setReqBody(reqJson);
        machineTranslateErrorPo.setReqHeader("");
        machineTranslateErrorPo.setResBody(resJson);
        machineTranslateErrorPo.setReqPath(url);
        machineTranslateErrorPo.setReqType(MachineTranslateErrorDto.REQ_TYPE_URL);
        machineTranslateErrorInnerServiceSMOImpl.saveMachineTranslateError(machineTranslateErrorPo);
    }


}
