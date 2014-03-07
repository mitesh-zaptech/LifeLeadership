package com.rmrdevelopment.lifeleadership.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import com.crittercism.app.Crittercism;
import com.rmrdevelopment.lifeleadership.LLApplication;
import com.rmrdevelopment.lifeleadership.R;
import com.rmrdevelopment.lifeleadership.util.DataBaseManager;

public class SplashActivity extends Activity {

	public static DataBaseManager db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash_scr);

		db = DataBaseManager.getDBAdapterInstance(getApplicationContext());
		Crittercism.initialize(getApplicationContext(),
				"52c68f1de432f5310b000009");

		new Thread() {
			public void run() {
				try {
					int waited = 0;
					while (waited < 2000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
				} finally {
					Cursor crsr = db.rawQuery("select * from user");
					if (crsr != null) {
						if (crsr.getCount() > 0) {
							crsr.moveToFirst();
							LLApplication.setUsername(""
									+ crsr.getString(crsr
											.getColumnIndex("username")));
							LLApplication.setPassword(""
									+ crsr.getString(crsr
											.getColumnIndex("password")));
							LLApplication.setGuid(""
									+ crsr.getString(crsr
											.getColumnIndex("guid")));
							LLApplication.setUserId(""
									+ crsr.getString(crsr
											.getColumnIndex("userid")));
							LLApplication.setRemember(crsr.getInt(crsr
									.getColumnIndex("remember")));
							LLApplication.setUserloggedin(crsr.getInt(crsr
									.getColumnIndex("userloggedin")));
						}
					}

					if (LLApplication.getUserloggedin() == 1) {
						finish();
						Intent intent = new Intent(SplashActivity.this,
								LifeLeadershipMainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.enter_from_bottom,
								R.anim.hold_bottom);
					} else {
						finish();
						Intent intent = new Intent(SplashActivity.this,
								LoginActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.enter_from_left,
								R.anim.hold_bottom);
					}
				}

			}
		}.start();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
}
