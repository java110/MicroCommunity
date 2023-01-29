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
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    protected static MessageDigest messageDigest = null;
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
        String name = machineRecordPo.getName();

        //身份证为空时 ，门禁记录没法传 所以就不传了
        if (StringUtil.isEmpty(name)) {
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(machineRecordPo.getName());
        ownerDto.setCommunityId(machineRecordPo.getCommunityId());
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
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
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
        String json = encryptStr(data.toJSONString(), TianChuangConstant.getAppSecret());
        String result = httpPOST2(json, TianChuangConstant.SERVICE_ID_PERSON_INOUT);
        JSONObject paramOut = JSONObject.parseObject(result);

        String code = paramOut.getJSONObject("sta").getString("code");
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
        dataObj.put("lvdjsj", DateUtil.getFormatTimeString(ownerDto.getCreateTime(),"yyyyMMdd HH:mm:ss"));
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
        logger.debug("发送住戶信息加密前，{}", data.toJSONString());
        String json = encryptStr(data.toJSONString(), TianChuangConstant.getAppSecret());
        String result = httpPOST2(json, TianChuangConstant.SERVICE_ID_OWNER);
        JSONObject paramOut = JSONObject.parseObject(result);

        String code = paramOut.getJSONObject("sta").getString("code");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.toJSONString());
        }
        String extTcOwnerId = paramOut.getJSONArray("datas").getJSONObject(0).getString("Result");
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
        String json = encryptStr(data.toJSONString(), TianChuangConstant.getAppSecret());
        String result = httpPOST2(json, TianChuangConstant.SERVICE_ID_MACHINE);
        JSONObject paramOut = JSONObject.parseObject(result);

        String code = paramOut.getJSONObject("sta").getString("code");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.toJSONString());
        }
        String extTcMachineId = paramOut.getJSONArray("datas").getJSONObject(0).getString("Result");
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
        logger.debug("发送小区加密前，{}", data.toJSONString());
        String json = encryptStr(data.toJSONString(), TianChuangConstant.getAppSecret());
        String result = httpPOST2(json, TianChuangConstant.SERVICE_ID_COMMUNITY);
        JSONObject paramOut = JSONObject.parseObject(result);
        String code = paramOut.getJSONObject("sta").getString("code");
        if (!"0000".equals(code)) {
            throw new IllegalArgumentException("同步小区失败，" + paramOut.getJSONObject("sta").getString("des"));
        }
        String extTcCommunityId = paramOut.getJSONArray("datas").getJSONObject(0).getString("Result");
        CommunityAttrPo communityAttrPo = new CommunityAttrPo();
        communityAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        communityAttrPo.setCommunityId(communityDto.getCommunityId());
        communityAttrPo.setSpecCd(TianChuangConstant.EXT_TC_COMMUNITY_ID);
        communityAttrPo.setValue(extTcCommunityId);
        communityInnerServiceSMOImpl.saveCommunityAttr(communityAttrPo);

        return extTcCommunityId;
    }


    /**
     * 字符串加密
     *
     * @param srcStr   加密字符串
     * @param password 加密密钥
     */
    public static String encryptStr(String srcStr, String password) {
        byte[] encryptResult = encryptData_AES(srcStr, password);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    private static byte[] encryptData_AES(String content, String password) {
        try {
            //SecretKey secretKey = getKey(password);
            //byte[] enCodeFormat = secretKey.getEncoded();
            byte[] enCodeFormat = parseHexStr2Byte(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 字符串解密
     *
     * @param srcStr   解密字符串
     * @param password 加密密钥
     */
    public static String decryptStr(String srcStr, String password) {
        String returnValue = "";
        try {
            byte[] decryptFrom = parseHexStr2Byte(srcStr);
            byte[] decryptResult = decryptData_AES(decryptFrom, password);
            returnValue = new String(decryptResult, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }


    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 生成指定字符串的密钥
     *
     * @param secret 要生成密钥的字符
     * @return secretKey 生成后的密钥
     * @throws GeneralSecurityException
     */
    public static SecretKey getKey(String secret) throws GeneralSecurityException {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secret.getBytes());
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("初始化密钥出现异常");
        }
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    private static byte[] decryptData_AES(byte[] content, String password) {
        try {
            SecretKey secretKey = getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            return str;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public static String httpPOST2(String json, String serviceId) {

        String url = TianChuangConstant.getUrl();
        String appId = TianChuangConstant.getAppId();
        String appSecret = TianChuangConstant.getAppSecret();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("appid", appId);
        String currdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String token = md5(appId + appSecret + currdate + json.replaceAll("\r\n", ""));
        System.out.println("token:" + token);

        post.setHeader("token", token);
        post.setHeader("tranId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_tranId));
        post.setHeader("serviceId", serviceId);
        post.setHeader("serviceValue", serviceId);
        post.setHeader("versionCode", "1.0");


        String body = "";

        logger.debug("请求报文={}", json);
        try {
            StringEntity s = new StringEntity(json, "UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity, "utf-8");
            }
            body = URLDecoder.decode(body, "utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        logger.debug("返回报文={}", body);

        return body;
    }

    public static String httpPOST(String url, String json) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        String body = "";

        try {
            StringEntity s = new StringEntity(json, "UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity, "utf-8");
            }
            return URLDecoder.decode(body, "utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private static final String SECRETKEY = "CAD3218426800B9FE0532D03A8C0310E";

    public static void main(String[] args) throws Exception {
        //String json = "{\"datas\":[{\"gmsfhm\":\"370122197507084821\",\"name\":\"李冰\"}],\"pages\":{\"psize\":\"10\",\"tcount\":\"\",\"pno\":\"1\",\"tsize\":\"\"}}";
        // 加密
        //String encryptResultStr = encryptStr(json, SECRETKEY);
        //String a = "CB42C3F519BC831961E4CE27AA00FA6DEF0E494A467187F77E01CDC8FC4605295FD1BED29065B244E4C2B1AB86D0BCE7A5914F2DF70C5360F2C58D9B2B9DC32D16EF3CB68AD1A7BEC820D690763511E57A55B13D4F0558BA64A14B2DEB3B5C762CC84D88DF60F223A467FF53615D9B7DF0BC32A20F96637D425F82A4CDD7A311";
        //System.out.println("加密后1：" + encryptResultStr);
        // System.out.println("加密后2：" +a);
        // System.out.println("加密后3：CB42C3F519BC831961E4CE27AA00FA6DEF0E494A467187F77E01CDC8FC4605295FD1BED29065B244E4C2B1AB86D0BCE7A5914F2DF70C5360F2C58D9B2B9DC32D16EF3CB68AD1A7BEC820D690763511E57A55B13D4F0558BA64A14B2DEB3B5C762CC84D88DF60F223A467FF53615D9B7D71A92761E9E455F104519DF3AFA4E5F2");
        // 解密
        String encryptResultStr = "%E8%AF%B7%E6%B1%82%E4%B8%8D%E5%90%88%E6%B3%95%EF%BC%8C%E5%8C%85%E4%BD%93%E6%A0%BC%E5%BC%8F%E9%94%99%E8%AF%AF";
        String decryptResultStr = decryptStr(encryptResultStr, SECRETKEY);
        System.out.println("解密后：" + decryptResultStr);

        //String decryptResultStr = decryptStr(a, "60F10B41F44BD4856C0C1952BF40690C");
        //System.out.println(decryptResultStr);

        //	System.out.println(getKey("9A1158154DFA42CADDBD0694A4E9BDC8"));

    }

}
