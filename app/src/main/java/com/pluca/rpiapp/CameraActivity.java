package com.pluca.rpiapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by pluca on 18/02/17.
 */

public class CameraActivity extends AppCompatActivity {

    Context appContext;
    ImageView button;
    TextView button_capture;
    Resources res;
    boolean state = false;
    ListView listview;
    String cureffect = "None";

    String[] effects = new String[] { "None", "Emboss", "Posterise", "Sketch", "Blur",
            "Pastel", "Negative", "Gpen", "Colorswap", "Hatch", "Cartoon",
            "Washedout", "Solarize", "Oilpaint" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        appContext = getApplicationContext();
        res = getResources();

        button = (ImageView) findViewById(R.id.cameraon_button);
        button.setOnClickListener(listenerState);
        Utils.setImage(res,button,R.drawable.off,500);

        button_capture = (TextView) findViewById(R.id.capture_button);
        button_capture.setOnClickListener(listenerCapture);

        listview = (ListView) findViewById(R.id.listview_effects);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.string_inlist_layout,effects));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                cureffect = (String)((TextView) view).getText();
                sendState(state,cureffect.toLowerCase());
                Toast.makeText(appContext,
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            createHorizontalalLayout();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            createVerticalLayout();
        }
    }
*/
    View.OnClickListener listenerState = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if(NetworkUtils.checkNetwork(appContext)) {
                state = !state;
                sendState(state,cureffect);
                if(state) Utils.setImage(res,button,R.drawable.on,500);
                else Utils.setImage(res,button,R.drawable.off,500);
            }
        }
    };

    View.OnClickListener listenerCapture = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if(NetworkUtils.checkNetwork(appContext)) {
                if(!state) {
                    Toast.makeText(appContext,
                            "Camera off, cannot capture!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String urls = Utils.base_url+"/camera/capture";
                    MyAsyncTask task = new MyAsyncTask() {
                        @Override
                        public void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            if(image!=null) {
                                Log.v("debug","got image");
                                String imageurl = MediaStore.Images.Media.insertImage(getContentResolver(),
                                        image, "RPi captured", "RPi captured");
                                Toast.makeText(appContext,
                                        "Saved to Pictures", Toast.LENGTH_SHORT).show();
                                Log.v("debug","saved "+imageurl);
                            }
                        }
                    };
                    task.setType("image");
                    task.execute(urls);
                }
            }
        }
    };


    static public Uri.Builder getTask(boolean state, String eff, String note) {

        String state_txt = "OFF";
        if (state) state_txt = "ON";
        String url = Utils.base_url+"/camera/set";

        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("state", state_txt);
        builder.appendQueryParameter("eff", eff);
        builder.appendQueryParameter("capture", "False");
        if (note != "") builder.appendQueryParameter("note", note);

        return builder;

    }

    static public void sendState(boolean state, String eff) {

        Uri.Builder builder = getTask(state, eff, "");

        MyAsyncTask task = new MyAsyncTask();
        task.execute(builder.build().toString());
    }

}
