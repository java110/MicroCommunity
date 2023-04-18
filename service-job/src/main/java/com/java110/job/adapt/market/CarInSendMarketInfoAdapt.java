package com.java110.job.adapt.market;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.market.MarketRuleCommunityDto;
import com.java110.dto.market.MarketRuleObjDto;
import com.java110.dto.market.MarketRuleWayDto;
import com.java110.dto.market.MarketTextDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IMarketRuleCommunityV1InnerServiceSMO;
import com.java110.intf.common.IMarketRuleObjV1InnerServiceSMO;
import com.java110.intf.common.IMarketRuleWayV1InnerServiceSMO;
import com.java110.intf.common.IMarketTextV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆进场发送营销信息
 *
 * 根据 admin账户下 营销配置 发送
 *
 *
 */
@Component(value = "carInSendMarketInfoAdapt")
public class CarInSendMarketInfoAdapt extends DatabusAdaptImpl {

    @Autowired
    private IMarketRuleCommunityV1InnerServiceSMO marketRuleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IMarketTextV1InnerServiceSMO marketTextV1InnerServiceSMOImpl;

    @Autowired
    private IMarketRuleObjV1InnerServiceSMO marketRuleObjV1InnerServiceSMOImpl;

    @Autowired
    private IMarketRuleWayV1InnerServiceSMO marketRuleWayV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessCarInoutDetailPos = new JSONArray();

        if (data instanceof JSONObject) {
            businessCarInoutDetailPos.add(data);
        }

        //JSONObject businessCarBlackWhite = data.getJSONObject("businessCarBlackWhite");
        for (int bCarInoutDetailPoIndex = 0; bCarInoutDetailPoIndex < businessCarInoutDetailPos.size(); bCarInoutDetailPoIndex++) {
            JSONObject businessCarInoutDetailPo = businessCarInoutDetailPos.getJSONObject(bCarInoutDetailPoIndex);
            doSendMarket(business, businessCarInoutDetailPo);
        }
    }

    /**
     * 发送营销信息
     *
     * @param business
     * @param businessCarInoutDetailPo 开门记录信息
     */
    private void doSendMarket(Business business, JSONObject businessCarInoutDetailPo) {
        CarInoutDetailPo carInoutDetailPo = BeanConvertUtil.covertBean(businessCarInoutDetailPo,CarInoutDetailPo.class);

        //没有手机号 说明  没法发送营销信息 所以 放弃 不推送
        if(StringUtil.isEmpty(carInoutDetailPo.getCarNum())){
            return ;
        }

        String communityId = carInoutDetailPo.getCommunityId();

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(carInoutDetailPo.getCarNum());
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setLeaseTypes(new String[]{OwnerCarDto.LEASE_TYPE_INNER,
                OwnerCarDto.LEASE_TYPE_MONTH,
                OwnerCarDto.LEASE_TYPE_NO_MONEY,
                OwnerCarDto.LEASE_TYPE_SALE});
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if(ownerCarDtos == null || ownerCarDtos.size()<1){
            return ;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if(ownerDtos == null || ownerDtos.size()<1){
            return ;
        }

        MarketRuleCommunityDto marketRuleCommunityDto = new MarketRuleCommunityDto();
        marketRuleCommunityDto.setCommunityId(communityId);
        List<MarketRuleCommunityDto> marketRuleCommunityDtos = marketRuleCommunityV1InnerServiceSMOImpl.queryMarketRuleCommunitys(marketRuleCommunityDto);

        if(marketRuleCommunityDtos == null || marketRuleCommunityDtos.size()<1){
            return;
        }

        List<String> ruleIds = new ArrayList<>();
        for(MarketRuleCommunityDto tmpMarketRuleCommunityDto: marketRuleCommunityDtos){
            ruleIds.add(tmpMarketRuleCommunityDto.getRuleId());
        }

        MarketRuleObjDto marketRuleObjDto = new MarketRuleObjDto();
        marketRuleObjDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        marketRuleObjDto.setObjType(MarketRuleObjDto.OBJ_TYPE_BARRIER);
        long count = marketRuleObjV1InnerServiceSMOImpl.queryMarketRuleObjsCount(marketRuleObjDto);

        if(count <1){
            return ;
        }


        MarketRuleWayDto marketRuleWayDto = new MarketRuleWayDto();
        marketRuleWayDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        marketRuleWayDto.setWayType(MarketRuleWayDto.WAY_TYPE_TEXT);
        List<MarketRuleWayDto> marketRuleWayDtos = marketRuleWayV1InnerServiceSMOImpl.queryMarketRuleWays(marketRuleWayDto);

        if(marketRuleWayDtos == null || marketRuleWayDtos.size()<1){
            return;
        }

        MarketTextDto marketTextDto = new MarketTextDto();
        marketTextDto.setTextId(marketRuleWayDto.getWayObjId());
       List<MarketTextDto> marketTextDtos = marketTextV1InnerServiceSMOImpl.queryMarketTexts(marketTextDto);

       if(marketTextDtos == null || marketTextDtos.size()<1){
           return ;
       }



       ISendExecutor sendExecuter = ApplicationContextFactory.getBean("sendExecutor"+marketTextDtos.get(0).getSmsType(),ISendExecutor.class);
        marketTextDtos.get(0).setRuleId(ruleIds.get(0));
       sendExecuter.send(marketTextDtos.get(0),ownerDtos.get(0).getLink(),communityId,marketRuleCommunityDtos.get(0).getCommunityName());



    }
}
