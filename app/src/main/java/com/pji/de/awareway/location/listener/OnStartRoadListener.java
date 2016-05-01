package com.pji.de.awareway.location.listener;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.utilitaires.XmlTask;
import com.pji.de.awareway.webbridge.AABridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sim on 08/03/2016.
 */
public class OnStartRoadListener implements Button.OnClickListener
{

    private String URL;
    private Spinner spinner;
    private XmlTask AsynTache;
    private Fragment fragment;

    public OnStartRoadListener(String uRL, XmlTask asynTache,
                                            Fragment fragment, Spinner spinner) {
        super();
        URL = uRL;
        AsynTache = asynTache;
        this.fragment = fragment;
        this.spinner = spinner;
    }

    @Override
    public void onClick(View view) {
        CheckBox c = (CheckBox)fragment.getActivity().findViewById(R.id.debugCheckBox);
        if(c.isChecked()){
            MainActivity.DEBUG = true;
        } else {
            MainActivity.DEBUG = false;
        }
        if (spinner.getSelectedItemPosition() > 0) {
            String nomLigne = (String) spinner.getSelectedItem();
            String[] tabLigne = nomLigne.split(": ");
            Long idRelation = Long.parseLong(tabLigne[2].trim());
            AsynTache.execute(String.format(URL, idRelation));

            try {
                String result = AsynTache.get();

                // CODE INUTILE SI LE SERVEUR EST DEPLOYE
                if (result.equals("")) {
                    AssetManager asm = fragment.getResources().getAssets();

                    try {
                        StringBuilder response = new StringBuilder();
                        InputStream is = asm.open("getNodesFromRelation.xml");

                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(is));
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }

                        result = response.toString();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                UserTravelNotifyTask travelNotifyTask = new UserTravelNotifyTask(idRelation);
                travelNotifyTask.execute((Void) null);

                ((com.pji.de.awareway.fragments.HomeFragment) fragment).populateListeNoeud(result,
                        tabLigne);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Context context = view.getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, view.getResources().getString(R.string.pois_fragment_no_road_selected), duration);
            toast.show();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserTravelNotifyTask extends AsyncTask<Void, Void, Boolean> {

        Long idRelation;
        UserTravelNotifyTask(Long idRelation) {
            this.idRelation = idRelation;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(MainActivity.userManager.isAuthentified()){
                Integer idUser = MainActivity.userManager.getUser().getIdUser();
                return AABridge.notifyTraveledRelationByUser(idRelation, idUser);
            } else {
                return AABridge.notifyTraveledRelation(idRelation);
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d("User Travel Notify : ", success.toString());
        }

    }
}
