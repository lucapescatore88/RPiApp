package com.pluca.rpiapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;

/**
 * Created by pluca on 24/02/17.
 */

public class RGBActivity extends AppCompatActivity {

    Context appContext;
    Resources res;

    Button button_r;
    Button button_g;
    Button button_b;

    Boolean rgb[] = {false,false,false};
    static final String cols[] = {"red","green","blue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb);

        appContext = getApplicationContext();
        res = getResources();

        button_r = (Button) findViewById(R.id.rgb_button_red);
        button_g = (Button) findViewById(R.id.rgb_button_green);
        button_b = (Button) findViewById(R.id.rgb_button_blue);
        button_r.setOnClickListener(listener);
        button_g.setOnClickListener(listener);
        button_b.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            
            if(vv.getTag().toString().indexOf("blue")>-1) rgb[2] = !rgb[2];
            else if (vv.getTag().toString().indexOf("red")>-1) rgb[0] = !rgb[0];
            else  rgb[1] = !rgb[1];

            if(NetworkUtils.checkNetwork(appContext)) sendRGB(rgb);
        }
    };

    static public void sendRGB(Boolean rgb[]) {

        String url = Utils.base_url+"/rgb/set";

        Uri.Builder builder = Uri.parse(url).buildUpon();
        int irow = 0;
        for (Boolean col : rgb) {
            builder.appendQueryParameter(cols[irow],col.toString());
            irow += 1;
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(builder.build().toString());
    }

}
