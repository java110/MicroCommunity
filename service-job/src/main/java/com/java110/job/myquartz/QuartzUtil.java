package com.java110.job.myquartz;

import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * 首先定义好工具类以备后面业务需要
 * 2019/07/26 师延俊
 */
@Component
public class QuartzUtil {
    private final static Logger logger = LoggerFactory.getLogger(QuartzUtil.class);
    @Autowired
    private Scheduler scheduler;

    public String seveJob(QuartzEntity quartzEntity, JSONObject param){
        logger.info("开始新增任务");
        String massage="新增任务成功!";
        //如果保存的任务已经存在 侧先清理任务
        try {
            if(quartzEntity.getOldJobGroup()!=null){
                JobKey jobKey = new JobKey(quartzEntity.getOldJobName(),quartzEntity.getOldJobName());

                    scheduler.deleteJob(jobKey);
            }
            //获取到job执行内容的class类
            Class clas = Class.forName(quartzEntity.getJobClassName());
            //构建job信息
           JobDetail jobDetail= JobBuilder.newJob(clas).withIdentity(quartzEntity.getJobName(),quartzEntity.getJobGroup()).withDescription("加入任务"+quartzEntity.getJobName()).build();
            jobDetail.getJobDataMap().put("param",param);
            //定义触发时间点
            CronScheduleBuilder cronScheduleBuilder= CronScheduleBuilder.cronSchedule(quartzEntity.getCronExpression());
            Trigger trigger=TriggerBuilder.newTrigger().withIdentity("trigger"+quartzEntity.getJobName(),quartzEntity.getJobGroup())
                    .startNow().withSchedule(cronScheduleBuilder).build();

            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            massage="新增任务失败";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            massage="新增任务失败";
        }
        return massage;
    }


}
