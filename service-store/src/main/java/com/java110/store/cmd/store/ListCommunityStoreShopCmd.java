package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.parkingCoupon.ParkingCouponShopDto;
import com.java110.dto.shopCommunity.ShopCommunityDto;
import com.java110.dto.storeShopCommunity.StoreShopCommunityDto;
import com.java110.intf.acct.IParkingCouponShopV1InnerServiceSMO;
import com.java110.intf.mall.IShopCommunityInnerServiceSMO;
import com.java110.intf.store.IStoreShopCommunityV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Java110CmdDoc(title = "查询周围店铺",
        description = "根据小区ID查询周围店铺，设计此接口主要 用于停车卷 商家购买 停车卷时 查询小区周边 商铺，比如 饭店等",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/store.listCommunityStoreShop",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "store.listCommunityStoreShop",
        seq = 7
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/store.listCommunityStoreShop?communityId=123123&page=1&row=10",
        resBody = "{'code':0,'msg':'成功'}"
)

@Java110Cmd(serviceCode = "store.listCommunityStoreShop")
public class ListCommunityStoreShopCmd extends Cmd {

    @Autowired(required = false)
    private IShopCommunityInnerServiceSMO shopCommunityInnerServiceSMOImpl;

    @Autowired
    private IStoreShopCommunityV1InnerServiceSMO storeShopCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponShopV1InnerServiceSMO parkingCouponShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        StoreShopCommunityDto storeShopCommunityDto = BeanConvertUtil.covertBean(reqJson, StoreShopCommunityDto.class);
        List<ShopCommunityDto> storeShopCommunityDtos = null;
        int count = 0;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            count = shopCommunityInnerServiceSMOImpl.queryShopCommunitysCount(storeShopCommunityDto);
            if (count > 0) {
                storeShopCommunityDtos = shopCommunityInnerServiceSMOImpl.queryShopCommunitys(storeShopCommunityDto);
            } else {
                storeShopCommunityDtos = new ArrayList<>();
            }
        } else {
            count = storeShopCommunityV1InnerServiceSMOImpl.queryStoreShopCommunitysCount(storeShopCommunityDto);
            if (count > 0) {
                storeShopCommunityDtos = storeShopCommunityV1InnerServiceSMOImpl.queryStoreShopCommunitys(storeShopCommunityDto);
            } else {
                storeShopCommunityDtos = new ArrayList<>();
            }
        }


        freshParkingCoupon(storeShopCommunityDtos);


        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, storeShopCommunityDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void freshParkingCoupon(List<ShopCommunityDto> storeShopCommunityDtos) {
        /**
         * 刷入停车卷数
         */
        if (storeShopCommunityDtos == null || storeShopCommunityDtos.size() < 1) {
            return;
        }

        if (storeShopCommunityDtos.size() > 20) {
            return;
        }

        List<String> shopIds = new ArrayList<>();

        for (ShopCommunityDto shopCommunityDto : storeShopCommunityDtos) {
            shopIds.add(shopCommunityDto.getShopId());
        }

        ParkingCouponShopDto parkingCouponShopDto = new ParkingCouponShopDto();
        parkingCouponShopDto.setShopIds(shopIds.toArray(new String[shopIds.size()]));
        List<ParkingCouponShopDto> parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShopStatistics(parkingCouponShopDto);

        for (ShopCommunityDto shopCommunityDto : storeShopCommunityDtos) {
            for (ParkingCouponShopDto parkingCouponShopDto1 : parkingCouponShopDtos) {
                if (!parkingCouponShopDto1.getShopId().equals(shopCommunityDto.getShopId())) {
                    continue;
                }

                shopCommunityDto.setParkingCouponCount(parkingCouponShopDto1.getQuantity());
            }
        }
    }
}
