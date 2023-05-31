package com.java110.store.cmd.reserveOrder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunitySpacePersonTimeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.reserve.ReserveGoodsConfirmOrderDto;
import com.java110.dto.reserve.ReserveGoodsDto;
import com.java110.dto.reserve.ReserveGoodsOrderDto;
import com.java110.dto.reserve.ReserveGoodsOrderTimeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IReserveGoodsConfirmOrderV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderTimeV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.reserveGoodsConfirmOrder.ReserveGoodsConfirmOrderPo;
import com.java110.po.reserveGoodsOrder.ReserveGoodsOrderPo;
import com.java110.po.reserveGoodsOrderTime.ReserveGoodsOrderTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 就餐 处理类
 */
@Java110Cmd(serviceCode = "reserveOrder.doDining")
public class DoDiningCmd extends Cmd {
    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReserveGoodsOrderV1InnerServiceSMO reserveGoodsOrderV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderTimeV1InnerServiceSMO reserveGoodsOrderTimeV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsV1InnerServiceSMO reserveGoodsV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsConfirmOrderV1InnerServiceSMO reserveGoodsConfirmOrderV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "qrCode", "未包含二维码");
        Assert.hasKeyAndValue(reqJson, "goodsId", "未包含商品");

        String userId = userV1InnerServiceSMOImpl.getUserIdByQrCode(reqJson.getString("qrCode"));

        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("二维码过期");
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        // todo 判断用户是否为这个小区业主
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setLink(userDtos.get(0).getTel());
        int count = ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);
        if (count < 1) {
            throw new CmdException("业主不存在");
        }
        reqJson.put("userId", userId);

        ReserveGoodsDto reserveGoodsDto = new ReserveGoodsDto();
        reserveGoodsDto.setGoodsId(reqJson.getString("goodsId"));
        List<ReserveGoodsDto> reserveGoodsDtos = reserveGoodsV1InnerServiceSMOImpl.queryReserveGoodss(reserveGoodsDto);
        Assert.listOnlyOne(reserveGoodsDtos, "就餐不存在");

        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = null;
        int flag = 0;
        int quantity = 0;
        Calendar calendar = Calendar.getInstance();
        reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
        reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderTimeDto.setAppointmentTime(DateUtil.getFormatTimeStringB(DateUtil.getCurrentDate()));
        reserveGoodsOrderTimeDto.setHours(calendar.get(Calendar.HOUR) + "");
        reserveGoodsOrderTimeDto.setGoodsId(reqJson.getString("goodsId"));
        reserveGoodsOrderTimeDto.setPersonTel(userDtos.get(0).getTel());
        flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimesCount(reserveGoodsOrderTimeDto);
        if (flag > 0) {
            throw new CmdException(reserveGoodsOrderTimeDto.getAppointmentTime() + "," + reserveGoodsOrderTimeDto.getHours() + "已经就餐");
        }

        reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
        reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderTimeDto.setAppointmentTime(DateUtil.getFormatTimeStringB(DateUtil.getCurrentDate()));
        reserveGoodsOrderTimeDto.setGoodsId(reqJson.getString("goodsId"));
        quantity = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimesCount(reserveGoodsOrderTimeDto);

        if (quantity > Integer.parseInt(reserveGoodsDtos.get(0).getHoursMaxQuantity())) {
            throw new CmdException("就餐数量超过设定数量");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
//校验是否可以预约
        ReserveGoodsDto reserveGoodsDto = new ReserveGoodsDto();
        reserveGoodsDto.setGoodsId(reqJson.getString("goodsId"));
        List<ReserveGoodsDto> reserveGoodsDtos = reserveGoodsV1InnerServiceSMOImpl.queryReserveGoodss(reserveGoodsDto);
        Assert.listOnlyOne(reserveGoodsDtos, "就餐不存在");


        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");


        ReserveGoodsOrderPo reserveGoodsOrderPo = new ReserveGoodsOrderPo();
        reserveGoodsOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        reserveGoodsOrderPo.setGoodsId(reqJson.getString("goodsId"));
        reserveGoodsOrderPo.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderPo.setAppointmentTime(DateUtil.getFormatTimeStringB(DateUtil.getCurrentDate()));
        reserveGoodsOrderPo.setPayWay("1"); // 现金支付
        reserveGoodsOrderPo.setPersonId(userDtos.get(0).getUserId());
        reserveGoodsOrderPo.setPersonName(userDtos.get(0).getName());
        reserveGoodsOrderPo.setPersonTel(userDtos.get(0).getTel());
        reserveGoodsOrderPo.setReceivableAmount(reserveGoodsDtos.get(0).getPrice());
        reserveGoodsOrderPo.setReceivedAmount("0");
        reserveGoodsOrderPo.setType(reserveGoodsDtos.get(0).getType());
        if (StringUtil.isEmpty(reserveGoodsOrderPo.getExtOrderId())) {
            reserveGoodsOrderPo.setExtOrderId("-1");
        }

        reserveGoodsOrderPo.setState(ReserveGoodsOrderDto.STATE_W);

        int flag = reserveGoodsOrderV1InnerServiceSMOImpl.saveReserveGoodsOrder(reserveGoodsOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //预约时间
        ReserveGoodsOrderTimePo reserveGoodsOrderTimePo = null;
        reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
        reserveGoodsOrderTimePo.setCommunityId(reserveGoodsOrderPo.getCommunityId());
        reserveGoodsOrderTimePo.setGoodsId(reserveGoodsOrderPo.getGoodsId());
        reserveGoodsOrderTimePo.setOrderId(reserveGoodsOrderPo.getOrderId());
        Calendar calendar = Calendar.getInstance();
        reserveGoodsOrderTimePo.setHours(calendar.get(Calendar.HOUR) + "");
        reserveGoodsOrderTimePo.setQuantity("1");
        reserveGoodsOrderTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        reserveGoodsOrderTimePo.setState(ReserveGoodsOrderTimeDto.STATE_WAIT_CONFIRM);
        reserveGoodsOrderTimeV1InnerServiceSMOImpl.saveReserveGoodsOrderTime(reserveGoodsOrderTimePo);

        // todo 核销预约
        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
        reserveGoodsOrderTimeDto.setTimeId(reserveGoodsOrderTimePo.getTimeId());
        reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderTimeDto.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
        List<ReserveGoodsOrderTimeDto> reserveGoodsOrderTimeDtos = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimes(reserveGoodsOrderTimeDto);

        Assert.listOnlyOne(reserveGoodsOrderTimeDtos, "未包含预约记录");

        ReserveGoodsOrderDto reserveGoodsOrderDto = new ReserveGoodsOrderDto();
        reserveGoodsOrderDto.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        List<ReserveGoodsOrderDto> reserveGoodsOrderDtos = reserveGoodsOrderV1InnerServiceSMOImpl.queryReserveGoodsOrders(reserveGoodsOrderDto);
        Assert.listOnlyOne(reserveGoodsOrderDtos, "预约订单不存在");

        //todo 将 时间修改 核销中
        reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
        reserveGoodsOrderTimePo.setTimeId(reserveGoodsOrderTimeDtos.get(0).getTimeId());
        reserveGoodsOrderTimePo.setState(CommunitySpacePersonTimeDto.STATE_FINISH);
        flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.updateReserveGoodsOrderTime(reserveGoodsOrderTimePo);
        if (flag < 1) {
            throw new CmdException("核销预约失败");
        }

        ReserveGoodsConfirmOrderPo reserveGoodsConfirmOrderPo = new ReserveGoodsConfirmOrderPo();
        reserveGoodsConfirmOrderPo.setCoId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        reserveGoodsConfirmOrderPo.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        reserveGoodsConfirmOrderPo.setType(reserveGoodsOrderDtos.get(0).getType());
        reserveGoodsConfirmOrderPo.setGoodsId(reserveGoodsOrderDtos.get(0).getGoodsId());
        reserveGoodsConfirmOrderPo.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsConfirmOrderPo.setTimeId(reserveGoodsOrderTimePo.getTimeId());
        flag = reserveGoodsConfirmOrderV1InnerServiceSMOImpl.saveReserveGoodsConfirmOrder(reserveGoodsConfirmOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
