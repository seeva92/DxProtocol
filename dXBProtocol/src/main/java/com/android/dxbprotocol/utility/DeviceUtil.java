package com.android.dxbprotocol.utility;

import java.io.DataOutputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.NeighboringCellInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.KeyEvent;

public class DeviceUtil {
	/*
	 * Network type constants
	 */
	public static final String NETWORK_CDMA = "CDMA: Either IS95A or IS95B (2G)";
	public static final String NETWORK_EDGE = "EDGE (2.75G)";
	public static final String NETWORK_GPRS = "GPRS (2.5G)";
	public static final String NETWORK_UMTS = "UMTS (3G)";
	public static final String NETWORK_EVDO_0 = "EVDO revision 0 (3G)";
	public static final String NETWORK_EVDO_A = "EVDO revision A (3G - Transitional)";
	public static final String NETWORK_EVDO_B = "EVDO revision B (3G - Transitional)";
	public static final String NETWORK_1X_RTT = "1xRTT  (2G - Transitional)";
	public static final String NETWORK_HSDPA = "HSDPA (3G - Transitional)";
	public static final String NETWORK_HSUPA = "HSUPA (3G - Transitional)";
	public static final String NETWORK_HSPA = "HSPA (3G - Transitional)";
	public static final String NETWORK_IDEN = "iDen (2G)";
	public static final String NETWORK_LTE = "LTE (4G)";
	public static final String NETWORK_EHRPD = "EHRPD (3G)";
	public static final String NETWORK_HSPAP = "HSPAP (3G)";
	public static final String NETWORK_UNKOWN = "Unknown";

	/*
	 * Phone type constants
	 */
	public static final String PHONE_CDMA = "CDMA";
	public static final String PHONE_GSM = "GSM";
	public static final String PHONE_SIP = "SIP";
	public static final String PHONE_NONE = "No radio";

	/*
	 * Service state constants
	 */
	public static final String SERVICE_STATE_IN = "IN";
	public static final String SERVICE_STATE_OUT = "OUT";
	public static final String SERVICE_STATE_EMERGENCY = "EMERGENCY";
	public static final String SERVICE_STATE_POWER_OFF = "POWER_OFF";

	Context context;
	private String xmlFile = "";

	public DeviceUtil(Context context) {
		this.context = context;
	}

	public int cell_info;
	public int cid;
	public int lac;
	public int network_type;
	public int phone_type;
	public int datastate;
	public int dataactivity;
	public int batteryPercentage;

	public String device_id;
	public String phone_number;
	public String network_operator;
	public String network_operator_name;

	public String phone_name;
	public String phone_model;
	public String manufacturer;
	public String version;
	public String wifi_state, network_state;

	public String mcc;
	public String mnc;

	double latitude;
	double longitude;

	boolean roaming = false;
	boolean manuallyset = false;

	boolean isGSM = false;

	StringBuffer strBuffer;


	private static String DEVICEINFO_START = "<deviceinfo>";
	private static String DEVICEINFO_END = "</deviceinfo>";

	private static String METADATA_START = "<metadata>";
	private static String METADATA_END = "</metadata>";

	private static String TIMESTAMP_START = "<timestamp>";
	private static String TIMESTAMP_END = "</timestamp>";

	private static String IMEI_START = "<imei>";
	private static String IMEI_END = "</imei>";

	private static String TESTTYPE_START = "<testtype>";
	private static String TESTTYPE_END = "</testtype>";

	private static String TEST_NAME_START = "<testname>";
	private static String TEST_NAME_END = "</testname>";

	private static String USERNAME_NAME_START = "<user>";
	private static String USERNAME_NAME_END = "</user>";

	private static String DEVICE_START = "<device>";
	private static String DEVICE_END = "</device>";

	private static String PHONE_NUMBER_START = "<phonenumber>";
	private static String PHONE_NUMBER_END = "</phonenumber>";

	private static String PHONE_TYPE_START = "<phonetype>";
	private static String PHONE_TYPE_END = "</phonetype>";

	private static String PHONE_MODEL_START = "<model>";
	private static String PHONE_MODEL_END = "</model>";

