package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.ImageUtils;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 派单(抢单、转单)通知适配器
 *
 * @author fqz
 * @date 2021-01-13 13:55
 */
@Component(value = "machineDistributeLeaflets")
public class MachineDistributeLeaflets extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineDistributeLeaflets.class);

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessRepairUsers = new JSONArray();
        System.out.println("收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        if (data.containsKey(RepairUserPo.class.getSimpleName())) {
            Object bObj = data.get(RepairUserPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessRepairUsers.add(bObj);
            } else if (bObj instanceof List) {
                businessRepairUsers = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessRepairUsers = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessRepairUsers.add(data);
            }
        }
        for (int bOwnerRepairIndex = 0; bOwnerRepairIndex < businessRepairUsers.size(); bOwnerRepairIndex++) {
            JSONObject businessRepairUser = businessRepairUsers.getJSONObject(bOwnerRepairIndex);
            doDealOwnerRepair(businesses, businessRepairUser);
        }
    }

    private void doDealOwnerRepair(List<Business> businesses, JSONObject businessRepairUser) {
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRuId(businessRepairUser.getString("ruId"));
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
        //获取员工处理状态(10001 处理中；10002 结单；10003 退单；10004 转单；10005 提交；10006 已派单；10007 已评价；10008 已回访；10009 待支付；11000 待评价；12000 已支付；12001 暂停)
        String state = repairUserDtos.get(0).getState();
        if (!state.equals("10005")) {
            //获取报修id
            String repairId = repairUserDtos.get(0).getRepairId();
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(repairId);
            List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
            //查询报修状态(1000 未派单；1100 接单；1200 退单；1300 转单；1400 申请支付；1500 支付失败；1700 待评价；1800 电话回访；1900 办理完成；2000 未办理结单)
            String repairState = repairDtos.get(0).getState();
            //获取联系人姓名
            String repairName = repairDtos.get(0).getRepairName();
            //获取联系人电话
            String tel = repairDtos.get(0).getTel();
            //获取位置信息
            String repairObjName = repairDtos.get(0).getRepairObjName();
            //报修对象ID
            String repairObjId = repairDtos.get(0).getRepairObjId();
            //获取报修内容
            String context = repairDtos.get(0).getContext();
            //获取派单时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(repairUserDtos.get(0).getCreateTime());
            //获取小区id
            String communityId = repairDtos.get(0).getCommunityId();
            //查询小区信息
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(communityId);
            List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
            if (repairState.equals("1100") && businessRepairUser.getString("state").equals("10006")) { //派单
                JSONObject paramIn = new JSONObject();
                for (Business business : businesses) {
                    String businessTypeCd = business.getBusinessTypeCd();
                    if (!StringUtil.isEmpty(businessTypeCd) && businessTypeCd.equals("130200030001")) {
                        JSONObject data = business.getData();
                        if (!StringUtil.isEmpty(data.getString("state")) && data.getString("state").equals("10001")) {
                            paramIn.put("staffId", data.getString("staffId"));
                            paramIn.put("staffName", data.getString("staffName"));
                        } else if (data.getString("state").equals("10006")) {
                            paramIn.put("preStaffId", data.getString("preStaffId"));
                            paramIn.put("preStaffName", data.getString("preStaffName"));
                        }
                    }
                }
                paramIn.put("repairName", repairName);
                paramIn.put("repairObjName", repairObjName);
                paramIn.put("tel", tel);
                paramIn.put("communityId", communityId);
                paramIn.put("context", context);
                paramIn.put("time", time);
                paramIn.put("repairObjId", repairObjId);
                paramIn.put("repairId", repairId);
                //给维修师傅推送信息
                sendMsg(paramIn, communityDtos.get(0));
                //派单成功给业主推送信息
                publishMsg(paramIn, communityDtos.get(0));
                //为企业微信群发消息
                sendMsgToWechatGroup(paramIn, communityDtos.get(0));
            } else if (repairState.equals("1100") && !businessRepairUser.getString("state").equals("10006")) {
                JSONObject paramIn = new JSONObject();
                paramIn.put("staffId", businessRepairUser.getString("staffId"));
                paramIn.put("context", context);
                paramIn.put("time", time);
                paramIn.put("repairObjId", repairObjId);
                paramIn.put("preStaffId", businessRepairUser.getString("preStaffId"));
                paramIn.put("repairName", repairName);
                paramIn.put("tel", tel);
                paramIn.put("repairObjName", repairObjName);
                //抢单成功给维修师傅推送信息
                publishMessage(paramIn, communityDtos.get(0));
                //抢单成功给业主推送信息
                publishMsg(paramIn, communityDtos.get(0));
            } else if (repairState.equals("1300")) {   //转单
                JSONObject paramIn = new JSONObject();
                paramIn.put("repairName", repairName);
                paramIn.put("repairObjName", repairObjName);
                paramIn.put("tel", tel);
                paramIn.put("context", context);
                paramIn.put("time", time);
                paramIn.put("staffId", businessRepairUser.getString("staffId"));
                paramIn.put("preStaffName", businessRepairUser.getString("preStaffName"));
                //给维修师傅推送信息
                sendMessage(paramIn, communityDtos.get(0));
            }
        }
    }

    /**
     * 转单给维修师傅推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        String url = sendMsgUrl + accessToken;
        //根据 userId 查询到openId
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(paramIn.getString("staffId"));
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos.size() > 0) {
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("您有新的维修任务，维修信息如下："));
            data.setKeyword1(new Content(paramIn.getString("repairName")));
            data.setKeyword2(new Content(paramIn.getString("tel")));
            data.setKeyword3(new Content(paramIn.getString("time")));
            data.setKeyword4(new Content(paramIn.getString("context") + "\r\n" + "报修位置：" + paramIn.getString("repairObjName")));
            data.setRemark(new Content(paramIn.getString("preStaffName") + "转单给您，请及时登录公众号接单确认！"));
            templateMessage.setData(data);
            //获取员工公众号地址
            String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }

    /**
     * 派单给维修师傅推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        //获取具体位置
        String address = "";
        if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
            address = paramIn.getString("repairObjName");
        } else {
            address = communityDto.getName() + paramIn.getString("repairObjName");
        }
        String url = sendMsgUrl + accessToken;
        //根据 userId 查询到openId
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(paramIn.getString("staffId"));
        staffAppAuthDto.setStaffName(paramIn.getString("staffName"));
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos.size() > 0) {
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("您有新的维修任务，维修信息如下："));
            data.setKeyword1(new Content(paramIn.getString("repairName")));
            data.setKeyword2(new Content(paramIn.getString("tel")));
            data.setKeyword3(new Content(paramIn.getString("time")));
            data.setKeyword4(new Content(paramIn.getString("context") + "\r\n" + "报修位置：" + address));
            data.setRemark(new Content("请及时登录公众号接单确认！"));
            templateMessage.setData(data);
            //获取员工公众号地址
            String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }


    private void sendMsgToWechatGroup(JSONObject paramIn, CommunityDto communityDto) {

        //查询公众号配置
        String url = CommunitySettingFactory.getRemark(communityDto.getCommunityId(), "WECHAT_SEND_REPAIR_URL");
        if (StringUtil.isEmpty(url)) {
            return;
        }

        JSONObject rebootParam = new JSONObject();
        rebootParam.put("msgtype", "markdown");
        JSONObject rebootMarkdown = new JSONObject();
        rebootParam.put("markdown", rebootMarkdown);


        //获取具体位置
        String address = "";
        if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
            address = paramIn.getString("repairObjName");
        } else {
            address = communityDto.getName() + paramIn.getString("repairObjName");
        }

        //根据 userId 查询到openId
        UserDto userDto = new UserDto();
        userDto.setUserId(paramIn.getString("staffId"));
        List<UserDto> userDtos = userInnerServiceSMO.getUsers(userDto);
        String staffName = "";
        if (userDtos != null && userDtos.size() > 0) {
            staffName = userDtos.get(0).getName();
        }
        String content = staffName + " 您有新的维修任务，维修信息如下：\n";

        content += ("> 标题：<font color=\"comment\">" + paramIn.getString("repairName") + "</font> \n");
        content += ("> 电话：<font color=\"comment\">" + paramIn.getString("tel") + "</font> \n");
        content += ("> 时间：<font color=\"comment\">" + paramIn.getString("time") + "</font> \n");
        content += ("> 内容：<font color=\"comment\">" + paramIn.getString("context") + "</font> \n");
        content += ("> 位置：<font color=\"comment\">" + address + "</font> \n");
        content += ("> 单号：<font color=\"comment\">" + paramIn.getString("repairId") + "</font> \n");

        rebootMarkdown.put("content", content);
        logger.info("发送消息内容:{}", content);
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, rebootParam.toJSONString(), String.class);
        logger.info("企业微信返回内容:{}", responseEntity);


        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        String imgUrl = MappingCache.getValue("IMG_PATH");
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(paramIn.getString("repairId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return;
        }

        rebootParam = JSONObject.parseObject(" {\n" +
                "            \"msgtype\":\"image\",\n" +
                "            \"image\":{\n" +
                "              }\n" +
                "        }");


        JSONObject image = rebootParam.getJSONObject("image");

        String imageUrl = "";
        for (FileRelDto tmpFileRelDto : fileRelDtos) {

            if (!tmpFileRelDto.getRelTypeCd().equals(FileRelDto.REL_TYPE_CD_REPAIR)) {  //维修图片
                continue;
            }
            imageUrl = imgUrl + tmpFileRelDto.getFileRealName();
            image.put("base64", ImageUtils.getBase64ByImgUrl(imageUrl));
            image.put("md5", ImageUtils.getMd5ByImgUrl(imageUrl));
            responseEntity = outRestTemplate.postForEntity(url, rebootParam.toJSONString(), String.class);
            logger.debug("返回信息：" + responseEntity);
        }

    }


    /**
     * 派单(抢单)成功后给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_SCHEDULE_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        //查询维修员工信息
        UserDto userDto = new UserDto();
        userDto.setUserId(paramIn.getString("staffId"));
        userDto.setStatusCd("0");
        List<UserDto> users = userInnerServiceSMO.getUsers(userDto);
        //获取维修员工姓名
        String name = users.get(0).getName();
        //获取维修员工联系方式
        String tel = users.get(0).getTel();
        //获取用户id
        String preStaffId = paramIn.getString("preStaffId");
        if (!StringUtil.isEmpty(preStaffId)) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setUserId(preStaffId);
            ownerAppUserDto.setAppType("WECHAT");
            //查询绑定业主
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
            if (ownerAppUserDtos.size() > 0) {
                //获取openId
                String openId = ownerAppUserDtos.get(0).getOpenId();
                String url = sendMsgUrl + accessToken;
                Data data = new Data();
                PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                templateMessage.setTemplate_id(templateId);
                templateMessage.setTouser(openId);
                data.setFirst(new Content("您的报修受理成功，维修人员信息如下："));
                data.setKeyword1(new Content(paramIn.getString("context")));
                data.setKeyword2(new Content(name));
                data.setKeyword3(new Content(tel));
                data.setKeyword4(new Content(paramIn.getString("time")));
                data.setRemark(new Content("您的报修已受理，请保持电话畅通，以便维修人员及时跟您取得联系！感谢您的使用！"));
                templateMessage.setData(data);
                //获取业主公众号地址
                String wechatUrl = MappingCache.getValue("OWNER_WECHAT_URL");
                if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
                    wechatUrl += ("&wAppId=" + weChatDto.getAppId());
                } else {
                    wechatUrl += ("?wAppId=" + weChatDto.getAppId());
                }

                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }

    /**
     * 抢单成功后给维修师傅推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishMessage(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        //获取具体位置
        String address = "";
        if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
            address = paramIn.getString("repairObjName");
        } else {
            address = communityDto.getName() + paramIn.getString("repairObjName");
        }
        //根据 userId 查询到openId
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(paramIn.getString("staffId"));
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        String openId = staffAppAuthDtos.get(0).getOpenId();
        String url = sendMsgUrl + accessToken;
        Data data = new Data();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.setFirst(new Content("恭喜您抢单成功，报修信息如下："));
        data.setKeyword1(new Content(paramIn.getString("repairName")));
        data.setKeyword2(new Content(paramIn.getString("tel")));
        data.setKeyword3(new Content(paramIn.getString("time")));
        data.setKeyword4(new Content(paramIn.getString("context") + "\r\n" + "报修位置：" + address));
        data.setRemark(new Content("请及时与客户取得联系！"));
        templateMessage.setData(data);
        //获取员工公众号地址
        String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);
    }
}
