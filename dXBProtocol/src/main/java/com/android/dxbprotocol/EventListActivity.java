package com.android.dxbprotocol;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.api.APIParams;
import com.android.dxbprotocol.api.APIParser;
import com.android.dxbprotocol.api.ParseStatus;
import com.android.dxbprotocol.classes.SegmentedRadioGroup;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.config.StringUtils;
import com.android.dxbprotocol.utility.AllEvents;
import com.android.dxbprotocol.utility.Event;
import com.android.dxbprotocol.utility.EventDetails;
import com.android.dxbprotocol.utility.L;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class EventListActivity extends AppActivity implements
		View.OnClickListener, OnShowcaseEventListener {

	private AppActivity activity = null;
	private LinearLayout linearHome, linearAccountUser, linearEvent,
			linearSettings, linearEvents;
	private static final String LOG_TAG = "EventListActivity";
	private ThreadProcessUpcomingListEvents threadProcessUpcomingListEvents;
	private ThreadProcessPastListEvents threadProcessPastListEvents;
	private AllEvents allEvents;
	private SegmentedRadioGroup segmentExploreType;
	private EventDetails eventDetails;
	private ThreadProcessEventsDetails threadProcessEventsDetails;
	private String eventId;
	private ThreadProcessSearchUpComingListEvents threadProcessSearchUpComingListEvents;
	private ThreadProcessSearchPastListEvents threadProcessSearchPastListEvents;
	ShowcaseView sv;
	private SearchView searchViewEvents;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To setup the title bar
		setAppTitle(R.layout.list_events);

		activity = this;

		// 3rd Github
		if (preference.isCoachMark()) {
			// showCaseEvent();
			onCoachMark();
		}

		initLayouts();
	}

	public void onCoachMark() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.coach_mark_events);
		dialog.setCanceledOnTouchOutside(true);
		// for dismissing anywhere you touch
		View masterView = dialog
				.findViewById(R.id.coach_mark_master_events_view);
		masterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showCaseEvent() {
		ViewTarget target = new ViewTarget(R.id.eventstab, this);
		sv = new ShowcaseView.Builder(this, true)
				.setTarget(target)
				.setContentTitle("")
				.setContentText(
						"Switch between the future events and past events")
				.setStyle(R.style.CustomShowcaseTheme2).hideOnTouchOutside()
				.setShowcaseEventListener(this).build();
		sv.hideButton();
	}

	/**
	 * To initialize the layout and assign listeners for that fields.
	 */
	private void initLayouts() {

		linearEvents = (LinearLayout) findViewById(R.id.linearEvents);
		linearHome = (LinearLayout) findViewById(R.id.linearHome);
		linearAccountUser = (LinearLayout) findViewById(R.id.linearAccountUser);
		linearEvent = (LinearLayout) findViewById(R.id.linearEvent);
		linearSettings = (LinearLayout) findViewById(R.id.linearSettings);
		linearHome.setOnClickListener(clickListener);
		linearAccountUser.setOnClickListener(clickListener);
		linearEvent.setOnClickListener(clickListener);
		linearSettings.setOnClickListener(clickListener);

		segmentExploreType = (SegmentedRadioGroup) findViewById(R.id.segmentExploreType);
		segmentExploreType.setOnCheckedChangeListener(checkedChangeListener);

		searchViewEvents = (SearchView) findViewById(R.id.searchViewEvents);
		searchViewEvents.setQueryHint(getResources().getString(
				R.string.search_events));

		searchViewEvents
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextChange(String newText) {
						return true;
					}

					@Override
					public boolean onQueryTextSubmit(String query) {
						if (segmentExploreType.getCheckedRadioButtonId() == R.id.radioButtonUpcoming) {
							showLoading(Messages.LOADING);
							threadProcessSearchUpComingListEvents = new ThreadProcessSearchUpComingListEvents(
									query);
							threadProcessSearchUpComingListEvents.start();
						} else {
							showLoading(Messages.LOADING);
							threadProcessSearchPastListEvents = new ThreadProcessSearchPastListEvents(
									query);
							threadProcessSearchPastListEvents.start();
						}
						return true;
					}
				});

		searchViewEvents.performClick();
		searchViewEvents.requestFocus();

		allEvents = new AllEvents();
		eventDetails = new EventDetails();

		showLoading(Messages.LOADING);
		threadProcessUpcomingListEvents = new ThreadProcessUpcomingListEvents();
		threadProcessUpcomingListEvents.start();

	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (group == segmentExploreType) {
				if (checkedId == R.id.radioButtonUpcoming) {
					listEvents(allEvents.arrUpcomingEvents);
				} else if (checkedId == R.id.radioButtonPast) {
					listEvents(allEvents.arrPastEvents);
				}
			}

		}
	};

	private OnClickListener linearClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.linearEventItem) {

				int tagIndex = (Integer) view.getTag();
				ArrayList<Event> arrEvents = new ArrayList<Event>();

				if (segmentExploreType.getCheckedRadioButtonId() == R.id.radioButtonUpcoming) {
					arrEvents = allEvents.arrUpcomingEvents;
				} else {
					arrEvents = allEvents.arrPastEvents;
				}

				eventId = arrEvents.get(tagIndex).eventID;

				showLoading("Loading...");
				threadProcessEventsDetails = new ThreadProcessEventsDetails(
						eventId);
				threadProcessEventsDetails.start();

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
			} else if (view == linearSettings) {
				showActivity(SettingsActivity.class, DIRECTION_FRONT);
			}
		}
	};

	class ThreadProcessUpcomingListEvents extends Thread {

		public ThreadProcessUpcomingListEvents() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getAllEventsParameters(preference.getUserid());

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_LIST_UPCOMING_EVENTS);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventsStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						allEvents.arrUpcomingEvents = parser.getEvents();

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

			getUpcomingHandler.sendMessage(msg);

		}
	};

	private Handler getUpcomingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessUpcomingListEvents.interrupt();
			} catch (Exception e) {
			}

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				showLoading(Messages.LOADING);
				threadProcessPastListEvents = new ThreadProcessPastListEvents();
				threadProcessPastListEvents.start();
			} else {
				alert(sResponseMsg);
			}

		}
	};

	class ThreadProcessPastListEvents extends Thread {

		public ThreadProcessPastListEvents() {
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getAllEventsParameters(preference.getUserid());

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_LIST_PAST_EVENTS);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventsStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						allEvents.arrPastEvents = parser.getEvents();

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

			getPastHandler.sendMessage(msg);

		}
	};

	private Handler getPastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessPastListEvents.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				listEvents(allEvents.arrUpcomingEvents);
			} else {
				alert(sResponseMsg);
			}

		}
	};

	class ThreadProcessSearchPastListEvents extends Thread {

		String searchText;

		public ThreadProcessSearchPastListEvents(String searchText) {
			this.searchText = searchText;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getSearchEventsParameters(preference.getUserid(),
								searchText);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_SEARCH_EVENT_LIST_PAST);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventsStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						allEvents.arrSearchPastEvents = parser.getEvents();

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

			getSearchPastHandler.sendMessage(msg);

		}
	};

	private Handler getSearchPastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessSearchPastListEvents.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				listEvents(allEvents.arrSearchPastEvents);
			} else {
				alert(sResponseMsg);
			}

		}
	};

	class ThreadProcessSearchUpComingListEvents extends Thread {

		String searchText;

		public ThreadProcessSearchUpComingListEvents(String searchText) {
			this.searchText = searchText;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getSearchEventsParameters(preference.getUserid(),
								searchText);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_SEARCH_EVENT_LIST_UPCOMING);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventsStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						allEvents.arrSearchUpcomingEvents = parser.getEvents();

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

			getSearchUpComingHandler.sendMessage(msg);

		}
	};

	private Handler getSearchUpComingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				threadProcessSearchUpComingListEvents.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				listEvents(allEvents.arrSearchUpcomingEvents);
			} else {
				alert(sResponseMsg);
			}

		}
	};

	private void listEvents(ArrayList<Event> arrEvents) {
		linearEvents.removeAllViews();
		LayoutInflater inflater = activity.getLayoutInflater();
		for (int i = 0; i < arrEvents.size(); i++) {

			Event upComingEvent = arrEvents.get(i);

			LinearLayout rowView = (LinearLayout) inflater.inflate(
					R.layout.event_item, null, true);

			LinearLayout linearEventItem = (LinearLayout) rowView
					.findViewById(R.id.linearEventItem);
			linearEventItem.setTag(i);
			linearEventItem.setOnClickListener(linearClickListener);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(5, 0, 0, 0);

			rowView.setLayoutParams(layoutParams);

			TextView textViewTitleDate = (TextView) rowView
					.findViewById(R.id.textViewTitleDate);
			TextView textViewTitleDay = (TextView) rowView
					.findViewById(R.id.textViewTitleDay);
			TextView textViewTitleMonth = (TextView) rowView
					.findViewById(R.id.textViewTitleMonth);
			TextView textViewTitleYear = (TextView) rowView
					.findViewById(R.id.textViewTitleYear);
			TextView textViewDescTime = (TextView) rowView
					.findViewById(R.id.textViewDescTime);
			TextView textViewDescDetail = (TextView) rowView
					.findViewById(R.id.textViewDescDetail);

			String eventDate = upComingEvent.eventDate;
			String[] arr = eventDate.split("-");

			if (arr.length == 3) {
				textViewTitleDate.setText(arr[2]);
				textViewTitleMonth.setText(getMonthForInt(Integer
						.parseInt(arr[1])));
				textViewTitleYear.setText(arr[0]);
			} else {
				textViewTitleDate.setText("");
				textViewTitleMonth.setText("");
				textViewTitleYear.setText("");
			}

			try {
				textViewTitleDay
						.setText(upComingEvent.eventDay.substring(0, 3));
			} catch (Exception e) {
				textViewTitleDay.setText("");
			}

			textViewDescTime.setText(upComingEvent.eventTime);
			if (preference.isLanguageIsEnglish()) {
				textViewDescDetail.setText(upComingEvent.eventTitleE);
			} else {
				textViewDescDetail.setText(upComingEvent.eventTitleA);
			}

			linearEvents.addView(rowView);

		}
	}

	class ThreadProcessEventsDetails extends Thread {

		String eventId;

		public ThreadProcessEventsDetails(String eventId) {
			this.eventId = eventId;
		}

		@Override
		public void run() {

			Bundle bndle = new Bundle();
			String sCode, sDesc;
			Message msg = new Message();

			try {

				Log.i(LOG_TAG, "Thread called");

				LinkedHashMap<String, String> params = APIParams
						.getEventDetailsParameters(preference.getUserid(),
								eventId);

				APIManager api = new APIManager(params, activity);

				APIManager.Status status = api
						.getResultResponse(APIManager.ACTION_EVENT_DETAILS);

				if (status != APIManager.Status.SUCCESS) {
					String error_msg = api.getErrorMessage();
					sCode = "ERROR";
					sDesc = error_msg;
				} else {

					APIParser parser = new APIParser(activity,
							api.getResponse());

					ParseStatus parseStatus = parser.getEventDetailsStatus();

					if (parseStatus.ResponseCode.equals(APIParser.ERROR)) {
						sCode = "ERROR";
						sDesc = parseStatus.ResponseDesc;
					} else {

						eventDetails = parser.getEventDetails();

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
				threadProcessEventsDetails.interrupt();
			} catch (Exception e) {
			}

			hideLoading();

			String sResponseCode = msg.getData().getString("CODE");
			String sResponseMsg = msg.getData().getString("DESC");

			if (sResponseCode.equalsIgnoreCase("SUCCESS")) {
				Intent intent = new Intent(activity, EventDetailsActivity.class);
				intent.putExtra(StringUtils.EXTRA_EVENT_DETAILS, eventDetails);
				intent.putExtra(StringUtils.EXTRA_EVENT_ID, eventId);
				startActivity(intent);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}