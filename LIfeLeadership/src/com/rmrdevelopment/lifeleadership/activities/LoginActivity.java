package com.rmrdevelopment.lifeleadership.activities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.analytics.tracking.android.EasyTracker;
import com.rmrdevelopment.lifeleadership.LLApplication;
import com.rmrdevelopment.lifeleadership.R;
import com.rmrdevelopment.lifeleadership.SQLiteHelper;
import com.rmrdevelopment.lifeleadership.util.Constant;

public class LoginActivity extends BaseActivity {

	private Button btnSubmit;
	
	private EditText editUsername;
	private EditText editPass;
	
	private ToggleButton btnRememberOnOff;
	
	private TextView txtForgotpass;
	private TextView txtlogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_scr);

		init();
		clickEvents();

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

	private void init() {
		// TODO Auto-generated method stub
		editUsername = (EditText) findViewById(R.id.editTextUserName);
		editPass = (EditText) findViewById(R.id.editTextPassword);
		btnSubmit = (Button) findViewById(R.id.btnSubmitLogin);
		btnRememberOnOff = (ToggleButton) findViewById(R.id.toggleButtonRememberButton);
		txtForgotpass = (TextView) findViewById(R.id.forgotpass);
		txtlogin = (TextView) findViewById(R.id.textViewLoginTitle);

		Log.i("remember", "" + LLApplication.getRemember());
		if (LLApplication.getRemember() == 0) {
			btnRememberOnOff.setChecked(false);
			btnRememberOnOff.setBackgroundResource(R.drawable.off);
		} else {
			btnRememberOnOff.setBackgroundResource(R.drawable.on);
			btnRememberOnOff.setChecked(true);
			editUsername.setText("" + LLApplication.getUsername());
			editPass.setText("" + LLApplication.getPassword());
		}

		@SuppressWarnings("unused")
		Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "dsdigi.ttf");
		Typeface typeFace2 = Typeface.createFromAsset(getAssets(),
				"times_reg.ttf");
		txtlogin.setTypeface(typeFace2);
	}

	private void clickEvents() {
		// TODO Auto-generated method stub
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editUsername.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editPass.getWindowToken(), 0);

				String email_ = editUsername.getText().toString();
				String pass_ = editPass.getText().toString();

				if (isValidate(email_, pass_)) {

					if (isOnline()) {
						login_check_event(email_, pass_);
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}

			}
		});

		btnRememberOnOff
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						Log.e("btnRememberOnOff", "" + isChecked);
						if (isChecked) {
							btnRememberOnOff
									.setBackgroundResource(R.drawable.on);
						} else {
							btnRememberOnOff
									.setBackgroundResource(R.drawable.off);
						}
					}
				});

		txtForgotpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent browserIntent = new Intent(LoginActivity.this,ForgotPassword.class);
				startActivity(browserIntent);
				overridePendingTransition(R.anim.enter_from_bottom,
						R.anim.hold_bottom);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void login_check_event(final String em, final String pwd) {
		// TODO Auto-generated method stub
		try {
			progressDialog = ProgressDialog.show(LoginActivity.this, null,
					"Loading...	", true, false);
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("func", "accountAuthentication");
					map.put("username", "" + em.trim());
					map.put("password", "" + pwd);

					response = getResponse(map);
					Update();
				}
			});
			t.start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void Update() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (response != null) {
					try {
						json_str = new JSONObject(response);
						Valid = json_str.getString("Valid");
						LLApplication.setGuid(json_str.getString("Valid"));

						if (Valid.equals("true")) {
							if (isOnline()) {
								AuthenticateUserKey(""
										+ json_str.getString("Guid"));
							} else {
								if (progressDialog!=null && progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								Toast.makeText(getApplicationContext(),
										"" + Constant.network_error,
										Toast.LENGTH_SHORT).show();
							}
						} else {
							if (progressDialog!=null && progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							Toast.makeText(getApplicationContext(),
									"Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
					if(progressDialog!=null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				}
			}
		});
	}

	protected void AuthenticateUserKey(final String key) {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "authenicateKey");
				map.put("key", ""+ key);
				Log.i("Request AuthenticateUserKey", "" + map);
				response = getResponse(map);
				Log.i("response AuthenticateUserKey", "" + response);
				UpdateAuthenticateUserKey();
			}
		});
		t.start();
	}

	protected void UpdateAuthenticateUserKey() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*
				 * if (progressDialog.isShowing()) { progressDialog.dismiss(); }
				 */
				if (response != null) {
					try {
						json_str = new JSONObject(response);
						LLApplication.setUserId(""
								+ json_str.getString("UserID"));
						LLApplication.setUsername(editUsername.getText()
								.toString());
						LLApplication
								.setPassword(editPass.getText().toString());
						LLApplication.setUserloggedin(1);

						//Log.i("login rem..", "" + btnRememberOnOff.isChecked());
						if(btnRememberOnOff!=null){
							if (btnRememberOnOff.isChecked()) {
								LLApplication.setRemember(1);
							} else {
								LLApplication.setRemember(0);
							}
						}

						ContentValues values = new ContentValues();
						values.put("username", "" + LLApplication.getUsername());
						values.put("password", "" + LLApplication.getPassword());
						values.put("Guid", "" + LLApplication.getGuid());
						values.put("UserID", "" + LLApplication.getUserId());
						values.put("remember", "" + LLApplication.getRemember());
						values.put("userloggedin",
								"" + LLApplication.getUserloggedin());
						/*if(Constant.db == null){ //new 
							
						}*/
						SQLiteHelper helper = new SQLiteHelper(LoginActivity.this, "lifeleadership.sqlite");
						helper.createDatabase();
						SQLiteDatabase db = helper.openDatabase();
						db.update("user", values, "pk=1", null);
						helper.close();
						
						if (progressDialog!=null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						
						Intent intent = new Intent(LoginActivity.this,
								LifeLeadershipMainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.enter_from_bottom,
								R.anim.hold_bottom);
						finish();

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
					if(progressDialog!=null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				}

			}
		});
	}

	public boolean isValidate(String email, String password) {

		if (email.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "Enter username.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (password.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "Enter password.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		finish();
	}
}
