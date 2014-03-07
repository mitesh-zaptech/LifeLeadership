package com.rmrdevelopment.lifeleadership.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.rmrdevelopment.lifeleadership.R;
import com.rmrdevelopment.lifeleadership.util.Constant;

public class ForgotPassword extends Activity {

	WebView webView;
	ImageView back;

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);

		// WebView:
		webView = (WebView) findViewById(R.id.webview);
		webView.setBackgroundColor(0x00000000);
		webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(true);
		webView.loadUrl("" + Constant.forgotPass_Link);

		back = (ImageView) findViewById(R.id.imageView3);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.hold_top,
						R.anim.exit_in_bottom);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.hold_top, R.anim.exit_in_bottom);
	}
}
