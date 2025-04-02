package com.java110.job.task.noticeStaff;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;

import java.util.Calendar;
import java.util.Date;

public class NoticeTaskUtil {
    /**
     * 计算是否为通知时间内
     *
     * @return
     */
    public static boolean isNotifyTodayInspection(CommunityDto communityDto) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours != 18) {
            return false;
        }
        String hasValue = CommonCache.getValue("notify_today_inspection_" + communityDto.getCommunityId());
        if (!StringUtil.isEmpty(hasValue)) {
            return false;
        }
        CommonCache.setValue("notify_today_inspection_" + communityDto.getCommunityId(), hours + "", 12 * 60 * 60);
        return true;
    }

    /**
     * 快超时
     *
     * @param tRepairDto
     * @return
     */
    public static boolean isRepairAboutTimeout(RepairDto tRepairDto) {
        Date timeout = DateUtil.getDateFromStringA(tRepairDto.getTimeout());
        Date nowTime = DateUtil.getCurrentDate();
        if (nowTime.after(timeout)) { // 说明已经超时了
            return false;
        }
        int warnTime = Integer.parseInt(tRepairDto.getWarningTime());

        if (timeout.getTime() - nowTime.getTime() > warnTime * 60 * 1000) {
            return false;
        }
        String hasValue = CommonCache.getValue("notify_repair_about_timeout_" + tRepairDto.getRepairId());
        if (!StringUtil.isEmpty(hasValue)) {
            return false;
        }
        if (warnTime > 1) {
            warnTime -= 1;
        }
        CommonCache.setValue("notify_repair_about_timeout_" + tRepairDto.getRepairId(), warnTime + "", warnTime * 60);

        return true;
    }

    /**
     * 已经超时
     *
     * @param tRepairDto
     * @return
     */
    public static boolean isRepairTimeout(RepairDto tRepairDto) {
        Date timeout = DateUtil.getDateFromStringA(tRepairDto.getTimeout());
        Date nowTime = DateUtil.getCurrentDate();
        if (nowTime.before(timeout)) { // 说明已经没有超时
            return false;
        }

        String hasValue = CommonCache.getValue("notify_repair_timeout_" + tRepairDto.getRepairId());
        if (!StringUtil.isEmpty(hasValue)) {
            return false;
        }
        CommonCache.setValue("notify_repair_timeout_" + tRepairDto.getRepairId(), "Y", 24 * 60 * 60);

        return true;
    }
}
