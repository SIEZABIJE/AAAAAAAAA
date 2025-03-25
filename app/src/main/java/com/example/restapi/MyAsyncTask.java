package com.example.restapi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyAsyncTask extends AsyncTask {

    InputStream inputStream;
    URL url = null;

    @Override
    protected void onPreExecute() {
        Log.v(MainActivity.TAG, "------------------------> Start onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.v(MainActivity.TAG,"----------------------------------------> Start doInBackground");
        try{
            url = new URL("https://randomuser.me/api");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try{
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[4096];
        StringBuilder stringBuilder = new StringBuilder();
        while(true){
            try{
                if(!(inputStream.read(buffer)>0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(new String(buffer));
        }
        publishProgress(stringBuilder.toString());
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        Log.v(MainActivity.TAG,"----------------------------------------> Start doOnProgressUpdate");
        Log.v(MainActivity.TAG,values[0].toString());

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(MainActivity.TAG,"----------------------------------------> Start doOnPostExecute");

        super.onPostExecute(o);
    }
}
