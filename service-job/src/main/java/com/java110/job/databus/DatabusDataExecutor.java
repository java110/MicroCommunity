package com.java110.job.databus;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.DatabusQueueDataDto;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.job.adapt.IDatabusAdapt;
import com.java110.job.importData.ImportDataQueue;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ExceptionUtil;
import org.slf4j.Logger;

import java.util.concurrent.*;

/**
 * 导入资产数据执行器
 */
public class DatabusDataExecutor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ImportDataQueue.class);

    private static final int MAX_ROW = 200;

    private static final int DEFAULT_TIMEOUT_TIME = 5000; // 5秒超时

    //默认线程大小
    private static final int DEFAULT_EXPORT_POOL = 4;

    private boolean isRun = false;

    private ExecutorService executorService;

    public DatabusDataExecutor(boolean isRun) {
        this.isRun = isRun;
    }

    public DatabusDataExecutor() {
    }

    @Override
    public void run() {

        while (isRun) {
            log.debug("databus数据线程开始处理");
            try {
                doQueueData();
            } catch (Throwable e) {
                log.error("处理databus异常", e);
                e.printStackTrace();
            }
            log.debug("databus数据线程处理完成");

        }

    }

    private void doQueueData() throws Exception {

        DatabusQueueDataDto databusQueueDataDto = DatabusDataQueue.getData();
        if (databusQueueDataDto == null) {
            return;
        }

        String action = databusQueueDataDto.getBeanName();

        IDatabusAdapt databusAdaptImpl = ApplicationContextFactory.getBean(action, IDatabusAdapt.class);

        if (databusAdaptImpl == null) {
            return;
        }

        executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    databusAdaptImpl.execute(databusQueueDataDto.getBusiness(), databusQueueDataDto.getBusinesses());
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("执行databus失败", e);
                }
                return "";
            }
        });
        executorService.execute(futureTask);
        try {
            futureTask.get(DEFAULT_TIMEOUT_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException |
                 TimeoutException e) {
            futureTask.cancel(true);
        }
        executorService.shutdown();

    }

    /**
     * 线程启动器
     */
    public static void startQueueDataExecutor() {
        log.debug("开始初始化消息队列");
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_EXPORT_POOL);
        executorService.execute(new DatabusDataExecutor(true));
        log.debug("初始化导入消息完成");

    }
}
