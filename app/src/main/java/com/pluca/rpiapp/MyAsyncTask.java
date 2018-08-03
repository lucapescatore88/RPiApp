package com.pluca.rpiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by pluca on 08/02/17.
 */

class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

    JSONObject jsono;
    String data;
    Bitmap image;
    String type = "text";

    int timeout = 100000;
    int readtimeout = 1000000;

    @Override
    protected void onPreExecute() { super.onPreExecute(); }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(timeout /* milliseconds */);
            conn.setConnectTimeout(readtimeout /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream stream = conn.getInputStream();

            if(type=="json") {
                try {
                    data = convertStreamToString(stream);
                    Log.v("app",data);
                    jsono = new JSONObject(data);
                } catch (Exception e) {
                    Log.v("debug", "Error receving JSON");
                }
            }
            else if (type=="image") {
                try {
                    image = BitmapFactory.decodeStream(stream);
                } catch (Exception e2) {
                    Log.v("debug", "Error receiving Bitmap");
                }
            }
            else {
                data = convertStreamToString(stream);
            }

            stream.close();

            return true;
        }
        catch (Exception e3) {
            Log.v("debug","Exception: probably can't connect to server.");
            //e.printStackTrace();
        }
        return false;
    };

    protected void onPostExecute(Boolean result) { super.onPostExecute(result); }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void setTimeOut(int timeout) { this.timeout = timeout;}
    public void setReadTimeOut(int timeout) { this.readtimeout = timeout;}
    public void setType(String mytype) { type = mytype; }

}