	private static String MANUFACTURER_START = "<manufacturer>";
	private static String MANUFACTURER_END = "</manufacturer>";

	private static String VERSION_START = "<version>";
	private static String VERSION_END = "</version>";

	private static String DATA_START = "<data>";
	private static String DATA_END = "</data>";

	private static String NETWORK_INFO_START = "<networkinfo>";
	private static String NETWORK_INFO_END = "</networkinfo>";

	private static String NETWORK_OPERATOR_START = "<networkoperator>";
	private static String NETWORK_OPERATOR_END = "</networkoperator>";

	private static String NETWORK_OPERATOR_NAME_START = "<networkoperatorname>";
	private static String NETWORK_OPERATOR_NAME_END = "</networkoperatorname>";

	private static String NETWORK_TYPE_START = "<networktype>";
	private static String NETWORK_TYPE_END = "</networktype>";

	private static String DATA_STATE_START = "<datastate>";
	private static String DATA_STATE_END = "</datastate>";

	private static String DATA_ACTIVITY_START = "<dataactivity>";
	private static String DATA_ACTIVITY_END = "</dataactivity>";

	private static String WIFI_STATE_START = "<wifistate>";
	private static String WIFI_STATE_END = "</wifistate>";

	private static String ROAMING_START = "<roaming>";
	private static String ROAMING_END = "</roaming>";

	private static String MCC_START = "<mcc>";
	private static String MCC_END = "</mcc>";

	private static String MNC_START = "<mnc>";
	private static String MNC_END = "</mnc>";

	private static String SIGNAL_STRENGTH_START = "<signalstrength>";
	private static String SIGNAL_STRENGTH_END = "</signalstrength>";

	private static String GSM_SIGNAL_STRENGTH_START = "<gsmsignalstrength>";
	private static String GSM_SIGNAL_STRENGTH_END = "</gsmsignalstrength>";

	private static String CDMA_DBM_START = "<cdmadbm>";
	private static String CDMA_DBM_END = "</cdmadbm>";

	private static String CDMA_ECIO_START = "<cdmaecio>";
	private static String CDMA_ECIO_END = "</cdmaecio>";

	private static String EVDO_DBM_START = "<EvdoDbm>";
	private static String EVDO_DBM_END = "</EvdoDbm>";

	private static String EVDO_ECIO_START = "<EvdoEcio>";
	private static String EVDO_ECIO_END = "</EvdoEcio>";

	private static String EVDO_SNR_START = "<EvdoSnr>";
	private static String EVDO_SNR_END = "</EvdoSnr>";

	private static String GSM_START = "<gsm>";
	private static String GSM_END = "</gsm>";

	private static String GSM_BITRATE_ERROR_START = "<gsmbitrateerror>";
	private static String GSM_BITRATE_ERROR_END = "</gsmbitrateerror>";

	private static String LTE_SIGNAL_STRENGTH_START = "<LteSignalStrength>";
	private static String LTE_SIGNAL_STRENGTH_END = "</LteSignalStrength>";

	private static String LTE_RSRP_START = "<LteRsrp>";
	private static String LTE_RSRP_END = "</LteRsrp>";

	private static String LTE_RSRQ_START = "<LteRsrq>";
	private static String LTE_RSRQ_END = "</LteRsrq>";

	private static String LTE_RSSNR_START = "<LteRssnr>";
	private static String LTE_RSSNR_END = "</LteRssnr>";

	private static String LTE_CQI_START = "<LteCqi>";
	private static String LTE_CQI_END = "</LteCqi>";

	private static String CELL_LOCATION_START = "<celllocation>";
	private static String CELL_LOCATION_END = "</celllocation>";

	private static String CID_START = "<cid>";
	private static String CID_END = "</cid>";

	private static String LAC_START = "<lac>";
	private static String LAC_END = "</lac>";

	private static String NEIGHBORING_CELL_INFO_START = "<neighboringcellinfo>";
	private static String NEIGHBORING_CELL_INFO_END = "</neighboringcellinfo>";

	private static String NEIGHBORING_INFO_START = "<neighboringinfo>";
	private static String NEIGHBORING_INFO_END = "</neighboringinfo>";

