package com.android.dxbprotocol.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

import com.android.dxbprotocol.config.Constants;

/**
 * Alert class to show the alert messages.
 * 
 */
public class AppAlert extends AlertDialog.Builder {

	public static final String APP_NAME =  Constants.APP_NAME;
	/**
	 * Constructor to show the alert dialog with title as application name
	 * 
	 * @param context
	 *            Activity which shows the alert.
	 * @param msg
	 *            The message to be displayed.
	 */
	public AppAlert(Context context, String msg) {
		super(context);
		this.setTitle(APP_NAME);
		this.setMessage(msg);
		this.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				okClickListener();
			}
		});
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Constructor to show the alert dialog with title as application name and
	 * message id
	 * 
	 * 
	 * @param context
	 *            Activity which shows the alert.
	 * @param sMsgCode
	 *            Message id to be displayed with title
	 * @param msg
	 *            The message to be displayed.
	 */
	public AppAlert(Context context, String sMsgCode, String msg) {
		super(context);
		String sTitle = APP_NAME;
		if (sMsgCode != null && !sMsgCode.trim().equals("")
				&& !sMsgCode.trim().equals("0"))
			sTitle += " :  " + sMsgCode;
		this.setTitle(sTitle);
		this.setMessage(msg);
		this.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				okClickListener();
			}
		});
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Constructor to show alert with message and list of options
	 * 
	 * @param context
	 *            Activity which shows the alert.
	 * @param msg
	 *            The message to be displayed.
	 * @param items
	 *            List of optinos to be displayed.
	 */

	public AppAlert(Context context, String msg, String[] items) {
		super(context);
		this.setTitle(msg);
		// DeviceArrayAdapter itemAdapter = new DeviceArrayAdapter(context,
		// R.layout.list_items, items);
		this.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				okClickListener(arg1);
			}
		});
		// this.setAdapter(itemAdapter, new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		// okClickListener(arg1);
		// }
		// });
		this.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				cancelClickListener();
			}
		});
		this.setCancelable(true);
		this.show();
	}

	/**
	 * Constructor to show alert dialog with Yes/No buttons
	 * 
	 * @param context
	 *            Activity which shows the alert.
	 * @param msg
	 *            The message to be displayed.
	 * @param isYesNoAlert
	 *            Flag to show the Yes/No buttons.
	 */

	public AppAlert(Context context, String msg, boolean isYesNoAlert) {
		super(context);
		this.setTitle(APP_NAME);
		this.setMessage(msg);
		this.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				okClickListener();
			}
		});
		this.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cancelClickListener();
			}
		});
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Constructor to show alert dialog with Yes/No buttons
	 * 
	 * @param context
	 *            Activity which shows the alert.
	 * @param sMsgCode
	 *            Message id to be displayed with title
	 * @param msg
	 *            The message to be displayed.
	 * @param isYesNoAlert
	 *            Flag to show the Yes/No buttons.
	 */
	public AppAlert(Context context, String sMsgCode, String msg,
			boolean isYesNoAlert) {
		super(context);
		this.setTitle(APP_NAME + " : " + sMsgCode);
		this.setMessage(msg);
		this.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				okClickListener();
			}
		});
		this.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cancelClickListener();
			}
		});
		this.setCancelable(false);
		this.show();
	}

	public void okClickListener(int pos) {
	}

	public void okClickListener() {
	}

	public void cancelClickListener() {
	}
}