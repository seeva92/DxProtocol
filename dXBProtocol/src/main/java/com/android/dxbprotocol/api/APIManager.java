package com.android.dxbprotocol.api;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.storage.Preferences;
import com.android.dxbprotocol.utility.L;

/**
 * Utitility class to send request to server and returns the response from the
 * server.
 * 
 */
public class APIManager {

	public static final String URL_PRODUCTION = "http://www.flagshippro.com/apps/protocol/";
	// public static final String URL_PRODUCTION =
	// "https://web2.verbalizeit.com";
	public static final String URL_DEMO = "http://www.flagshippro.com/apps/protocol";

	private Activity activity = null;
	private String errorMsg = "", errorStackMsg = "";
	private Exception api_exception;
	private String response;
	private Status status;
	private LinkedHashMap<String, String> apiParamsList;

	public static final String ACTION_REGISTER_DEVICE_ID = "/registerAndroidDevice?";
	public static final String ACTION_LOGIN = "/loginVIP?";
	public static final String ACTION_HOME_LOGO = "/getBG?";
	public static final String ACTION_LIST_UPCOMING_EVENTS = "/eventListUpComing?";
	public static final String ACTION_LIST_PAST_EVENTS = "/eventListPast?";
	public static final String ACTION_EVENT_DETAILS = "/eventDetails?";
	public static final String ACTION_EVENT_RESPONSE = "/eventResponse?";
	public static final String ACTION_EVENT_LOGOUT = "/logoutVIP?";
	public static final String ACTION_CONTACTUS = "/sendMessage?";
	public static final String ACTION_RATING = "/rateEvent?";
	public static final String ACTION_SEARCH_EVENT_LIST_UPCOMING = "/searchEventListUpComing?";
	public static final String ACTION_SEARCH_EVENT_LIST_PAST = "/searchEventListPast?";
	public static final String ACTION_PROFILE_EDIT = "/vipProfileEdit?";
	public static final String ACTION_PROFILE = "/vipProfile?";

	/**
	 * Enumeration values for the request status.
	 * 
	 */
	public enum Status {
		NONE, SUCCESS, FAILED, ERROR, NO_INTERNET
	};

	public APIManager(Activity thisActivity) {
		this.activity = thisActivity;
	}

	public APIManager(LinkedHashMap<String, String> paramsList,
			Activity thisActivity) {
		this.activity = thisActivity;
		this.apiParamsList = paramsList;
	}

