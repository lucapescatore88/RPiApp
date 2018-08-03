package com.pluca.rpiapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pluca on 26/02/17.
 */

public class SurveillanceService extends Service {

    Timer timer;
    String motion;
    SimpleDateFormat sdf;

    @Override
    public void onCreate() {
        super.onCreate();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer();
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void startNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.motion);
        mBuilder.setContentTitle("Motion detected");
        mBuilder.setContentText("Motion detected at " + sdf.format(new Date()).toString());

        Intent resultIntent = new Intent(this, MotionActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        resultIntent.putExtra("surveillance","ON");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MotionActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    public void startTimer() {

        timer = Utils.getTimer(new Command() {
            @Override
            public void execute(){ readMotion(); } },2000);
    }

    public void readMotion() {

        Uri.Builder builder = Utils.getUrl("/motion/read");
        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (jsono != null) {
                    try {
                        motion = jsono.getString("motion");
                        if (motion.indexOf("YES") >= 0) {
                            startNotification();
                        }
                    } catch (JSONException e) {
                        Log.v("debug","error finding arguments");
                    }
                }
            }
        };
        task.setType("json");
        task.execute(builder.build().toString());

    }

}