	private static String NET_TYPE_START = "<nettype>";
	private static String NET_TYPE_END = "</nettype>";

	private static String PSC_START = "<psc>";
	private static String PSC_END = "</psc>";

	private static String RSSI_START = "<rssi>";
	private static String RSSI_END = "</rssi>";

	private static String BATTERY_LEVEL_START = "<batterylevel>";
	private static String BATTERY_LEVEL_END = "</batterylevel>";

	private static String GEO_LOCATION_START = "<geolocation>";
	private static String GEO_LOCATION_END = "</geolocation>";

	private static String NETWORK_MANUALLYSET_START = "<networkmanuallyset>";
	private static String NETWORK_MANUALLYSET_END = "</networkmanuallyset>";

	private static String GEO_LAT_START = "<lat>";
	private static String GEO_LAT_END = "</lat>";

	private static String GEO_LAN_START = "<lan>";
	private static String GEO_LAN_END = "</lan>";

	public String getDeviceInfo(String sTestType, String sTestName,
			String sUsername) {

		strBuffer = new StringBuffer();

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		device_id = telephonyManager.getDeviceId();
		cell_info = telephonyManager.getCallState();
		phone_type = telephonyManager.getPhoneType();
		phone_number = "";

		phone_number = telephonyManager.getLine1Number();
		if (phone_number == null)
			phone_number = "";
		if (phone_number.equals(""))
			phone_number = telephonyManager.getSubscriberId();
		phone_model = android.os.Build.MODEL;
		phone_model = XMLUtils.clean(phone_model);
		manufacturer = android.os.Build.MANUFACTURER;
		manufacturer = XMLUtils.clean(manufacturer);
		version = android.os.Build.VERSION.RELEASE;

		// wifi state
		ConnectivityManager c_m = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile_info = c_m
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi_info = c_m
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobile_info != null)
			network_state = "" + mobile_info.getState();

		if (wifi_info != null)
			wifi_state = "" + wifi_info.getState();

		if (telephonyManager.isNetworkRoaming())
			roaming = true;

		GsmCellLocation cell_loc = (GsmCellLocation) telephonyManager
				.getCellLocation();
		if (cell_loc != null) {
			cid = cell_loc.getCid();
			lac = cell_loc.getLac();
		}

		strBuffer.append(DEVICEINFO_START);
		strBuffer.append(METADATA_START);
		strBuffer.append(TIMESTAMP_START);
		strBuffer.append(TimeUtil.getCurrentTime());
		strBuffer.append(TIMESTAMP_END);
		strBuffer.append(IMEI_START);
		strBuffer.append(device_id);
		strBuffer.append(IMEI_END);
		strBuffer.append(TESTTYPE_START);
		strBuffer.append(sTestType);
		strBuffer.append(TESTTYPE_END);
		strBuffer.append(TEST_NAME_START);
		strBuffer.append(sTestName);
		strBuffer.append(TEST_NAME_END);
		strBuffer.append(USERNAME_NAME_START);
		strBuffer.append(sUsername);
		strBuffer.append(USERNAME_NAME_END);
		strBuffer.append(METADATA_END);

		strBuffer.append(DEVICE_START);

		strBuffer.append(IMEI_START);
		if (device_id != null)
			strBuffer.append(device_id);

		strBuffer.append(IMEI_END);

		strBuffer.append(PHONE_NUMBER_START);
		if (phone_number != null)
			strBuffer.append(phone_number);
		strBuffer.append(PHONE_NUMBER_END);

		strBuffer.append(PHONE_TYPE_START);
		strBuffer.append(getPhoneTypeString(phone_type));
		strBuffer.append(PHONE_TYPE_END);

		strBuffer.append(PHONE_MODEL_START);
		if (phone_model != null)
			strBuffer.append(phone_model);
		strBuffer.append(PHONE_MODEL_END);

		strBuffer.append(MANUFACTURER_START);
		if (manufacturer != null)
			strBuffer.append(manufacturer);
		strBuffer.append(MANUFACTURER_END);

