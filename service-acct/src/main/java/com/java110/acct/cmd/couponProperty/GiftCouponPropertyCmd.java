package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.couponPool.CouponPropertyPoolDto;
import com.java110.dto.couponPool.CouponPropertyPoolConfigDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.ICouponPropertyPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyPoolDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.couponPropertyPool.CouponPropertyPoolPo;
import com.java110.po.couponPropertyPoolDetail.CouponPropertyPoolDetailPo;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "手工赠送优惠券",
        description = "一般推荐根据赠送规则，缴费时赠送优惠券给业主，但是也会存在前台手工赠送",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/couponProperty.couponPropertyUserGiftCar",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "couponProperty.giftCouponProperty"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "tel", length = 12, remark = "业主手机号"),
        @Java110ParamDoc(name = "cppId", length = 30, remark = "赠送优惠券"),
        @Java110ParamDoc(name = "giftCount", type = "int", length = 11, remark = "赠送数量")
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
@Java110Cmd(serviceCode = "couponProperty.giftCouponProperty")
public class GiftCouponPropertyCmd extends Cmd{

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolDetailV1InnerServiceSMO couponPropertyPoolDetailV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "tel", "未包含业主手机号");
        Assert.hasKeyAndValue(reqJson, "cppId", "未包含停车券");
        Assert.hasKeyAndValue(reqJson, "giftCount", "未包含赠送数量");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("tel"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if(ownerDtos == null || ownerDtos.size()< 1){
            throw new CmdException("根据手机号未查询到业主");
        }

        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("cppId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
            couponPropertyPoolDto.setCppId(reqJson.getString("cppId"));
            List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPools(couponPropertyPoolDto);

            if (couponPropertyPoolDtos == null || couponPropertyPoolDtos.size() < 1) {
                return;
            }

            int stock = Integer.parseInt(couponPropertyPoolDtos.get(0).getStock());

            int quantity = Integer.parseInt(reqJson.getString("giftCount"));

            if (stock < quantity) {
                return;
            }


            CouponPropertyPoolConfigDto couponPropertyPoolConfigDto = new CouponPropertyPoolConfigDto();
            couponPropertyPoolConfigDto.setCouponId(reqJson.getString("cppId"));
            List<CouponPropertyPoolConfigDto> couponPropertyPoolConfigDtos
                    = couponPropertyPoolConfigV1InnerServiceSMOImpl.queryCouponPropertyPoolConfigs(couponPropertyPoolConfigDto);
            String value = "";
            for (CouponPropertyPoolConfigDto couponPropertyPoolConfigDto1 : couponPropertyPoolConfigDtos) {
                value += (couponPropertyPoolConfigDto1.getName() + ":" + couponPropertyPoolConfigDto1.getColumnValue() + ";");
            }

            //先加明细
            CouponPropertyPoolDetailPo couponPropertyPoolDetailPo = new CouponPropertyPoolDetailPo();
            couponPropertyPoolDetailPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyPoolDetailPo.setCouponName(couponPropertyPoolDtos.get(0).getCouponName());
            couponPropertyPoolDetailPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyPoolDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
            couponPropertyPoolDetailPo.setSendCount(quantity + "");
            couponPropertyPoolDetailPo.setUserId(ownerDtos.get(0).getMemberId());
            couponPropertyPoolDetailPo.setUserName(ownerDtos.get(0).getName());
            couponPropertyPoolDetailPo.setTel(ownerDtos.get(0).getLink());
            couponPropertyPoolDetailPo.setValue(value);
            int flag = couponPropertyPoolDetailV1InnerServiceSMOImpl.saveCouponPropertyPoolDetail(couponPropertyPoolDetailPo);
            if(flag < 1){
                throw new CmdException("保存赠送记录失败");
            }

            //优惠券扣除账户
            CouponPropertyPoolPo couponPropertyPoolPo = new CouponPropertyPoolPo();
            couponPropertyPoolPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyPoolPo.setStock((stock - quantity) + "");
            flag = couponPropertyPoolV1InnerServiceSMOImpl.updateCouponPropertyPool(couponPropertyPoolPo);
            if(flag < 1){
                throw new CmdException("赠送失败");
            }

            //用户账户写入优惠券
            CouponPropertyUserPo couponPropertyUserPo = new CouponPropertyUserPo();
            couponPropertyUserPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyUserPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyUserPo.setState(CouponPropertyUserDto.STATE_WAIT);
            couponPropertyUserPo.setCouponId(GenerateCodeFactory.getGeneratorId("10"));
            couponPropertyUserPo.setCouponName(couponPropertyPoolDtos.get(0).getCouponName());
            couponPropertyUserPo.setStock(quantity + "");
            couponPropertyUserPo.setToType(couponPropertyPoolDtos.get(0).getToType());
            couponPropertyUserPo.setValidityDay(couponPropertyPoolDtos.get(0).getValidityDay());
            couponPropertyUserPo.setUserId(ownerDtos.get(0).getMemberId());
            couponPropertyUserPo.setUserName(ownerDtos.get(0).getName());
            couponPropertyUserPo.setTel(ownerDtos.get(0).getLink());
            couponPropertyUserPo.setValue(value);
            couponPropertyUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
            flag = couponPropertyUserV1InnerServiceSMOImpl.saveCouponPropertyUser(couponPropertyUserPo);

            if(flag < 1){
                throw new CmdException("保存赠送业主记录失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        context.setResponseEntity(ResultVo.success());
    }
}
