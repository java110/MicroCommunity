package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.couponPropertyUser.CouponPropertyUserDto;
import com.java110.dto.couponPropertyUserDetail.CouponPropertyUserDetailDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.ICouponPropertyUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
import com.java110.po.couponPropertyUserDetail.CouponPropertyUserDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "核销优惠券",
        description = "物业手机版 或者第三方平台调用 核销功能核销优惠券",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/couponProperty.writeOffCouponPropertyUser",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "couponProperty.writeOffCouponPropertyUser"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "couponQrcode", length = 30, remark = "核销码"),
        @Java110ParamDoc(name = "giftCount", type = "int", length = 11, remark = "核销数量"),


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

/**
 * 核销优惠券
 *
 */
@Java110Cmd(serviceCode = "couponProperty.writeOffCouponPropertyUser")
public class WriteOffCouponPropertyUserCmd extends Cmd {

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyUserDetailV1InnerServiceSMO couponPropertyUserDetailV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "couponQrcode", "未包含优惠券");
        Assert.hasKeyAndValue(reqJson, "giftCount", "未包含核销数量");

        String couponId = CommonCache.getAndRemoveValue(reqJson.getString("couponQrcode"));

        if(StringUtil.isEmpty(couponId)){
            throw new CmdException("优惠券不存在");
        }

        reqJson.put("couponId", couponId);

        CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
        couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
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
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("couponId");
        int flag = 0;
        List<CouponPropertyUserDto> couponPropertyUserDtos = null;
        try {
            CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
            couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
            couponPropertyUserDto.setState(CouponPropertyUserDto.STATE_WAIT);

            couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

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

        //保存核销记录
        String userId = context.getReqHeaders().get("user-id");
        String remark = getRemark(userId);

        CouponPropertyUserDetailPo couponPropertyUserDetailPo = new CouponPropertyUserDetailPo();
        couponPropertyUserDetailPo.setBusinessKey(StringUtil.isEmpty(userId)?"-1":userId);
        couponPropertyUserDetailPo.setCommunityId(reqJson.getString("communityId"));
        couponPropertyUserDetailPo.setCouponId(couponPropertyUserDtos.get(0).getCouponId());
        couponPropertyUserDetailPo.setCouponName(couponPropertyUserDtos.get(0).getCouponName());
        couponPropertyUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId("11"));
        couponPropertyUserDetailPo.setDetailType(couponPropertyUserDtos.get(0).getToType());
        if(reqJson.containsKey("remark")) {
            couponPropertyUserDetailPo.setRemark(remark + reqJson.getString("remark"));
        }else{
            couponPropertyUserDetailPo.setRemark(remark);
        }
        flag = couponPropertyUserDetailV1InnerServiceSMOImpl.saveCouponPropertyUserDetail(couponPropertyUserDetailPo);
        if (flag < 1) {
            throw new CmdException("赠送失败");
        }

        CouponPropertyUserDetailDto couponPropertyUserDetailDto = new CouponPropertyUserDetailDto();
        couponPropertyUserDetailDto.setUoId(couponPropertyUserDetailPo.getUoId());
        List<CouponPropertyUserDetailDto> couponPropertyUserDetailDtos
                = couponPropertyUserDetailV1InnerServiceSMOImpl.queryCouponPropertyUserDetails(couponPropertyUserDetailDto);

        context.setResponseEntity(ResultVo.createResponseEntity(couponPropertyUserDetailDtos.get(0)));
    }

    private String getRemark(String userId) {
        if(StringUtil.isEmpty(userId) || "-1".equals(userId)){
            return "接口核销";
        }
        //校验优惠券是否存在
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos == null || userDtos.size() < 1) {
            return "接口核销";
        }

        return userDtos.get(0).getName() + "-" + userDtos.get(0).getTel() + "核销";

    }
}
