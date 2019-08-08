package com.example.nhavo.map4d123;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cafe on 11/08/2016.
 */

public class FireBaseIDTask extends AsyncTask<String,Void,Boolean> {
    HttpURLConnection connection;
    @Override
    protected Boolean doInBackground(String... params) {
        if (params[0]!=null) {
            try {
                URL url = new URL("http://192.168.31.232/jamserver/api/fcm?token=" + params[0]);
                //Toast.makeText(MainActivity.class,"hello",Toast.LENGTH_LONG).show();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                boolean kt = builder.toString().contains("true");
                return kt;
            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            } finally {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}

