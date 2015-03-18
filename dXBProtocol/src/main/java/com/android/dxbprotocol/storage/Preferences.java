package com.android.dxbprotocol.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity;

import com.android.dxbprotocol.api.APIManager;
import com.android.dxbprotocol.config.Constants;

/**
 * Preferences - To retrieve and save from Shared Preferences in the Android
 * device
 * 
 */
public class Preferences {

	private Activity activity = null;
	private Context context = null;
	private SharedPreferences prefsPrivate;
	public static final String PREFS_PRIVATE = Constants.APP_NAME
			+ "_PREFS_PRIVATE";

	private static String KEY_API_ACCESS = "access_key";

	public static String KEY_USER_ID = "userID";
	public static String KEY_SURNAME_E = "nameSurnameE";
	public static String KEY_SURNAME_A = "nameSurnameA";
	public static String KEY_LANGUAGE = "language";
	public static String KEY_LANGUAGE_ENGLISH = "english";
	public static String KEY_LANGUAGE_ARABIC = "arabic";
	public static String KEY_COACH_MARK = "coachMark";

	public Preferences(Activity thisActivity) {
		activity = thisActivity;
		prefsPrivate = activity.getSharedPreferences(PREFS_PRIVATE,
				Context.MODE_PRIVATE);
	}

	public Preferences(PreferenceActivity thisActivity) {
		activity = thisActivity;
		prefsPrivate = activity.getSharedPreferences(PREFS_PRIVATE,
				Context.MODE_PRIVATE);
	}

	public Preferences(Context context) {
		this.context = context;
		prefsPrivate = this.context.getSharedPreferences(PREFS_PRIVATE,
				Context.MODE_PRIVATE);
	}

	/**
	 * Method to get the value from SharedPreferences.
	 * 
	 * @param keyValue
	 *            The key name to be retrieved.
	 * @param defualtValue
	 *            Default value if key not found.
	 * @return Returns the stored value of the key
	 */
	public String getValue(String keyValue, String defualtValue) {
		return prefsPrivate.getString(keyValue, defualtValue);
	}

	/**
	 * Method to save the values with that key in SharedPreferences.
	 * 
	 * @param keyValue
	 *            Key name to be stored.
	 * @param strValue
	 *            Value to be stored with that key name.
	 */
	public void putValue(String keyValue, String strValue) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(keyValue, strValue);
		prefsPrivateEditor.commit();
	}

	public String getURL() {

		if (!Constants.DEBUG)
			return APIManager.URL_PRODUCTION;
		else
			return APIManager.URL_DEMO;
	}

	public void clearLoginDetails() {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(KEY_USER_ID, "");
		prefsPrivateEditor.putString(KEY_SURNAME_E, "");
		prefsPrivateEditor.putString(KEY_SURNAME_A, "");
		prefsPrivateEditor.commit();
	}

	public void setLoginDetails(String id, String surNameE, String surNameA) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(KEY_USER_ID, id);
		prefsPrivateEditor.putString(KEY_SURNAME_E, surNameE);
		prefsPrivateEditor.putString(KEY_SURNAME_A, surNameA);
		prefsPrivateEditor.commit();
	}

	public boolean isAppLaunchedFirstTime() {
		String userId = prefsPrivate.getString(KEY_USER_ID, "");
		if (userId.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String getUserid() {
		return prefsPrivate.getString(KEY_USER_ID, "");
	}

	public String getApiAccessKey() {
		return prefsPrivate.getString(KEY_API_ACCESS, Constants.API_ACCESS_KEY);
	}

	public void setApiAccessKey(String key_api_access) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(KEY_API_ACCESS, key_api_access);
		prefsPrivateEditor.commit();
	}

	public boolean isLanguageIsEnglish() {

		if ((prefsPrivate.getString(KEY_LANGUAGE, KEY_LANGUAGE_ENGLISH))
				.equals(KEY_LANGUAGE_ENGLISH)) {
			return true;
		} else {
			return false;
		}

	}

	public String getLanguage() {
		return prefsPrivate.getString(KEY_LANGUAGE, KEY_LANGUAGE_ENGLISH);
	}

	public void setLanguage(String key_api_access) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(KEY_LANGUAGE, key_api_access);
		prefsPrivateEditor.commit();
	}

	public boolean isCoachMark() {
		return prefsPrivate.getBoolean(KEY_COACH_MARK, true);
	}

	public void setCoachMark(boolean coachMark) {
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putBoolean(KEY_COACH_MARK, coachMark);
		prefsPrivateEditor.commit();
	}

}
