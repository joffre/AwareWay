package com.pji.de.awareway.location.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import com.pji.de.awareway.utilitaires.XmlTask;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class RelationLocationListeneur implements LocationListener {

	public Location position;

	private String URL;

	private LocationManager lManager;

	private Fragment fragment;

	public RelationLocationListeneur(String url, LocationManager lm, Fragment frag) {
		this.URL = url;
		this.lManager = lm;
		if (ActivityCompat.checkSelfPermission(frag.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(frag.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);
		} else {
			Log.d("RelationLocationList" , "Permissions de localisations non accordees par l'utilisateur");
		}
		this.fragment = frag;
	}

	@Override
	public void onLocationChanged(Location location) {
	     position = location;
		 if (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			 lManager.removeUpdates(this);
		 }  else {
			 Log.d("RelationLocationList" , "Permissions de localisations non accordees par l'utilisateur");
		 }

		 loadRelation(location);
	}

	public void refresh(){
		if(position != null)loadRelation(position);
	}

	public void loadRelation(final Location location) {
		XmlTask asynTask = new XmlTask();
		asynTask.execute(String.format(URL, location.getLatitude(), location.getLongitude()));

		try {
			String result = asynTask.get();
			if (!result.equals("")) {
				((com.pji.de.awareway.fragments.HomeFragment) fragment).populateListeRelations(result);
			} else {
					/*AssetManager asm = fragment.getResources().getAssets();

					try {
						StringBuilder response = new StringBuilder();
						InputStream is = asm.open("getRelationName.xml");

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
					}*/

				new AlertDialog.Builder(fragment.getActivity())
						.setTitle("Récupération des lignes proches de vous impossible")
						.setMessage("Voulez vous réessayez ? Vérifiez que vous êtes connecté à internet")
						.setNegativeButton(android.R.string.no, null)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								loadRelation(location);
							}
						}).create().show();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
