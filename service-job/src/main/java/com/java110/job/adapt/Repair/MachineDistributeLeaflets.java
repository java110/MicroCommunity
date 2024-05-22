package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachinePrinterDto;
import com.java110.dto.printerRule.PrinterRuleDto;
import com.java110.dto.printerRule.PrinterRuleMachineDto;
import com.java110.dto.printerRule.PrinterRuleRepairDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.job.printer.IPrinter;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
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


    @Autowired
    private IPrinterRuleRepairV1InnerServiceSMO printerRuleRepairV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleV1InnerServiceSMO printerRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleMachineV1InnerServiceSMO printerRuleMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMachinePrinterV1InnerServiceSMO machinePrinterV1InnerServiceSMOImpl;


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
        if (RepairUserDto.STATE_SUBMIT.equals(state)) {
            return;
        }
        //获取报修id
        String repairId = repairUserDtos.get(0).getRepairId();
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        IMsgNotify msgNotify = null;
        if (RepairSettingDto.NOTIFY_WAY_SMS.equals(repairDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
        } else if (RepairSettingDto.NOTIFY_WAY_WECHAT.equals(repairDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        } else {
            return;
        }

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
        //todo 转单
        if (RepairDto.STATE_TRANSFER.equals(repairState)) {
            JSONObject paramIn = new JSONObject();
            paramIn.put("repairName", repairName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("tel", tel);
            paramIn.put("context", context);
            paramIn.put("time", time);
            paramIn.put("staffId", businessRepairUser.getString("staffId"));
            paramIn.put("preStaffName", businessRepairUser.getString("preStaffName"));
            paramIn.put("repairId", repairId);
            //给维修师傅推送信息
            sendStaffMsg(paramIn, communityDtos.get(0), msgNotify);
            return;
        }
        //todo 不是结单跳过
        if (!RepairDto.STATE_TAKING.equals(repairState)) {
            return;
        }
        //todo 派单只打印小票
        if (RepairUserDto.STATE_DISPATCH.equals(businessRepairUser.getString("state"))) { //派单
            // 自动打印小票
            autoPrintRepair(repairUserDtos.get(0).getRuId(), repairDtos.get(0).getRepairType(), communityDtos.get(0));
            return;
        }
        JSONObject paramIn = new JSONObject();
        paramIn.put("staffId", businessRepairUser.getString("staffId"));
        paramIn.put("context", context);
        paramIn.put("time", time);
        paramIn.put("repairObjId", repairObjId);
        paramIn.put("preStaffId", businessRepairUser.getString("preStaffId"));
        paramIn.put("repairName", repairName);
        paramIn.put("tel", tel);
        paramIn.put("repairObjName", repairObjName);
        paramIn.put("repairId", repairId);
        //抢单成功给维修师傅推送信息
        sendStaffMsg(paramIn, communityDtos.get(0), msgNotify);
        //抢单成功给业主推送信息
        sendOwnerMsg(paramIn, communityDtos.get(0), msgNotify);
        //为企业微信群发消息
        sendMsgToWechatGroup(paramIn, communityDtos.get(0));
    }


    /**
     * 派单给维修师傅推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendStaffMsg(JSONObject paramIn, CommunityDto communityDto, IMsgNotify msgNotify) {
        JSONObject content = new JSONObject();
        content.put("repairId", paramIn.getString("repairId"));
        content.put("repairName", paramIn.getString("repairName"));
        content.put("tel", paramIn.getString("tel"));
        content.put("time", paramIn.getString("time"));
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        //获取具体位置
        String address = "";
        if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
            address = paramIn.getString("repairObjName");
        } else {
            address = communityDto.getName() + paramIn.getString("repairObjName");
        }
        content.put("address", address);
        msgNotify.sendDistributeRepairStaffMsg(communityDto.getCommunityId(), paramIn.getString("staffId"), content);
    }


    /**
     * 派单(抢单)成功后给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendOwnerMsg(JSONObject paramIn, CommunityDto communityDto, IMsgNotify msgNotify) {
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
        if (StringUtil.isEmpty(preStaffId)) {
            return;
        }

        JSONObject content = new JSONObject();
        content.put("name", name);
        content.put("tel", tel);
        content.put("time", paramIn.getString("time"));
        String wechatUrl = UrlCache.getOwnerUrl();
        if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
            wechatUrl += ("&wAppId=" + smallWeChatDtos.get(0).getAppId());
        } else {
            wechatUrl += ("?wAppId=" + smallWeChatDtos.get(0).getAppId());
        }
        content.put("url", wechatUrl);

        msgNotify.sendDistributeRepairOwnerMsg(communityDto.getCommunityId(), preStaffId, content);

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

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
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
     * // 自动打印小票
     *
     * @param ruId
     * @param repairType
     * @param communityDto
     */
    private void autoPrintRepair(String ruId, String repairType, CommunityDto communityDto) {

        PrinterRuleRepairDto printerRuleRepairDto = new PrinterRuleRepairDto();
        printerRuleRepairDto.setCommunityId(communityDto.getCommunityId());
        printerRuleRepairDto.setRepairType(repairType);
        List<PrinterRuleRepairDto> printerRuleRepairDtos = printerRuleRepairV1InnerServiceSMOImpl.queryPrinterRuleRepairs(printerRuleRepairDto);

        if (printerRuleRepairDtos == null || printerRuleRepairDtos.size() < 1) {
            return;
        }

        PrinterRuleDto printerRuleDto = new PrinterRuleDto();
        printerRuleDto.setRuleId(printerRuleRepairDtos.get(0).getRuleId());
        printerRuleDto.setCommunityId(communityDto.getCommunityId());
        printerRuleDto.setState(PrinterRuleDto.STATE_NORMAL);
        int count = printerRuleV1InnerServiceSMOImpl.queryPrinterRulesCount(printerRuleDto);

        if (count < 1) {
            return;
        }

        PrinterRuleMachineDto printerRuleMachineDto = new PrinterRuleMachineDto();
        printerRuleMachineDto.setCommunityId(communityDto.getCommunityId());
        printerRuleMachineDto.setRuleId(printerRuleRepairDtos.get(0).getRuleId());
        List<PrinterRuleMachineDto> printerRuleMachineDtos = printerRuleMachineV1InnerServiceSMOImpl.queryPrinterRuleMachines(printerRuleMachineDto);
        if (printerRuleMachineDtos == null || printerRuleMachineDtos.size() < 1) {
            return;
        }

        for (PrinterRuleMachineDto tmpPrinterRuleMachineDto : printerRuleMachineDtos) {
            try {
                doPrint(tmpPrinterRuleMachineDto, ruId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void doPrint(PrinterRuleMachineDto tmpPrinterRuleMachineDto, String ruId) {
        MachinePrinterDto machinePrinterDto = new MachinePrinterDto();
        machinePrinterDto.setCommunityId(tmpPrinterRuleMachineDto.getCommunityId());
        machinePrinterDto.setMachineId(tmpPrinterRuleMachineDto.getMachineId());
        List<MachinePrinterDto> machinePrinterDtos = machinePrinterV1InnerServiceSMOImpl.queryMachinePrinters(machinePrinterDto);

        Assert.listOnlyOne(machinePrinterDtos, "云打印机不存在");

        IPrinter printer = ApplicationContextFactory.getBean(machinePrinterDtos.get(0).getImplBean(), IPrinter.class);

        if (printer == null) {
            throw new CmdException("打印机异常，未包含适配器");
        }

        printer.printRepair(ruId, tmpPrinterRuleMachineDto.getCommunityId(), Integer.parseInt(tmpPrinterRuleMachineDto.getQuantity()), machinePrinterDtos.get(0));

    }
}
