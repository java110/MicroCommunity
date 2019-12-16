package com.java110.utils.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class FtpUpload {
    private static Logger logger = LoggerFactory.getLogger(FtpUpload.class);

    /*
     * private static String server = "www.datasvisser.cn"; //地址 private static
     * int port = 41023;//端口号 private static String userName = "jntechFTP1";//登录名
     * private static String userPassword ="MXUsssMjhssE+*=a3C4\\0";//密码
     */
    private static String ftpPath = "uploadFiles"; // 文件上传目录
    private static FTPClient ftpClient = new FTPClient();
    private static String LOCAL_CHARSET = "GBK";
    private static String SERVER_CHARSET = "ISO-8859-1";
    private final static String localpath = "F:/";//下载到F盘下
    private final static String fileSeparator = System.getProperty("file.separator");

    /*
     *文件上传工具方法
     */
    public static String upload(MultipartFile uploadFile, String server, int port,
                                String userName, String userPassword, String ftpPath) {
        String fileName = "";
        try {
            // request.setCharacterEncoding("utf-8");
            ftpClient.connect(server, port);
            ftpClient.login(userName, userPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            mkDir(ftpPath);// 创建目录
            // 设置上传目录 must
            ftpClient.changeWorkingDirectory(ftpPath);
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            //fileName = new String(uploadFile.getOriginalFilename().getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            /*fileName = id + "-" + fileName;// 构建上传到服务器上的文件名 20-文件名.后缀*/
            fileName = UUID.randomUUID().toString() + "." + uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            InputStream is = uploadFile.getInputStream();
            boolean saveFlag = ftpClient.storeFile(fileName, is);
            is.close();
            if (!saveFlag) {
                throw new IllegalArgumentException("存储文件失败");
            }
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /*
     *文件下载工具方法
     */
    public static byte[] downFileByte(String remotePath, String fileName, String server, int port, String userName, String userPassword) throws Exception {
        ftpClient.connect(server, port);
        ftpClient.login(userName, userPassword);
        ftpClient.enterLocalPassiveMode();
        if (remotePath != null && !remotePath.equals("")) {
            ftpClient.changeWorkingDirectory(remotePath);
            System.out.println("file success");
        }
        byte[] return_arraybyte = null;
        if (ftpClient != null) {
            try {
                FTPFile[] files = ftpClient.listFiles();
                for (FTPFile file : files) {
                    String f = new String(file.getName().getBytes("iso-8859-1"), "utf-8");//防止乱码
                    System.out.println(f);
                    System.out.println(f.equals(fileName));
                    if (f.equals(fileName)) {
                        InputStream ins = ftpClient.retrieveFileStream(file.getName());//需使用file.getName获值，若用f会乱码
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        byte[] buf = new byte[204800];
                        int bufsize = 0;
                        while ((bufsize = ins.read(buf, 0, buf.length)) != -1) {
                            byteOut.write(buf, 0, bufsize);
                        }
                        return_arraybyte = byteOut.toByteArray();

                        File localFile = new File(localpath + fileSeparator + f);
                        OutputStream is = new FileOutputStream(localFile);
                        ftpClient.retrieveFile(f, is);
                        is.close();
                        byteOut.close();
                        ins.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnect();
            }
        }
        return return_arraybyte;
    }


    public static void closeConnect() {
        try {
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择上传的目录，没有创建目录
     *
     * @param ftpPath 需要上传、创建的目录
     * @return
     */
    public static boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            // System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
