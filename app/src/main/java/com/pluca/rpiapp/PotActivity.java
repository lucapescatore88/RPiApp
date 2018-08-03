package com.pluca.rpiapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pluca on 25/02/17.
 */

public class PotActivity extends AppCompatActivity {

    Context appContext;
    TextView display;
    Resources res;
    Timer timer;
    float value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot);

        appContext = getApplicationContext();
        res = getResources();

        display = (TextView) findViewById(R.id.pot_background);

        startTimer();
    }

    @Override
    protected void onPause(){
        super.onPause();
        timer.cancel();
        timer = null;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(timer == null) startTimer();
    }

    public void readMotion() {

        Uri.Builder builder = Utils.getUrl("/potentiometer/read");
        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if(data!=null) {
                    try {
                        value = Float.parseFloat(data);
                        int intvalue = (int) (value * 255);
                        int color = Color.argb(255, intvalue, intvalue, 64);
                        display.setBackgroundColor(color);
                    }
                    catch (Exception e) {}
                }
            }
        };
        task.execute(builder.build().toString());

    }

    public void startTimer() {

        timer = Utils.getTimer(new Command() {
            @Override
            public void execute(){ readMotion(); } },100);
    }
}
