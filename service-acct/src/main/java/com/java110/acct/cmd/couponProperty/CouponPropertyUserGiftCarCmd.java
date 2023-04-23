package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.couponPool.CouponPropertyPoolConfigDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.parkingCoupon.ParkingCouponCarDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
import com.java110.po.couponPropertyUserDetail.CouponPropertyUserDetailPo;
import com.java110.po.parkingCouponCar.ParkingCouponCarPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110CmdDoc(title = "优惠券赠送车辆",
        description = "业主缴费获取的优惠券赠送车辆免费停车",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/couponProperty.couponPropertyUserGiftCar",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "couponProperty.couponPropertyUserGiftCar"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "carNum", length = 12, remark = "赠送车辆"),
        @Java110ParamDoc(name = "couponId", length = 30, remark = "赠送优惠券"),
        @Java110ParamDoc(name = "giftCount", type = "int", length = 11, remark = "赠送数量"),
        @Java110ParamDoc(name = "paId", length = 30, remark = "停车场"),


})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/parkingArea.listParkingAreas?num=&typeCd=&paId=&page=1&row=10&communityId=2022112555490011",
        resBody = "{\"page\":0,\"parkingAreas\":[{\"attrs\":[{\"attrId\":\"112022112796270047\",\"communityId\":\"2022112555490011\",\"listShow\":\"Y\",\"paId\":\"102022112706900045\",\"page\":-1,\"records\":0,\"row\":0,\"specCd\":\"6185-17861\",\"specName\":\"外部编码\",\"specType\":\"2233\",\"statusCd\":\"0\",\"total\":0,\"value\":\"123\"}],\"createTime\":\"2022-11-27 01:48:27\",\"num\":\"A\",\"paId\":\"102022112706900045\",\"remark\":\"\",\"typeCd\":\"1001\"}],\"records\":1,\"rows\":0,\"total\":1}"
)
@Java110Cmd(serviceCode = "couponProperty.couponPropertyUserGiftCar")
public class CouponPropertyUserGiftCarCmd extends Cmd {

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyUserDetailV1InnerServiceSMO couponPropertyUserDetailV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "carNum", "未包含车辆");
        Assert.hasKeyAndValue(reqJson, "couponId", "未包含停车券");
        Assert.hasKeyAndValue(reqJson, "giftCount", "未包含赠送数量");
        Assert.hasKeyAndValue(reqJson, "paId", "未包含停车场");


        String userId = context.getReqHeaders().get("user-id");

        //前端车牌号输入问题处理 去除空格和小写
        reqJson.put("carNum",reqJson.getString("carNum").trim().toUpperCase());

        //校验优惠券是否存在
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");

        CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
        couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
        couponPropertyUserDto.setTel(userDtos.get(0).getTel());
        couponPropertyUserDto.setToType(CouponPropertyUserDto.TO_TYPE_PARKING);
        couponPropertyUserDto.setState(CouponPropertyUserDto.STATE_WAIT);

        List<CouponPropertyUserDto> couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

        if (couponPropertyUserDtos == null || couponPropertyUserDtos.size() < 1) {
            throw new CmdException("优惠券不存在");
        }

        if (!"Y".equals(couponPropertyUserDtos.get(0).getIsExpire())) {
            throw new CmdException("优惠券已过期");
        }

        int stock = Integer.parseInt(couponPropertyUserDtos.get(0).getStock());
        int giftCount = Integer.parseInt(reqJson.getString("giftCount"));
        if (stock < giftCount) {
            throw new CmdException("优惠券不够赠送，当前数量为：" + stock);
        }

        CouponPropertyPoolConfigDto couponPropertyPoolConfigDto = new CouponPropertyPoolConfigDto();
        couponPropertyPoolConfigDto.setCouponId(couponPropertyUserDtos.get(0).getCppId());
        couponPropertyPoolConfigDto.setColumnKey("hours");
        List<CouponPropertyPoolConfigDto> couponPropertyPoolConfigDtos = couponPropertyPoolConfigV1InnerServiceSMOImpl.queryCouponPropertyPoolConfigs(couponPropertyPoolConfigDto);

        Assert.listOnlyOne(couponPropertyPoolConfigDtos, "未包含优惠券配置信息");

        Double.parseDouble(couponPropertyPoolConfigDtos.get(0).getColumnValue());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");

        //校验优惠券是否存在
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("couponId");
        int flag = 0;
        List<CouponPropertyUserDto> couponPropertyUserDtos = null;
        int giftCount = Integer.parseInt(reqJson.getString("giftCount"));
        try {
            CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
            couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
            couponPropertyUserDto.setTel(userDtos.get(0).getTel());
            couponPropertyUserDto.setToType(CouponPropertyUserDto.TO_TYPE_PARKING);
            couponPropertyUserDto.setState(CouponPropertyUserDto.STATE_WAIT);

            couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

            if (couponPropertyUserDtos == null || couponPropertyUserDtos.size() < 1) {
                throw new CmdException("优惠券不存在");
            }

            if (!"Y".equals(couponPropertyUserDtos.get(0).getIsExpire())) {
                throw new CmdException("优惠券已过期");
            }

            int stock = Integer.parseInt(couponPropertyUserDtos.get(0).getStock());

            if (stock < giftCount) {
                throw new CmdException("优惠券不够赠送，当前数量为：" + stock);
            }
            CouponPropertyUserPo couponPropertyUserPo = new CouponPropertyUserPo();
            couponPropertyUserPo.setCouponId(couponPropertyUserDtos.get(0).getCouponId());
            couponPropertyUserPo.setCommunityId(couponPropertyUserDtos.get(0).getCommunityId());
            couponPropertyUserPo.setStock((stock - giftCount) + "");
            if (stock == giftCount) {
                couponPropertyUserPo.setState(CouponPropertyUserDto.STATE_FINISH);
            }
            flag = couponPropertyUserV1InnerServiceSMOImpl.updateCouponPropertyUser(couponPropertyUserPo);

            if (flag < 1) {
                throw new CmdException("赠送失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        for(int giftIndex = 0; giftIndex < giftCount;giftIndex ++) {
            //保存核销记录
            String pccId = GenerateCodeFactory.getGeneratorId("11");

            CouponPropertyUserDetailPo couponPropertyUserDetailPo = new CouponPropertyUserDetailPo();
            couponPropertyUserDetailPo.setBusinessKey(pccId);
            couponPropertyUserDetailPo.setCommunityId(reqJson.getString("communityId"));
            couponPropertyUserDetailPo.setCouponId(couponPropertyUserDtos.get(0).getCouponId());
            couponPropertyUserDetailPo.setCouponName(couponPropertyUserDtos.get(0).getCouponName());
            couponPropertyUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId("11"));
            couponPropertyUserDetailPo.setDetailType(couponPropertyUserDtos.get(0).getToType());
            couponPropertyUserDetailPo.setRemark("赠送" + reqJson.getString("carNum") + "停车券");
            flag = couponPropertyUserDetailV1InnerServiceSMOImpl.saveCouponPropertyUserDetail(couponPropertyUserDetailPo);
            if (flag < 1) {
                throw new CmdException("赠送失败");
            }

            CouponPropertyPoolConfigDto couponPropertyPoolConfigDto = new CouponPropertyPoolConfigDto();
            couponPropertyPoolConfigDto.setCouponId(couponPropertyUserDtos.get(0).getCppId());
            couponPropertyPoolConfigDto.setColumnKey("hours");
            List<CouponPropertyPoolConfigDto> couponPropertyPoolConfigDtos = couponPropertyPoolConfigV1InnerServiceSMOImpl.queryCouponPropertyPoolConfigs(couponPropertyPoolConfigDto);

            Assert.listOnlyOne(couponPropertyPoolConfigDtos, "未包含优惠券配置信息");

            double value = Double.parseDouble(couponPropertyPoolConfigDtos.get(0).getColumnValue()) * 60;
            value = Math.ceil(value);

            ParkingCouponCarPo parkingCouponCarPo = new ParkingCouponCarPo();
            parkingCouponCarPo.setPccId(pccId);
            parkingCouponCarPo.setCouponId(reqJson.getString("couponId"));
            parkingCouponCarPo.setCouponShopId(reqJson.getString("couponId"));
            parkingCouponCarPo.setCommunityId(reqJson.getString("communityId"));
            parkingCouponCarPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            parkingCouponCarPo.setEndTime(DateUtil.getAddDayString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A, 1));
            parkingCouponCarPo.setPaId(reqJson.getString("paId"));
            parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_WAIT);
            parkingCouponCarPo.setTypeCd("1001"); // 时长赠送
            parkingCouponCarPo.setGiveWay("4004"); //物业缴费赠送
            parkingCouponCarPo.setValue(value + "");
            parkingCouponCarPo.setCarNum(reqJson.getString("carNum"));
            parkingCouponCarPo.setRemark(userDtos.get(0).getName() + "-" + userDtos.get(0).getTel() + "赠送");
            parkingCouponCarPo.setShopId(userDtos.get(0).getUserId());

            flag = parkingCouponCarV1InnerServiceSMOImpl.saveParkingCouponCar(parkingCouponCarPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }
        context.setResponseEntity(ResultVo.success());


    }
}
