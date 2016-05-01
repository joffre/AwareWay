package com.pji.de.awareway.webbridge;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.bean.AAUser;
import com.pji.de.awareway.bean.Poi;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.utilitaires.ParseurXmlToBean;

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

    public static ListePois getUserPois(Integer idUser){
        ListePois userPois = new ListePois();
        Map<String, String> params = new HashMap<String, String>();
        params.put("idUser",idUser.toString());

        String result = performGetCall(CONTEXT_PATH+USER_BRIDGE+"getUserPois", params);
        if(!result.isEmpty() && !result.equals("error")){
            userPois = ParseurXmlToBean.parseXmlToPoiList(result);
        }

        return userPois;
    }

    public static boolean deleteUserPoi(Integer idUser, Long idPoi){
        Map<String, String> params = new HashMap<String, String>();
        params.put("idUser",idUser.toString());
        params.put("idPoi", idPoi.toString());

        String result = performPostCall(CONTEXT_PATH+USER_BRIDGE+"deleteUserPoi", params);
        if(!result.isEmpty() && !result.equals("error")){
            return true;
        }
        return false;
    }

    public static boolean notifyTraveledRelationByUser(Long idRelation, Integer idUser){
        Map<String, String> params = new HashMap<String, String>();
        params.put("idUser",idUser.toString());
        params.put("idRelation", idRelation.toString());

        String result = performPostCall(CONTEXT_PATH+USER_BRIDGE+"notifyTraveledRelationByUser", params);
        if(!result.isEmpty() && !result.equals("error")){
            return true;
        }
        return false;
    }

    public static boolean notifyTraveledRelation(Long idRelation){
        Map<String, String> params = new HashMap<String, String>();
        params.put("idRelation", idRelation.toString());

        String result = performPostCall(CONTEXT_PATH+USER_BRIDGE+"notifyTraveledRelation", params);
        if(!result.isEmpty() && !result.equals("error")){
            return true;
        }
        return false;
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
            writer.write(getDataParamString(postDataParams));

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

    public static String performGetCall(String requestURL,
                                          Map<String, String> params) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL+"?"+getDataParamString(params));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
           // conn.setDoOutput(true);

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
            Log.e("[GET REQUEST]", e.getMessage() !=null ? e.getMessage() : "erreur");
        }
        Log.d("[GET REQUEST]", "Done with HTTP getting");

        return response;
    }

    private static String getDataParamString(Map<String, String> params) throws UnsupportedEncodingException {
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

    public static Bitmap getBitmapFromURL(URL url){
        URL imageUrl = url;
        Bitmap bitmap = null;
        try {
            InputStream in = imageUrl.openStream();
            bitmap = BitmapFactory.decodeStream(in);
        }catch (IOException e){
            Log.d("getBitmapFromURI", "Image impossible à récupérer : " + e.getMessage());
        }
        return bitmap;
    }
}
