package com.android.dxbprotocol.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.android.dxbprotocol.AppActivity;
import com.android.dxbprotocol.config.Messages;
import com.android.dxbprotocol.storage.Preferences;
import com.android.dxbprotocol.utility.Event;
import com.android.dxbprotocol.utility.EventDetails;
import com.android.dxbprotocol.utility.L;
import com.android.dxbprotocol.utility.UserProfile;

public class APIParser {

	private static final String EVENT_TITLE_A = "eventTitleA";
	private static final String EVENT_TITLE_E = "eventTitleE";
	private static final String EVENT_TIME = "eventTime";
	private static final String EVENT_DAY = "eventDay";
	private static final String EVENT_DATE = "eventDate";
	private static final String EVENT_ID = "eventID";
	private static final String LIST_OF_EVENTS = "List of Events";
	private static final String EVENT_DETAILS = "Event Details";
	private static final String USER_PROFILE = "User Profile";
	private static final String EVENT_RESPONSE_MESSAGE = "Message";
	private static final String EVENT_LOGOUT_MESSAGE = "Message";
	private static final String EVENT_CONTACTUS_MESSAGE = "Message";
	private static final String EVENT_RATING_MESSAGE = "Message";
	private static final String EVENT_PROFILE_EDIT_MESSAGE = "Message";
	private static final String IMAGEJSON2 = "image";
	private static final String IMAGEJSON = "Image";
	private static final String ERRORJSON = "Error";
	private static final String NAME_SURNAME_A = "nameSurnameA";
	String sResponse = null;
	public static String REPONSECODE = "ResponseCode";
	public static String REPONSEMSG = "ResponseMessage";
	Document doc;

	public static String SUCCESS = "SUCCESS";
	public static String ERROR = "error";
	private AppActivity activity;

	private String CURRENT_TRANSLATION_ID = "id";
	private String USERID = "userID";
	private static final String NAME_SURNAME_E = "nameSurnameE";

	private static final String EVENT_DETAILS_IMAGES = "images";
	private static final String EVENT_DETAILS_IMAGE_URL = "image";
	private static final String EVENT_DETAILS_EVENT_TITLE_E = "eventTitleE";
	private static final String EVENT_DETAILS_EVENT_TITLE_A = "eventTitleA";
	private static final String EVENT_DETAILS_EVENT_DATE = "eventDate";
	private static final String EVENT_DETAILS_EVENT_DAY = "eventDay";
	private static final String EVENT_DETAILS_EVENT_TIME = "eventTime";
	private static final String EVENT_DETAILS_EVENT_LOCATION_E = "eventLocationE";
	private static final String EVENT_DETAILS_EVENT_LOCATION_A = "eventLocationA";
	private static final String EVENT_DETAILS_EVENT_DRESS_CODE_E = "eventDressCodeE";
	private static final String EVENT_DETAILS_EVENT_DRESS_CODE_A = "eventDressCodeA";
	private static final String EVENT_DETAILS_EVENT_NOTE_E = "eventNoteE";
	private static final String EVENT_DETAILS_EVENT_NOTE_A = "eventNoteA";
	private static final String EVENT_DETAILS_EVENT_LONGITUDE = "eventLongitude";
	private static final String EVENT_DETAILS_EVENT_LATITUDE = "eventLatitude";
	private static final String EVENT_DETAILS_EVENT_RESPONSE = "eventResponse";

