package com.java110.job.printer.manufactor;

import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.StringUtil;

import java.util.List;

public class FeieGetPayFeeDetail {


    public static String getPrintPayFeeDetailHeaderContent(List<FeieLine> order) {
        String orderInfo = "<CB>缴费通知单</CB><BR>";
        orderInfo += "********************************<BR>";
        for (int i = 0; i < order.size(); i++) {
            String title = order.get(i).getTitle();
            String value = order.get(i).getValue();
            orderInfo += (title + ":" + value + "<BR>");
        }
        orderInfo += "********************************<BR>";
        return orderInfo;
    }

    public static String getPrintPayFeeDetailBodyContent(List<FeieLine> business) {

        String orderInfo = "";
        for (int i = 0; i < business.size(); i++) {
            String title = business.get(i).getTitle();
            String value = business.get(i).getValue();
            orderInfo += (title + ":" + value + "<BR>");
        }
        orderInfo += "********************************<BR>";
        return orderInfo;
    }

    public static String getPrintPayFeeDetailFloorContent(String communityId, double totals,String staffName, ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl) {
        String orderInfo = "";
        //orderInfo += "********************************<BR>";
        orderInfo += "合计：" + totals + "元<BR>";
        if(!StringUtil.isEmpty(staffName)) {
            orderInfo += "开票人：" + staffName + "<BR>";
        }
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);


        if (smallWeChatDto != null && smallWeChatDtos.size() > 0) {
            orderInfo += "<QR>" + UrlCache.getOwnerUrl() + "/#/?wAppId=" + smallWeChatDtos.get(0).getAppId() + "</QR>";
        }

        return orderInfo;
    }

}
