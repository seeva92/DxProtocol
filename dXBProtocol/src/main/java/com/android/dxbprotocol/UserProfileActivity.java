package com.android.dxbprotocol;

import java.io.InputStream;
import java.util.LinkedHashMap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class UserProfileActivity extends AppActivity implements
		View.OnClickListener, OnShowcaseEventListener {

	private AppActivity activity = null;
	private static final String LOG_TAG = "UserProfileActivity";
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings, linearPaInformation, linearPersonalInformation;
	private ThreadProcessProfileEdit threadProcessProfileEdit;
	private ImageView imageViewProfileImage;
	private SegmentedRadioGroup segmentInformationType;
	private TextView textViewFullName, textViewGender, textViewBirthday,
			textViewCompany, textViewPosition, textViewLocalNumber,
			textViewMobileNumber, textViewMainNumber, textViewFaxNumber,
			textViewEmail, textViewProfileNameInTitle,
			textViewProfileAddressInTitle, textViewNationality, textViewPaName,
			textViewPaMobile, textViewPaTelephone, textViewPaEmail;
	private ThreadProcessProfile threadProcessProfile;
	private UserProfile userProfile;
	private ImageView imageViewEditProfile;
	ShowcaseView sv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.profile);

		activity = this;

		// 4rth git hub
		if (preference.isCoachMark()) {
			// showCaseProfile();
			onCoachMark();
		}

		initLayouts();

		showLoading(Messages.LOADING);
		threadProcessProfile = new ThreadProcessProfile();
		threadProcessProfile.start();
	}

	public void onCoachMark() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.coach_mark_profile);
		dialog.setCanceledOnTouchOutside(true);
		// for dismissing anywhere you touch
		View masterView = dialog
				.findViewById(R.id.coach_mark_master_profile_view);
		masterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showCaseProfile() {
		ViewTarget target = new ViewTarget(R.id.tabNavigator, this);
		sv = new ShowcaseView.Builder(this, true)
				.setTarget(target)
				.setContentTitle("")
				.setContentText(
						"Switch between your personal Profile or the profile of your personal Assistant")
				.setStyle(R.style.CustomShowcaseTheme2).hideOnTouchOutside()
				.setShowcaseEventListener(this).build();
		sv.hideButton();
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

		textViewFullName = (TextView) findViewById(R.id.textViewFullName);
		textViewGender = (TextView) findViewById(R.id.textViewGender);
		textViewBirthday = (TextView) findViewById(R.id.textViewBirthday);
		textViewCompany = (TextView) findViewById(R.id.textViewCompany);
		textViewPosition = (TextView) findViewById(R.id.textViewPosition);
		textViewLocalNumber = (TextView) findViewById(R.id.textViewLocalNumber);
		textViewMobileNumber = (TextView) findViewById(R.id.textViewMobileNumber);
		textViewMainNumber = (TextView) findViewById(R.id.textViewMainNumber);
		textViewFaxNumber = (TextView) findViewById(R.id.textViewFaxNumber);
		textViewEmail = (TextView) findViewById(R.id.textViewEmail);
		textViewProfileNameInTitle = (TextView) findViewById(R.id.textViewProfileNameInTitle);
		textViewProfileAddressInTitle = (TextView) findViewById(R.id.textViewProfileAddressInTitle);
		textViewNationality = (TextView) findViewById(R.id.textViewNationality);
		textViewPaName = (TextView) findViewById(R.id.textViewPaName);
		textViewPaMobile = (TextView) findViewById(R.id.textViewPaMobile);
		textViewPaTelephone = (TextView) findViewById(R.id.textViewPaTelephone);
		textViewPaEmail = (TextView) findViewById(R.id.textViewPaEmail);

		imageViewProfileImage = (ImageView) findViewById(R.id.imageViewProfileImage);

		linearPaInformation = (LinearLayout) findViewById(R.id.linearPaInformation);
		linearPersonalInformation = (LinearLayout) findViewById(R.id.linearPersonalInformation);

		linearPaInformation.setVisibility(View.GONE);
		linearPersonalInformation.setVisibility(View.VISIBLE);

		segmentInformationType = (SegmentedRadioGroup) findViewById(R.id.segmentInformationType);
		segmentInformationType
				.setOnCheckedChangeListener(checkedChangeListener);

		imageViewEditProfile = (ImageView) findViewById(R.id.imageViewEditProfile);
		imageViewEditProfile.setOnClickListener(clickListener);

	}

	private void displayProfileDetails() {
		if (userProfile != null) {

			textViewGender.setText(getString(userProfile.sex));
			textViewBirthday.setText(getString(userProfile.birthDate));
			textViewLocalNumber.setText(getString(userProfile.localMobile));
			textViewMobileNumber.setText(getString(userProfile.officialMobile));
			textViewMainNumber.setText(getString(userProfile.telephone));
			textViewFaxNumber.setText(getString(userProfile.fax));
			textViewEmail.setText(getString(userProfile.email));

			textViewPaName.setText(getString(userProfile.paName));
			textViewPaMobile.setText(getString(userProfile.paMobile));
			textViewPaTelephone.setText(getString(userProfile.paTelephone));
			textViewPaEmail.setText(getString(userProfile.paEmail));

			if (preference.isLanguageIsEnglish()) {
				textViewProfileNameInTitle
						.setText(getString(userProfile.nameSurnameE));
				textViewProfileAddressInTitle
						.setText(getString(userProfile.positionE) + ", "
								+ getString(userProfile.companyE));

				textViewFullName.setText(getString(userProfile.nameSurnameE));
				textViewCompany.setText(getString(userProfile.companyE));
				textViewPosition.setText(getString(userProfile.positionE));
				textViewNationality
						.setText(getString(userProfile.nationalityE));
			} else {
				textViewProfileNameInTitle
						.setText(getString(userProfile.nameSurnameA));
				textViewProfileAddressInTitle
						.setText(getString(userProfile.positionA) + ", "
								+ getString(userProfile.companyA));

				textViewFullName.setText(getString(userProfile.nameSurnameA));
				textViewCompany.setText(getString(userProfile.companyA));
				textViewPosition.setText(getString(userProfile.positionA));
				textViewNationality
						.setText(getString(userProfile.nationalityA));
			}

			try {
				if (userProfile.profileImage != null) {
					new DownloadImageTask(imageViewProfileImage)
							.execute(userProfile.profileImage);
				}
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
			} else if (view == imageViewEditProfile) {
				Intent intent = new Intent(activity,
						UserProfileEditActivity.class);
				intent.putExtra(StringUtils.EXTRA_PROFILE, userProfile);
				startActivity(intent);
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
				displayProfileDetails();
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
				// Log.e("Error", e.getMessage());
				// e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			hideLoading();
			if (result != null) {
				bmImage.setImageBitmap(result);
			}
		}
	}

	@Override
	public void goBack() {
		super.goBack();
	}

	@Override
	public void onShowcaseViewHide(ShowcaseView showcaseView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShowcaseViewShow(ShowcaseView showcaseView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}