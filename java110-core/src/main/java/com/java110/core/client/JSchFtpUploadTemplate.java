package com.java110.core.client;

import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.DateUtil;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

/**
 * @author joychen
 * @date 2020/4/21 4:59 PM
 */
@Component
public class JSchFtpUploadTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(JSchFtpUploadTemplate.class);



    private static String ftpPath = "uploadFiles"; // 文件上传目录

    private static String LOCAL_CHARSET = "GBK";
    private static String SERVER_CHARSET = "ISO-8859-1";
    private final static String localpath = "F:/";//下载到F盘下
    private final static String fileSeparator = System.getProperty("file.separator");

    private final static String DEFAULT_IMG_SUFFIX = ".jpg";

    private final static String IMAGE_DEFAULT_PATH = "img/";


    private Channel channel = null;
    private Session session = null;


    /*
     *图片上传工具方法
     * 默认上传至 img 文件下的当前日期下
     */
    public String upload(String imageBase64, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";
        ChannelSftp sftp = null;
        try {
            sftp = getChannel(server,port+"",userName,userPassword,0);

            ftpPath = ftpPath + IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/";

            createDir(sftp, ftpPath);// 创建目录

            // 设置上传目录 must
            fileName = UUID.randomUUID().toString();

            if (imageBase64.contains("data:image/png;base64,")) {
                imageBase64 = imageBase64.replace("data:image/png;base64,", "");
                fileName += ".png";
            } else if (imageBase64.contains("data:image/jpeg;base64,")) {
                imageBase64 = imageBase64.replace("data:image/jpeg;base64,", "");
                fileName += ".jpg";
            } else if (imageBase64.contains("data:image/webp;base64,")) {
                imageBase64 = imageBase64.replace("data:image/webp;base64,", "");
                fileName += ".jpg";
            } else if(imageBase64.contains("data:application/octet-stream;base64,")){
                imageBase64 = imageBase64.replace("data:application/octet-stream;base64,", "");
                fileName += ".jpg";
            }else {
                fileName += ".jpg";
            }
            byte[] context = Base64Convert.base64ToByte(imageBase64);
            ByteArrayInputStream is = new ByteArrayInputStream(context);

            Vector<String> vector =  sftp.ls(ftpPath);
            if (vector.contains(fileName)){
                System.out.println("this file exist ftp");
                sftp.rm(fileName);
            }else{
                System.out.println("this file not exist ftp");
            }

            sftp.put(is,fileName);
            is.close();
            sftp.disconnect();
        } catch (Exception e) {
            LOG.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            try {
                if (sftp !=null) {
                    sftp.disconnect();
                }
                closeChannel();
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            }
        }
        return IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/" + fileName;
    }

    /**
     *
     * @param ftpHost
     * @param port
     * @param ftpUserName
     * @param ftpPassword
     * @param timeout
     * @return
     * @throws JSchException
     */
    public ChannelSftp getChannel(String ftpHost,
                                  String port ,  String ftpUserName ,String ftpPassword ,int timeout) throws JSchException {

        int ftpPort = 22;
        if (port != null && !port.equals("")) {
            ftpPort = Integer.valueOf(port);
        }

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        LOG.debug("Session created.");
        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        LOG.debug("Session connected.");

        LOG.debug("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        LOG.debug("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }



    /*
     *文件上传工具方法
     */
    public String upload(MultipartFile uploadFile, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";

        ChannelSftp sftp = null;

        try {
            sftp = getChannel(server,port+"",userName,userPassword,0);
            createDir(sftp, ftpPath);// 创建目录
            fileName = UUID.randomUUID().toString() + "." + uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
            InputStream os = sftp.get(fileName);
            int length =  os.read();
            if (length == 0) {
                System.out.println("this file not exist ftp");
            } else if (length >= 1) {
                System.out.println("this file exist ftp");
                sftp.rm(fileName);
            }


            InputStream is = uploadFile.getInputStream();
            sftp.put(is,fileName);
            is.close();
            sftp.disconnect();

        } catch (Exception e) {
            // logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            try {
                if (sftp !=null) {
                    sftp.disconnect();
                }
                closeChannel();
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            }
        }
        return fileName;
    }

    /*
     *文件下载工具方法
     */
    public byte[] downFileByte(String remotePath, String fileName, String server, int port, String userName, String userPassword) {
        byte[] return_arraybyte = null;

        ChannelSftp sftp = null;
        LOG.info("remotePath"+remotePath+"      fileName = "+fileName);
        try {
            sftp = getChannel(server,port+"",userName,userPassword,0);

            if (sftp != null) {
                String f = new String(
                        (remotePath + fileName).getBytes("UTF-8"), SERVER_CHARSET);//防止乱码
                InputStream ins = sftp.get(f);//需使用file.getName获值，若用f会乱码


                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

                sftp.get(f,byteOut);
//                byte[] buf = new byte[204800];
//                int bufsize = 0;
//                int readLength = 2048;
//                while (ins != null && (bufsize = ins.read(buf, bufsize,bufsize+readLength)) != -1) {
//                    byteOut.write(buf, bufsize, bufsize  + readLength);
//                }
                return_arraybyte = byteOut.toByteArray();
                byteOut.close();
                if (ins != null) {
                    ins.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("从ftp读取文件失败", e);
        } finally {
            try {
                if (sftp !=null) {
                    sftp.disconnect();
                }
                closeChannel();
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                LOG.error("关闭ftpClient 失败", e);
            }
        }
        return return_arraybyte;
    }



    /**
     * 创建一个文件目录
     */
    public void createDir(ChannelSftp sftp,String createpath) {
        try {
            if (isDirExist(sftp,createpath)) {
                if (sftp.pwd().equals(createpath)){
                    return;
                }
                sftp.cd(createpath);
                return;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(sftp,filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
            throw new IllegalArgumentException("创建路径错误：" + createpath);
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(ChannelSftp sftp,String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }


}
