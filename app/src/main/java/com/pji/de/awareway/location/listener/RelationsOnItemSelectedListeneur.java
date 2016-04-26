package com.pji.de.awareway.location.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import com.pji.de.awareway.utilitaires.XmlTask;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RelationsOnItemSelectedListeneur implements OnItemSelectedListener {

	private String URL;

	private XmlTask AsynTache;
	private Fragment fragment;

	public RelationsOnItemSelectedListeneur(String uRL, XmlTask asynTache,
			Fragment fragment) {
		super();
		URL = uRL;
		AsynTache = asynTache;
		this.fragment = fragment;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		if (position > 0) {
			String nomLigne = (String) parent.getItemAtPosition(position);
			String[] tabLigne = nomLigne.split(":");
			AsynTache.execute(String.format(URL, tabLigne[1].trim()));
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
				((com.pji.de.awareway.fragments.HomeFragment) fragment).populateListeNoeud(result,
							tabLigne);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
