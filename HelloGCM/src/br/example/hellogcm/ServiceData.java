package br.example.hellogcm;

import java.util.Random;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ServiceData extends IntentService {

	private static final String DEBUG_TAG = "debug_app";

	public ServiceData() {
		super(ServiceData.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ReceiverData.completeWakefulIntent(intent);
		Bundle extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging
				.getInstance(ServiceData.this);
		String messageType = gcm.getMessageType(intent);

		if (extras != null) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.v(DEBUG_TAG, "Error:" + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.v(DEBUG_TAG, "Error:" + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				String message = extras.getString("message");
				sendNotification(ServiceData.this, message);
			}
		}

		ReceiverData.completeWakefulIntent(intent);
	}

	private void sendNotification(Context context, String msg) {

		NotificationManager mNotificationManager;

		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Class to open: MainActivity
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Hello GCM App")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Notification ID first parameter
		mNotificationManager.notify(randInt(), notification);
	}

	public static int randInt() {
		Random rand = new Random();
		int randomNum = rand.nextInt((50000 - 0) + 1) + 0;
		return randomNum;
	}

}
