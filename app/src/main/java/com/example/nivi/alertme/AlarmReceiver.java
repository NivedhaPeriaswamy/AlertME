/**
 * PillMinder (c) 2013 by Clyde Thomas Zuber
 */
package com.example.nivi.alertme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * AlarmReceiver
 * 
 * @author Clyde Zuber
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent i) {
		//long rxId = i.getLongExtra(ID, 0);
		
		AlarmWakeLock.acquireWakeLock(context);
		
		Intent intent = new Intent(context,
				Alarm_Activity.class);
		//intent.putExtra(ID, rxId);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//intent.setAction(PACKAGE + System.currentTimeMillis());
		context.startActivity(intent);
	}
}
