package com.android.dxbprotocol;

import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dxbprotocol.adapters.EventDetailsImagesAdapter;
import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.config.StringUtils;
import com.android.dxbprotocol.utility.EventDetails;
import com.android.dxbprotocol.utility.L;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class EventDetailsActivity extends AppActivity implements
		View.OnClickListener, OnShowcaseEventListener,
		BaseSliderView.OnSliderClickListener {

	private AppActivity activity = null;
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings, linearEvents, buttonrateLinear;
	private static final String LOG_TAG = "EventDetailsActivity";
	private EventDetails eventDetails;
	private TextView textViewEventDetailsTitle, textViewTitleDate,
			textViewDescDetail, textViewAddress, textViewDressCode,
			textViewNote;
	private ViewPager pager = null;
	private EventDetailsImagesAdapter pagerAdapter = null;
	private Button buttonNo, buttonYes, buttonRate;
	private ThreadProcessEventResponse threadProcessEventResponse;
	private String eventId;
	ShowcaseView sv;
	private SliderLayout mDemoSlider;
	private Context context = this;

	// http://stackoverflow.com/questions/13664155/dynamically-add-and-remove-view-to-viewpager

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.event_details);

		activity = this;

		// 2nd github check
		if (preference.isCoachMark()) {

			onCoachMark();
			// showCaseEvenetDetails();

		}

		initLayouts();

		displayContents();

		loadImages();
	}

	private void loadImages() {
		HashMap<String, String> url_maps = new HashMap<String, String>();
		url_maps.put(
				"Hannibal",
				"http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
		url_maps.put("Big Bang Theory",
				"http://tvfiles.alphacoders.com/100/hdclearart-10.png");
		url_maps.put("House of Cards",
				"http://cdn3.nflximg.net/images/3093/2043093.jpg");
		url_maps.put(
				"Game of Thrones",
				"http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(this);
			// initialize a SliderLayout
			textSliderView.description(name).image(url_maps.get(name))
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);

			// add your extra information
			textSliderView.getBundle().putString("extra", name);

			mDemoSlider.addSlider(textSliderView);

			mDemoSlider
					.setPresetTransformer(SliderLayout.Transformer.Accordion);
			mDemoSlider
					.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
			mDemoSlider.setCustomAnimation(new DescriptionAnimation());
			mDemoSlider.setDuration(4000);
		}

	}

	private void showCaseEvenetDetails() {
		ViewTarget target = new ViewTarget(R.id.buttonLinear, this);
		sv = new ShowcaseView.Builder(this, true).setTarget(target)
				.setContentTitle("")
				.setContentText("Accept or reject the invitaion")
				.hideOnTouchOutside().setStyle(R.style.CustomShowcaseTheme2)
				.setShowcaseEventListener(this).build();
		sv.hideButton();

	}

	public void onCoachMark() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.coach_mark_event_detail);
		dialog.setCanceledOnTouchOutside(true);
		// for dismissing anywhere you touch
		View masterView = dialog
				.findViewById(R.id.coach_mark_master_events_detail_view);
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

		eventDetails = new EventDetails();

		linearEvents = (LinearLayout) findViewById(R.id.linearEvents);
		linearHome = (LinearLayout) findViewById(R.id.linearHome);
		linearAccountUser = (LinearLayout) findViewById(R.id.linearAccountUser);
		linearEvent = (LinearLayout) findViewById(R.id.linearEvent);
		linearSettings = (LinearLayout) findViewById(R.id.linearSettings);
		linearHome.setOnClickListener(clickListener);
		linearAccountUser.setOnClickListener(clickListener);
		linearEvent.setOnClickListener(clickListener);
		linearSettings.setOnClickListener(clickListener);
		buttonrateLinear = (LinearLayout) findViewById(R.id.buttonrateLinear);
		buttonrateLinear.setOnClickListener(clickListener);

		textViewTitleDate = (TextView) findViewById(R.id.textViewTitleDate);
		textViewDescDetail = (TextView) findViewById(R.id.textViewDescDetail);
		textViewAddress = (TextView) findViewById(R.id.textViewAddress);
		textViewDressCode = (TextView) findViewById(R.id.textViewDressCode);
		textViewNote = (TextView) findViewById(R.id.textViewNote);

		pagerAdapter = new EventDetailsImagesAdapter();
		pager = (ViewPager) findViewById(R.id.view_pager);
		pager.setAdapter(pagerAdapter);

		// Create an initial view to display; must be a subclass of FrameLayout.
		LayoutInflater inflater = activity.getLayoutInflater();
		LinearLayout v0 = (LinearLayout) inflater.inflate(
				R.layout.event_details_image, null);
		pagerAdapter.addView(v0, 0);

		buttonNo = (Button) findViewById(R.id.buttonNo);
		buttonYes = (Button) findViewById(R.id.buttonYes);
		buttonRate = (Button) findViewById(R.id.buttonRate);
		buttonRate.setOnClickListener(clickListener);
		buttonNo.setOnClickListener(clickListener);
		buttonYes.setOnClickListener(clickListener);

		textViewEventDetailsTitle = (TextView) findViewById(R.id.textViewEventDetailsTitle);
		mDemoSlider = (SliderLayout) findViewById(R.id.slider);

	}

	// Here's what the app should do to add a view to the ViewPager.
	public void addView(View newPage) {
		int pageIndex = pagerAdapter.addView(newPage);
		// You might want to make "newPage" the currently displayed page:
		pager.setCurrentItem(pageIndex, true);
	}

	// -----------------------------------------------------------------------------
	// Here's what the app should do to remove a view from the ViewPager.
	public void removeView(View defunctPage) {
		int pageIndex = pagerAdapter.removeView(pager, defunctPage);
		// You might want to choose what page to display, if the current page
		// was "defunctPage".
		if (pageIndex == pagerAdapter.getCount())
			pageIndex--;
		pager.setCurrentItem(pageIndex);
	}

	// -----------------------------------------------------------------------------
	// Here's what the app should do to get the currently displayed page.
	public View getCurrentPage() {
		return pagerAdapter.getView(pager.getCurrentItem());
	}

	// -----------------------------------------------------------------------------
	// Here's what the app should do to set the currently displayed page.
	// "pageToShow" must
	// currently be in the adapter, or this will crash.
	public void setCurrentPage(View pageToShow) {
		pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
	}

	private void displayContents() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			eventDetails = (EventDetails) bundle
					.get(StringUtils.EXTRA_EVENT_DETAILS);

			if (preference.isLanguageIsEnglish()) {
				textViewDescDetail.setText(eventDetails.eventTitleE);
				textViewAddress.setText(eventDetails.eventLocationE);
				textViewDressCode.setText(eventDetails.eventDressCodeE);
				textViewNote.setText(eventDetails.eventNoteE);
			} else {
				textViewDescDetail.setText(eventDetails.eventTitleA);
				textViewAddress.setText(eventDetails.eventLocationA);
				textViewDressCode.setText(eventDetails.eventDressCodeA);
				textViewNote.setText(eventDetails.eventNoteA);
			}

			textViewTitleDate.setText(eventDetails.eventTime);
			textViewEventDetailsTitle.setText(eventDetails.eventDate);

			eventId = bundle.getString(StringUtils.EXTRA_EVENT_ID);

			try {
				// new DownloadImageTask(
				// (ImageView) findViewById(R.id.imageViewEventDetailImages))
				// .execute(eventDetails.arrImages.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

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

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view == linearHome) {
				showActivity(HomeActivity.class, DIRECTION_FRONT);
			} else if (view == linearAccountUser) {
				showActivity(UserProfileActivity.class, DIRECTION_FRONT);
			} else if (view == linearEvent) {
			} else if (view == linearSettings) {
				showActivity(SettingsActivity.class, DIRECTION_FRONT);
			} else if (view == buttonNo) {

			} else if (view == buttonYes) {
				showLoading(Messages.LOADING);
				threadProcessEventResponse = new ThreadProcessEventResponse(
						eventId, "1", null);
				threadProcessEventResponse.start();
			} else if (view == buttonRate) {
				rateDialog();
			}
		}

		private void rateDialog() {
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.rateone_dialog);
			dialog.setTitle("Please rate the event:");

			Button dialogButton = (Button) dialog
					.findViewById(R.id.submitbutton);
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					compaintDialog();
				}
			});

			dialog.show();

		}

		private void compaintDialog() {
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.ratetwo_dialog);
			Button skipButton = (Button) dialog.findViewById(R.id.skipbutton);
			Button sendButton = (Button) dialog.findViewById(R.id.sendbutton);
			skipButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			sendButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					thankyouDialog();

				}
			});
			dialog.show();
		}

		private void thankyouDialog() {
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.thankyou_dialog);
			Button closeButton = (Button) dialog.findViewById(R.id.closebutton);
			closeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
	};

	class ThreadProcessEventResponse extends Thread {

		String eventId;
		String response;
		String responseText;

		public ThreadProcessEventResponse(String eventId, String response,
				String responseText) {
			this.eventId = eventId;
			this.response = response;
			this.responseText = responseText;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getEventResponseParameters(preference.getUserid(),
								eventId, response, responseText);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_EVENT_RESPONSE);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventResponseStatus();

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
				threadProcessEventResponse.interrupt();
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

	class ThreadProcessEventRating extends Thread {

		String eventId;
		String rating;
		String ratingComment;

		public ThreadProcessEventRating(String eventId, String response,
				String responseText) {
			this.eventId = eventId;
			this.rating = response;
			this.ratingComment = responseText;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getEventRatingParameters(preference.getUserid(),
								eventId, rating, ratingComment);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_RATING);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventRatingStatus();

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

			getEventRatingHandler.sendMessage(msg);

		}
	};

	private Handler getEventRatingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessEventResponse.interrupt();
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

	String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Toast.makeText(this, slider.getBundle().get("extra") + "",
				Toast.LENGTH_SHORT).show();

	}
}