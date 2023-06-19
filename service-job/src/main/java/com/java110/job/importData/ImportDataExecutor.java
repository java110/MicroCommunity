package com.java110.job.importData;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.ImportDataDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.log.AssetImportLogDto;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.po.log.AssetImportLogPo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 导入资产数据执行器
 */
public class ImportDataExecutor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ImportDataQueue.class);


    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;


    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    private static final int MAX_ROW = 200;

    //默认线程大小
    private static final int DEFAULT_EXPORT_POOL = 4;

    private boolean isRun = false;

    public ImportDataExecutor(boolean isRun) {

        this.isRun = isRun;
    }

    public ImportDataExecutor() {
    }

    @Override
    public void run() {

        while (isRun) {
            log.debug("导入数据线程开始处理");
            try {
                doImportData();
            } catch (Throwable e) {
                log.error("处理消息异常", e);
                e.printStackTrace();
            }
            log.debug("导入数据线程处理完成");

        }

    }

    private void doImportData() throws Exception {

        ImportDataDto importDataDto = ImportDataQueue.getData();
        if (importDataDto == null) {
            return;
        }

        String businessAdapt = importDataDto.getBusinessAdapt();

        IImportDataAdapt importDataAdaptImpl = ApplicationContextFactory.getBean(businessAdapt + "QueueData", IImportDataAdapt.class);

        if (importDataAdaptImpl == null) {
            return;
        }

        try {
            assetImportLogInnerServiceSMOImpl
                    = ApplicationContextFactory.getBean(IAssetImportLogInnerServiceSMO.class.getName(), IAssetImportLogInnerServiceSMO.class);
        } catch (Exception e) {
        }
        if (assetImportLogInnerServiceSMOImpl == null) {
            assetImportLogInnerServiceSMOImpl
                    = ApplicationContextFactory.getBean("assetImportLogInnerServiceSMOImpl", IAssetImportLogInnerServiceSMO.class);
        }
        Assert.hasLength(importDataDto.getLogId(), "未包含导入数据");
        Assert.hasLength(importDataDto.getCommunityId(), "未包含小区信息");

        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setLogId(importDataDto.getLogId());
        assetImportLogDto.setCommunityId(importDataDto.getCommunityId());
        assetImportLogDto.setState(AssetImportLogDto.STATE_WAIT_IMPORT);
        int count = assetImportLogInnerServiceSMOImpl.queryAssetImportLogsCount(assetImportLogDto);
        if (count < 1) {
            throw new IllegalArgumentException("没有需要导入的房产数据" + importDataDto.getLogId());
        }

        //todo 修改为 导入中
        AssetImportLogPo assetImportLogPo = new AssetImportLogPo();
        assetImportLogPo.setLogId(importDataDto.getLogId());
        assetImportLogPo.setState(AssetImportLogDto.STATE_DOING_IMPORT);
        assetImportLogInnerServiceSMOImpl.updateAssetImportLog(assetImportLogPo);

        // todo 查询detail数据
        try {
            assetImportLogDetailInnerServiceSMOImpl
                    = ApplicationContextFactory.getBean(IAssetImportLogDetailInnerServiceSMO.class.getName(), IAssetImportLogDetailInnerServiceSMO.class);
        } catch (Exception e) {
        }
        if (assetImportLogDetailInnerServiceSMOImpl == null) {
            assetImportLogDetailInnerServiceSMOImpl
                    = ApplicationContextFactory.getBean("assetImportLogDetailInnerServiceSMOImpl", IAssetImportLogDetailInnerServiceSMO.class);
        }

        // todo 查询 房产 导入数据
        AssetImportLogDetailDto assetImportLogDetailDto = new AssetImportLogDetailDto();
        assetImportLogDetailDto.setLogId(importDataDto.getLogId());
        assetImportLogDetailDto.setCommunityId(importDataDto.getCommunityId());
        int total = assetImportLogDetailInnerServiceSMOImpl.queryAssetImportLogDetailsCount(assetImportLogDetailDto);

        if (total < 1) {
            return;
        }

        count = (int) Math.ceil((double) total / (double) MAX_ROW);

        for (int page = 1; page <= count; page++) {
            assetImportLogDetailDto.setPage(page);
            assetImportLogDetailDto.setRow(MAX_ROW);

            List<AssetImportLogDetailDto> assetImportLogDetailDtos = assetImportLogDetailInnerServiceSMOImpl.queryAssetImportLogDetails(assetImportLogDetailDto);
            if (assetImportLogDetailDtos == null || assetImportLogDetailDtos.size() < 1) {
                continue;
            }

            try {
                importDataAdaptImpl.importData(assetImportLogDetailDtos);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //todo 修改为 处理完成
        assetImportLogPo = new AssetImportLogPo();
        assetImportLogPo.setLogId(importDataDto.getLogId());
        assetImportLogPo.setState(AssetImportLogDto.STATE_COMPLETE_IMPORT);
        assetImportLogInnerServiceSMOImpl.updateAssetImportLog(assetImportLogPo);
    }

    /**
     * 线程启动器
     */
    public static void startExportDataExecutor() {
        log.debug("开始初始化导入队列");
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_EXPORT_POOL);
        executorService.execute(new ImportDataExecutor(true));
        log.debug("初始化导入队列完成");

    }
}
