package com.rmrdevelopment.lifeleadership.activities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.rmrdevelopment.lifeleadership.util.Constant;

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


	/*public String getResponse_(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		String SetServerString = null ;
		
		//
		// Do not do this in production!!!
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient Client = new DefaultHttpClient(mgr, client.getParams());

		// Set verifier     
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);			
		//
		
		
		//HttpClient Client = new DefaultHttpClient();
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
			//
			
			//
			
			
			// Create Request to server and get response
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			SetServerString = Client.execute(httpget, responseHandler);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return SetServerString;
	}*/
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}
	
	
	/*public String getResponse(HashMap<String, String> map) {
		
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> mapp : map.entrySet()) {
			String key = mapp.getKey();
			String value = mapp.getValue();
			postParameters.add(new BasicNameValuePair(key,value));
		}
		
		String URL = Constant.appPath;
		String strresponse = "";
		BufferedReader bufferedReader = null;
		
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient Client = new DefaultHttpClient(mgr, client.getParams());

		// Set verifier     
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);		
		HttpPost request = new HttpPost(URL);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					postParameters);
			// request.setHeader("application/x-www-form-urlencoded");
			request.setHeader("Content-type",
					"application/x-www-form-urlencoded");
			request.setEntity(entity);

			HttpResponse response = Client.execute(request);

			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + LineSeparator);
			}
			bufferedReader.close();

			// result.setText(stringBuffer.toString());
			//Log.e("response ", stringBuffer.toString());
			
			
			
			strresponse = stringBuffer.toString();
		
		} catch (ClientProtocolException e) {
			strresponse = "";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			strresponse = "";
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return strresponse;
	}*/
	
	public String getResponse(HashMap<String, String> map) {
		Log.d("Request Param", "== "+map);

		String URL = Constant.appPath;
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> mapp : map.entrySet()) {
			String key = mapp.getKey();
			String value = mapp.getValue();
			postParameters.add(new BasicNameValuePair(key,value));
			URL = URL.concat("&"+key+"="+value);
		}

		String strresponse = "";
		BufferedReader bufferedReader = null;

		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		registry.register(new Scheme("http", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient Client = new DefaultHttpClient(mgr, client.getParams());

		// Set verifier     
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);		
		HttpPost request = new HttpPost(URL);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					postParameters);
			// request.setHeader("application/x-www-form-urlencoded");
			request.setHeader("Content-type",
					"application/x-www-form-urlencoded");
			//request.setEntity(entity);

			HttpResponse response = Client.execute(request);

			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + LineSeparator);
			}
			bufferedReader.close();

			// result.setText(stringBuffer.toString());
			//Log.e("response ", stringBuffer.toString());
			strresponse = stringBuffer.toString();

		} catch (ClientProtocolException e) {
			strresponse = "";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			strresponse = "";
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return strresponse;
	}
	
/*public String getLoginResponse(HashMap<String, String> map) {
		String URL = Constant.appPath;
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> mapp : map.entrySet()) {
			String key = mapp.getKey();
			String value = mapp.getValue();
			postParameters.add(new BasicNameValuePair(key,value));
			URL = URL.concat("&"+key+"="+value);
		}
		Log.d("URL", "== "+URL);
		
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient Client = new DefaultHttpClient(mgr, client.getParams());

		// Set verifier     
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);		
		
		String strresponse = "";
		StringBuilder builder = new StringBuilder();
        HttpGet httpGet = new HttpGet(URL);
        
        try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                                builder.append(line);
                        }
                        strresponse = line;
                        Log.v("Getter", "Your data: " + builder.toString()); //response data
                } else {
                        Log.e("Getter", "Failed to download file");
                }
        } catch (ClientProtocolException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
		return strresponse;
	}*/
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); 
	}

}
