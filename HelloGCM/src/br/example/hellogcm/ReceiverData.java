package br.example.hellogcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class ReceiverData extends WakefulBroadcastReceiver {

	/**
	 * Called when a message is received from GCM for the user
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		ComponentName component = new ComponentName(context.getPackageName(),
				ServiceData.class.getName());

		intent.setComponent(component);

		startWakefulService(context, intent);
		setResultCode(Activity.RESULT_OK);
	}

}
