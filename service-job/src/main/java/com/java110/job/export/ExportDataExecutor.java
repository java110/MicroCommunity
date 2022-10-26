package com.java110.job.export;

import com.java110.core.client.FileUploadTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.userDownloadFile.UserDownloadFileDto;
import com.java110.intf.job.IUserDownloadFileV1InnerServiceSMO;
import com.java110.po.userDownloadFile.UserDownloadFilePo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 导出数据执行器
 */
public class ExportDataExecutor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ExportDataQueue.class);

    private IUserDownloadFileV1InnerServiceSMO userDownloadFileV1InnerServiceSMOImpl;

    private FileUploadTemplate fileUploadTemplate;

    //默认线程大小
    private static final int DEFAULT_EXPORT_POOL = 4;

    private boolean isRun = false;

    public ExportDataExecutor(boolean isRun) {

        this.isRun = isRun;
    }

    public ExportDataExecutor() {
    }

    @Override
    public void run() {

        while (isRun) {
            log.debug("导出数据线程开始处理");
            try {
                doExportData();
            } catch (Throwable e) {
                log.error("处理消息异常", e);
                e.printStackTrace();
            }
            log.debug("导出数据线程处理完成");

        }

    }

    private void doExportData() throws Exception {

        ExportDataDto exportDataDto = ExportDataQueue.getData();

        this.userDownloadFileV1InnerServiceSMOImpl = ApplicationContextFactory.getBean("userDownloadFileV1InnerServiceSMOImpl", IUserDownloadFileV1InnerServiceSMO.class);
        this.fileUploadTemplate = ApplicationContextFactory.getBean("fileUploadTemplate", FileUploadTemplate.class);

        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream os = null;
        SXSSFWorkbook workbook = null;
        String fileName = "";

        if (exportDataDto == null) {
            return;
        }

        String businessAdapt = exportDataDto.getBusinessAdapt();

        IExportDataAdapt exportDataAdaptImpl = ApplicationContextFactory.getBean(businessAdapt, IExportDataAdapt.class);

        if (exportDataAdaptImpl == null) {
            return;
        }
        updateUserDownloadFile(exportDataDto, UserDownloadFileDto.STATE_DOING,"", "开始下载");
        try {
            workbook = exportDataAdaptImpl.exportData(exportDataDto);
            //保存文件路径到 文件下载表
            os = new ByteArrayOutputStream();
            workbook.write(os);
            inputStream = new ByteArrayInputStream(os.toByteArray());

            fileName = fileUploadTemplate.saveFile(inputStream, exportDataDto.getFileName());


            updateUserDownloadFile(exportDataDto, UserDownloadFileDto.STATE_FINISH,fileName, "下载完成");

        } catch (Throwable e) {
            e.printStackTrace();
            updateUserDownloadFile(exportDataDto, UserDownloadFileDto.STATE_FAIL, "","下载失败" + ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {

            }
            try {
                inputStream.close();
            } catch (Exception e) {

            }
            try {
                os.close();
            } catch (Exception e) {

            }

        }
    }

    private void updateUserDownloadFile(ExportDataDto exportDataDto, String state,String url, String remark) {
        UserDownloadFilePo userDownloadFilePo = new UserDownloadFilePo();
        userDownloadFilePo.setDownloadId(exportDataDto.getDownloadId());
        userDownloadFilePo.setState(state);
        if(!StringUtil.isEmpty(url)){
            userDownloadFilePo.setTempUrl(url);
        }
        userDownloadFilePo.setRemark(remark.length() > 512 ? remark.substring(0, 512): remark);
        userDownloadFileV1InnerServiceSMOImpl.updateUserDownloadFile(userDownloadFilePo);
    }

    /**
     * 线程启动器
     */
    public static void startExportDataExecutor() {
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_EXPORT_POOL);
        executorService.execute(new ExportDataExecutor(true));
    }
}
