package com.java110.acct.payment.adapt.bbgpay.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http请求工具类
 * 
 * @author lizhongfu
 *
 */
public class HttpRequestUtil {
	private final static int connectTimeOut = 30000;// 连接超时时间
	private final static int readTimeOut = 30000;// 读超时时间

	/**
	 * httpPost
	 * 
	 * @param url       路径
	 * @param jsonParam 参数
	 * @return
	 */
	public static String httpPost(String url, String jsonParam) throws Exception {
		return httpPost(url, jsonParam, false);
	}

	/**
	 * post请求
	 *
	 * @return
	 */
	public static String httpPost(String url, String jsonParam, boolean bUseProxy) throws Exception {
		// post请求返回结果
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		String str = "";
		try {
			RequestConfig config1 = null;
			// 是否采用代理
			if (bUseProxy) {
				// HttpHost proxy = new HttpHost("代理服务器IP", 代理服务器端口);
				HttpHost proxy = new HttpHost("127.0.0.1", 8828);
				config1 = RequestConfig.custom().setProxy(proxy).setConnectTimeout(connectTimeOut).setSocketTimeout(readTimeOut).build();
			} else {
				config1 = RequestConfig.custom().setConnectTimeout(connectTimeOut).setSocketTimeout(readTimeOut).build();
			}
			method.setConfig(config1);
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				// entity.setContentType("application/x-www-form-urlencoded");
				method.setEntity(entity);
			}
			HttpResponse result = client.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				/** 读取服务器返回过来的json字符串数据 **/
				// str = EntityUtils.toString(result.getEntity());
				str = EntityUtils.toString(result.getEntity(), "UTF-8");
			}
		} catch (IOException e) {
			System.err.println("网络异常:" + e.getMessage());
			// 网络异常
			throw e;
		} catch (Exception e) {
			System.err.println("系统错误:" + e.getMessage());
			throw e;
		}
		return str;
	}

	/**
	 * 获取到下载的outputstream
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream httpGetToStream(String url) throws Exception {
		InputStream input = null;
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			input = response.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len;
			while ((len = input.read(buff)) > -1) {
				baos.write(buff, 0, len);
			}
			baos.flush();
			return baos;
		} catch (Exception e) {
			System.err.println("系统错误:" + e.getMessage());
			return null;
		} finally {
			input.close();
			client.close();
		}
	}
}
