package com.rmrdevelopment.lifeleadership.activities;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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
