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
package com.java110.job.adapt.hcToTianchuang;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomAttrDto;
import com.java110.dto.RoomDto;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineAttrDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineAttrInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.machine.MachineAttrPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * databus 监听业务类型 990000060001
 * 开门记录信息 同步天窗接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "personToTianchuangAdapt")
public class PersonToTianchuangAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(PersonToTianchuangAdapt.class);
    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;


    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineAttrInnerServiceSMO machineAttrInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    /**
     * @param customBusinessDatabusDto 当前处理业务
     */
    @Override
    public void customExchange(CustomBusinessDatabusDto customBusinessDatabusDto) {
        JSONObject data = customBusinessDatabusDto.getData();
        doInoutRecord(null, data);
    }

    private void doInoutRecord(Business business, JSONObject businessInoutRecord) {

        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(businessInoutRecord, MachineRecordPo.class);
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(machineRecordPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);
        String extCommunityId = "";
        String communityId = tmpCommunityDto.getCommunityId();
        String machineRecordId = machineRecordPo.getMachineRecordId();

        for (CommunityAttrDto communityAttrDto : tmpCommunityDto.getCommunityAttrDtos()) {
            if (TianChuangConstant.EXT_TC_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                extCommunityId = communityAttrDto.getValue();
            }
        }
        if (StringUtil.isEmpty(extCommunityId)) {
            //传送小区信息
            extCommunityId = sendCommunity(communityDtos.get(0));
        }

        //判断设备是否 传天创
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        machineDto.setMachineId(machineRecordPo.getMachineId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        Assert.listOnlyOne(machineDtos, "不包含 设备信息");
        String extMachineId = "";
        for (MachineAttrDto machineAttrDto : machineDtos.get(0).getMachineAttrs()) {
            if (TianChuangConstant.EXT_TC_MACHINE_ID.equals(machineAttrDto.getSpecCd())) {
                extMachineId = machineAttrDto.getValue();
            }
        }

        if (StringUtil.isEmpty(extMachineId)) {
            //传送门禁信息
            extMachineId = sendMachine(machineDtos.get(0), extCommunityId, tmpCommunityDto);
        }

        //判断住户是否传天创
        String idCard = machineRecordPo.getIdCard();

        //身份证为空时 ，门禁记录没法传 所以就不传了
        if (StringUtil.isEmpty(idCard)) {
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setIdCard(idCard);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            throw new IllegalArgumentException("业主不存在");
        }

        String extMemberId = "";
        for (OwnerAttrDto ownerAttrDto : ownerDtos.get(0).getOwnerAttrDtos()) {
            if (TianChuangConstant.EXT_TC_OWNER_ID.equals(ownerAttrDto.getSpecCd())) {
                extMemberId = ownerAttrDto.getValue();
            }
        }

        if (StringUtil.isEmpty(extMemberId)) {
            //传送门禁信息
            extMemberId = sendOwner(ownerDtos.get(0), extCommunityId, tmpCommunityDto);
        }

        // 送人员进出记录
        sendPersonInout(machineRecordPo, extCommunityId, machineDtos.get(0), ownerDtos.get(0));
    }

    private void sendPersonInout(MachineRecordPo machineRecordPo, String extCommunityId, MachineDto machineDto, OwnerDto ownerDto) {
        JSONObject data = new JSONObject();
        JSONArray datas = new JSONArray();
        JSONObject dataObj = new JSONObject();
        dataObj.put("lv_ssxqbm", extCommunityId);
        dataObj.put("lv_gmsfhm", machineRecordPo.getIdCard());
        dataObj.put("lv_crlb", "3306".equals(machineDto.getDirection()) ? "1" : "2");
        dataObj.put("lv_zpsj", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        dataObj.put("lv_sbxt", "小区管理系统");
        dataObj.put("lv_mjxtwybm", machineDto.getMachineId());
        dataObj.put("lv_ry_id", ownerDto.getMemberId());
        dataObj.put("lv_ffms", "1");
        dataObj.put("lv_kmfs", "1");
        dataObj.put("lv_procmode", "PMINSERT");

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("60000");
        fileRelDto.setObjId(machineRecordPo.getMachineRecordId());
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        String url = "";
        String imgUrl = MappingCache.getValue("IMG_PATH");
        imgUrl += (!StringUtil.isEmpty(imgUrl) && imgUrl.endsWith("/") ? "" : "/");
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            url = imgUrl + fileRelDtos.get(0).getFileRealName();
        }

        dataObj.put("lv_zpzpa", ImageUtils.getBase64ByImgUrl(url));

        datas.add(dataObj);

        data.put("datas", datas);

        JSONArray pages = new JSONArray();
        JSONObject page = new JSONObject();
        page.put("psize", "100");
        page.put("tcount", "1");
        page.put("pno", "1");
        page.put("tsize", "");
        pages.add(page);

        data.put("pages", pages);

        String dataStr = AuthenticationFactory.AesDecrypt(data.toJSONString(), TianChuangConstant.getAppSecret());

        HttpEntity httpEntity = new HttpEntity(dataStr, GetTianChuangToken.getHeaders(TianChuangConstant.SERVICE_ID_PERSON_INOUT, dataStr));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(TianChuangConstant.getUrl(), HttpMethod.POST, httpEntity, String.class);
        logger.debug("调用HC IOT信息：" + responseEntity);
        JSONObject paramOut = JSONObject.parseObject(AuthenticationFactory.AesEncrypt(responseEntity.getBody(), TianChuangConstant.getAppSecret()));

        String code = paramOut.getJSONObject("sta").getString("cod");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.toJSONString());
        }
    }

    /**
     * 住户信息上报
     *
     * @param ownerDto
     * @param tmpCommunityDto
     * @return
     */
    private String sendOwner(OwnerDto ownerDto, String extCommunityId, CommunityDto tmpCommunityDto) {
        //查询业主房屋
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDto.getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        //
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            throw new IllegalArgumentException("业主不存在房屋无法同步，因为没有地址二维码编码");
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(ownerDto.getCommunityId());
        roomDto.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");

        JSONObject data = new JSONObject();
        JSONArray datas = new JSONArray();
        JSONObject dataObj = new JSONObject();
        dataObj.put("lvgmsfhm", ownerDto.getIdCard());
        dataObj.put("lvxm", ownerDto.getName());
        dataObj.put("lvlxdh", ownerDto.getLink());
        dataObj.put("lvdjsj", ownerDto.getCreateTime());
        dataObj.put("lvrybm", ownerDto.getMemberId());
        String qrCodeAddress = "";
        for (RoomAttrDto roomAttrDto : roomDtos.get(0).getRoomAttrDto()) {
            if (TianChuangConstant.EXT_TC_ROOM_QRCODE.equals(roomAttrDto.getSpecCd())) {
                qrCodeAddress = roomAttrDto.getValue();
            }
        }
        dataObj.put("lvdzbm", qrCodeAddress);
        dataObj.put("lvzhlx", OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerDto.getOwnerTypeCd()) ? "10" : "11");
        dataObj.put("lvssxqbm", extCommunityId);
        dataObj.put("lv_procmode", "PMINSERT");

        datas.add(dataObj);

        data.put("datas", datas);

        JSONArray pages = new JSONArray();
        JSONObject page = new JSONObject();
        page.put("psize", "100");
        page.put("tcount", "1");
        page.put("pno", "1");
        page.put("tsize", "");
        pages.add(page);

        data.put("pages", pages);

        String dataStr = AuthenticationFactory.AesDecrypt(data.toJSONString(), TianChuangConstant.getAppSecret());

        HttpEntity httpEntity = new HttpEntity(dataStr, GetTianChuangToken.getHeaders(TianChuangConstant.SERVICE_ID_OWNER, dataStr));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(TianChuangConstant.getUrl(), HttpMethod.POST, httpEntity, String.class);
        logger.debug("调用HC IOT信息：" + responseEntity);
        JSONObject paramOut = JSONObject.parseObject(AuthenticationFactory.AesEncrypt(responseEntity.getBody(), TianChuangConstant.getAppSecret()));

        String code = paramOut.getJSONObject("sta").getString("cod");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.toJSONString());
        }
        String extTcOwnerId = paramOut.getJSONArray("data").getJSONObject(0).getString("result");
        OwnerAttrPo ownerAttrPo = new OwnerAttrPo();
        ownerAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        ownerAttrPo.setCommunityId(ownerDto.getCommunityId());
        ownerAttrPo.setMemberId(ownerDto.getMemberId());
        ownerAttrPo.setSpecCd(TianChuangConstant.EXT_TC_OWNER_ID);
        ownerAttrPo.setValue(extTcOwnerId);
        ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);

        return extTcOwnerId;
    }

    /**
     * 门禁信息上报
     *
     * @param machineDto
     * @return
     */
    private String sendMachine(MachineDto machineDto, String extCommunityId, CommunityDto communityDto) {
        JSONObject data = new JSONObject();
        JSONArray datas = new JSONArray();
        JSONObject dataObj = new JSONObject();
        dataObj.put("lv_mjmc", machineDto.getMachineName());
        dataObj.put("lv_wzms", machineDto.getLocationObjName());
        dataObj.put("lv_sfysxt", "1");
        dataObj.put("lv_ssxqbm", extCommunityId);
        dataObj.put("lv_mjxtwybm", machineDto.getMachineId());
        String qrCodeAddress = "";
        for (CommunityAttrDto communityAttrDto : communityDto.getCommunityAttrDtos()) {
            if (TianChuangConstant.EXT_TC_COMMUNITY_QRCODE.equals(communityAttrDto.getSpecCd())) {
                qrCodeAddress = communityAttrDto.getValue();
            }
        }
        dataObj.put("lv_dzewmbm", qrCodeAddress);
        dataObj.put("lv_sbxt", "小区管理系统");
        dataObj.put("lv_procmode", "PMINSERT");

        datas.add(dataObj);

        data.put("datas", datas);

        JSONArray pages = new JSONArray();
        JSONObject page = new JSONObject();
        page.put("psize", "100");
        page.put("tcount", "1");
        page.put("pno", "1");
        page.put("tsize", "");
        pages.add(page);

        data.put("pages", pages);

        String dataStr = AuthenticationFactory.AesDecrypt(data.toJSONString(), TianChuangConstant.getAppSecret());

        HttpEntity httpEntity = new HttpEntity(dataStr, GetTianChuangToken.getHeaders(TianChuangConstant.SERVICE_ID_MACHINE, dataStr));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(TianChuangConstant.getUrl(), HttpMethod.POST, httpEntity, String.class);
        logger.debug("调用HC IOT信息：" + responseEntity);
        JSONObject paramOut = JSONObject.parseObject(AuthenticationFactory.AesEncrypt(responseEntity.getBody(), TianChuangConstant.getAppSecret()));

        String code = paramOut.getJSONObject("sta").getString("cod");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.toJSONString());
        }
        String extTcMachineId = paramOut.getJSONArray("data").getJSONObject(0).getString("result");
        MachineAttrPo machineAttrPo = new MachineAttrPo();
        machineAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        machineAttrPo.setCommunityId(communityDto.getCommunityId());
        machineAttrPo.setMachineId(machineDto.getMachineId());
        machineAttrPo.setSpecCd(TianChuangConstant.EXT_TC_MACHINE_ID);
        machineAttrPo.setValue(extTcMachineId);
        machineAttrInnerServiceSMOImpl.saveMachineAttrs(machineAttrPo);

        return extTcMachineId;
    }

    /**
     * 发送小区代码
     *
     * @param communityDto
     */
    private String sendCommunity(CommunityDto communityDto) {
        JSONObject data = new JSONObject();
        JSONArray datas = new JSONArray();
        JSONObject dataObj = new JSONObject();
        dataObj.put("lv_mjbh", TianChuangConstant.getCompany());
        dataObj.put("lv_sbxqmc", communityDto.getName());
        dataObj.put("lv_zt", "0");
        dataObj.put("lv_dxtzhm", StringUtil.isEmpty(communityDto.getTel()) ? "18909711234" : communityDto.getTel());
        String qrCodeAddress = "";
        for (CommunityAttrDto communityAttrDto : communityDto.getCommunityAttrDtos()) {
            if (TianChuangConstant.EXT_TC_COMMUNITY_QRCODE.equals(communityAttrDto.getSpecCd())) {
                qrCodeAddress = communityAttrDto.getValue();
            }
        }

        dataObj.put("lv_sbxqdzbm", qrCodeAddress);
        dataObj.put("lv_procmode", "PMINSERT");

        datas.add(dataObj);

        data.put("datas", datas);

        JSONArray pages = new JSONArray();
        JSONObject page = new JSONObject();
        page.put("psize", "100");
        page.put("tcount", "1");
        page.put("pno", "1");
        page.put("tsize", "");
        pages.add(page);

        data.put("pages", pages);

        String dataStr = AuthenticationFactory.AesDecrypt(data.toJSONString(), TianChuangConstant.getAppSecret());

        HttpEntity httpEntity = new HttpEntity(dataStr, GetTianChuangToken.getHeaders(TianChuangConstant.SERVICE_ID_COMMUNITY, dataStr));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(TianChuangConstant.getUrl(), HttpMethod.POST, httpEntity, String.class);
        logger.debug("调用HC IOT信息：" + responseEntity);
        JSONObject paramOut = JSONObject.parseObject(AuthenticationFactory.AesEncrypt(responseEntity.getBody(), TianChuangConstant.getAppSecret()));

        String code = paramOut.getJSONObject("sta").getString("code");
        if (!"0000".equals(code)) {

            throw new IllegalArgumentException("同步小区失败，" + AuthenticationFactory.AesDecrypt(paramOut.getJSONObject("sta").getString("des"), TianChuangConstant.getAppSecret()));
        }
        String extTcCommunityId = paramOut.getJSONArray("data").getJSONObject(0).getString("result");
        CommunityAttrPo communityAttrPo = new CommunityAttrPo();
        communityAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        communityAttrPo.setCommunityId(communityDto.getCommunityId());
        communityAttrPo.setSpecCd(TianChuangConstant.EXT_TC_COMMUNITY_ID);
        communityAttrPo.setValue(extTcCommunityId);
        communityInnerServiceSMOImpl.saveCommunityAttr(communityAttrPo);

        return extTcCommunityId;
    }

}
