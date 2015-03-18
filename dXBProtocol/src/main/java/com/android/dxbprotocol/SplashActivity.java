package com.android.dxbprotocol;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

import com.android.dxbprotocol.storage.DBConnection;

public class SplashActivity extends AppActivity {
	protected boolean _active = true;
	protected int _splashTime = 4000;
	Handler mHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		if (savedInstanceState == null) {
			try {
				DBConnection db = new DBConnection(activity);
				db.createDataBase();
				db.close();
				db = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread splashTread = new Thread() {
				@Override
				public void run() {
					try {
						int waited = 0;
						while (_active && (waited < _splashTime)) {
							sleep(100);
							if (_active) {
								waited += 100;
							}
						}
					} catch (Exception e) {

					}
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							showActivity(LoginActivity.class);
						}
					});

				}
			};
			splashTread.start();
		}
	}

	@Override
	public void goBack() {
		return;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}
}