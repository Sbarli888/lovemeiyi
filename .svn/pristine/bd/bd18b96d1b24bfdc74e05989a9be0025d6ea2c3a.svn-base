package com.huawei.ptn.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.huawei.ptn.common.Constants;

public class HttpUtils {

	private static final String TAG = HttpUtils.class.getSimpleName();

	private static HttpClient httpClient = new DefaultHttpClient();

	/**
	 * 向指定URL发起get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getRequest(String url) throws ClientProtocolException,
			IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);

		if (IsValid(httpResponse)) {
			// 获取服务器响应字符串
			return EntityUtils.toString(httpResponse.getEntity());
		}

		return null;
	}

	/**
	 * 向指定URL发起POST请求
	 * 
	 * @param url
	 * @param rawParams
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String postRequest(String url, Map<String, String> rawParams)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		// 如果传递参数个数较多，可以对传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装请求参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}

		// 设置请求参数
		httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		// 发送POST请求
		HttpResponse httpResponse = httpClient.execute(httpPost);

		if (IsValid(httpResponse)) {
			// 获取服务器响应字符串
			return EntityUtils.toString(httpResponse.getEntity());
		}

		return null;
	}

	private static boolean IsValid(HttpResponse httpResponse) {

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			Log.e(TAG, "200");
			Header[] headers = httpResponse
					.getHeaders(Constants.CONTENT_TYPE_HEADER);
			if (headers != null) {
				Log.e(TAG, "=====================");
				Log.e(TAG, headers[0].getName());
				Log.e(TAG, headers[0].getValue());
				if (Constants.CONTENT_TYPE_VALUE.equalsIgnoreCase(headers[0]
						.getValue())) {
					return true;
				}
			}
		}

		return false;
	}

}
