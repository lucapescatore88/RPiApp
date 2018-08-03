package com.pluca.rpiapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pluca on 18/02/17.
 */

public class Utils {

    static String base_url = "http://192.168.8.110:3000/api";

    public static void setImage(Resources res, ImageView view, int img_src, int size) {

        Bitmap src = BitmapFactory.decodeResource(res, img_src);
        Bitmap srcResized = Bitmap.createScaledBitmap(src, size, size, false);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, srcResized);
        dr.setCornerRadius(Math.max(srcResized.getWidth(), srcResized.getHeight()) / 10.f);
        view.setImageDrawable(dr);
    }

    public static Uri.Builder getUrl(String url){

        return Uri.parse(base_url+url).buildUpon();
    }

    static Timer getTimer(final Command cmd, int interval){

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                cmd.execute();
            }
        };
        timer.schedule(timerTask, 1000, interval);

        return timer;
    }

    static public void UrlTask(String url, String retType, final Command cmd) {

        Uri.Builder builder = Utils.getUrl(url);
        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                cmd.execute();
            }
        };
        task.setType(retType);
        task.execute(builder.build().toString());
    }

    static public void UrlTask(String url, String retType) {

        Uri.Builder builder = Utils.getUrl(url);
        MyAsyncTask task = new MyAsyncTask() {

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
            }
        };
        task.setType(retType);
        task.execute(builder.build().toString());
    }

}


