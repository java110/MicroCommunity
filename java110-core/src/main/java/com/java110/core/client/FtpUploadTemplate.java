package com.java110.core.client;

import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.DateUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Component
public class FtpUploadTemplate {

    private static Logger logger = LoggerFactory.getLogger(FtpUploadTemplate.class);

    /*
     * private static String server = "www.datasvisser.cn"; //地址 private static
     * int port = 41023;//端口号 private static String userName = "jntechFTP1";//登录名
     * private static String userPassword ="MXUsssMjhssE+*=a3C4\\0";//密码
     */
    private static String ftpPath = "uploadFiles"; // 文件上传目录
    private static String LOCAL_CHARSET = "GBK";
    private static String SERVER_CHARSET = "ISO-8859-1";
    private final static String localpath = "F:/";//下载到F盘下
    private final static String fileSeparator = System.getProperty("file.separator");
    private final static String DEFAULT_IMG_SUFFIX = ".jpg";
    private final static String IMAGE_DEFAULT_PATH = "img/";


    public final static String FTP_DOMAIN = "FTP_DOMAIN";
    public final static String FTP_SERVER = "FTP_SERVER";
    public final static String FTP_PORT = "FTP_PORT";
    public final static String FTP_USERNAME = "FTP_USERNAME";
    public final static String FTP_USERPASSWORD = "FTP_USERPASSWORD";
    public final static String FTP_PATH = "FTP_PATH";


    /*
     *图片上传工具方法
     * 默认上传至 img 文件下的当前日期下
     */
    public String upload(String imageBase64, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";
        FTPClient ftpClient = null;
        ByteArrayInputStream is = null;
        try {
            ftpClient = new FTPClient();
            // request.setCharacterEncoding("utf-8");
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpPath = ftpPath + IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/";
            mkDir(ftpClient, ftpPath);// 创建目录
            // 设置上传目录 must
            ftpClient.changeWorkingDirectory(ftpPath);
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
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
            } else if (imageBase64.contains("data:application/octet-stream;base64,")) {
                imageBase64 = imageBase64.replace("data:application/octet-stream;base64,", "");
                fileName += ".jpg";
            } else {
                fileName += ".jpg";
            }
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            byte[] context = Base64Convert.base64ToByte(imageBase64);
            is = new ByteArrayInputStream(context);
            boolean saveFlag = ftpClient.storeFile(fileName, is);
            if (!saveFlag) {
                throw new IllegalArgumentException("存储文件失败");
            }
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            try {
                ftpClient.disconnect();
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("关闭ftpClient 失败", e);
            }
        }
        return IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/" + fileName;
    }

    /*
     *文件上传工具方法
     */
    public String upload(MultipartFile uploadFile, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";
        FTPClient ftpClient = null;
        try {
            // request.setCharacterEncoding("utf-8");
            ftpClient = new FTPClient();
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            mkDir(ftpClient, ftpPath);// 创建目录
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
     *文件上传工具方法
     */
    public String upload(InputStream inputStream, String server, int port,
                         String userName, String userPassword, String fileName) {
        FTPClient ftpClient = null;
        String ftpPath = "/";

        if(fileName.contains("/")){
            ftpPath = fileName.substring(0,fileName.lastIndexOf("/")+1);
        }

        try {
            // request.setCharacterEncoding("utf-8");

            ftpClient = new FTPClient();
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            mkDir(ftpClient, ftpPath);// 创建目录
            // 设置上传目录 must
            ftpClient.changeWorkingDirectory(ftpPath);

            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            //fileName = new String(uploadFile.getOriginalFilename().getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            /*fileName = id + "-" + fileName;// 构建上传到服务器上的文件名 20-文件名.后缀*/
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            boolean saveFlag = ftpClient.storeFile("/"+fileName, inputStream);
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
    public byte[] downFileByte(String remotePath, String fileName, String server, int port, String userName, String userPassword) {
        byte[] return_arraybyte = null;
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.enterLocalPassiveMode();
            if (ftpClient != null) {
                String f = new String(
                        (remotePath + fileName).getBytes("GBK"),
                        FTP.DEFAULT_CONTROL_ENCODING);//防止乱码
                InputStream ins = ftpClient.retrieveFileStream(f);//需使用file.getName获值，若用f会乱码
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                byte[] buf = new byte[2048];
                int bufsize = 0;
                while (ins != null && (bufsize = ins.read(buf, 0, buf.length)) != -1) {
                    byteOut.write(buf, 0, bufsize);
                }
                //return_arraybyte = byteOut.toByteArray();
                ByteArrayInputStream fis = new ByteArrayInputStream(byteOut.toByteArray());
                byteOut.flush();
                byteOut.close();
                byte[] buffer = new byte[fis.available()];
                int offset = 0;
                int numRead = 0;
                while (offset < buffer.length && (numRead = fis.read(buffer, offset, buffer.length - offset)) >= 0) {
                    offset += numRead;
                }
                if (offset != buffer.length) {
                    throw new IOException("Could not completely read file ");
                }
                fis.close();
                if (ins != null) {
                    ins.close();
                }
                return_arraybyte = buffer;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("从ftp读取文件失败", e);
        } finally {
            closeConnect(ftpClient);
        }
        return return_arraybyte;
    }

    public static void main(String[] args) {
        FtpUploadTemplate ftpUploadTemplate = new FtpUploadTemplate();
        String img = ftpUploadTemplate.download("/hc/img/20200518/", "ed05abae-2eca-40ff-81a8-b586ff2e6a36.jpg",
                "118.89.243.11", 617, "hcdemo", "45j74jpWTf7bNhnC");
        System.out.printf("img=" + img);
    }

    public String download(String remotePath, String fileName, String server, int port, String userName, String userPassword) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream fis = null;
        FTPClient ftpClient = new FTPClient();
        try {
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            String f = new String(
                    (remotePath + fileName).getBytes("GBK"),
                    FTP.DEFAULT_CONTROL_ENCODING);
            is = ftpClient.retrieveFileStream(f);// 获取远程ftp上指定文件的InputStream
            if (null == is) {
                throw new FileNotFoundException(remotePath);
            }
            bos = new ByteArrayOutputStream();
            int length;
            byte[] buf = new byte[2048];
            while (-1 != (length = is.read(buf, 0, buf.length))) {
                bos.write(buf, 0, length);
            }
            fis = new ByteArrayInputStream(
                    bos.toByteArray());
            bos.flush();
            byte[] buffer = new byte[fis.available()];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length && (numRead = fis.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file ");
            }
            return Base64Convert.byteToBase64(buffer);
        } catch (Exception e) {
            logger.error("ftp通过文件名称获取远程文件流", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                closeConnect(ftpClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void closeConnect(FTPClient ftpClient) {
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
    public static boolean mkDir(FTPClient ftpClient, String ftpPath) {
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


    public InputStream download(String fileName, String server, int port, String userName, String userPassword) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream fis = null;
        FTPClient ftpClient = new FTPClient();
        try {
            if (!ftpClient.isConnected()) {
                ftpClient.connect(server, port);
            }
            ftpClient.login(userName, userPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            String f = new String(
                    ("/"+fileName).getBytes("GBK"),
                    FTP.DEFAULT_CONTROL_ENCODING);
            is = ftpClient.retrieveFileStream(f);// 获取远程ftp上指定文件的InputStream
            if (null == is) {
                throw new FileNotFoundException(fileName);
            }

        } catch (Exception e) {
            logger.error("ftp通过文件名称获取远程文件流", e);
        } finally {
            try {
                closeConnect(ftpClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return is;
    }
}