	private static final String PROFILE_VIPCATEGORY_E = "vipCategoryE";
	private static final String PROFILE_VIPCATEGORY_A = "vipCategoryA";
	private static final String PROFILE_PROFILEIMAGE = "profileImage";
	private static final String PROFILE_SALUTATIONE = "salutationE";
	private static final String PROFILE_SALUTATIONA = "salutationA";
	private static final String PROFILE_NAMESURNAMEE = "nameSurnameE";
	private static final String PROFILE_NAMESURNAMEA = "nameSurnameA";
	private static final String PROFILE_SEX = "sex";
	private static final String PROFILE_BIRTHDATE = "birthDate";
	private static final String PROFILE_COMPANYE = "companyE";
	private static final String PROFILE_COMPANYA = "companyA";
	private static final String PROFILE_POSITIONE = "positionE";
	private static final String PROFILE_POSITIONA = "positionA";
	private static final String PROFILE_LOCALMOBILE = "localMobile";
	private static final String PROFILE_TELEPHONE = "telephone";
	private static final String PROFILE_OFFICIALMOBILE = "officialMobile";
	private static final String PROFILE_FAX = "fax";
	private static final String PROFILE_EMAIL = "email";
	private static final String PROFILE_NATIONALITYE = "nationalityE";
	private static final String PROFILE_NATIONALITYA = "nationalityA";
	private static final String PROFILE_UAEID = "uaeID";
	private static final String PROFILE_PASSPORT = "passport";
	private static final String PROFILE_PANAME = "paName";
	private static final String PROFILE_PAMOBILE = "paMobile";
	private static final String PROFILE_PATELEPHONE = "paTelephone";
	private static final String PROFILE_PAEMAIL = "paEmail";
	private static final String PROFILE_PREFFEREDAPPLANGUAGE = "prefferedAppLanguage";
	private static final String PROFILE_PREFFEREDMESSAGELANGUAGE = "prefferedMessageLanguage";
	private static final String PROFILE_INVITATIONBY = "invitationBy";
	private static final String PROFILE_LOCALORFOREIGN = "localOrForeign";
	private static final String PROFILE_UPDATEREQUIRED = "updateRequired";
	private static final String PROFILE_BLACKLISTED = "blackListed";
	private static final String PROFILE_LOCALMOBILEVARIFIED = "localMobileVarified";
	private static final String PROFILE_OFFICEMOBILEVARIFIED = "officeMobileVarified";
	private static final String PROFILE_PAMOBILEVARIFIED = "paMobileVarified";
	private static final String PROFILE_LASTINVITED = "lastInvited";
	private static final String PROFILE_REGISTRATIONMSGSENTAPP = "registrationMsgSentApp";

	public APIParser(AppActivity appActivity, String response) throws Exception {
		sResponse = response;
		activity = appActivity;
	}

