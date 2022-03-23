package com.java110.utils.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.java110.utils.cache.MappingCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OSSUtil {

    public static final String DOMAIN = "OSS";
    public static final String OSS_SWITCH = "OSS_SWITCH";
    public static final String OSS_SWITCH_OSS = "OSS";
    public static final String OSS_SWITCH_FTP = "FTP";
    public static final String ENDPOINT = "endpoint";
    public static final String ACCESS_KEY_ID = "accessKeyId";
    public static final String ACCESS_KEY_SECRET = "accessKeySecret";
    public static final String BUCKET_NAME = "bucketName";

    /**
     * @return OSSClient oss客户端
     * @throws
     * @Title: getOSSClient
     * @Description: 获取oss客户端
     */
    public static OSSClient getOSSClient() {
        String endpoint = MappingCache.getValue(DOMAIN, ENDPOINT);
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = MappingCache.getValue(DOMAIN, ACCESS_KEY_ID);
        String accessKeySecret = MappingCache.getValue(DOMAIN, ACCESS_KEY_SECRET);
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    /**
     * @param ossClient  oss客户端
     * @param url        URL
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名）例如“test/index.html”
     * @return void        返回类型
     * @throws
     * @Title: uploadByNetworkStream
     * @Description: 通过网络流上传文件
     */
    public static void uploadByNetworkStream(OSSClient ossClient, URL url, String bucketName, String objectName) {
        try {
            InputStream inputStream = url.openStream();
            ossClient.putObject(bucketName, objectName, inputStream);
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * @param ossClient   oss客户端
     * @param inputStream 输入流
     * @param objectName  上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByInputStream
     * @Description: 通过输入流上传文件
     */
    public static void uploadByInputStream(OSSClient ossClient, InputStream inputStream,
                                           String objectName) {

        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        uploadByInputStream(ossClient, inputStream, bucketName, objectName);
    }

    /**
     * @param ossClient   oss客户端
     * @param inputStream 输入流
     * @param bucketName  bucket名称
     * @param objectName  上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByInputStream
     * @Description: 通过输入流上传文件
     */
    public static void uploadByInputStream(OSSClient ossClient, InputStream inputStream, String bucketName,
                                           String objectName) {
        try {
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * @param ossClient  oss客户端
     * @param file       上传的文件
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByFile
     * @Description: 通过file上传文件
     */
    public static void uploadByFile(OSSClient ossClient, File file, String bucketName, String objectName) {
        try {
            ossClient.putObject(bucketName, objectName, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径/名称，例如“test/a.txt”
     * @return void            返回类型
     * @throws
     * @Title: deleteFile
     * @Description: 根据key删除oss服务器上的文件
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String key) {
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * @param ossClient oss客户端
     * @param key       文件路径和名称
     * @return InputStream    文件输入流
     * @throws
     * @Title: getInputStreamByOSS
     * @Description:根据key获取服务器上的文件的输入流
     */
    public static InputStream getInputStreamByOSS(OSSClient ossClient, String key) {
        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        return getInputStreamByOSS(ossClient,bucketName,key);

    }


    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径和名称
     * @return InputStream    文件输入流
     * @throws
     * @Title: getInputStreamByOSS
     * @Description:根据key获取服务器上的文件的输入流
     */
    public static InputStream getInputStreamByOSS(OSSClient ossClient, String bucketName, String key) {
        InputStream content = null;
        try {
            OSSObject ossObj = ossClient.getObject(bucketName, key);
            content = ossObj.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @return List<String>  文件路径和大小集合
     * @throws
     * @Title: queryAllObject
     * @Description: 查询某个bucket里面的所有文件
     */
    public static List<String> queryAllObject(OSSClient ossClient, String bucketName) {
        List<String> results = new ArrayList<String>();
        try {
            // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            // objectListing.getObjectSummaries获取所有文件的描述信息。
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                results.add(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return results;
    }
}
