package com.example.nivi.alertme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

public class Alarm_Activity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    private boolean serviceBound = false;
    MediaPlayer alarmSound;
    Vibrator vibrator;
    private boolean cancelled;
    AlertDialog.Builder msg;
    private AlarmService minderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String dateTime = Calendar.getInstance().getTime().toString();
        if (serviceBound) {
            AlarmDialog();
        } else {
            Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
           // i.setAction(PACKAGE + dateTime);
           // i.setAction(PACKAGE + dateTime);
            bindService(i, myConnection, Context.BIND_AUTO_CREATE);
        }

        alarmSound = MediaPlayer.create(getApplicationContext(),
                Settings.System.DEFAULT_ALARM_ALERT_URI);
        vibrator = (Vibrator)
                getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        alarmSound.start();
        vibrator.vibrate(300);
    }

    protected void onDestroy() {
        super.onDestroy();
        alarmSound.stop();
        if (serviceBound) {
            unbindService(myConnection);
            serviceBound = false;
        }
    }

    private void AlarmDialog() {
         msg = new AlertDialog.Builder(getApplicationContext());
        msg.setTitle("Destination Reached");
        msg.setMessage("You will reach your destination in Half an hr");

        msg.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alarmSound.stop();
                        AlarmWakeLock.releaseWakeLock();
                        dialog.dismiss();
                        finish();
                    }
                });

        msg.setNegativeButton("Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alarmSound.stop();
                        AlarmWakeLock.releaseWakeLock();
                        dialog.dismiss();
                        finish();
                    }
                });
        msg.setOnDismissListener(this);
        msg.setOnCancelListener(this);
        msgShow();
    }

    public void msgShow() {
        cancelled = false;
        msg.show();
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        alarmSound.stop();
        cancelled = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // You have to respond to the dialog..
        if (cancelled) {
            msgShow();
        }
    }

private ServiceConnection myConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
        /**
         * This is called when the connection with the service has
         * been established, giving us the service object we can
         * use to interact with the service.  Because we have bound
         * to an explicit service that we know is running in our
         * own process, we can cast its IBinder to a concrete class
         * and directly access it.
         */
        minderService = ((AlarmService.LocalBinder) service)
                .getService();
        serviceBound = true;
    }

    public void onServiceDisconnected(ComponentName className) {
        /**
         * This is called when the connection with the service has
         * been unexpectedly disconnected -- that is, its process
         * crashed.  Because it is running in our same process,
         * we should never see this happen.
         */
        minderService = null;
        serviceBound = false;
    }
};
}
