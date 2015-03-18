package com.android.dxbprotocol.config;


public class Messages {
	public static final String ERROR_PRECEDE = "Error - ";

	public static final String CONFIRM_CLOSE_APP = "Confirm to close application?";

	public static final String NO_INTERNET = "Unable to process. Internet connection unavailable. ";
	public static final String NO_API = "Unable to process. Server API connection problem occured.";

	public static final String UNABLE_PROCESS_TRY_AGAIN = "Unable to process. Please try again later.";

	public static final String CONNECTION_TIMEOUT_TRY_AGAIN = "Connection timed out. Please try again.";

	public static final String INVALID_RESPONSE = "Invalid response from server. Please try again";
	public static final String UNABLE_PROCESS_RESPONSE_TRY_AGAIN = "Unable to process the response. Please try again later.";
	
	public static final String LOADING = "Loading...";

	public static String getEmptyText(String str) {
		return String.format("Please enter %s!", str);
	}

	public static String getEmptyChoose(String str) {
		return String.format("Please choose %s!", str);
	}

	public static String getInvalidText(String str) {
		return String.format("Invalid %s value", str);
	}

	public static String getInvalidChoose(String str) {
		return String.format("Please choose valid %s!", str);
	}

	public static String err(Exception e) {
		return ERROR_PRECEDE + e.toString();
	}

}