		strBuffer.append(VERSION_START);
		if (version != null)
			strBuffer.append(version);
		strBuffer.append(VERSION_END);

		strBuffer.append(DEVICE_END);

		xmlFile = strBuffer.toString();

		return xmlFile;
	}

	public String getListenerInfo() {

		strBuffer = new StringBuffer();


		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		cell_info = telephonyManager.getCallState();
		network_operator = telephonyManager.getNetworkOperator();

		network_operator_name = telephonyManager.getNetworkOperatorName();
		network_operator_name = XMLUtils.clean(network_operator_name);

		network_type = telephonyManager.getNetworkType();

		datastate = telephonyManager.getDataState();
		dataactivity = telephonyManager.getDataActivity();

		String networkOperator = telephonyManager.getNetworkOperator();
		try {
			mcc = networkOperator.substring(0, 3);
			mnc = networkOperator.substring(3);
		} catch (Exception e) {

		}

		// wifi state
		ConnectivityManager c_m = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile_info = c_m
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi_info = c_m
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobile_info != null)
			network_state = "" + mobile_info.getState();

		if (wifi_info != null)
			wifi_state = "" + wifi_info.getState();

		if (telephonyManager.isNetworkRoaming())
			roaming = true;

		GsmCellLocation cell_loc = (GsmCellLocation) telephonyManager
				.getCellLocation();
		if (cell_loc != null) {
			cid = cell_loc.getCid();
			lac = cell_loc.getLac();

			L.debug("cid = " + cid + ", lac = " + lac);
		}

		strBuffer.append(DATA_START);

		strBuffer.append(TIMESTAMP_START);
		strBuffer.append(TimeUtil.getCurrentTime());
		strBuffer.append(TIMESTAMP_END);

		strBuffer.append(NETWORK_INFO_START);

		strBuffer.append(NETWORK_OPERATOR_START);
		if (network_operator != null)
			strBuffer.append(network_operator);
		strBuffer.append(NETWORK_OPERATOR_END);

		strBuffer.append(NETWORK_OPERATOR_NAME_START);
		if (network_operator_name != null)
			strBuffer.append(network_operator_name);
		strBuffer.append(NETWORK_OPERATOR_NAME_END);

		strBuffer.append(NETWORK_TYPE_START);
		strBuffer.append(getNetworkTypeString(network_type));
		strBuffer.append(NETWORK_TYPE_END);

		strBuffer.append(DATA_STATE_START);
		strBuffer.append(getDataStateString(datastate));
		strBuffer.append(DATA_STATE_END);

		strBuffer.append(DATA_ACTIVITY_START);
		strBuffer.append(getDataActivityString(dataactivity));
		strBuffer.append(DATA_ACTIVITY_END);

		strBuffer.append(WIFI_STATE_START);
		if (wifi_state != null)
			strBuffer.append(wifi_state);
		strBuffer.append(WIFI_STATE_END);

		strBuffer.append(ROAMING_START);
		strBuffer.append(roaming);
		strBuffer.append(ROAMING_END);

		strBuffer.append(MCC_START);
		strBuffer.append(mcc);
		strBuffer.append(MCC_END);

		strBuffer.append(MNC_START);
		strBuffer.append(mnc);
		strBuffer.append(MNC_END);

		strBuffer.append(SIGNAL_STRENGTH_END);

		strBuffer.append(CELL_LOCATION_START);
		strBuffer.append(CID_START);
		strBuffer.append(cid);
		strBuffer.append(CID_END);

		strBuffer.append(LAC_START);
		strBuffer.append(lac);
		strBuffer.append(LAC_END);
		strBuffer.append(CELL_LOCATION_END);

		strBuffer.append(NEIGHBORING_CELL_INFO_START);

		StringBuffer sNeightboringCellInfos = new StringBuffer();
		List<NeighboringCellInfo> neighbor_cell_infos = telephonyManager
				.getNeighboringCellInfo();

		for (int i = 0; i < neighbor_cell_infos.size(); i++) {
			sNeightboringCellInfos.append(NEIGHBORING_INFO_START);
			NeighboringCellInfo nci = neighbor_cell_infos.get(i);
			sNeightboringCellInfos.append(CID_START);
			sNeightboringCellInfos.append(nci.getCid());
			sNeightboringCellInfos.append(CID_END);

			sNeightboringCellInfos.append(LAC_START);
			sNeightboringCellInfos.append(nci.getLac());
			sNeightboringCellInfos.append(LAC_END);

			sNeightboringCellInfos.append(NET_TYPE_START);
			sNeightboringCellInfos.append(nci.getNetworkType());
			sNeightboringCellInfos.append(NET_TYPE_END);

			sNeightboringCellInfos.append(PSC_START);
			sNeightboringCellInfos.append(nci.getPsc());
			sNeightboringCellInfos.append(PSC_END);

			sNeightboringCellInfos.append(RSSI_START);
			sNeightboringCellInfos.append(nci.getRssi());
			sNeightboringCellInfos.append(RSSI_END);
			sNeightboringCellInfos.append(NEIGHBORING_INFO_END);
		}

		strBuffer.append(sNeightboringCellInfos);
		strBuffer.append(NEIGHBORING_CELL_INFO_END);

		strBuffer.append(BATTERY_LEVEL_START);
		strBuffer.append(batteryPercentage);
		strBuffer.append(BATTERY_LEVEL_END);

		strBuffer.append(NETWORK_MANUALLYSET_START);
		manuallyset = isSpoofLocation();
		strBuffer.append(manuallyset);
		strBuffer.append(NETWORK_MANUALLYSET_END);

		strBuffer.append(GEO_LOCATION_START);

		strBuffer.append(GEO_LOCATION_END);

		strBuffer.append(DATA_END);

		xmlFile = strBuffer.toString();

		return xmlFile;
	}

	public String getEndInfo() {
		return DEVICEINFO_END;
	}

	boolean isSpoofLocation() {
		if (Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
			return false;
		else
			return true;
	}

	public int getBatteryLevel() {
		Intent batteryIntent = context.registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int rawlevel = batteryIntent.getIntExtra("level", -1);
		double scale = batteryIntent.getIntExtra("scale", -1);
		double level = -1;
		if (rawlevel >= 0 && scale > 0) {
			level = rawlevel / scale;
		}
		return rawlevel;
	}

	public String getIMEI() {

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String sIMEI = telephonyManager.getDeviceId();
		return sIMEI;
	}

	private String getDataStateString(int state) {

		String phoneState = "UNKNOWN";
		switch (state) {
		case TelephonyManager.DATA_CONNECTED:
			phoneState = "Connected";
			break;
		case TelephonyManager.DATA_CONNECTING:
			phoneState = "Connecting";
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			phoneState = "Disconnected";
			break;
		case TelephonyManager.DATA_SUSPENDED:
			phoneState = "Suspended";
			break;
		}
		return phoneState;
	}

	private String getDataActivityString(int direction) {

		String strDirection = "NONE";
		switch (direction) {
		case TelephonyManager.DATA_ACTIVITY_IN:
			strDirection = "IN";
			break;
		case TelephonyManager.DATA_ACTIVITY_INOUT:
			strDirection = "IN-OUT";
			break;
		case TelephonyManager.DATA_ACTIVITY_DORMANT:
			strDirection = "Dormant";
			break;
		case TelephonyManager.DATA_ACTIVITY_NONE:
			strDirection = "NONE";
			break;
		case TelephonyManager.DATA_ACTIVITY_OUT:
			strDirection = "OUT";
			break;
		}
		return strDirection;
	}

	private String getNetworkTypeString(int type) {
		String typeString = "Unknown";

		switch (type) {
		// case TelephonyManager.NETWORK_TYPE_EDGE:
		// typeString = "EDGE";
		// break;
		// case TelephonyManager.NETWORK_TYPE_GPRS:
		// typeString = "GPRS";
		// break;
		// case TelephonyManager.NETWORK_TYPE_UMTS:
		// typeString = "UMTS";
		// break;
		// case TelephonyManager.NETWORK_TYPE_1xRTT:
		// typeString = "RTT";
		// break;
		// case TelephonyManager.NETWORK_TYPE_CDMA:
		// typeString = "CDMA";
		// break;
		// case TelephonyManager.NETWORK_TYPE_HSPA:
		// typeString = "HSPA";
		// break;
		// case TelephonyManager.NETWORK_TYPE_HSDPA:
		// typeString = "HSDPA";
		// break;
		// case TelephonyManager.NETWORK_TYPE_IDEN:
		// typeString = "IDEN";
		// break;
		// case TelephonyManager.NETWORK_TYPE_LTE:
		// typeString = "LTE";
		// break;

		case TelephonyManager.NETWORK_TYPE_CDMA:
			return NETWORK_CDMA;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return NETWORK_EDGE;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return NETWORK_EDGE;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return NETWORK_UMTS;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return NETWORK_EVDO_0;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return NETWORK_EVDO_A;
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return NETWORK_EVDO_B;
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return NETWORK_1X_RTT;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return NETWORK_HSDPA;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return NETWORK_HSPA;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return NETWORK_HSUPA;
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NETWORK_IDEN;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return NETWORK_LTE;
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return NETWORK_EHRPD;
			// case TelephonyManager.NETWORK_TYPE_HSPAP:
			// return NETWORK_HSPAP;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		default:
			return NETWORK_UNKOWN;
		}

	}

	/**
	 * Returns a string indicating the phone radio type.
	 */
	private String getPhoneTypeString(int device) {
		switch (device) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			return PHONE_CDMA;
		case TelephonyManager.PHONE_TYPE_GSM:
			return PHONE_GSM;
		case TelephonyManager.PHONE_TYPE_SIP:
			return PHONE_SIP;
		case TelephonyManager.PHONE_TYPE_NONE:
			return PHONE_NONE;
		default:
			// shouldn't happen.
			return null;
		}
	}

	/**
	 * Returns a string indicating the service state.
	 */
	public static String getServiceString(int device) {
		switch (device) {
		case ServiceState.STATE_IN_SERVICE:
			return SERVICE_STATE_IN;
		case ServiceState.STATE_OUT_OF_SERVICE:
			return SERVICE_STATE_OUT;
		case ServiceState.STATE_EMERGENCY_ONLY:
			return SERVICE_STATE_EMERGENCY;
		case ServiceState.STATE_POWER_OFF:
			return SERVICE_STATE_POWER_OFF;
		default:
			// shouldn't happen.
			return null;
		}
	}

	public static void clearLogcat1() {
		try {
			Process process = Runtime.getRuntime().exec("logcat -c");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clearLogcat() {
		try {

			String generateLogcatLogCommond = "logcat -c";

			Process process = Runtime.getRuntime().exec("/system/bin/sh -");

			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			os.writeBytes(generateLogcatLogCommond);
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	static void answerfix(Context context) {
		try {
			Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
			buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
			context.sendOrderedBroadcast(buttonDown,
					"android.permission.CALL_PRIVILEGED");

			// froyo and beyond trigger on buttonUp instead of buttonDown
			Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
			buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
					KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
			context.sendOrderedBroadcast(buttonUp,
					"android.permission.CALL_PRIVILEGED");

			Intent headSetUnPluggedintent = new Intent(
					Intent.ACTION_HEADSET_PLUG);
			headSetUnPluggedintent
					.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
			headSetUnPluggedintent.putExtra("state", 1); // 0 = unplugged 1 =
															// Headset with
															// microphone 2 =
															// Headset without
															// microphone
			headSetUnPluggedintent.putExtra("name", "Headset");
			// TODO: Should we require a permission?
			context.sendOrderedBroadcast(headSetUnPluggedintent, null);

		} catch (Exception e) {

		}
	}

	public static void answerPhoneHeadsethook(Context context) {
		answerfix(context);

		// Simulate a press of the headset button to pick up the call
		// SettingsClass.logMe(tag, "Simulating headset button");
		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown,
				"android.permission.CALL_PRIVILEGED");

		// froyo and beyond trigger on buttonUp instead of buttonDown
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp,
				"android.permission.CALL_PRIVILEGED");
	}
	
}
