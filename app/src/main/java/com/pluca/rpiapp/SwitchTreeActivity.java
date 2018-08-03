package com.pluca.rpiapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class SwitchTreeActivity extends AppCompatActivity {

    Context appContext;
    ImageView button;
    boolean state = false;
    LinearLayout rl;
    Resources res;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_page);

        appContext = getApplicationContext();
        res = getResources();
        rl = (LinearLayout)findViewById(R.id.activity_switch_page);

        button = (ImageView) findViewById(R.id.switchtree_button);
        button.setOnClickListener(buttonListener);

        readState();
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
        readState();
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if(NetworkUtils.checkNetwork(appContext)) {
                state = !state;
                sendState(state);
                setBkgState(state);
            }
        }
    };

    public void sendState(boolean state) {

        String state_txt = "OFF";
        if (state) state_txt = "ON";
        String urls = Utils.base_url+"/tree/set?state=" + state_txt;
        MyAsyncTask task = new MyAsyncTask();
        task.execute(urls);
    }

    public void readState() {

        String urls = Utils.base_url+"/tree/read";
        MyAsyncTask task = new MyAsyncTask() {
            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(data!=null) {

                    if (data.indexOf("ON") > -1) state = true;
                    else state = false;
                }
                setBkgState(state);
            }
        };
        task.execute(urls);
    };

    public void setBkgState(boolean state) {

        if (state) {
            rl.setBackgroundColor(Color.GREEN);
            Utils.setImage(res,button,R.drawable.tree_on,700);
        }
        else {
            rl.setBackgroundColor(Color.GRAY);
            Utils.setImage(res,button,R.drawable.tree_off,700);
        }

    }

    public void startTimer() {

        timer = Utils.getTimer(new Command() {
                @Override
                public void execute(){ readState(); } },2000);
    }

}


