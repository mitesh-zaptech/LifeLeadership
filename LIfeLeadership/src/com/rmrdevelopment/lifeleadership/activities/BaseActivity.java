package com.rmrdevelopment.lifeleadership.activities;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
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


	public String getResponse(HashMap<String, String> map) {
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
			
			/*SSLSocketFactory sf = new SSLSocketFactory(
					SSLContext.getInstance("TLS"),
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
					Scheme sch = new Scheme("https", sf, 443);
					httpclinet.getConnectionManager().getSchemeRegistry().register(sch);*/

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
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); 
	}

}
