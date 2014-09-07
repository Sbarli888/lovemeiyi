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
	 * ��ָ��URL����get����
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
			// ��ȡ��������Ӧ�ַ���
			return EntityUtils.toString(httpResponse.getEntity());
		}

		return null;
	}

	/**
	 * ��ָ��URL����POST����
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
		// ������ݲ��������϶࣬���ԶԴ��ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// ��װ�������
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}

		// �����������
		httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		// ����POST����
		HttpResponse httpResponse = httpClient.execute(httpPost);

		if (IsValid(httpResponse)) {
			// ��ȡ��������Ӧ�ַ���
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