	public Status getResultResponsePut(String sURL) {
		this.status = Status.NONE;

		Preferences pref = new Preferences(activity);
		sURL = pref.getURL() + sURL;

		NetworkInfo info = ((ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			this.status = Status.NO_INTERNET;
			this.errorMsg = Messages.NO_INTERNET;
			return this.status;
		}

		L.debug("$$$$$$$$$$$$$$$$$$$$");
		L.debug("strURL: " + sURL);
		L.debug("$$$$$$$$$$$$$$$$$$$$");

		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(sURL);

		try {

			if (apiParamsList != null) {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Set<?> st = apiParamsList.keySet();
				Iterator<?> itr = st.iterator();
				Collection<?> c = apiParamsList.values();
				Iterator<?> vitr = c.iterator();

				while (itr.hasNext()) {

					String sKey = itr.next().toString();
					String sVal = vitr.next().toString();
					L.debug("sKey: " + sKey);
					L.debug("sVal: " + sVal);

					nameValuePairs.add(new BasicNameValuePair(sKey, sVal));

				}

				httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			}

			// httppost.setHeader("Content-type", "application/json");

			HttpResponse httpresponse = httpclient.execute(httpput);
			HttpEntity resEntity = httpresponse.getEntity();
			response = EntityUtils.toString(resEntity);

			L.debug("RESPONSE:");
			L.debug("$$$$$$$$$$$$$$$$$$$$");
			L.debug(response);
			L.debug("$$$$$$$$$$$$$$$$$$$$");

			this.status = Status.SUCCESS;
		} catch (UnknownHostException uhEx) {
			L.debug("Unknown Host Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.NO_INTERNET;
		} catch (HttpHostConnectException hhcEx) {
			L.debug("Http Host Connect Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (HttpResponseException httpResEx) {
			L.debug("Http Response Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (SocketTimeoutException exTO) {
			this.status = Status.ERROR;
			this.errorMsg = Messages.CONNECTION_TIMEOUT_TRY_AGAIN;
		} catch (FileNotFoundException ex) {
			L.debug("FileNotFoundException");
			this.status = Status.ERROR;
			this.errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
		} catch (Exception ex) {
			L.debug("Exception");
			ex.printStackTrace();
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		}

		L.debug("status = " + status);
		return status;
	}

	public Status getResultResponseGet(String sURL) {

		this.status = Status.NONE;
		// string liveurl =
		Preferences pref = new Preferences(activity);
		sURL = pref.getURL() + sURL;

		NetworkInfo info = ((ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			this.status = Status.NO_INTERNET;
			this.errorMsg = Messages.NO_INTERNET;
			return this.status;
		}

		L.debug("$$$$$$$$$$$$$$$$$$$$");
		L.debug("strURL: " + sURL);
		L.debug("$$$$$$$$$$$$$$$$$$$$");

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(sURL);

		try {

			HttpResponse httpresponse = httpclient.execute(httpget);
			HttpEntity resEntity = httpresponse.getEntity();
			response = EntityUtils.toString(resEntity);

			L.debug("RESPONSE:");
			L.debug("$$$$$$$$$$$$$$$$$$$$");
			L.debug(response);
			L.debug("$$$$$$$$$$$$$$$$$$$$");

			this.status = Status.SUCCESS;
		} catch (UnknownHostException uhEx) {
			L.debug("Unknown Host Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.NO_INTERNET;
		} catch (HttpHostConnectException hhcEx) {
			L.debug("Http Host Connect Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (HttpResponseException httpResEx) {
			L.debug("Http Response Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (SocketTimeoutException exTO) {
			this.status = Status.ERROR;
			this.errorMsg = Messages.CONNECTION_TIMEOUT_TRY_AGAIN;
		} catch (FileNotFoundException ex) {
			L.debug("FileNotFoundException");
			this.status = Status.ERROR;
			this.errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
		} catch (Exception ex) {
			L.debug("Exception");
			ex.printStackTrace();
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		}

		L.debug("status = " + status);
		return status;
	}

	public Status getResultResponse(String sURL) {
		this.status = Status.NONE;

		Preferences pref = new Preferences(activity);
		sURL = pref.getURL() + sURL;

		NetworkInfo info = ((ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			this.status = Status.NO_INTERNET;
			this.errorMsg = Messages.NO_INTERNET;
			return this.status;
		}

		L.debug("$$$$$$$$$$$$$$$$$$$$");
		L.debug("strURL: " + sURL);
		L.debug("$$$$$$$$$$$$$$$$$$$$");

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(sURL);

		try {

			if (apiParamsList != null) {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Set<?> st = apiParamsList.keySet();
				Iterator<?> itr = st.iterator();
				Collection<?> c = apiParamsList.values();
				Iterator<?> vitr = c.iterator();

				while (itr.hasNext()) {

					String sKey = itr.next().toString();
					String sVal = vitr.next().toString();
					L.debug("sKey: " + sKey);
					L.debug("sVal: " + sVal);

					nameValuePairs.add(new BasicNameValuePair(sKey, sVal));

				}

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			}

			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity resEntity = httpresponse.getEntity();
			response = EntityUtils.toString(resEntity);

			L.debug("RESPONSE:");
			L.debug("$$$$$$$$$$$$$$$$$$$$");
			L.debug(response);
			L.debug("$$$$$$$$$$$$$$$$$$$$");

			this.status = Status.SUCCESS;
		} catch (UnknownHostException uhEx) {
			L.debug("Unknown Host Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.NO_INTERNET;
		} catch (HttpHostConnectException hhcEx) {
			L.debug("Http Host Connect Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (HttpResponseException httpResEx) {
			L.debug("Http Response Exception");
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		} catch (SocketTimeoutException exTO) {
			this.status = Status.ERROR;
			this.errorMsg = Messages.CONNECTION_TIMEOUT_TRY_AGAIN;
		} catch (FileNotFoundException ex) {
			L.debug("FileNotFoundException");
			this.status = Status.ERROR;
			this.errorMsg = Messages.ERROR_PRECEDE + Messages.NO_API;
		} catch (Exception ex) {
			L.debug("Exception");
			ex.printStackTrace();
			this.status = Status.ERROR;
			this.errorMsg = Messages.UNABLE_PROCESS_TRY_AGAIN;
		}

		L.debug("status = " + status);
		return status;
	}

	/**
	 * To get the status of the request.
	 * 
	 * @return Returns the Status value of request.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * To get the full error stack.
	 * 
	 * @return Returns the error stack.
	 */
	public String getErrorStack() {
		return errorStackMsg;
	}

	/**
	 * To get the expection occured while sending request.
	 * 
	 * @return Exception of request.
	 */
	public Exception getException() {
		return api_exception;
	}

	/**
	 * To get the response returned from the server.
	 * 
	 * @return Returns the response string.
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * To get the error message.
	 * 
	 * @return Error message.
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
}
