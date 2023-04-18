package com.java110.job.adapt.market;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.market.MarketBlacklistDto;
import com.java110.dto.market.MarketLogDto;
import com.java110.dto.market.MarketSmsValueDto;
import com.java110.dto.market.MarketTextDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IMarketBlacklistV1InnerServiceSMO;
import com.java110.intf.common.IMarketLogV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.marketLog.MarketLogPo;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public  abstract class DefaultSendExecutor implements ISendExecutor {

    @Autowired
    private IMarketLogV1InnerServiceSMO marketLogV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IMarketBlacklistV1InnerServiceSMO marketBlacklistV1InnerServiceSMOImpl;


    @Override
    public void send(MarketTextDto marketTextDto,String tel,String communityId,String communityName) {

        MarketLogDto marketLogDto = new MarketLogDto();
        marketLogDto.setPersonTel(tel);
        marketLogDto.setCommunityId(communityId);
        long count = 0;
        if(MarketTextDto.SEND_RATE_DAY.equals(marketTextDto.getSendRate())){
            marketLogDto.setStartTime(DateUtil.getAddDayStringA(new Date(),-1));
            marketLogDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            count = marketLogV1InnerServiceSMOImpl.queryMarketLogsCount(marketLogDto);
        }else if(MarketTextDto.SEND_RATE_MONTH.equals(marketTextDto.getSendRate())){
            marketLogDto.setStartTime(DateUtil.getAddMonthStringA(new Date(),-1));
            marketLogDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            count = marketLogV1InnerServiceSMOImpl.queryMarketLogsCount(marketLogDto);
        }

        if(count > 0){
            return ;
        }


        String openId = getOpenId(marketTextDto ,tel, communityId);
        if(StringUtil.isEmpty(openId)){
            return;
        }

        //判断是否为黑名单

        if(validateBlacklist(tel,openId)){
            return ;
        }



        ResultVo resultVo = doSend(marketTextDto,tel,communityId,openId);

        String userName = "未知";

        UserDto userDto = new UserDto();
        userDto.setTel(tel);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if(userDtos != null && userDtos.size()> 0){
            userName = userDtos.get(0).getName();
        }

        String sendWay = "1001";

        if(StringUtil.isNullOrNone(resultVo.getData())){
            sendWay = resultVo.getData().toString();
        }

        MarketLogPo marketLogPo = new MarketLogPo();
        marketLogPo.setBusinessType("1001");
        marketLogPo.setCommunityId(communityId);
        marketLogPo.setCommunityName(communityName);
        marketLogPo.setLogId(GenerateCodeFactory.getGeneratorId("11"));
        marketLogPo.setOpenId(openId);
        marketLogPo.setPersonName(userName);
        marketLogPo.setPersonTel(tel);
        marketLogPo.setRemark(resultVo.getMsg());
        marketLogPo.setRuleId(marketTextDto.getRuleId());
        marketLogPo.setSendContent(marketTextDto.getTextContent());
        marketLogPo.setSendWay(sendWay);
        marketLogV1InnerServiceSMOImpl.saveMarketLog(marketLogPo);
    }

    private boolean validateBlacklist(String tel, String openId) {

        MarketBlacklistDto marketBlacklistDto = new MarketBlacklistDto();
        marketBlacklistDto.setFilters(new String[]{tel,openId});
        int count = marketBlacklistV1InnerServiceSMOImpl.queryMarketBlacklistsCount(marketBlacklistDto);

        if(count> 0){
            return true;
        }

        return false;
    }

    /**
     * 发送消息
     * @param marketTextDto
     * @param tel
     * @param communityId
     */
    public abstract ResultVo doSend(MarketTextDto marketTextDto, String tel, String communityId,String openId);


    private String getOpenId(MarketTextDto marketTextDto,String tel, String communityId) {
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(tel);
        ownerAppUserDto.setCommunityId(communityId);
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if(ownerAppUserDtos == null || ownerAppUserDtos.size()<1){
            return "-1";
        }

        String openId = ownerAppUserDtos.get(0).getOpenId();

        if(StringUtil.isEmpty(openId)){
            return "-1";
        }

        MarketLogDto marketLogDto = new MarketLogDto();
        marketLogDto.setOpenId(openId);
        marketLogDto.setCommunityId(communityId);
        long count = 0;
        if(MarketTextDto.SEND_RATE_DAY.equals(marketTextDto.getSendRate())){
            marketLogDto.setStartTime(DateUtil.getAddDayStringA(new Date(),-1));
            marketLogDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            count = marketLogV1InnerServiceSMOImpl.queryMarketLogsCount(marketLogDto);
        }else if(MarketTextDto.SEND_RATE_MONTH.equals(marketTextDto.getSendRate())){
            marketLogDto.setStartTime(DateUtil.getAddMonthStringA(new Date(),-1));
            marketLogDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            count = marketLogV1InnerServiceSMOImpl.queryMarketLogsCount(marketLogDto);
        }

        if(count > 0){
            return "";
        }

        return openId;
    }

    protected String getMarketValue(List<MarketSmsValueDto> marketSmsValueDtos,String specCd){
        for(MarketSmsValueDto marketSmsValueDto : marketSmsValueDtos){
            if(marketSmsValueDto.getSmsKey().equals(specCd)){
                return marketSmsValueDto.getSmsValue();
            }
        }

        return "";
    }
}
