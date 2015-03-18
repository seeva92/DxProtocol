package com.android.dxbprotocol.api;

import java.util.LinkedHashMap;

import com.android.dxbprotocol.config.Constants;
import com.android.dxbprotocol.utility.UserProfile;

/**
 * 
 * Utility class to keep the transaction types, code and parameters
 * 
 */
public class APIParams {

	public static LinkedHashMap<String, String> getRegisterParameters(
			String imeiNumber) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_APP_VERSION, APIUtils.PARAM_APP_VERSION_VALUE);
		params.put(APIUtils.PARAM_DEVICE_TOKEN, imeiNumber);
		params.put(APIUtils.PARAM_DEVICE_TYPE,
				APIUtils.PARAM_DEVICE_TYPE_ANDROID);
		params.put(APIUtils.PARAM_APP_VERSION, APIUtils.PARAM_APP_VERSION_VALUE);
		return params;
	}

	public static LinkedHashMap<String, String> getLogInParameters(
			String mobile, String smsCode, String imeiNumber) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_LOGIN, mobile);
		params.put(APIUtils.PARAM_PASSWORD, smsCode);
		params.put(APIUtils.PARAM_DEVICE_TOKEN, imeiNumber);
		params.put(APIUtils.PARAM_DEVICE_TYPE,
				APIUtils.PARAM_DEVICE_TYPE_ANDROID);
		return params;
	}

	public static LinkedHashMap<String, String> getHomeLogoParameters() {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		return params;
	}

	public static LinkedHashMap<String, String> getAllEventsParameters(
			String userID) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		return params;
	}

	public static LinkedHashMap<String, String> getEventDetailsParameters(
			String userID, String eventId) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_EVENTID, eventId);
		return params;
	}

	public static LinkedHashMap<String, String> getEventResponseParameters(
			String userID, String eventId, String response, String responseText) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_EVENTID, eventId);
		params.put(APIUtils.PARAM_EVENT_RESPONSE, response);
		if (responseText != null) {
			params.put(APIUtils.PARAM_EVENT_RESPONSE_TEXT, responseText);
		}
		return params;
	}

	public static LinkedHashMap<String, String> getLogoutParameters(
			String userID, String deviceId) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_DEVICE_TOKEN, deviceId);
		return params;
	}

	public static LinkedHashMap<String, String> getProfileParameters(
			String userID) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		return params;
	}

	public static LinkedHashMap<String, String> getContactUsParameters(
			String userID, String messsageType, String message, String callback) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_CONTACTUS_MESSAGETYPE, messsageType);
		params.put(APIUtils.PARAM_CONTACTUS_MESSAGE, message);
		params.put(APIUtils.PARAM_CONTACTUS_CALLBACK, callback);
		return params;
	}

	public static LinkedHashMap<String, String> getEventRatingParameters(
			String userID, String eventId, String rating, String ratingComment) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_EVENTID, eventId);
		params.put(APIUtils.PARAM_RATING, rating);
		if (ratingComment != null) {
			params.put(APIUtils.PARAM_RATING_COMMENT, ratingComment);
		}
		return params;
	}

	public static LinkedHashMap<String, String> getSearchEventsParameters(
			String userID, String searchText) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);
		params.put(APIUtils.PARAM_SEARCH, searchText);
		return params;
	}

	public static LinkedHashMap<String, String> getProfileEditParameters(
			String userID, UserProfile userProfile) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(APIUtils.PARAM_ACCESS_KEY, Constants.API_ACCESS_KEY);
		params.put(APIUtils.PARAM_USERID, userID);

		params.put(APIUtils.PARAM_PROFILE_VIPCATEGORY_E,
				userProfile.vipCategoryE);
		params.put(APIUtils.PARAM_PROFILE_VIPCATEGORY_A,
				userProfile.vipCategoryA);
		params.put(APIUtils.PARAM_PROFILE_PROFILEIMAGE,
				userProfile.profileImage);
		params.put(APIUtils.PARAM_PROFILE_SALUTATIONE, userProfile.salutationE);
		params.put(APIUtils.PARAM_PROFILE_SALUTATIONA, userProfile.salutationA);
		params.put(APIUtils.PARAM_PROFILE_NAMESURNAMEE,
				userProfile.nameSurnameE);
		params.put(APIUtils.PARAM_PROFILE_NAMESURNAMEA,
				userProfile.nameSurnameA);
		params.put(APIUtils.PARAM_PROFILE_SEX, userProfile.sex);
		params.put(APIUtils.PARAM_PROFILE_BIRTHDATE, userProfile.birthDate);
		params.put(APIUtils.PARAM_PROFILE_COMPANYE, userProfile.companyE);
		params.put(APIUtils.PARAM_PROFILE_COMPANYA, userProfile.companyA);
		params.put(APIUtils.PARAM_PROFILE_POSITIONE, userProfile.positionE);
		params.put(APIUtils.PARAM_PROFILE_POSITIONA, userProfile.positionA);
		params.put(APIUtils.PARAM_PROFILE_LOCALMOBILE, userProfile.localMobile);
		params.put(APIUtils.PARAM_PROFILE_TELEPHONE, userProfile.telephone);
		params.put(APIUtils.PARAM_PROFILE_OFFICIALMOBILE,
				userProfile.officialMobile);
		params.put(APIUtils.PARAM_PROFILE_FAX, userProfile.fax);
		params.put(APIUtils.PARAM_PROFILE_EMAIL, userProfile.email);
		params.put(APIUtils.PARAM_PROFILE_NATIONALITYE,
				userProfile.nationalityE);
		params.put(APIUtils.PARAM_PROFILE_NATIONALITYA,
				userProfile.nationalityA);
		params.put(APIUtils.PARAM_PROFILE_UAEID, userProfile.uaeID);
		params.put(APIUtils.PARAM_PROFILE_PASSPORT, userProfile.passport);
		params.put(APIUtils.PARAM_PROFILE_PANAME, userProfile.paName);
		params.put(APIUtils.PARAM_PROFILE_PAMOBILE, userProfile.paMobile);
		params.put(APIUtils.PARAM_PROFILE_PATELEPHONE, userProfile.paTelephone);
		params.put(APIUtils.PARAM_PROFILE_PAEMAIL, userProfile.paEmail);
		params.put(APIUtils.PARAM_PROFILE_PREFFEREDAPPLANGUAGE,
				userProfile.prefferedAppLanguage);
		params.put(APIUtils.PARAM_PROFILE_PREFFEREDMESSAGELANGUAGE,
				userProfile.prefferedMessageLanguage);
		params.put(APIUtils.PARAM_PROFILE_INVITATIONBY,
				userProfile.invitationBy);
		params.put(APIUtils.PARAM_PROFILE_LOCALORFOREIGN,
				userProfile.localOrForeign);
		params.put(APIUtils.PARAM_PROFILE_UPDATEREQUIRED,
				userProfile.updateRequired);
		params.put(APIUtils.PARAM_PROFILE_BLACKLISTED, userProfile.blackListed);
		params.put(APIUtils.PARAM_PROFILE_LOCALMOBILEVARIFIED,
				userProfile.localMobileVarified);
		params.put(APIUtils.PARAM_PROFILE_OFFICEMOBILEVARIFIED,
				userProfile.officeMobileVarified);
		params.put(APIUtils.PARAM_PROFILE_PAMOBILEVARIFIED,
				userProfile.paMobileVarified);
		params.put(APIUtils.PARAM_PROFILE_LASTINVITED, userProfile.lastInvited);
		params.put(APIUtils.PARAM_PROFILE_REGISTRATIONMSGSENTAPP,
				userProfile.registrationMsgSentApp);

		return params;
	}

}