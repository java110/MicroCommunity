package com.java110.store.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.area.AreaDto;
import com.java110.dto.shop.ShopDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.storeShop.StoreShopDto;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.store.IStoreShopV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询商铺信息
 * <p>
 * HC小区管理系统 原则上是 开源 HC小区管理系统后端 HC小区管理系统前段 HC智慧家园（业主端） HC掌上物业（物业手机端） HC小区物联网 HC社区政务
 * <p>
 * 为了社区的继续发展贡献代码 所以 HC智慧商城 需要购买 才能使用
 * <p>
 * 此类是为了解决未购买 HC社区商城的 小伙伴或者公司 提供查询商铺的功能
 * <p>
 * 如果你购买了 HC社区商城代码 请您在 开发这账户中 服务信息页面 将接口 /shop/queryShop 调整为调用 shop-service
 * <p>
 * 如果你没有购买 目前也没有购买计划 请您在 开发这账户中 服务信息页面 将接口 /shop/queryShop 调整为调用 store-service
 * 也就是走到此类中
 * <p>
 * 该接口是为了解决 停车劵商家登录后 赠送停车劵的功能 需要查询此接口获取 商铺信息
 */
@Java110Cmd(serviceCode = "/shop/queryShop")
public class QueryShopCmd extends Cmd {

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;
    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;
    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        StoreShopDto shopDto = BeanConvertUtil.covertBean(reqJson, StoreShopDto.class);
        String states = reqJson.getString("states");
        if (states != null && states.contains(",")) {
            shopDto.setStates(states.split(","));
        }
        shopDto.setStoreId(storeId);

        int count = storeShopV1InnerServiceSMOImpl.queryStoreShopsCount(shopDto);

        List<ShopDto> shopDtos = null;
        if (count > 0) {
            shopDtos = storeShopV1InnerServiceSMOImpl.queryStoreShops(shopDto);
            List<String> shopIds = new ArrayList<>();
            for (ShopDto prod : shopDtos) {
                shopIds.add(prod.getShopId());
            }
//            refreshDistributionMode(shopDtos, shopIds);

            freshShopImg(shopDtos);
            freshStore(shopDtos);
        } else {
            shopDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) shopDto.getRow()), count, shopDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 刷入商户信息
     *
     * @param shopDtos
     */
    private void freshStore(List<ShopDto> shopDtos) {

        if (shopDtos == null || shopDtos.size() < 1) {
            return;
        }
        List<String> storeIds = new ArrayList<>();
        for (ShopDto shopDto : shopDtos) {
            storeIds.add(shopDto.getStoreId());
        }

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreIds(storeIds.toArray(new String[storeIds.size()]));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

        for (ShopDto shopDto : shopDtos) {
            for (StoreDto tmpStoreDto : storeDtos) {
                if (shopDto.getStoreId().equals(tmpStoreDto.getStoreId())) {
                    shopDto.setStoreName(tmpStoreDto.getStoreName());
                    shopDto.setStoreTel(tmpStoreDto.getTel());
                    shopDto.setStoreAddress(tmpStoreDto.getAddress());
                }
            }
        }
    }

    /**
     * @param shopDtos
     */
    private void freshShopImg(List<ShopDto> shopDtos) {
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        String mallUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"MALL_URL");

        imgUrl += (!StringUtil.isEmpty(imgUrl) && imgUrl.endsWith("/") ? "" : "/");
        mallUrl += (!StringUtil.isEmpty(mallUrl) && mallUrl.endsWith("/") ? "" : "/");

        for (ShopDto shopDto : shopDtos) {
            shopDto.setShopLogo(imgUrl + shopDto.getShopLogo());
            shopDto.setPhoneIndexUrl(mallUrl + "#/pages/shopIndex/index?shopId=" + shopDto.getShopId());
            if (!StringUtil.isEmpty(shopDto.getAreaCode())) {
                AreaDto areaDto = new AreaDto();
                areaDto.setAreaCode(shopDto.getAreaCode());
                List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getProvCityArea(areaDto);
                if (areaDtos != null && areaDtos.size() > 0) {
                    shopDto.setAreaName(areaDtos.get(0).getProvName() + "-" + areaDtos.get(0).getCityName() + "-" + areaDtos.get(0).getAreaName());
                }
            }
        }
    }
}
