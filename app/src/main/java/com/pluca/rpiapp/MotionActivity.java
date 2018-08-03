package com.pluca.rpiapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Timer;


/**
 * Created by pluca on 24/02/17.
 */

public class MotionActivity extends AppCompatActivity {

    Context appContext;
    TextView surv_button, display, sound_display;
    ImageView stats_display;
    Resources res;
    boolean state;
    String motion, sound;
    Timer timer;
    Bitmap stats;
    SeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

        appContext = getApplicationContext();
        res = getResources();

        display = (TextView) findViewById(R.id.motion_display);
        stats_display = (ImageView) findViewById(R.id.motion_stats);
        sound_display = (TextView) findViewById(R.id.sound_display);
        surv_button = (TextView) findViewById(R.id.surveillance_button);
        surv_button.setOnClickListener(listener);

        bar = (SeekBar) findViewById(R.id.sensor_position_bar);
        bar.setOnSeekBarChangeListener(bar_listener);
        Utils.UrlTask("/motor/set?pos=100", "text");

        startTimer();

        String surv_value = getIntent().getStringExtra("surveillance");
        if (surv_value != null) {
            if (surv_value.indexOf("ON") >= 0) {
                state = false;
                capture();
            }
        } else {
            state = false;
        }
        motion = "NO";

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer = null;
        if (state) {
            startService(new Intent(appContext, SurveillanceService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null) startTimer();
        if (state) {
            stopService(new Intent(appContext, SurveillanceService.class));
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if (NetworkUtils.checkNetwork(appContext)) {
                setSurveillance(!state);
            }
        }
    };

    SeekBar.OnSeekBarChangeListener bar_listener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekbar, int i, boolean b) {}

        @Override
        public void onStartTrackingTouch (SeekBar seekbar){}

        @Override
        public void onStopTrackingTouch (SeekBar seekbar){
            Integer prog = seekbar.getProgress() + 30;
            Utils.UrlTask("/motor/set?pos=" + prog.toString(), "text");
        }
    };

    public void readMotion() {

        Uri.Builder builder = Utils.getUrl("/motion/read");
        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (jsono != null) {

                    try {
                        motion = jsono.getString("motion");
                        sound = jsono.getString("sound");
                    } catch (JSONException e) {
                        Log.v("debug", "error finding arguments");
                    }

                    display.setText(motion);
                    sound_display.setText(sound);

                    setDisplay();
                }
            }
        };
        task.setType("json");
        task.execute(builder.build().toString());

    }

    public void setSurveillance(final boolean setstate) {

        Uri.Builder builder = CameraActivity.getTask(setstate, "None", "These images are recorded: you're screwed!");

        if (setstate) LCDActivity.sendMessage("I'm watching you\nYou can't escape");
        else LCDActivity.sendMessage("");

        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (!setstate) {
                    state = false;
                    Toast.makeText(appContext, "Surveillance set OFF", Toast.LENGTH_SHORT).show();
                    return;
                }

                capture();
            }
        };
        task.execute(builder.build().toString());
    }

    public void startTimer() {

        timer = Utils.getTimer(new Command() {
            @Override
            public void execute() {
                readMotion();
            }
        }, 1000);
    }

    public void setDisplay() {

        Uri.Builder builder2 = null;
        if (state) {
            if (motion.indexOf("YES") >= 0) {
                builder2 = Utils.getUrl("/camera/capture");
            }
        } else {
            builder2 = Utils.getUrl("/motion/stats");
        }

        MyAsyncTask task2 = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (image != null) {
                    stats = Bitmap.createScaledBitmap(image, 700, 700, false);
                    stats_display.setImageBitmap(stats);
                }
            }
        };
        task2.setType("image");
        if (builder2 != null) task2.execute(builder2.build().toString());
    }


    public void capture() {
        Uri.Builder builder2 = Utils.getUrl("/camera/capture");
        MyAsyncTask task2 = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (image != null) {
                    stats = Bitmap.createScaledBitmap(image, 700, 700, false);
                    stats_display.setImageBitmap(stats);
                    state = true;

                    Toast.makeText(appContext, "Surveillance set ON", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task2.setType("image");
        task2.execute(builder2.build().toString());
    }


}
