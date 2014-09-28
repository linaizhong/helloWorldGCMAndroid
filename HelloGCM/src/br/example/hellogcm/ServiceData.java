package br.example.hellogcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_DELETED;
import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE;
import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ServiceData extends IntentService {

	final static String TAG = "LOG_TAG";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public ServiceData() {
		super(ServiceData.class.getName());
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String messageType = GoogleCloudMessaging.getInstance(this)
				.getMessageType(intent);

		Bundle extras = intent.getExtras();

		if (messageType.equals(MESSAGE_TYPE_MESSAGE)) {

			String message = extras.getString("message");
			Log.d(TAG, "MESSAGE = '" + message + "' (" + extras.toString()
					+ ")");
			sendNotification("Message: " + extras.toString());
		} else if (messageType.equals(MESSAGE_TYPE_SEND_ERROR)) {
			Log.e(TAG,
					"Error sending previous message (which is odd because we don't send any");
		} else if (messageType.equals(MESSAGE_TYPE_DELETED)) {
			// Too many messages for you, server deleted some
		}

		ReceiverData.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}
