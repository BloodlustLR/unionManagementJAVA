package org.eu.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpclient模拟post/get请求json封装表单数据
 * 
 * @author: dylan
 * @date: 2019-08-03 17:01:20
 */
public class HttpClientUtils {

	public static String get(String url) throws ClientProtocolException, IOException {
		// 创建一个默认连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 获取状态吗，判断状态
		int statusCode = response.getStatusLine().getStatusCode();
		String result = null;
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		}
		return result;
	}

	/**
	 * post请求，相应请求失败或异常暂未处理
	 *
	 * @param url      请求路径
	 * @param paramMap 请求参数
	 * @return 请求返回信息
	 */
	public static String postUrlencoded(String url, Map<String, String> paramMap) throws UnsupportedEncodingException {
		// 创建一个默认连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

		// 请求参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key : paramMap.keySet()){
			params.add(new BasicNameValuePair(key,paramMap.get(key)));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"UTF-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(entity);

		// 请求返回信息
		String responseBody = null;

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			// 获取状态码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity responseEntity = response.getEntity();
				// 获取返回对象时中文出现???
				responseBody = EntityUtils.toString(responseEntity, "UTF-8");
			} else {
				return ResponseUtil.error(500, "请求错误");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

	/**
	 * post请求，相应请求失败或异常暂未处理
	 * 
	 * @param url      请求路径
	 * @param paramMap 请求参数
	 * @return 请求返回信息
	 */
	public static String post(String url, Map<String, String> paramMap) {
		// 创建一个默认连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		// 请求参数
		JSONObject jsonParam = new JSONObject();
		for (Map.Entry<String, String> param : paramMap.entrySet()) {
			jsonParam.put(param.getKey(), param.getValue());
		}
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);

		// 请求返回信息
		String responseBody = null;

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			// 获取状态码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity responseEntity = response.getEntity();
				// 获取返回对象时中文出现???
				responseBody = EntityUtils.toString(responseEntity, "UTF-8");
			} else {
				return ResponseUtil.error(500, "请求错误");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

	/**
	 * post请求，处理小程序码图片
	 * 
	 * @param url      请求路径
	 * @param paramMap 请求参数
	 * @return 请求返回信息
	 * @throws Exception
	 */
	public static String postImg(String url, Map<String, Object> paramMap) throws Exception {
		InputStream inputStream = null;
		byte[] data = null;
		// 创建一个默认连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		// 请求参数
		JSONObject jsonParam = new JSONObject();
		for (Map.Entry<String, Object> param : paramMap.entrySet()) {
			jsonParam.put(param.getKey(), param.getValue());
		}
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);

		// 请求返回信息
		// String responseBody = null;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				inputStream = responseEntity.getContent();
				data = readInputStream(inputStream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
		return ResponseUtil.success(data);
	}

	/**
	 * 读数据流
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();

	}
}
