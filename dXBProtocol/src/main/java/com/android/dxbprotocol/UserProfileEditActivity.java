package com.android.dxbprotocol;

import java.io.InputStream;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.classes.SegmentedRadioGroup;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.config.StringUtils;
import com.android.dxbprotocol.utility.L;
import com.android.dxbprotocol.utility.UserProfile;

public class UserProfileEditActivity extends AppActivity {

	private AppActivity activity = null;
	private static final String LOG_TAG = "UserProfileEditActivity";
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings, linearPaInformation, linearPersonalInformation;
	private ThreadProcessProfileEdit threadProcessProfileEdit;
	private ImageView imageViewProfileImage;
	private SegmentedRadioGroup segmentInformationType;
	private EditText textViewFullName, textViewGender, textViewBirthday,
			textViewCompany, textViewPosition, textViewLocalNumber,
			textViewMobileNumber, textViewMainNumber, textViewFaxNumber,
			textViewEmail, textViewNationality, textViewPaName,
			textViewPaMobile, textViewPaTelephone, textViewPaEmail ;
	private ThreadProcessProfile threadProcessProfile;
	private UserProfile userProfile;
	private TextView textViewProfileAddressInTitle , textViewProfileNameInTitle;
	private TextView textViewSubmit, textViewCancel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.profile_edit);

		activity = this;

		initLayouts();

		displayEditProfileDetails();
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

		textViewFullName = (EditText) findViewById(R.id.textViewFullName);
		textViewGender = (EditText) findViewById(R.id.textViewGender);
		textViewBirthday = (EditText) findViewById(R.id.textViewBirthday);
		textViewCompany = (EditText) findViewById(R.id.textViewCompany);
		textViewPosition = (EditText) findViewById(R.id.textViewPosition);
		textViewLocalNumber = (EditText) findViewById(R.id.textViewLocalNumber);
		textViewMobileNumber = (EditText) findViewById(R.id.textViewMobileNumber);
		textViewMainNumber = (EditText) findViewById(R.id.textViewMainNumber);
		textViewFaxNumber = (EditText) findViewById(R.id.textViewFaxNumber);
		textViewEmail = (EditText) findViewById(R.id.textViewEmail);
		textViewProfileNameInTitle = (TextView) findViewById(R.id.textViewProfileNameInTitle);
		textViewProfileAddressInTitle = (TextView) findViewById(R.id.textViewProfileAddressInTitle);
		textViewNationality = (EditText) findViewById(R.id.textViewNationality);
		textViewPaName = (EditText) findViewById(R.id.textViewPaName);
		textViewPaMobile = (EditText) findViewById(R.id.textViewPaMobile);
		textViewPaTelephone = (EditText) findViewById(R.id.textViewPaTelephone);
		textViewPaEmail = (EditText) findViewById(R.id.textViewPaEmail);

		imageViewProfileImage = (ImageView) findViewById(R.id.imageViewProfileImage);

		linearPaInformation = (LinearLayout) findViewById(R.id.linearPaInformation);
		linearPersonalInformation = (LinearLayout) findViewById(R.id.linearPersonalInformation);

		linearPaInformation.setVisibility(View.GONE);
		linearPersonalInformation.setVisibility(View.VISIBLE);

		segmentInformationType = (SegmentedRadioGroup) findViewById(R.id.segmentInformationType);
		segmentInformationType
				.setOnCheckedChangeListener(checkedChangeListener);

		textViewSubmit = (TextView) findViewById(R.id.textViewSubmit);
		textViewCancel = (TextView) findViewById(R.id.textViewCancel);
		textViewSubmit.setOnClickListener(clickListener);
		textViewCancel.setOnClickListener(clickListener);

	}

	private void displayEditProfileDetails() {

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			userProfile = (UserProfile) bundle.get(StringUtils.EXTRA_PROFILE);
		}

		if (userProfile != null) {
			textViewProfileNameInTitle
					.setText(getString(userProfile.nameSurnameE));
			textViewProfileAddressInTitle
					.setText(getString(userProfile.positionE) + ", "
							+ getString(userProfile.companyE));

			textViewFullName.setText(getString(userProfile.nameSurnameE));
			textViewGender.setText(getString(userProfile.sex));
			textViewBirthday.setText(getString(userProfile.birthDate));
			textViewCompany.setText(getString(userProfile.companyE));
			textViewPosition.setText(getString(userProfile.positionE));
			textViewLocalNumber.setText(getString(userProfile.localMobile));
			textViewMobileNumber.setText(getString(userProfile.officialMobile));
			textViewMainNumber.setText(getString(userProfile.telephone));
			textViewFaxNumber.setText(getString(userProfile.fax));
			textViewEmail.setText(getString(userProfile.email));
			textViewNationality.setText(getString(userProfile.nationalityE));

			textViewPaName.setText(getString(userProfile.paName));
			textViewPaMobile.setText(getString(userProfile.paMobile));
			textViewPaTelephone.setText(getString(userProfile.paTelephone));
			textViewPaEmail.setText(getString(userProfile.paEmail));

			try {
				new DownloadImageTask(imageViewProfileImage)
						.execute(userProfile.profileImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getString(String str) {
		if (str != null)
			return str;
		else
			return "";
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (group == segmentInformationType) {

				if (checkedId == R.id.radioButtonPersonalInformation) {
					linearPaInformation.setVisibility(View.GONE);
					linearPersonalInformation.setVisibility(View.VISIBLE);
				} else if (checkedId == R.id.radioButtonPaInformation) {
					linearPaInformation.setVisibility(View.VISIBLE);
					linearPersonalInformation.setVisibility(View.GONE);
				}
			}

		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == linearHome) {
				finish();
			} else if (view == linearAccountUser) {

			} else if (view == linearEvent) {
				showActivity(EventListActivity.class, DIRECTION_BACK);
			} else if (view == linearSettings) {
				showActivity(SettingsActivity.class, DIRECTION_BACK);
			} else if (view == textViewSubmit) {
				showLoading(Messages.LOADING);
				threadProcessProfileEdit = new ThreadProcessProfileEdit();
				threadProcessProfileEdit.start();
			} else if (view == textViewCancel) {
				finish();
			}
		}
	};

	class ThreadProcessProfileEdit extends Thread {

		public ThreadProcessProfileEdit() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getProfileEditParameters(preference.getUserid(),
								userProfile);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_PROFILE_EDIT);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getProfileEditStatus();

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

			getProfileEditHandler.sendMessage(msg);

		}
	};

	private Handler getProfileEditHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessProfileEdit.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
			} else {
				alert(sResponseMsg);
			}

		}
	};

	class ThreadProcessProfile extends Thread {

		public ThreadProcessProfile() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getProfileParameters(preference.getUserid());

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_PROFILE);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getProfileStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						userProfile = parser.getUserProfile();

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

			getEventDetailsHandler.sendMessage(msg);

		}
	};

	private Handler getEventDetailsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessProfile.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				displayEditProfileDetails();
			} else {
				alert(sResponseMsg);
			}

		}
	};

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			hideLoading();
			bmImage.setImageBitmap(result);
		}
	}

	@Override
	public void goBack() {
		super.goBack();
	}
}