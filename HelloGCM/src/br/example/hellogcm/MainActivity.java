package br.example.hellogcm;

import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "debug_app";

	// Problems ??? Without PlayServices
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	// Server SENDER PROJECT ID
	private static final String SERVER_SENDER = "PUT PROJECT ID HERE...";

	// Cloud Message Object
	private GoogleCloudMessaging gcm;

	// Number of mobile register (ID)
	private String registerID;

	// TextView
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerID = SharedPreferenceApp.getRegisterID(MainActivity.this);
		enableGCM();

		textView = (TextView) findViewById(R.id.textView);

		// Only for test
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				textView.setText("Hello New Push");
			}
		});
	}

	/**
	 * This function
	 */
	public void enableGCM() {
		if (isEnablePlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
			// id is empty???
			if (registerID.trim().length() == 0) {
				//Search ID in background
				Log.v(DEBUG_TAG, "Getting registration_id in background");
				sendRegistrationInBackground();
			} else {
				Log.v(DEBUG_TAG, "We've registration_id: " + registerID);
			}
		}
	}

	private void sendRegistrationInBackground() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(MainActivity.this);
					}
					// get the registration_id from user
					registerID = gcm.register(SERVER_SENDER);
					Log.v(DEBUG_TAG, "RegistrationID: " + registerID);

					/*
					 * Create a Post Request to a server... Send registration to
					 * a server for example Post
					 */

					// Save context in Preferences
					SharedPreferenceApp.storeRegisterID(MainActivity.this,
							registerID);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	/**
	 * Check the availability of Playservices
	 * 
	 * @return boolean
	 */
	public boolean isEnablePlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(MainActivity.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,
						MainActivity.this, PLAY_SERVICES_RESOLUTION_REQUEST);
			} else {
				// Error message to user
			}
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		isEnablePlayServices();
	}

}
