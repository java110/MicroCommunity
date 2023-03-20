package com.java110.report.dao;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ICommunityServiceDao
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:10
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
public interface IReportAttendanceServiceDao {



     int getMonthAttendanceCount(Map info);


     List<Map> getMonthAttendance(Map info);

     /**
      * 考勤明细
      * @param info
      * @return
      */
     List<Map> getMonthAttendanceDetail(Map info);
}
