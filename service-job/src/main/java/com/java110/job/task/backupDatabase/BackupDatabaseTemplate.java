package com.java110.job.task.backupDatabase;

import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 备份数据库定时任务
 *
 * @author fqz
 * @date 2021-08-04 14:36
 */
@Component
public class BackupDatabaseTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(BackupDatabaseTemplate.class);

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(tt)
    public static final String BACKUP_DATABASE = "TT_BACKUP_DATABASE";

    //键(hc_community)
    public static final String HC_COMMUNITY_BACKUP_DATABASE = "HC_COMMUNITY_BACKUP_DATABASE";

    //键(备份的数据库文件存储位置)
    public static final String FILE_LOCATION = "FILE_LOCATION";

    //键(区分windows和linux操作系统)
    public static final String LINUX_OR_WINDOWS = "LINUX_OR_WINDOWS";

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("======开始执行数据库备份======" + taskDto.toString());
        //取出tt库开关映射的值
        String val = MappingCache.getValue(DOMAIN_COMMON, BACKUP_DATABASE);
        //取出hc_community库开关映射的值
        String hcVal = MappingCache.getValue(DOMAIN_COMMON, HC_COMMUNITY_BACKUP_DATABASE);
        //取出数据库文件存储位置
        String fileUrl = MappingCache.getValue(DOMAIN_COMMON, FILE_LOCATION);
        if (StringUtil.isEmpty(fileUrl)) {
            throw new IllegalArgumentException("文件存储位置不能为空！");
        }
        //备份tt库
        if (!StringUtil.isEmpty(val)) {
            String[] split = val.split("&");
            String userName = split[0];
            String password = split[1];
            String databaseName = split[2];
            String port = split[3];
            String backName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "-tt.sql";
            BackupDatabaseTemplate.dbBackUp(userName, password, databaseName, fileUrl, backName, port);
        }
        //备份hc_community库
        if (!StringUtil.isEmpty(hcVal)) {
            String[] split = val.split("&");
            String userName = split[0];
            String password = split[1];
            String databaseName = split[2];
            String port = split[3];
            String backName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "-hc_community.sql";
            BackupDatabaseTemplate.dbBackUp(userName, password, databaseName, fileUrl, backName, port);
        }
    }

    /**
     * 导出tt数据库
     *
     * @param userName
     * @param password
     * @param dbName
     * @param backPath
     * @param backName
     * @param port
     * @throws Exception
     */
    public static void dbBackUp(String userName, String password, String dbName, String backPath, String backName, String port) throws Exception {
        //获取操作系统区分标识
        String operatingSystem = MappingCache.getValue(DOMAIN_COMMON, LINUX_OR_WINDOWS);
        String pathSql = backPath + backName;
        //创建备份sql文件
        try {
            File file = new File(pathSql);
            System.out.println("======文件位置======：" + file);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" -h" + port);
        sb.append(" -u" + userName);
        sb.append(" -p" + password);
        sb.append(" " + dbName + " >");
        sb.append(" " + pathSql);
        System.out.println("======命令为======：" + sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("======开始备份数据库======：" + dbName);
        if (!StringUtil.isEmpty(operatingSystem) && operatingSystem.equals("1")) { //1表示windows操作系统
            Process process = runtime.exec("cmd /c" + sb.toString());
            System.out.println("======返回结果======:" + process.toString());
        } else if (!StringUtil.isEmpty(operatingSystem) && operatingSystem.equals("2")) { //2表示linux操作系统
            Process process = runtime.exec("/home/mysql/mysql/bin/" + sb.toString());
            System.out.println("======返回结果======:" + process.toString());
        }
        System.out.println("======备份数据库" + dbName + "成功!======");
    }

}
