package com.java110.core.client;


import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.DateUtil;
import com.qcloud.cos.COSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Component
public class CosUploadTemplate {
    private static Logger logger = LoggerFactory.getLogger(CosUploadTemplate.class);

    /*
     * private static String server = "www.datasvisser.cn"; //地址 private static
     * int port = 41023;//端口号 private static String userName = "jntechFTP1";//登录名
     * private static String userPassword ="MXUsssMjhssE+*=a3C4\\0";//密码
     */
    private static String FIX_PATH = "uploadFiles"; // 文件上传目录

    private static String LOCAL_CHARSET = "GBK";
    private static String SERVER_CHARSET = "ISO-8859-1";
    private final static String localpath = "F:/";//下载到F盘下
    private final static String fileSeparator = System.getProperty("file.separator");

    private final static String DEFAULT_IMG_SUFFIX = ".jpg";

    private final static String IMAGE_DEFAULT_PATH = "img/";

    /*
     *图片上传工具方法
     * 默认上传至 img 文件下的当前日期下
     */
    public String upload(String imageBase64, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";
        COSClient cosClient = null;
        ByteArrayInputStream is = null;
        String urlPath = "";
        try {
            cosClient = COSUtil.getCOSClient();
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
            urlPath = IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/" + fileName;
            byte[] context = Base64Convert.base64ToByte(imageBase64);
            is = new ByteArrayInputStream(context);

            COSUtil.uploadByInputStream(cosClient, is, ftpPath + urlPath);
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return urlPath;
    }


    /*
     *文件上传工具方法
     */
    public String upload(MultipartFile uploadFile, String server, int port,
                         String userName, String userPassword, String ftpPath) {
        String fileName = "";
        COSClient cosClient = null;
        InputStream is = null;
        try {
            cosClient = COSUtil.getCOSClient();
            fileName = UUID.randomUUID().toString() + "." + uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
            fileName = IMAGE_DEFAULT_PATH + DateUtil.getNowII() + "/" + fileName;
            is = uploadFile.getInputStream();
            COSUtil.uploadByInputStream(cosClient, is, ftpPath + fileName);
        } catch (Exception e) {
            // logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    /*
     *文件上传工具方法
     */
    public String upload(InputStream inputStream, String ftpPath) {
        COSClient cosClient = null;
        try {
            cosClient = COSUtil.getCOSClient();
            COSUtil.uploadByInputStream(cosClient, inputStream, ftpPath);
        } catch (Exception e) {
            // logger.error("上传文件失败", e);
            throw new IllegalArgumentException("上传文件失败");
        } finally {

        }
        return ftpPath;
    }

    /*
     *文件下载工具方法
     */
    public byte[] downFileByte(String remotePath, String fileName, String server, int port, String userName, String userPassword) {
        byte[] return_arraybyte = null;
        COSClient ossClient = null;
        ByteArrayOutputStream byteOut = null;
        InputStream ins = null;
        ByteArrayInputStream fis = null;
        try {
            ossClient = COSUtil.getCOSClient();
            COSUtil.getInputStreamByCOS(ossClient, remotePath + fileName);
            byteOut = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];
            int bufsize = 0;
            while (ins != null && (bufsize = ins.read(buf, 0, buf.length)) != -1) {
                byteOut.write(buf, 0, bufsize);
            }
            fis = new ByteArrayInputStream(byteOut.toByteArray());
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

            return_arraybyte = buffer;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("从ftp读取文件失败", e);
        } finally {
            if (byteOut != null) {
                try {
                    byteOut.close();
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
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return return_arraybyte;
    }

    public static void main(String[] args) {
        CosUploadTemplate ftpUploadTemplate = new CosUploadTemplate();
        String img = ftpUploadTemplate.download("/hc/img/20220322/", "b3d668c2-96fd-4722-82dd-431dc08941a2.png",
                "118.89.243.11", 617, "hcdemo", "45j74jpWTf7bNhnC");

        System.out.printf("img=" + img);
    }

    public String download(String remotePath, String fileName, String server, int port, String userName, String userPassword) {
        COSClient ossClient = null;
        ByteArrayOutputStream bos = null;
        InputStream is = null;
        ByteArrayInputStream fis = null;
        try {
            ossClient = COSUtil.getCOSClient();
            is = COSUtil.getInputStreamByCOS(ossClient, remotePath + fileName);
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
            if (fis != null) {
                try {
                    fis.close();
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
        }
        return null;
    }

    public InputStream download(String fileName) {
        COSClient ossClient = null;
        InputStream is = null;
        try {
            ossClient = COSUtil.getCOSClient();
            is = COSUtil.getInputStreamByCOS(ossClient, fileName);
            if (null == is) {
                throw new FileNotFoundException(fileName);
            }

        } catch (Exception e) {
            logger.error("ftp通过文件名称获取远程文件流", e);
        }
        return is;
    }

}
