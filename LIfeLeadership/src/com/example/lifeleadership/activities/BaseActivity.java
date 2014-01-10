package com.example.lifeleadership.activities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.lifeleadership.util.Constant;
import com.example.lifeleadership.util.RestClient;

public class BaseActivity extends Activity{

	Context context=this;
	String response;JSONObject json_str;
	String Valid;
	String strRequest = null;
	String data_array;
	JSONArray array = null;
	ProgressDialog progressDialog;

	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}



	public String getResponse(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		String SetServerString = null ;
		HttpClient Client = new DefaultHttpClient();
		// Create URL string
		String URL;
		URL=Constant.appPath;


		for (Map.Entry<String, String> mapp : map.entrySet()) {
			String key = mapp.getKey();
			String value = mapp.getValue();
			URL+="&"+key+"="+value;
		}

		Log.i("URL", ""+URL);
		try
		{
			// Create Request to server and get response
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			SetServerString = Client.execute(httpget, responseHandler);

		}
		catch(Exception ex)
		{
		}
		return SetServerString;
	}




	public String callAPI(String strRequest) {
		String result = null;
		int ResponseCode;

		// TODO Auto-generated method stub
		try {
			JSONObject json = new JSONObject();

			HttpParams httpParams = new BasicHttpParams();

			httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
			httpParams.setParameter(CoreProtocolPNames.USER_AGENT, "Apache-HttpClient/Android");
			//httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
			HttpClient client = new DefaultHttpClient(cm,httpParams);

			String url = Constant.appPath;

			HttpPost request = new HttpPost(url);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("json", strRequest));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			//		request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
			request.setHeader("json", json.toString());

			HttpResponse response = client.execute(request);

			ResponseCode = response.getStatusLine().getStatusCode();
			//Log.d("ResponseCode", ""+ResponseCode);
			if(ResponseCode==200){
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				if (entity != null) {
					InputStream instream = entity.getContent();

					result = RestClient.convertStreamToString(instream);
					//	Log.i("Read from server", result);
				}
			}

			else if(ResponseCode!=200){
				return "Server down";
			}

		} catch (Throwable t) {
			return null;
		}
		return result;
	}

}
