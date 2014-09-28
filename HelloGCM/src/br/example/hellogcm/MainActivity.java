package br.example.hellogcm;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static String TAG = "LOG_TAG";
	private static final String TAG_CONFORMANCE = "RegulatoryCompliance";
	// Util Vars

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	/**
	 * The Sender Id is the Project ID number from the Google Developer's
	 * Console
	 */
	
	// change this id
	private static final String GCM_SENDER_ID = "API CONSOLE ID";

	public static final String EXTRA_MESSAGE = "message";

	public static final String PROPERTY_REG_ID = "registration_id";

	private static final String PROPERTY_APP_VERSION = "appVersion";

	/** A Context to be used in inner classes */
	private Context context;
	/** In real life this would be in the Application singleton class */
	private Executor threadPool = Executors.newFixedThreadPool(1);
	/** the GCM instance */
	private GoogleCloudMessaging gcm;
	/** The registration Id we get back from GCM the first time we register */
	private String registrationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();

		// GCM stuff
		if (checkForGcm()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			registrationId = getRegistrationId(this);
			if (registrationId.isEmpty()) {
				threadPool.execute(new Runnable() {
					public void run() {
						final String regn = registerWithGcm();
						Log.d(TAG, "New Registration = " + regn);
						setMessageOnUiThread(regn + "\n");
					}
				});
			} else {
				// Hide from user this code; registration
				final String mesg = "Previous Registration = " + registrationId;
				Log.d(TAG, mesg);
				Toast.makeText(this, "previous reg:" + mesg, Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
		}
	}

	void setMessageOnUiThread(final String mesg) {
		runOnUiThread(new Runnable() {
			public void run() {
				// Hide from user this code
				Toast.makeText(MainActivity.this, "messag:" + mesg,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// This line is necessary
		checkForGcm();
	}

	boolean checkForGcm() {
		int ret = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == ret) {
			Log.d(TAG, "MainActivity.checkForGcm(): SUCCESS");
			return true;
		} else {
			Log.d(TAG, "MainActivity.checkForGcm(): FAILURE");
			if (GooglePlayServicesUtil.isUserRecoverableError(ret)) {
				GooglePlayServicesUtil.getErrorDialog(ret, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						this,
						"Google Message Not Supported on this device; you will not get update notifications!!",
						Toast.LENGTH_LONG).show();
				Log.d(TAG_CONFORMANCE,
						"User accepted to run the app without update notifications!");
			}
			return false;
		}
	}
	
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			final String mesg = "CANTHAPPEN: could not get my own package name!?!?: "
					+ e;
			Log.wtf(TAG, mesg);
			throw new RuntimeException(mesg);
		}
	}

	/**
	 * Register this app, the first time on this device, with Google Cloud
	 * Messaging Service
	 * 
	 * @return The registration ID
	 * @author Adapted from the Google Cloud Messaging example
	 */
	private String registerWithGcm() {
		String mesg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			registrationId = gcm.register(GCM_SENDER_ID);
			mesg = "Device registered! My registration =" + registrationId;
			sendRegistrationIdToMyServer();
			storeRegistrationId(context, registrationId);
		} catch (IOException ex) {
			mesg = "Error :" + ex.getMessage();
			Toast.makeText(context, mesg, Toast.LENGTH_LONG).show();
			throw new RuntimeException(mesg);
		}
		return mesg;

	}

	private void sendRegistrationIdToMyServer() {
		// TODO Auto-generated method stub
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

}
