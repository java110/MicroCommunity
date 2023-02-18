package com.java110.job.printer.manufactor;

import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.UrlCache;

import java.util.List;

public class FeieGetRepair {


    public static String getPrintRepairHeaderContent(List<FeieLine> order) {
        String orderInfo = "<CB>维修工单</CB><BR>";
        orderInfo += "您有新的维修任务如下：<BR>";
        orderInfo += "********************************<BR>";
        for (int i = 0; i < order.size(); i++) {
            String title = order.get(i).getTitle();
            String value = order.get(i).getValue();
            orderInfo += (title + ":" + value + "<BR>");
        }
        orderInfo += "********************************<BR>";
        return orderInfo;
    }

}
