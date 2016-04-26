package com.pji.de.awareway.location.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import com.pji.de.awareway.utilitaires.XmlTask;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class RelationLocationListeneur implements LocationListener {

	public Location position;

	private String URL;

	private XmlTask AsynTache;

	private LocationManager lManager;

	private Fragment fragment;

	public RelationLocationListeneur(String url, XmlTask tache, LocationManager lm, Fragment frag) {
		this.URL = url;
		this.AsynTache = tache;
		this.lManager = lm;
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);
		this.fragment = frag;
	}

	@Override
	public void onLocationChanged(Location location) {
	     position = location;
	     AsynTache.execute(String.format(URL, location.getLatitude(), location.getLongitude()));
	     try {
			 String result = AsynTache.get();
			 lManager.removeUpdates(this);
			 
			 	// CODE INUTILE SI LE SERVEUR EST DEPLOYE
				if (result.equals("")) {
					AssetManager asm = fragment.getResources().getAssets();
					
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
					}
				} 
			 
			 ((com.pji.de.awareway.fragments.HomeFragment)fragment).populateListeRelations(result);
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
