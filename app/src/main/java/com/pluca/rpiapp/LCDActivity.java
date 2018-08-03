package com.pluca.rpiapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by pluca on 24/02/17.
 */

public class LCDActivity extends AppCompatActivity {

    Context appContext;
    TextView button;
    EditText text;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcd);

        appContext = getApplicationContext();
        res = getResources();

        text = (EditText) findViewById(R.id.lcd_message_area);
        button = (TextView) findViewById(R.id.send_message_button);
        button.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if(NetworkUtils.checkNetwork(appContext)) {

                sendMessage(text.getText().toString());

            }
        }
    };

    static public void sendMessage(String message) {

        String url = Utils.base_url+"/lcd/set";

        Uri.Builder builder = Uri.parse(url).buildUpon();
        String [] rows = message.replace(" ","_").split("\n");
        Integer irow = 0;
        for (String r : rows) {
            builder.appendQueryParameter("message" + irow.toString(), r);
            irow += 1;
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(builder.build().toString());

    }
}
