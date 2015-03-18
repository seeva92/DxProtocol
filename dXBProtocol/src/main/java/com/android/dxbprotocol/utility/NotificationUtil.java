package com.android.dxbprotocol.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android.dxbprotocol.LoginActivity;
import com.android.dxbprotocol.R;
import com.android.dxbprotocol.config.StringUtils;

public class NotificationUtil {
	private static int TEST_FINISHED_NOTI = R.string.app_name;
	private static int TEST_RUNNING_NOTI = R.string.app_name;

	/**
	 * Show a notification while this service is running.
	 */
	public static void showFinishedNotification(Context context, String sType,
			String name, String msg) {
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence text = "Notification Message";
		Notification notification = new Notification(R.drawable.ic_launcher, text,
				System.currentTimeMillis());
		Intent intent_finished = new Intent(context.getApplicationContext(),
				LoginActivity.class);
		// intent_finished.putExtra(StringUtils.EXTRA_STATUS_MSG, text);

		intent_finished.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, intent_finished,
				PendingIntent.FLAG_ONE_SHOT);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context.getApplicationContext(),
				context.getText(R.string.app_name), text, contentIntent);
		mNM.notify(0, notification);
	}

	/**
	 * Show a notification while this service is running.
	 */
	public static void showRunningNotification(Context context, String sType) {
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence text = " Running";
		Notification notification = new Notification(R.drawable.ic_launcher, text,
				System.currentTimeMillis());
		Intent running = new Intent(context.getApplicationContext(),
				LoginActivity.class);
		// running.putExtra(StringUtils.EXTRA_TEST_STATUS,
		// StringUtils.EXTRA_STATUS_RUNNING);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, running, 0);
		notification.flags = Notification.FLAG_NO_CLEAR;

		notification.setLatestEventInfo(context.getApplicationContext(),
				context.getText(R.string.app_name), text, contentIntent);
		mNM.notify(0, notification);
	}

	public static void cancelAllNotification(Context context) {

		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			mNM.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cancelNotification(Context context, String sTestType) {
		int id = 0;
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			mNM.cancel(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
