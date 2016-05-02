package com.pji.de.awareway.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.bean.Poi;
import com.pji.de.awareway.webbridge.AABridge;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class EditPoiActivity extends Activity  implements LocationListener {

    private Button submitBtn;
    private float currentKm;
    private Location debutLigne;
    private Button startBtn, refreshBtn;
    private float startPk, endPk;
    private EditText name, description;
    private Location currentLocation;
    private String lineName, idLigne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poi);

        Bundle b    = getIntent().getExtras();
        debutLigne = b.getParcelable("debutLigne");
        currentLocation = b.getParcelable("currentLocation");
        lineName = b.getParcelable("lineName");
        idLigne = b.getString("idLigne");

        submitBtn = (Button)findViewById(R.id.poisSubmit);
        startBtn = (Button)findViewById(R.id.startBtn);
        refreshBtn = (Button)findViewById(R.id.refreshBtn);
        name = (EditText)findViewById(R.id.edit_poi_name);
        description = (EditText)findViewById(R.id.edit_poi_description);
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run(){

                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submitAction(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(/*startPk > 0.0 && endPk > 0.0 && */(name.getText().toString() != "")){
                    try {
                        List<String> l_value = generateURIPoiCreation();
                        List<ContentValues> params = new ArrayList<ContentValues>(2);

                        Poi currentPoi = new Poi();
                        currentPoi.setLat(l_value.get(0));
                        currentPoi.setLon(l_value.get(1));
                        currentPoi.setCategorie(l_value.get(2));
                        currentPoi.setNom(l_value.get(3));
                        currentPoi.setDebutVisible(Float.parseFloat(l_value.get(4)));
                        currentPoi.setFinVisible(Float.parseFloat(l_value.get(5)));
                        currentPoi.setIdRelation(l_value.get(6));
                        currentPoi.setCommentaire(l_value.get(7));
                        currentPoi.setLienImage(l_value.get(8));
                        currentPoi.setLienWeb(l_value.get(9));

                        AABridge.createPoi(currentPoi);
                        showToast(getResources().getString(R.string.poi_creator_poi_created));
                    } catch (Exception e){
                        e.printStackTrace();
                        showToast(getResources().getString(R.string.poi_creator_error));
                    }
                    finish();
                } else {
                    showToast(getResources().getString(R.string.poi_creator_not_enough_info));
                }
            }
        }).start();
    }

    private List<String> generateURIPoiCreation() throws URISyntaxException {
        List<String> l_return = new ArrayList<String>();
        double latitude;
        double longitude;
        float debutVisible;
        float finVisible;
        if(MainActivity.DEBUG)
        {

            latitude = 50.6239420;
            longitude = 3.1241463;
            debutVisible = 3.8f;
            finVisible =4.5f;


        }
        else
        {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            debutVisible = startPk;
            finVisible = endPk;
        }
        String categorie = "";
        String nom = name.getText().toString();
        String nomRelation = idLigne; //idRelation
        String commentaire = description.getText().toString();
        String lienImage = "";
        String lienWeb = "";

        l_return.add(""+latitude);
        l_return.add(""+longitude);
        l_return.add(categorie);
        l_return.add(nom);
        l_return.add(debutVisible+"");
        l_return.add(finVisible+"");
        l_return.add(nomRelation);
        l_return.add(commentaire);
        l_return.add(lienImage);
        l_return.add(lienWeb);
        String createLink = latitude + "/" + longitude + "/"+ categorie+"/"+ nom+"/"+ debutVisible + "/"+ finVisible + "/"+ nomRelation+"/" +commentaire+ "/"+lienImage+"/"+ lienWeb;
        //String createLink = String.format(latitude + "", longitude + "", categorie, nom, debutVisible + "", finVisible + "", nomRelation, commentaire, lienImage, lienWeb);

        return l_return;
    }

    public void startBtn(View view){
        if(startBtn.getText().equals("Début")) {
            startPk = currentKm;
            startBtn.setText("Fin");
            refreshBtn.setVisibility(View.VISIBLE);
        } else {
            endPk = currentKm;
            startBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshBtn(View view){
        startPk = -1.0f;
        endPk = -1.0f;
        startBtn.setVisibility(View.VISIBLE);
        refreshBtn.setVisibility(View.INVISIBLE);
        startBtn.setText("Début");
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentKm = (debutLigne.distanceTo(location)/1000)+1.0f;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
