package com.java110.acct.payment.adapt.easypay.utils;


import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HttpUrlConnect 请求类
 */
public class HttpConnectUtils {

    private static HttpURLConnection createConnection(
            String url,
            String encoding,
            int connectionTimeout,
            int readTimeOut,
            String method,
            Map param,
            SSLSocketFactory context, String boundary
    ) throws IOException {
        URL u_url = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) u_url.openConnection();
        // 连接超时时间
        httpURLConnection.setConnectTimeout(connectionTimeout);
        // 读取结果超时时间
        httpURLConnection.setReadTimeout(readTimeOut);
        // 可读
        httpURLConnection.setDoInput(true);
        // 可写
        httpURLConnection.setDoOutput(true);
        // 取消缓存
        httpURLConnection.setUseCaches(false);
        if ("POST".equalsIgnoreCase(method)) {
            httpURLConnection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded;charset=" + encoding);
        } else if (method.toUpperCase() == "POST_NO_CHARSET") {
            httpURLConnection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            method = "POST";
        } else if ("ZXF".equalsIgnoreCase(method)) {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type",
                    "text/plain;charset=" + encoding);
        } else if ("JSON".equalsIgnoreCase(method)) {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type",
                    "application/json;charset=" + encoding);
        } else if ("WL".equalsIgnoreCase(method)) {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type", "application/xml;charset=utf-8");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("MsgTp", String.valueOf(param.get("MsgTp")));

            httpURLConnection.setRequestProperty("OriIssrId", String.valueOf(param.get("OriIssrId")));
            httpURLConnection.setRequestProperty("PyeeAcctTp", "00");
            httpURLConnection.setRequestProperty("PyerAcctTp", "04");
            httpURLConnection.setRequestProperty("ReservedFiedld", "");
        }else if("MULTIPART".equalsIgnoreCase(method)){
            method = "POST";
            httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary="+ boundary);
        } else {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        httpURLConnection.setRequestMethod(method);
        if ("https".equalsIgnoreCase(u_url.getProtocol())) {
            HttpsURLConnection connection = (HttpsURLConnection) httpURLConnection;
            if (context != null) {
                connection.setSSLSocketFactory(context);
            } else {
                connection.setSSLSocketFactory(new BaseHttpSslSocketFactory());
            }
            //解决由于服务器证书问题导致HTTPS无法访问的情况
            connection.setHostnameVerifier(new BaseHttpSslSocketFactory.TrustAnyHostnameVerifier());
            return connection;
        }
        return httpURLConnection;
    }
    private static String generateBoundary() {
        return "--------------------------" +
                UUID.randomUUID().toString().replace("-", "");
    }

    public static int sendRequest(String url,
                                  String encoding,
                                  Object request,
                                  int connectionTimeout,
                                  int readTimeout,
                                  String method,
                                  StringBuilder sb_ret,
                                  Map extraMap
    ) throws IOException {
        Map map = _sendRequest(encoding, request, createConnection(
                url,
                encoding,
                connectionTimeout,
                readTimeout,
                method,
                extraMap, null, null));
        return _receiveResponse((Integer) map.get("i_ret"), (HttpURLConnection) map.get("connection"), sb_ret, encoding, request);
    }

    /**
     * 支持定制的https证书
     *
     * @param url
     * @param request
     * @param method
     * @param contextFactory
     * @return
     * @throws IOException
     */
    public static String sendHttpSRequest(String url,
                                          Object request,
                                          String method,
                                          SSLSocketFactory contextFactory

    ) throws IOException {
        System.out.println(url);
        String encoding = "UTF-8";
        int connectionTimeout = 30000;
        int readTimeout = 60000;
        StringBuilder sb_ret = new StringBuilder();
        Map map = null;
        if("MULTIPART".equals(method)) {
            String boundary = generateBoundary();
            map = _sendRequestMulitiForm(boundary, encoding, (Map) request, createConnection(
                    url,
                    encoding,
                    connectionTimeout,
                    readTimeout,
                    method,
                    null,
                    contextFactory, boundary
            ));
        }else {
            map = _sendRequest(encoding, request, createConnection(
                    url,
                    encoding,
                    connectionTimeout,
                    readTimeout,
                    method,
                    null,
                    contextFactory, null
            ));
        }
        int recCode = _receiveResponse((Integer) map.get("i_ret"), (HttpURLConnection) map.get("connection"), sb_ret, encoding, request);
        assert HttpURLConnection.HTTP_OK ==  recCode : map.get("i_ret") + "通讯异常";
        return sb_ret.toString();
    }
    /**
     * @param encoding:
     * @param request:
     * @param connection:
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Description  推送send
     * @date: 2021/10/14 22:12
     * @author: pandans
     */
    private static Map<String, Object> _sendRequestMulitiForm(String boundary, String encoding, Map request, HttpURLConnection connection) throws IOException {
        int i_ret = 0;
        String dash = "--";
        String newLine = "\r\n";
        FileInputStream inputStream = null;
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        Map<String, String> formMap = (Map<String, String>) request.get("form");

        if(formMap != null) {
            Set<String> formDataKeySet = formMap.keySet();
            for(String formDataKey : formDataKeySet) {
                // form data
                String linHead = dash + boundary + newLine;
                System.out.print(linHead);
                outputStream.writeBytes(linHead);

                String knowledgeRequest = "Content-Disposition: form-data; name=\"" + formDataKey + "\"" + newLine +
                        "Content-Type: text/plain; charset=utf-8" + newLine +
                        newLine ;// important !
                System.out.print(knowledgeRequest);
                outputStream.write(knowledgeRequest.getBytes(StandardCharsets.UTF_8));

                String content = formMap.get(formDataKey) + newLine;
                System.out.print(content);
                outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }
            String lineEnd = dash + boundary;
            System.out.print(lineEnd);
            outputStream.writeBytes(lineEnd);
        }
        Map<String, File> fileMap = (Map<String, File>) request.get("file");
        if(fileMap != null) {
            Set<String> fileDataKeySet = fileMap.keySet();
            for(String fileKey : fileDataKeySet) {
                File file = fileMap.get(fileKey);
                // form data
                String linHead = newLine + dash + boundary + newLine;
                System.out.print(linHead);
                outputStream.writeBytes(linHead);
                // file data
                String fileHeader = "Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"" + newLine +
                        "Content-Type:application/octet-stream"  + newLine +
                        "Content-Transfer-Encoding: binary" + newLine +
                        newLine;// important !
                System.out.print(fileHeader);
                outputStream.write(fileHeader.getBytes("UTF-8"));

                byte[] buffer = new byte[1024];
                int count;
                inputStream = new FileInputStream(file);
                while (true) {
                    count = inputStream.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, count);
                }
            }
            System.out.print(newLine);
            outputStream.writeBytes(newLine);// important !

            String end = dash + boundary + dash;
            System.out.print(end);
            outputStream.writeBytes(end);
        }else {
            String end = dash + newLine;
            System.out.print(end);
            outputStream.writeBytes(end);
        }



        outputStream.flush();
        Map<String, Object> ret = new HashMap<String, Object>(2);
        ret.put("i_ret", i_ret);
        ret.put("connection", connection);
        return ret;
    }
    /**
     * @param encoding:
     * @param request:
     * @param connection:
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Description  推送send
     * @date: 2021/10/14 22:12
     * @author: pandans
     */
    private static Map<String, Object> _sendRequest(String encoding, Object request, HttpURLConnection connection) throws UnsupportedEncodingException {
        Map<String, List<String>> headerFields = connection.getRequestProperties();
        for(String head : headerFields.keySet()){
            System.out.println(head + ":" + String.join(";", headerFields.get(head)));
        }
        System.out.println("method:" + connection.getRequestMethod());
        int i_ret = 0;
        PrintStream out = null;
        String s_request = "";
        if (request instanceof String) {
            s_request = (String) request;
        } else if (request instanceof Map) {
            try {
                s_request = getRequestParamString((Map) request, encoding);
            } catch (UnsupportedEncodingException e) {
                assert false : "不支持的类型:" + e.getLocalizedMessage();
            }
        } else {
            assert false : "不支持的类型:" + request;
        }
        System.out.println("\n请求报文：" + s_request);
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoding);
            out.print(s_request);
            out.flush();
        } catch (Exception ignore) {
            System.out.print("通讯连接成功.返回错误,超时:" + ignore);
            i_ret = -1;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        Map<String, Object> ret = new HashMap<String, Object>(2);
        ret.put("i_ret", i_ret);
        ret.put("connection", connection);

//        System.out.println("----------------请求header start-----------------");
        Map<String, List<String>> headerRFields = connection.getHeaderFields();
        for(String head : headerRFields.keySet()){
//            System.out.println(head + ":" + String.join(";", headerRFields.get(head)));
        }
//        System.out.println("----------------请求header end-----------------");
        return ret;
    }

    /**
     * @param i_ret:
     * @param connection:
     * @param sb_ret:
     * @param encoding:
     * @param request:
     * @return int
     * @Description 处理请求
     * @date: 2021/10/14 22:11
     * @author: pandans
     */
    public static int _receiveResponse(int i_ret, HttpURLConnection connection, StringBuilder sb_ret, String encoding, Object request) {
        if (i_ret != -1) {
            InputStream inputStream = null;
            try {
                i_ret = connection.getResponseCode();
                if (i_ret == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    sb_ret.append(new String(read(inputStream), encoding));
                } else {
                    inputStream = connection.getErrorStream();
                    sb_ret.append(new String(read(inputStream), encoding));
                }
            } catch (Exception ignore) {
                i_ret = -2;
                System.out.print("通讯连接成功.返回错误,超时:" + ignore.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Throwable ignore) {
                    System.out.print("关闭流异常:" + ignore.getMessage());
                }
            }
        }
        if (i_ret != HttpURLConnection.HTTP_OK) {
            if (request instanceof Map) {
                System.out.println("原始报文111:" + ((Map) request).toString());
            } else {
                System.out.println("原始报文:[${request}]");
            }
        }
        return i_ret;
    }

    public static byte[] read(InputStream inputStream) throws IOException {
        byte[] buf = new byte[1024];
        int length = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        while ((length = inputStream.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }
        bout.flush();
        return bout.toByteArray();
    }

    /**
     * 将Map存储的对象，转换为key=value&key=value的字符 并且做urlencoding
     *
     * @param requestParam
     * @param charset      URL.encoding
     * @return
     */
    public static String getRequestParamString(Map<String, String> requestParam, String charset) throws UnsupportedEncodingException {
        return getRequestParamString(requestParam, charset, true);
    }

    public static String getRequestParamString(Map<String, String> requestParam, String charset, boolean isEncode) throws UnsupportedEncodingException {
        if (charset == null) {
            charset = "UTF-8";
        }
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : requestParam.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (isEncode) {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset)).append("&");
            } else {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String s_ret = sb.toString();
        if (s_ret == null || "".equals(s_ret)) {
            return "";
        }
        s_ret = s_ret.substring(0, s_ret.length() - 1);
        return s_ret;
    }

    /**
     * @param file
     * @param pwd
     * @param type PKCS12  jks
     */
    public static KeyStore loadKeyStore(File file, String pwd, String type) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(type);
        InputStream ksIn = new FileInputStream(file);
        keyStore.load(ksIn, pwd.toCharArray());
        return keyStore;
    }

    /**
     * 初始化https环境  设置秘钥和证书
     * 目前华势使用
     *
     * @param keyFilePath
     * @param keyPW
     * @param trustFilePath
     * @param trustPW
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(String keyFilePath, String keyPW, String trustFilePath, String trustPW, String defaultTLS) {
        if (defaultTLS == null) {
            defaultTLS = "TLS";
        }
        try {
            String pwd = keyPW;
            KeyStore keyStore = loadKeyStore(
                    new File("../cert/88888888.p12"),
                    pwd,
                    "PKCS12");
            KeyStore trustStore = loadKeyStore(
                    new File("../cert/client.truststore"),
                    trustPW,
                    "jks");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, pwd.toCharArray());
            SecureRandom rand = new SecureRandom();
            SSLContext sslContext = SSLContext.getInstance(defaultTLS);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);
            return sslContext.getSocketFactory();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }


}