package com.pji.de.awareway.utilitaires;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sim on 15/03/2016.
 */
public class XmlTask extends AsyncTask<String, Void, String> {



    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Log.d("[GET REQUEST]", "HTTP Get succeeded");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            Log.e("[GET REQUEST]", e.getMessage() !=null ? e.getMessage() : "erreur");
        }
        Log.d("[GET REQUEST]", "Done with HTTP getting");

        return response.toString();
    }

}