	public ParseStatus getLoginDetails() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(USERID)) {
					JSONObject jsonObj = new JSONObject(sResponse);
					String id = jsonObj.getString(USERID);
					String nameSurnameE = jsonObj.getString(NAME_SURNAME_E);
					String nameSurnameA = jsonObj.getString(NAME_SURNAME_A);
					Preferences preferences = new Preferences(activity);
					preferences.setLoginDetails(id, nameSurnameE, nameSurnameA);
					status.ResponseCode = SUCCESS;
					status.ResponseDesc = SUCCESS;
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getHomeLogo() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(IMAGEJSON)) {
					JSONObject jsonObj = new JSONObject(sResponse);
					String Image = jsonObj.getString(IMAGEJSON);

					JSONObject jsonObjImage = new JSONObject(Image);
					String ImageUrl = jsonObjImage.getString(IMAGEJSON2);

					status.ResponseCode = SUCCESS;
					status.ResponseDesc = ImageUrl;
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getEventsStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {

					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						JSONArray arrEvents = jsonObj
								.getJSONArray(LIST_OF_EVENTS);

						status.ResponseCode = SUCCESS;
						status.ResponseDesc = sResponse;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ArrayList<Event> getEvents() {

		ArrayList<Event> arrEvents = new ArrayList<Event>();

		if (sResponse != null && !sResponse.equals("")) {

			try {
				JSONObject jsonObj = new JSONObject(sResponse);
				JSONArray arrEventsJson = jsonObj.getJSONArray(LIST_OF_EVENTS);

				for (int i = 0; i < arrEventsJson.length(); i++) {
					JSONObject eventJson = arrEventsJson.getJSONObject(i);
					Event event = new Event();
					event.eventID = eventJson.getString(EVENT_ID);
					event.eventDate = eventJson.getString(EVENT_DATE);
					event.eventDay = eventJson.getString(EVENT_DAY);
					event.eventTime = eventJson.getString(EVENT_TIME);
					event.eventTitleE = eventJson.getString(EVENT_TITLE_E);
					event.eventTitleA = eventJson.getString(EVENT_TITLE_A);
					arrEvents.add(event);
				}
				return arrEvents;
			} catch (Exception e) {
				return arrEvents;
			}
		}
		return arrEvents;
	}

	public EventDetails getEventDetails() {

		EventDetails eventDetails = new EventDetails();

		if (sResponse != null && !sResponse.equals("")) {

			try {
				JSONObject jsonObj = new JSONObject(sResponse);
				JSONObject eventDetailsJson = jsonObj
						.getJSONObject(EVENT_DETAILS);

				JSONObject imagesJson = eventDetailsJson
						.getJSONObject(EVENT_DETAILS_IMAGES);
				for (int i = 0; i < imagesJson.length(); i++) {
					JSONObject imageUrlJson = imagesJson.getJSONObject(""
							+ (i + 1));
					String imageUrl = imageUrlJson
							.getString(EVENT_DETAILS_IMAGE_URL);
					eventDetails.arrImages.add(imageUrl);
				}

				eventDetails.eventTitleE = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_TITLE_E);
				eventDetails.eventTitleA = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_TITLE_A);
				eventDetails.eventDate = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_DATE);
				eventDetails.eventDay = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_DAY);
				eventDetails.eventTime = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_TIME);
				eventDetails.eventLocationE = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_LOCATION_E);
				eventDetails.eventLocationA = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_LOCATION_A);
				eventDetails.eventDressCodeE = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_DRESS_CODE_E);
				eventDetails.eventDressCodeA = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_DRESS_CODE_A);
				eventDetails.eventNoteE = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_NOTE_E);
				eventDetails.eventNoteA = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_NOTE_A);
				eventDetails.eventLongitude = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_LONGITUDE);
				eventDetails.eventLatitude = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_LATITUDE);
				eventDetails.eventResponse = eventDetailsJson
						.getString(EVENT_DETAILS_EVENT_RESPONSE);

			} catch (Exception e) {
				return eventDetails;
			}

		}
		return eventDetails;
	}

	public ParseStatus getEventDetailsStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {

					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						JSONObject eventDetails = jsonObj
								.getJSONObject(EVENT_DETAILS);

						status.ResponseCode = SUCCESS;
						status.ResponseDesc = sResponse;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getEventResponseStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_RESPONSE_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getLogoutStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_LOGOUT_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getProfileStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {

					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						JSONObject eventDetails = jsonObj
								.getJSONObject(USER_PROFILE);

						status.ResponseCode = SUCCESS;
						status.ResponseDesc = sResponse;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public UserProfile getUserProfile() {

		UserProfile userProfile = new UserProfile();

		if (sResponse != null && !sResponse.equals("")) {

			try {
				JSONObject jsonObj = new JSONObject(sResponse);
				JSONObject userProfileJson = jsonObj
						.getJSONObject(USER_PROFILE);

				userProfile.vipCategoryE = userProfileJson
						.getString(PROFILE_VIPCATEGORY_E);
				userProfile.vipCategoryE = userProfileJson
						.getString(PROFILE_VIPCATEGORY_A);
				userProfile.profileImage = userProfileJson
						.getString(PROFILE_PROFILEIMAGE);
				userProfile.salutationE = userProfileJson
						.getString(PROFILE_SALUTATIONE);
				userProfile.salutationA = userProfileJson
						.getString(PROFILE_SALUTATIONA);
				userProfile.nameSurnameE = userProfileJson
						.getString(PROFILE_NAMESURNAMEE);
				userProfile.nameSurnameA = userProfileJson
						.getString(PROFILE_NAMESURNAMEA);
				userProfile.sex = userProfileJson.getString(PROFILE_SEX);
				userProfile.birthDate = userProfileJson
						.getString(PROFILE_BIRTHDATE);
				userProfile.companyE = userProfileJson
						.getString(PROFILE_COMPANYE);
				userProfile.companyA = userProfileJson
						.getString(PROFILE_COMPANYA);
				userProfile.positionE = userProfileJson
						.getString(PROFILE_POSITIONE);
				userProfile.positionA = userProfileJson
						.getString(PROFILE_POSITIONA);
				userProfile.localMobile = userProfileJson
						.getString(PROFILE_LOCALMOBILE);
				userProfile.telephone = userProfileJson
						.getString(PROFILE_TELEPHONE);
				userProfile.officialMobile = userProfileJson
						.getString(PROFILE_OFFICIALMOBILE);
				userProfile.fax = userProfileJson.getString(PROFILE_FAX);
				userProfile.email = userProfileJson.getString(PROFILE_EMAIL);
				userProfile.nationalityE = userProfileJson
						.getString(PROFILE_NATIONALITYE);
				userProfile.nationalityA = userProfileJson
						.getString(PROFILE_NATIONALITYA);
				userProfile.uaeID = userProfileJson.getString(PROFILE_UAEID);
				userProfile.passport = userProfileJson
						.getString(PROFILE_PASSPORT);
				userProfile.paName = userProfileJson.getString(PROFILE_PANAME);
				userProfile.paMobile = userProfileJson
						.getString(PROFILE_PAMOBILE);
				userProfile.paTelephone = userProfileJson
						.getString(PROFILE_PATELEPHONE);
				userProfile.paEmail = userProfileJson
						.getString(PROFILE_PAEMAIL);
				userProfile.prefferedAppLanguage = userProfileJson
						.getString(PROFILE_PREFFEREDAPPLANGUAGE);
				userProfile.prefferedMessageLanguage = userProfileJson
						.getString(PROFILE_PREFFEREDMESSAGELANGUAGE);
				userProfile.invitationBy = userProfileJson
						.getString(PROFILE_INVITATIONBY);
				userProfile.localOrForeign = userProfileJson
						.getString(PROFILE_LOCALORFOREIGN);
				userProfile.updateRequired = userProfileJson
						.getString(PROFILE_UPDATEREQUIRED);
				userProfile.blackListed = userProfileJson
						.getString(PROFILE_BLACKLISTED);
				userProfile.localMobileVarified = userProfileJson
						.getString(PROFILE_LOCALMOBILEVARIFIED);
				userProfile.officeMobileVarified = userProfileJson
						.getString(PROFILE_OFFICEMOBILEVARIFIED);
				userProfile.paMobileVarified = userProfileJson
						.getString(PROFILE_PAMOBILEVARIFIED);
				userProfile.lastInvited = userProfileJson
						.getString(PROFILE_LASTINVITED);
				userProfile.registrationMsgSentApp = userProfileJson
						.getString(PROFILE_REGISTRATIONMSGSENTAPP);

			} catch (Exception e) {
				return userProfile;
			}

		}
		return userProfile;
	}

	public String getResponseDesc() {

		String sResponseDesc = sResponse.substring(sResponse.indexOf("|") + 1,
				sResponse.length());

		L.info("ResponseDesc " + sResponseDesc);

		return sResponseDesc;

	}

	public ParseStatus getContactUsStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_CONTACTUS_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getEventRatingStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_RATING_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getProfileEditStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_PROFILE_EDIT_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

	public ParseStatus getRegisterResponseStatus() {

		ParseStatus status = new ParseStatus();

		if (sResponse != null && !sResponse.equals("")) {

			try {

				if (sResponse.contains(ERRORJSON)) {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String error = jsonObj.getString(ERRORJSON);
						status.ResponseCode = ERROR;
						status.ResponseDesc = error;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				} else {
					try {
						JSONObject jsonObj = new JSONObject(sResponse);
						String responseMessage = jsonObj
								.getString(EVENT_RESPONSE_MESSAGE);
						status.ResponseCode = SUCCESS;
						status.ResponseDesc = responseMessage;
					} catch (Exception e) {
						status.ResponseCode = ERROR;
						status.ResponseDesc = sResponse;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status.ResponseCode = ERROR;
				status.ResponseDesc = Messages.UNABLE_PROCESS_RESPONSE_TRY_AGAIN;
			}

		} else {
			status.ResponseCode = ERROR;
			status.ResponseDesc = Messages.INVALID_RESPONSE;
		}
		return status;
	}

}