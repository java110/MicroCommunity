package com.java110.job.export;

import com.java110.core.client.*;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.ExportDataDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.OSSUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 导出数据执行器
 */
public class ExportDataExecutor implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(ExportDataQueue.class);


    //默认线程大小
    private static final int DEFAULT_EXPORT_POOL = 4;

    @Override
    public void run() {

        try {
            doExportData();
        }catch (Exception e){
            log.error("处理消息异常",e);
            e.printStackTrace();
        }

    }

    private void doExportData() throws Exception {

        ExportDataDto exportDataDto =  ExportDataQueue.getData();

        if(exportDataDto == null){
            return;
        }

        String businessAdapt = exportDataDto.getBusinessAdapt();

        IExportDataAdapt exportDataAdaptImpl = ApplicationContextFactory.getBean(businessAdapt, IExportDataAdapt.class);

        if(exportDataAdaptImpl == null){
            return ;
        }
        SXSSFWorkbook workbook= exportDataAdaptImpl.exportData(exportDataDto);

        //保存文件路径到 文件下载表
        FileUploadTemplate fileUploadTemplate = ApplicationContextFactory.getBean("fileUploadTemplate", FileUploadTemplate.class);


        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream os = null;
        try {

            os = new ByteArrayOutputStream();
            workbook.write(os);
            inputStream = new ByteArrayInputStream(os.toByteArray());

            fileUploadTemplate.saveFile(inputStream,exportDataDto.getFileName());

        }finally {
            try {
                workbook.close();
            }catch (Exception e){

            }
            try {
                inputStream.close();
            }catch (Exception e){

            }
            try {
                os.close();
            }catch (Exception e){

            }

        }
    }

    /**
     * 线程启动器
     */
    public static void startExportDataExecutor(){
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_EXPORT_POOL);
        executorService.execute(new ExportDataExecutor());
    }
}
