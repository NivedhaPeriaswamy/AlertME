/**
 * PillMinder (c) 2013 by Clyde Thomas Zuber
 */
package com.example.nivi.alertme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * PillMinderService
 * 
 * @author Clyde Zuber
 *
 */
public class AlarmService extends Service {
	private Context context;
	
	private NotificationManager AlarmServiceNM;
	private Notification n;
	private Resources res;
	private final IBinder myBinder = new LocalBinder();
	
	
	/**
     * Class for clients to access.  Because we know this service
     * always runs in the same process as its clients, we don't need
     * to deal with IPC.
     */
    public class LocalBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;

		/**
		 * Display a notification about service starting.
		 * It also puts an icon in the status bar.
		 */
		Intent notificationIntent = new Intent(context,
				AlarmService.class);
		PendingIntent contentIntent =
				PendingIntent.getActivity(context, 0,
		        notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		AlarmServiceNM = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);

		res = getResources();       
        Notification.Builder builder =
        		new Notification.Builder(context);
        
        builder.setContentIntent(contentIntent)
        	//.setSmallIcon(R.drawable.pill_minder)
        	.setTicker("TravelAlarm started")
        	.setWhen(System.currentTimeMillis())
        	.setAutoCancel(false)
        	.setContentTitle(res.getString(R.string.app_name))
        	.setContentText("AlarmService running");
        
        n = builder.build();
        //AlarmServiceNM.notify(NOTIFICATION, n);
        
        /**
         * Database/Scheduler initialization.
         */
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(res.getString(R.string.app_name), "Received start id " +
        	startId + ": " + intent);
       
        // continue running service until explicitly stopped 
        return START_STICKY;
    }
	
	@Override
    public void onDestroy() {
        // Cancel the persistent notification.
		//AlarmServiceNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(context, "service stopped",
        		Toast.LENGTH_LONG).show();
    }

}
