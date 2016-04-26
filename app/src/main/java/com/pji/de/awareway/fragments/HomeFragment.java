package com.pji.de.awareway.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pji.de.awareway.R;
import com.pji.de.awareway.activity.CarteActivity;
import com.pji.de.awareway.bean.Relation;
import com.pji.de.awareway.liste.ListeNoeuds;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.location.listener.OnStartRoadListener;
import com.pji.de.awareway.location.listener.RelationLocationListeneur;
import com.pji.de.awareway.utilitaires.ParseurXmlToBean;
import com.pji.de.awareway.utilitaires.XmlTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

	private View view;
	private LocationManager lManager;
	private Button btnStart;
	private ListeNoeuds listeNodes;
	private ListePois listePois;
	private String idLigne;
	private SeekBar seekbar;
	private Spinner spinner;
	private TextView waiting_text;
    private ProgressBar loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pois, null);

		// look if your GPS is enable
		lManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		if (lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			new RelationLocationListeneur(getResources().getString(
					R.string.URL_GET_RELATION_NAME), new XmlTask(), lManager, this);
		} else {
			Toast.makeText(
					getActivity(),
					"Vous devez activer le GPS pour pouvoir utiliser l'application",
					Toast.LENGTH_LONG).show();
			Intent myIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
		}

		//handle the seekBar
		seekbar = (SeekBar)view.findViewById(R.id.seekBar);
		seekbar.setProgress(0);
		seekbar.incrementProgressBy(1);
		seekbar.setMax(2);

        btnStart = (Button)view.findViewById(R.id.btnStart);
        btnStart.setClickable(false);

		return view;
	}

	public void populateListeRelations(String result){
		
		spinner = (Spinner) view
				.findViewById(R.id.spinner);
		btnStart = (Button)view.findViewById(R.id.btnStart);
        btnStart.setClickable(true);

        List<String> listeTER = new ArrayList<String>();
		
		List<Relation> listeRelations = ParseurXmlToBean
				.parseXmlToRelationList(result);
		listeTER.add(getResources().getString(R.string.pois_fragment_first_value_list));
		for(Relation rel : listeRelations){
			listeTER.add(rel.getNom()+": "+rel.getIdentifiant());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				listeTER);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		waiting_text = (TextView)view.findViewById(R.id.waiting_text);
        loading = (ProgressBar)view.findViewById(R.id.loading);
		waiting_text.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

		OnStartRoadListener btnListener = new OnStartRoadListener(getResources().getString(R.string.URL_GET_NODES), new XmlTask(), this, spinner);
		btnStart.setOnClickListener(btnListener);

	
	}

	
	public void populateListeNoeud(String result, String[] nomLigne){
		listeNodes = ParseurXmlToBean.parseXmlToNoeudList(result);
		idLigne = nomLigne[2].trim();
		
		XmlTask asynTache = new XmlTask();
		asynTache.execute(String.format(getResources().getString(R.string.URL_GET_POIS), idLigne));
			String xml;
			try {
				xml = asynTache.get();
			 
			 	// CODE INUTILE SI LE SERVEUR EST DEPLOYE
				if (xml.equals("")) {
					AssetManager asm = this.getResources().getAssets();

						StringBuilder response = new StringBuilder();
						InputStream is = asm.open("getPoisFromRelation.xml");
						
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						String line;
						while ((line = br.readLine()) != null) {
							response.append(line);
						}
						
						xml = response.toString();
					
				}
				
				listePois = ParseurXmlToBean.parseXmlToPoiList(xml);
				
				Intent intent = new Intent(this.getActivity(),CarteActivity.class);
				intent.putExtra("listeNoeud", (Parcelable)listeNodes);
				intent.putExtra("listePois", (Parcelable)listePois);
				intent.putExtra("LineName", nomLigne[0].trim());
				intent.putExtra("idLigne", idLigne);
				
				startActivity(intent);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static com.pji.de.awareway.fragments.HomeFragment newInstance() {
		return new com.pji.de.awareway.fragments.HomeFragment();
	}



}
