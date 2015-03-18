package com.android.dxbprotocol;

import java.util.LinkedHashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.classes.SegmentedRadioGroupContact;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.utility.L;

public class ContactUsActivity extends AppActivity {

	private AppActivity activity = null;
	private static final String LOG_TAG = "ContactUsActivity";
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings;
	private ThreadProcessContactUs threadProcessContactUs;
	private ImageView imageViewBack;
	private SegmentedRadioGroupContact segmentedRadioGroupContact;
	private Button buttonSubmit;
	private EditText editTextMessage;
	private CheckBox checkBoxCallback;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.contact);

		activity = this;

		initLayouts();
	}

	/**
	 * To initialize the layout and assign listeners for that fields.
	 */
	private void initLayouts() {

		linearHome = (LinearLayout) findViewById(R.id.linearHome);
		linearAccountUser = (LinearLayout) findViewById(R.id.linearAccountUser);
		linearEvent = (LinearLayout) findViewById(R.id.linearEvent);
		linearSettings = (LinearLayout) findViewById(R.id.linearSettings);
		linearHome.setOnClickListener(clickListener);
		linearAccountUser.setOnClickListener(clickListener);
		linearEvent.setOnClickListener(clickListener);
		linearSettings.setOnClickListener(clickListener);

		imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
		imageViewBack.setOnClickListener(clickListener);

		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(clickListener);

		segmentedRadioGroupContact = (SegmentedRadioGroupContact) findViewById(R.id.segmentedRadioGroupContact);
		segmentedRadioGroupContact
				.setOnCheckedChangeListener(checkedChangeListener);

		editTextMessage = (EditText) findViewById(R.id.editTextMessage);

		checkBoxCallback = (CheckBox) findViewById(R.id.checkBoxCallback);

	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (group == segmentedRadioGroupContact) {

				if (checkedId == R.id.radioButtonQuestion) {
				} else if (checkedId == R.id.radioButtonComplaint) {
				} else if (checkedId == R.id.radioButtonSuggestion) {

				}
			}

		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == linearHome) {
				showActivity(HomeActivity.class, DIRECTION_BACK);
			} else if (view == linearAccountUser) {

			} else if (view == linearEvent) {

			} else if (view == linearSettings) {

			} else if (view == buttonSubmit) {

				int checkedId = segmentedRadioGroupContact
						.getCheckedRadioButtonId();
				String messageType = "1";

				if (checkedId == R.id.radioButtonQuestion) {
					messageType = "1";
				} else if (checkedId == R.id.radioButtonComplaint) {
					messageType = "2";
				} else if (checkedId == R.id.radioButtonSuggestion) {
					messageType = "3";
				}

				String callback = "0";

				if (checkBoxCallback.isChecked()) {
					callback = "1";
				} else {
					callback = "0";
				}

				String message = editTextMessage.getText().toString();

				showLoading(Messages.LOADING);
				threadProcessContactUs = new ThreadProcessContactUs(
						messageType, message, callback);
				threadProcessContactUs.start();
			} else if (view == imageViewBack) {
				finish();
			}
		}
	};

	class ThreadProcessContactUs extends Thread {

		String messsageType;
		String message;
		String callback;

		public ThreadProcessContactUs(String messsageType, String message,
				String callback) {
			this.messsageType = messsageType;
			this.message = message;
			this.callback = callback;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getContactUsParameters(preference.getUserid(),
								messsageType, message, callback);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_CONTACTUS);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getContactUsStatus();

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

			getContactUsHandler.sendMessage(msg);

		}
	};

	private Handler getContactUsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessContactUs.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				alert(sResponseMsg);
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