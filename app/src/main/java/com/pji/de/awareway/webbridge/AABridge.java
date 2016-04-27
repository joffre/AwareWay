package com.pji.de.awareway.webbridge;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.activity.CarteActivity;
import com.pji.de.awareway.bean.AAUser;
import com.pji.de.awareway.bean.Poi;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Geoffrey on 18/04/2016.
 */
public class AABridge {

    public static final String CONTEXT_PATH = "http://was59-h01-5-50-85-94.dsl.sta.abo.bbox.fr:8080/PJI40_serveur/";

    private static final String OSM_BRIDGE = "osm/";
    private static final String USER_BRIDGE = "user/";

    /*public static DefaultHttpClient getHttpClient(){
        int timeout = 10000;
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = timeout;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = timeout;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return new DefaultHttpClient(httpParameters);
    }*/

    public static AAUser login(String email, String password){
        AAUser user = new AAUser();
        Map<String, String> params = new HashMap<String, String>();
        params.put("signType", "simple");
        params.put("emailUtilisateur", email.toLowerCase());
        params.put("mdpUtilisateur", password);

        String result = performPostCall(CONTEXT_PATH+USER_BRIDGE+"login", params);
        System.out.println("Login : " + result);
        if(!result.isEmpty() && !result.equals("error")){
            Gson gson = new Gson();
            user = gson.fromJson(result, AAUser.class);
            System.out.println("Bienvenue : " + user.getFirstName() + " " + user.getLastName() + " ("+ user.getEmail() +")");
        }
        return user;
    }

    public static AAUser loginWithGoogle(String email){
        AAUser user = new AAUser();
        Map<String, String> params = new HashMap<String, String>();
        params.put("signType", "google");
        params.put("emailUtilisateur", email.toLowerCase());
        params.put("mdpUtilisateur", "null");

        String result = performPostCall(CONTEXT_PATH+USER_BRIDGE+"login", params);
        System.out.println("Google Sign In : " + result);
        if(!result.isEmpty() && !result.equals("error")){
            Gson gson = new Gson();
            user = gson.fromJson(result, AAUser.class);
        }
        return user;
    }

    public static boolean createPoi(Poi poi){
        String passerelle = OSM_BRIDGE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("latitude", poi.getLat());
        params.put("longitude", poi.getLon());
        params.put("categorie", poi.getCategorie());
        params.put("nom", poi.getNom());
        params.put("debutVisible", "" + poi.getDebutVisible());
        params.put("finVisible", "" + poi.getFinVisible());
        params.put("idRelation", poi.getIdRelation());
        params.put("commentaire", poi.getCommentaire());
        params.put("lienImage", poi.getLienImage());
        params.put("lienWeb", poi.getLienWeb());
        if(MainActivity.userManager.isAuthentified()) {
            params.put("idUser", MainActivity.userManager.getUser().getIdUser().toString());
            passerelle = USER_BRIDGE;
        }
        performPostCall(CONTEXT_PATH + passerelle + "ajouterPoi", params);
        return false;
    }

    public static String  performPostCall(String requestURL,
                                   Map<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.d("GetPostDataString : ", result.toString());
        return result.toString();
    }

    /*private static String executePOST(String url,  List<NameValuePair> params){
        String result = "";
        try {
            HttpClient httpclient = getHttpClient();//HttpClientFactory.createHttpClient();//HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);

            // Request parameters and other properties.

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }
                    result = out.toString();   //Prints the string content read from input stream
                    reader.close();
                } finally {
                    instream.close();
                }
            }
        } catch (IOException e) {
        }
        return result;
    }
*/

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromURI(ContentResolver contentResolver, Uri uri){
        Uri imageUri = uri;
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        }catch (IOException e){
            Log.d("getBitmapFromURI", "Image impossible à récupérer");
        }
        return bitmap;
    }
}
