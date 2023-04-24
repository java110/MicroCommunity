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
package com.java110.acct.cmd.parkingCoupon;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.parkingCoupon.ParkingCouponCarDto;
import com.java110.dto.parkingCoupon.ParkingCouponShopDto;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponShopV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponV1InnerServiceSMO;
import com.java110.po.parkingCouponCar.ParkingCouponCarPo;
import com.java110.po.parkingCouponShop.ParkingCouponShopPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "商家赠送停车劵",
        description = "商家通过此接口赠送停车劵",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/parkingCoupon.saveParkingCouponCar",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "parkingCoupon.saveParkingCouponCar"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "couponShopId", length = 30, remark = "优惠劵ID"),
        @Java110ParamDoc(name = "giveWay", length = 30, remark = "赠送方式 1001 扫码获取 2002 商家添加 3003 购物自动赠送"),
        @Java110ParamDoc(name = "carNum", length = 30, remark = "车牌号"),
        @Java110ParamDoc(name = "shopId", length = 30, remark = "店铺ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),

        }
)

@Java110ExampleDoc(
        reqBody = "{\"shopId\":\"502022101140520018\",\"giveWay\":\"2002\",\"carNum\":\"青A88888\",\"couponShopId\":\"102022101112890007\"}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)

/**
 * 类表述：保存
 * 服务编码：parkingCouponCar.saveParkingCouponCar
 * 请求路劲：/app/parkingCouponCar.SaveParkingCouponCar
 * add by 吴学文 at 2022-10-12 13:02:09 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingCoupon.saveParkingCouponCar")
public class SaveParkingCouponCarCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveParkingCouponCarCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponV1InnerServiceSMO parkingCouponV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponShopV1InnerServiceSMO parkingCouponShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "couponShopId", "请求报文中未包含couponShopId");
        Assert.hasKeyAndValue(reqJson, "shopId", "请求报文中未包含shopId");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "giveWay", "请求报文中未包含giveWay");
        Assert.hasKeyAndValue(reqJson, "code", "请求报文中未包含临时票据");

        String codeKey = reqJson.getString("shopId") + reqJson.getString("code");

        String checkCode = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,"CHECK_PARKING_COUPON_QRCODE_CODE");

        if ("OFF".equals(checkCode)) {
            return;
        }
        if (!reqJson.getString("code").equals(CommonCache.getAndRemoveValue(codeKey))) {
            throw new CmdException("非法操作");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        ParkingCouponShopDto parkingCouponShopDto = new ParkingCouponShopDto();
        parkingCouponShopDto.setCouponShopId(reqJson.getString("couponShopId"));
        parkingCouponShopDto.setShopId(reqJson.getString("shopId"));
        List<ParkingCouponShopDto> parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);

        Assert.listOnlyOne(parkingCouponShopDtos, "停车劵不存在");

        int quantity = Integer.parseInt(parkingCouponShopDtos.get(0).getQuantity());

        if (quantity < 1) {
            throw new CmdException("停车劵不足，请购买");
        }
        int flag = 0;
        // 这里加全局锁 防止 并发
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("couponShopId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            parkingCouponShopDto = new ParkingCouponShopDto();
            parkingCouponShopDto.setCouponShopId(reqJson.getString("couponShopId"));
            parkingCouponShopDto.setShopId(reqJson.getString("shopId"));
            parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);
            quantity = Integer.parseInt(parkingCouponShopDtos.get(0).getQuantity());
            if (quantity < 1) {
                throw new CmdException("停车劵不足，请购买");
            }
            ParkingCouponShopPo parkingCouponShopPo = new ParkingCouponShopPo();
            parkingCouponShopPo.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
            parkingCouponShopPo.setQuantity((quantity - 1) + "");
            flag = parkingCouponShopV1InnerServiceSMOImpl.updateParkingCouponShop(parkingCouponShopPo);
            if (flag < 1) {
                throw new CmdException("优惠券递减失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        ParkingCouponCarPo parkingCouponCarPo = BeanConvertUtil.covertBean(reqJson, ParkingCouponCarPo.class);
        parkingCouponCarPo.setPccId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        parkingCouponCarPo.setCouponId(parkingCouponShopDtos.get(0).getCouponId());
        parkingCouponCarPo.setCommunityId(parkingCouponShopDtos.get(0).getCommunityId());
        parkingCouponCarPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        parkingCouponCarPo.setEndTime(DateUtil.getAddDayString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A, 1));
        parkingCouponCarPo.setPaId(parkingCouponShopDtos.get(0).getPaId());
        parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_WAIT);
        parkingCouponCarPo.setTypeCd(parkingCouponShopDtos.get(0).getTypeCd());
        parkingCouponCarPo.setValue(parkingCouponShopDtos.get(0).getValue());

        flag = parkingCouponCarV1InnerServiceSMOImpl.saveParkingCouponCar(parkingCouponCarPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
