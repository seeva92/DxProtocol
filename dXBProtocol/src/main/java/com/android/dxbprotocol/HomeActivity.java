package com.android.dxbprotocol;

import java.io.InputStream;
import java.util.LinkedHashMap;

import android.app.Dialog;
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

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.utility.L;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;

public class HomeActivity extends AppActivity implements View.OnClickListener,
		OnShowcaseEventListener {

	private AppActivity activity = null;
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings;
	private static final String LOG_TAG = "LoginActivity";
	private ThreadProcessHomeLogo threadProcessLogIn;
	ShowcaseView sv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.home);

		if (preference.isCoachMark()) {
			onCoachMark();
		}

		activity = this;

		initLayouts();
	}

	public void onCoachMark() {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.coach_mark);
		dialog.setCanceledOnTouchOutside(true);
		// for dismissing anywhere you touch
		View masterView = dialog.findViewById(R.id.coach_mark_master_view);
		masterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.show();
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

		showLoading(Messages.LOADING);
		threadProcessLogIn = new ThreadProcessHomeLogo();
		threadProcessLogIn.start();

	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == linearHome) {

			} else if (view == linearAccountUser) {
				showActivity(UserProfileActivity.class, DIRECTION_FRONT);
			} else if (view == linearEvent) {
				showActivity(EventListActivity.class, DIRECTION_FRONT);
			} else if (view == linearSettings) {
				showActivity(SettingsActivity.class, DIRECTION_FRONT);
			}
		}
	};

	class ThreadProcessHomeLogo extends Thread {

		public ThreadProcessHomeLogo() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getHomeLogoParameters();

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_HOME_LOGO);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getHomeLogo();

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

			try {
				threadProcessLogIn.interrupt();
			} catch (Exception e) {
			}

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				new DownloadImageTask(
						(ImageView) findViewById(R.id.imageViewHomeLogo))
						.execute(sResponseMsg);
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