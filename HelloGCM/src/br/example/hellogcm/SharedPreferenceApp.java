package br.example.hellogcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class SharedPreferenceApp {

	private static final String DEBUG_TAG = "debug_app";
	public static final String PREFERENCES_DATA_GCM = "REGISTER_PREFERENCES";
	public static final String REGISTRATION_ID = "REGISTRATION_ID";
	public static final String APP_VERSION = "REG_APP_VERSION";

	/**
	 * Return the app version
	 * 
	 * @param context
	 *            (Activity)
	 * @return int
	 */
	public static int getAppVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.wtf(DEBUG_TAG, "Any problem during getPackageName???");
			throw new RuntimeException();
		}
	}

	/**
	 * Return the registration_id for mobile (if in shared preferences)
	 * 
	 * @return String registration_id from GCM
	 */
	public static String getRegisterID(Context context) {
		Log.v(DEBUG_TAG, "Trying to use getRegisterID");
		String registrationID = "";
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					PREFERENCES_DATA_GCM, Context.MODE_PRIVATE);

			registrationID = preferences.getString(REGISTRATION_ID, "");

			int appVersion = preferences.getInt(APP_VERSION, Integer.MIN_VALUE);
			int currentVersion = getAppVersion(context);

			// Compare app version
			if (appVersion != currentVersion) {
				Log.v(DEBUG_TAG, "App changed version: " + appVersion
						+ "  to  " + currentVersion);
				registrationID = "";
			}
		} catch (Exception e) {
			Log.v(DEBUG_TAG, "Exception:" + e.toString());
			return "";
		}

		return registrationID;
	}
	/**
	 * Save the value of registration_id and app version in SharedPreferences
	 * @param context Activity
	 * @param registrationID identifier of mobile
	 */
	public static void storeRegisterID(Context context, String registrationID) {
		final SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_DATA_GCM, Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(REGISTRATION_ID, registrationID);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

}
