package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.ValidatorUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

/**
 * 向业主发送验证码
 */
@Java110Cmd(serviceCode = "user.ownerSendSms")
public class OwnerSendSmsCmd extends Cmd {

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    public static final String ID_CARD_SWITCH = "ID_CARD_SWITCH";

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    /**
     * communityId:this.communityId,
     * queryWay:this.queryWay,
     * roomNum: this.roomNum,
     * link: this.link,
     *
     * @param event   事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


//super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "queryWay", "未包含查询方式");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

        if ("room".equals(reqJson.getString("queryWay"))) {
            Assert.hasKeyAndValue(reqJson, "roomNum", "未包含房屋信息");
        } else {
            Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
            if (!ValidatorUtil.isMobile(reqJson.getString("link"))) {
                throw new IllegalArgumentException("手机号格式错误");
            }
        }

        //todo 计算业主手机号
        String link = computeOwnerLink(reqJson);

        reqJson.put("tel", link);


        //校验是否有有效的验证码
        String smsCode = CommonCache.getValue(reqJson.getString("tel") + SendSmsFactory.VALIDATE_CODE);

        if (!StringUtil.isEmpty(smsCode) && smsCode.contains("-")) {
            long oldTime = Long.parseLong(smsCode.substring(smsCode.indexOf("-"), smsCode.length()));
            long nowTime = new Date().getTime();
            if (nowTime - oldTime < 1000 * 60 * 2) {
                throw new IllegalArgumentException("请不要重复发送验证码");
            }
        }
    }

    /**
     * 计算业主手机号
     *
     * @param reqJson
     * @return
     */
    private String computeOwnerLink(JSONObject reqJson) {

        String link = "";
        if ("phone".equals(reqJson.getString("queryWay"))) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(reqJson.getString("link"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if (ownerDtos == null || ownerDtos.size() < 1) {
                throw new CmdException("业主不存在");
            }
            link = reqJson.getString("link");
            return link;
        }
        String roomNumStr = reqJson.getString("roomNum");
        String[] roomNums = roomNumStr.split("-", 3);
        if (roomNums == null || roomNums.length != 3) {
            throw new CmdException("业主不存在");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(roomNums[0]);
        roomDto.setUnitNum(roomNums[1]);
        roomDto.setRoomNum(roomNums[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            throw new CmdException("业主不存在");
        }

        return ownerDtos.get(0).getLink();
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String tel = reqJson.getString("tel");
        String captchaType = reqJson.getString("captchaType");
        //校验是否传了 分页信息
        String msgCode = SendSmsFactory.generateMessageCode(6);
        SmsDto smsDto = new SmsDto();
        smsDto.setTel(tel);
        smsDto.setCode(msgCode);
        if ("ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN, SendSmsFactory.SMS_SEND_SWITCH))) {
            smsDto = smsInnerServiceSMOImpl.send(smsDto);
            smsDto.setMsg("向业主手机(尾号" + tel.substring(7, tel.length()) + ")下发验证码");
        } else {
            CommonCache.setValue(smsDto.getTel() + SendSmsFactory.VALIDATE_CODE, smsDto.getCode().toLowerCase() + "-" + new Date().getTime(), CommonCache.defaultExpireTime);
            smsDto.setSuccess(true);
            smsDto.setMsg("您的验证码为" + msgCode);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(smsDto.isSuccess() ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, smsDto.getMsg()));

    }
}
