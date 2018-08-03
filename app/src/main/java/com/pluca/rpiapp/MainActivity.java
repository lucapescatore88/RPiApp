package com.pluca.rpiapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import static com.pluca.rpiapp.Utils.*;

public class MainActivity extends AppCompatActivity {

    Context appContext;
    ImageView button_camera, button_tree, button_motion, button_lcd, button_pot, button_rgb;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appContext = getApplicationContext();
        res = getResources();

        button_tree = (ImageView) findViewById(R.id.tree_button);
        button_tree.setOnClickListener(listenerTree);
        button_camera = (ImageView) findViewById(R.id.camera_button);
        button_camera.setOnClickListener(listenerCamera);
        button_motion = (ImageView) findViewById(R.id.motion_button);
        button_motion.setOnClickListener(listenerMotion);
        button_lcd = (ImageView) findViewById(R.id.lcd_button);
        button_lcd.setOnClickListener(listenerLCD);
        button_pot = (ImageView) findViewById(R.id.potentiometer_button);
        button_pot.setOnClickListener(listenerPot);
        button_rgb = (ImageView) findViewById(R.id.rgb_button);
        button_rgb.setOnClickListener(listenerRGB);
        Utils.setImage(res, button_camera, R.drawable.camera, 300);
        Utils.setImage(res, button_tree, R.drawable.tree_on, 300);
        Utils.setImage(res, button_lcd, R.drawable.lcd, 300);
        Utils.setImage(res, button_motion, R.drawable.motion, 300);
        Utils.setImage(res, button_pot, R.drawable.ohm, 300);
        Utils.setImage(res, button_rgb, R.drawable.rgb_led, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener listenerTree = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), SwitchTreeActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerCamera = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), CameraActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerLCD = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), LCDActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerMotion = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), MotionActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerPot = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), PotActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerRGB = new View.OnClickListener() {

        @Override
        public void onClick(View vv) {
            Intent intent = new Intent(vv.getContext(), RGBActivity.class);
            startActivity(intent);
        }
    };

}
