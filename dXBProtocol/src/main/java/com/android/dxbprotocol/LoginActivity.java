package com.android.dxbprotocol;

import java.util.LinkedHashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.utility.L;

public class LoginActivity extends AppActivity {

	private AppActivity activity = null;
	private EditText editTextMobile, editTextSMSCode;
	private Button buttonLogin;
	private static final String LOG_TAG = "LoginActivity";
	private ThreadProcessLogIn threadProcessLogIn;
	private ThreadProcessLogInRegisterDeviceID threadProcessLogInRegisterDeviceID;
	String mobile, smsCode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.login);

		activity = this;

		initLayouts();

		if (!preference.isAppLaunchedFirstTime()) {
			showActivity(HomeActivity.class, DIRECTION_FRONT);
			finish();
		}
	}

	/**
	 * To initialize the layout and assign listeners for that fields.
	 */
	private void initLayouts() {
		editTextMobile = (EditText) findViewById(R.id.editTextMobile);
		editTextSMSCode = (EditText) findViewById(R.id.editTextSMSCode);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == buttonLogin) {

				// showActivity(EventListActivity.class, DIRECTION_FRONT);

				mobile = editTextMobile.getText().toString();
				smsCode = editTextSMSCode.getText().toString();

				if (mobile.equals("")) {
					alert(editTextMobile, "Please enter the mobile number");
					return;
				} else if (smsCode.equals("")) {
					alert(editTextSMSCode, "Please enter the SMS Code");
					return;
				} else {
					showLoading(Messages.LOADING);
					threadProcessLogInRegisterDeviceID = new ThreadProcessLogInRegisterDeviceID();
					threadProcessLogInRegisterDeviceID.start();
				}

				// preference.setLoginDetails("8", "E", "A");
				// showActivity(HomeActivity.class, DIRECTION_FRONT);
			}
		}
	};

	class ThreadProcessLogIn extends Thread {

		String mobile, smsCode;

		public ThreadProcessLogIn(String mobile, String smsCode) {
			this.mobile = mobile;
			this.smsCode = smsCode;
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
						.getLogInParameters(mobile, smsCode, sIMEI);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_LOGIN);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getLoginDetails();

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

			getProgressHandler.sendMessage(msg);

		}
	};

	private Handler getProgressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			hideLoading();

			try {
				threadProcessLogIn.interrupt();
			} catch (Exception e) {
			}

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				showActivity(HomeActivity.class, DIRECTION_FRONT);
				finish();
			} else {
				alert(sResponseMsg);
			}

		}
	};

	class ThreadProcessLogInRegisterDeviceID extends Thread {

		public ThreadProcessLogInRegisterDeviceID() {
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
						.getRegisterParameters(sIMEI);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_REGISTER_DEVICE_ID);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser
							.getRegisterResponseStatus();

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

			getProgressHandlerRegisterDeviceId.sendMessage(msg);

		}
	};

	private Handler getProgressHandlerRegisterDeviceId = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessLogInRegisterDeviceID.interrupt();
			} catch (Exception e) {
			}

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				threadProcessLogIn = new ThreadProcessLogIn(mobile, smsCode);
				threadProcessLogIn.start();
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