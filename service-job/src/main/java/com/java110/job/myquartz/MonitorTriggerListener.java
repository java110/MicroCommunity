package com.java110.job.myquartz;

import groovy.util.logging.Log4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

/**
 * 个人练习
 * 2019/07/26
 * 师延俊
 */
@Log4j
public class MonitorTriggerListener implements TriggerListener {
    private final static Logger logger = LoggerFactory.getLogger(MonitorTriggerListener.class);

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "MonitorTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        logger.info("Trigger 被触发了，此时job上的execute()方法将要被执行");

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // TODO Auto-generated method stub
        logger.info("trigger被触发后，job将要被执行时Scheduler调用该方法，如返回true则job此次将不被执行");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        logger.info("当前Trigger触发错过了");

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        logger.info("Trigger被触发并且完成了job的执行，此方法被调用");

    }

}