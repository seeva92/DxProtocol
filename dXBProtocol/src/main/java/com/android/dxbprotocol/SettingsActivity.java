package com.android.dxbprotocol;

import java.util.LinkedHashMap;
import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.storage.Preferences;
import com.android.dxbprotocol.utility.L;

public class SettingsActivity extends AppActivity {

	private AppActivity activity = null;
	private LinearLayout linearAboutApp, linearContactUs, linearLogout;
	private static final String LOG_TAG = "SettingsActivity";
	private Switch switchCoachMark, switchNotification, switchLanguage;
	private ThreadProcessLogout threadProcessLogout;
	private boolean startFirstTime = false;
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.settings);

		activity = this;

		initLayouts();

	}

	/**
	 * To initialize the layout and assign listeners for that fields.
	 */
	private void initLayouts() {

		linearAboutApp = (LinearLayout) findViewById(R.id.linearAboutApp);
		linearContactUs = (LinearLayout) findViewById(R.id.linearContactUs);
		linearLogout = (LinearLayout) findViewById(R.id.linearLogout);
		linearAboutApp.setOnClickListener(clickListener);
		linearContactUs.setOnClickListener(clickListener);
		linearLogout.setOnClickListener(clickListener);

		switchCoachMark = (Switch) findViewById(R.id.switchCoachMark);
		switchNotification = (Switch) findViewById(R.id.switchNotification);
		switchLanguage = (Switch) findViewById(R.id.switchLanguage);

		switchLanguage.setChecked(!preference.isLanguageIsEnglish());
		switchLanguage.setOnCheckedChangeListener(switchListener);

		switchCoachMark.setChecked(preference.isCoachMark());
		switchCoachMark.setOnCheckedChangeListener(switchListener);

		linearHome = (LinearLayout) findViewById(R.id.linearHome);
		linearAccountUser = (LinearLayout) findViewById(R.id.linearAccountUser);
		linearEvent = (LinearLayout) findViewById(R.id.linearEvent);
		linearSettings = (LinearLayout) findViewById(R.id.linearSettings);
		linearHome.setOnClickListener(clickListener);
		linearAccountUser.setOnClickListener(clickListener);
		linearEvent.setOnClickListener(clickListener);
		linearSettings.setOnClickListener(clickListener);

	}

	private OnCheckedChangeListener switchListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (buttonView == switchCoachMark) {
				preference.setCoachMark(isChecked);
			} else if (buttonView == switchLanguage) {
				if (!isChecked) {
					preference.setLanguage(Preferences.KEY_LANGUAGE_ENGLISH);
					Configuration config = new Configuration();
					config.locale = Locale.ENGLISH;
					getResources().updateConfiguration(config, null);
				} else {
					preference.setLanguage(Preferences.KEY_LANGUAGE_ARABIC);
					Locale locale = new Locale("ar");
					Locale.setDefault(locale);
					Configuration config = new Configuration();
					config.locale = locale;
					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());
				}

				showActivity(SettingsActivity.class);
				finish();
			}

		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == linearHome) {
				showActivity(HomeActivity.class, DIRECTION_FRONT);
			} else if (view == linearAccountUser) {
				showActivity(UserProfileActivity.class, DIRECTION_FRONT);
			} else if (view == linearEvent) {
				showActivity(EventListActivity.class, DIRECTION_FRONT);
			} else if (view == linearSettings) {
			} else if (view == linearAboutApp) {
				showActivity(AboutActivity.class, DIRECTION_FRONT);
			} else if (view == linearContactUs) {
				showActivity(ContactUsActivity.class, DIRECTION_FRONT);
			} else if (view == linearLogout) {
				showLoading(Messages.LOADING);
				threadProcessLogout = new ThreadProcessLogout();
				threadProcessLogout.start();
			}
		}
	};

	class ThreadProcessLogout extends Thread {

		public ThreadProcessLogout() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				TelephonyManager telephonyManager = (TelephonyManager) activity
						.getSystemService(Context.TELEPHONY_SERVICE);

				String sIMEI = telephonyManager.getDeviceId();

				LinkedHashMap<String, String> params = APIParams
						.getLogoutParameters(preference.getUserid(), sIMEI);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_EVENT_LOGOUT);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getLogoutStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {
						sCode = "SUCCESS";
						sDesc = parseStatus.ResponseDesc;
					}
				}
			} catch (Exception e) {
				sCode = "ERROR";
				sDesc = Messages.err(e);
				L.error(e);
			}

			bndle.putString("CODE", sCode);
			bndle.putString("DESC", sDesc);
			msg.setData(bndle);

			getEventResponseHandler.sendMessage(msg);

		}
	};

	private Handler getEventResponseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessLogout.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				if (sResponseMsg.contains("Logged Out Successfully")) {
					preference.clearLoginDetails();
					showActivity(LoginActivity.class, DIRECTION_BACK);
					finish();
				} else {
					alert(sResponseMsg);
				}
			} else {
				alert(sResponseMsg);
			}

		}
	};

	@Override
	public void goBack() {
		super.goBack();
	}
}