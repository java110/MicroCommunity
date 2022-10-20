package com.java110.utils.util;


import com.alibaba.fastjson.JSON;
import com.java110.utils.cache.MappingCache;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class COSUtil {

    public static final String DOMAIN = "COS";
    public static final String COS_SWITCH = "COS_SWITCH";
    public static final String COS_SWITCH_COS = "COS";
    public static final String REGION = "regionName";
    public static final String ACCESS_KEY_ID = "accessKeyId";
    public static final String ACCESS_KEY_SECRET = "accessKeySecret";
    public static final String BUCKET_NAME = "bucketName";

    /**
     * @return OSSClient oss客户端
     * @throws
     * @Title: getOSSClient
     * @Description: 获取oss客户端
     */
    public static COSClient getCOSClient() {
        String regionName = MappingCache.getValue(DOMAIN, REGION);

        String secretId = MappingCache.getValue(DOMAIN, ACCESS_KEY_ID);
        String secretKey = MappingCache.getValue(DOMAIN, ACCESS_KEY_SECRET);

        return COSUtil.getCOSClient(regionName, secretId, secretKey);
    }

    private static COSClient getCOSClient(String regionName, String secretId, String secretKey) {

        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        return cosClient;
    }

    /**
     * @param cosClient  oss客户端
     * @param url        URL
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名）例如“test/index.html”
     * @return void        返回类型
     * @throws
     * @Title: uploadByNetworkStream
     * @Description: 通过网络流上传文件
     */
    public static void uploadByNetworkStream(COSClient cosClient, URL url, String bucketName, String objectName) {
        try {
            InputStream inputStream = url.openStream();

            objectName = url.getFile();
            uploadByInputStream(cosClient, inputStream, objectName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * @param cosClient   oss客户端
     * @param inputStream 输入流
     * @param objectName  上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByInputStream
     * @Description: 通过输入流上传文件
     */
    public static void uploadByInputStream(COSClient cosClient, InputStream inputStream,
                                           String objectName) throws Exception {

        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        uploadByInputStream(cosClient, inputStream, bucketName, objectName);
    }

    /**
     * @param cosClient   oss客户端
     * @param inputStream 输入流
     * @param bucketName  bucket名称
     * @param objectName  上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByInputStream
     * @Description: 通过输入流上传文件
     */
    public static void uploadByInputStream(COSClient cosClient, InputStream inputStream, String bucketName,
                                           String objectName) throws Exception {
        if (!objectName.startsWith("/")) {
            objectName = "/" + objectName;
        }

        try {

            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            //  objectMetadata.setContentLength(1024);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream, objectMetadata);

            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            //System.out.println(JSON.toJSONString(putObjectResult));
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            if (etag == null || etag.length() == 0) {
                throw new Exception("文件上传失败:" + putObjectResult.getDateStr());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * @param cosClient  oss客户端
     * @param file       上传的文件
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadByFile
     * @Description: 通过file上传文件
     */
    public static void uploadByFile(COSClient cosClient, File file, String bucketName, String objectName) {
        try {
            cosClient.putObject(bucketName, objectName, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }


    /**
     * @param cosClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径/名称，例如“test/a.txt”
     * @return void            返回类型
     * @throws
     * @Title: deleteFile
     * @Description: 根据key删除oss服务器上的文件
     */
    public static void deleteFile(COSClient cosClient, String bucketName, String key) {
        cosClient.deleteObject(bucketName, key);
    }

    /**
     * @param cosClient oss客户端
     * @param key       文件路径和名称
     * @return InputStream    文件输入流
     * @throws
     * @Title: getInputStreamByOSS
     * @Description:根据key获取服务器上的文件的输入流
     */
    public static InputStream getInputStreamByCOS(COSClient cosClient, String key) {
        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        return getInputStreamByCOS(cosClient, bucketName, key);

    }


    /**
     * @param cosClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径和名称
     * @return InputStream    文件输入流
     * @throws
     * @Title: getInputStreamByOSS
     * @Description:根据key获取服务器上的文件的输入流
     */
    public static InputStream getInputStreamByCOS(COSClient cosClient, String bucketName, String key) {
        InputStream content = null;
        try {
            // 方法1 获取下载输入流
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);

            content = cosObject.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return content;
    }

    /**
     * @param cosClient  oss客户端
     * @param bucketName bucket名称
     * @return List<String>  文件路径和大小集合
     * @throws
     * @Title: queryAllObject
     * @Description: 查询某个bucket里面的所有文件
     */
    public static List<String> queryAllObject(COSClient cosClient, String bucketName) {
        List<String> results = new ArrayList<String>();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置bucket名称
            listObjectsRequest.setBucketName(bucketName);

            // 设置最大遍历出多少个对象, 一次listobject最大支持1000
            //listObjectsRequest.setMaxKeys(1000);

            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);

            for (COSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                results.add(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            }

        } catch (CosServiceException e) {
            e.printStackTrace();
            return results;
        } catch (CosClientException e) {
            e.printStackTrace();
            return results;
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
        return results;
    }


    public static void main(String[] args) {
        COSClient cosClient = null;

        ByteArrayOutputStream bos = null;
        InputStream is = null;
        ByteArrayInputStream fis = null;


        try {
            String regionName = "ap-guangzhou";
            String secretId = "AKIDb08tQhA4AzXq1xtJiptgTjhsmfHRomlT";
            String secretKey = "XzAH6fFaeSzGMErJYTYP7VdVBakfZPzJ";
            String bucketName = "cyj-hc-1257195390";

            cosClient = COSUtil.getCOSClient(regionName, secretId, secretKey);


            String remotePath = "hc/img/20220322/";
            String fileName = "c2314f54-72d7-4bfd-ab4e-1eed3afd75d8.jpg";

            is = COSUtil.getInputStreamByCOS(cosClient, bucketName,remotePath + fileName);
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

            System.out.println(Base64Convert.byteToBase64(buffer));
        } catch (Exception e) {
            System.out.println("ftp通过文件名称获取远程文件流" + e.getMessage());
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
    }
}
