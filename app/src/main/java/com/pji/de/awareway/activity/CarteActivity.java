package com.pji.de.awareway.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.bean.Noeud;
import com.pji.de.awareway.bean.Poi;
import com.pji.de.awareway.dialog.PoiDialog;
import com.pji.de.awareway.liste.ListeNoeuds;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.utilitaires.FakeLocation;
import com.pji.de.awareway.utilitaires.PoisComparator;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarteActivity extends Activity implements LocationListener {

	private MapView mMapView;
	private IMapController mMapController;
	private OverlayItem current_position;
	private LocationManager lManager;
	private PathOverlay myPath;
	private ItemizedIconOverlay<OverlayItem> affichageGares;
	private ItemizedIconOverlay<OverlayItem> affichagePois;
	private List<Noeud> gares;
	private ListePois listePois;
	private ListeNoeuds nodeList;
	private TextView distanceTerminusTextView;
	private TextView kmCourantTextView;
	private Location debutLigne;
	private Location finLigne;
	private Location dernierLocation;
	private List<TextView> listeVuePoi;
	private TextView LineName;
	private TextView oldPoisDistance,oldPoisName,currentPoisDistance,currentPoisName,
			currentPoisPk, nextPois1Distance, nextPois1Name, nextPois2Distance,
			nextPois2Name, nextPois2Pk, oldPoisPk, nextPois1Pk, nomPoi, poiWeb, poiDescription;
	public Poi currentPoi;
	private boolean poisInformationsOpen = false;
    private boolean currentPoiShowed = false;
	private FakeLocation fLoc;
	private Button poisCreator;
	private Location currentLocation;
	private String idLigne;
    private int indexOfCurrentPoi;
    private ViewSwitcher viewSwitcher;
    private Animation slide_in_left, slide_out_right;
    private Button toMap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carte);

		distanceTerminusTextView = (TextView)findViewById(R.id.terminus);

        poiWeb = (TextView)findViewById(R.id.poiWeb);
        poiDescription = (TextView)findViewById(R.id.poiDescription);
        nomPoi = (TextView)findViewById(R.id.nomPoi);
		oldPoisDistance = (TextView)findViewById(R.id.oldPoisDistance);
		oldPoisName = (TextView)findViewById(R.id.oldPoisName);
		currentPoisDistance = (TextView)findViewById(R.id.currentPoisDistance);
		currentPoisName = (TextView)findViewById(R.id.currentPoisName);
		currentPoisPk = (TextView)findViewById(R.id.currentPoisPk);
		nextPois1Distance = (TextView)findViewById(R.id.nextPois1Distance);
		nextPois1Name = (TextView)findViewById(R.id.nextPois1Name);
		nextPois2Distance = (TextView)findViewById(R.id.nextPois2Distance);
		nextPois2Name = (TextView)findViewById(R.id.nextPois2Name);
		nextPois2Pk = (TextView)findViewById(R.id.nextPois2Pk);
		oldPoisPk = (TextView)findViewById(R.id.oldPoisPk);
		nextPois1Pk = (TextView)findViewById(R.id.nextPois1Pk);
		poisCreator = (Button)findViewById(R.id.PoisCreator);

		listeVuePoi = new ArrayList<TextView>();

		kmCourantTextView = (TextView)findViewById(R.id.kmcourant);
		//poiProcheTextView = (TextView)findViewById(R.id.poiproche);
		LineName = (TextView)findViewById(R.id.trajet);

		Bundle b    = getIntent().getExtras();
		nodeList    = b.getParcelable("listeNoeud");
		LineName.setText(b.getString("LineName"));
		idLigne = b.getString("idLigne");
		listePois = b.getParcelable("listePois");


		gares = new ArrayList<Noeud>();

		// test the viewSwitcher
		//buttonPrev = (Button) findViewById(R.id.prev);
		toMap = (Button) findViewById(R.id.toMap);
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);

		slide_in_left = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		slide_out_right = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);

		viewSwitcher.setInAnimation(slide_in_left);
		viewSwitcher.setOutAnimation(slide_out_right);

		// Creation de la carte
		this.mMapView = (MapView) findViewById(R.id.mapView);
		this.mMapView.setBuiltInZoomControls(true);
		this.mMapView.setMultiTouchControls(true);

		this.mMapController = this.mMapView.getController();
		// Configuration du zoom
		this.mMapController.setZoom(15);

		// Ligne de Train
		myPath = new PathOverlay(Color.RED, this);

		for(Noeud noeud : nodeList){
			if(noeud.isEstUneGare()){
				gares.add(noeud);
			}else{
				GeoPoint pt = new GeoPoint(Double.valueOf(noeud.getLat()),Double.valueOf(noeud.getLon()));

				myPath.addPoint(pt);
			}
		}
		nodeList.removeAll(gares);

		finLigne = new Location("finLigne");
		finLigne.setLatitude(Double.valueOf(nodeList.get(nodeList.size() - 1).getLat()));
		finLigne.setLongitude(Double.valueOf(nodeList.get(nodeList.size() - 1).getLon()));

		debutLigne = new Location("debutLigne");
		debutLigne.setLatitude(Double.valueOf(nodeList.get(0).getLat()));
		debutLigne.setLongitude(Double.valueOf(nodeList.get(0).getLon()));


		myPath.getPaint().setStrokeWidth(3.0f);

		//Affichage des gares

		ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();

		for(Noeud noeud : gares){
			OverlayItem gare = new OverlayItem("gare", noeud.getNom(), new GeoPoint(Double.valueOf(noeud.getLat()),Double.valueOf(noeud.getLon())));
			Drawable newMarker = this.getResources().getDrawable(R.drawable.gare);
			gare.setMarker(newMarker);
			mItems.add(gare);
		}
		affichageGares = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), mItems, new OnItemGestureListener<OverlayItem>() {
			@Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
				Toast.makeText(CarteActivity.this, item.getTitle()+": "+item.getSnippet(), Toast.LENGTH_SHORT).show();
				return true;
			}
			@Override public boolean onItemLongPress(final int index, final OverlayItem item) {
				return true;
			}
		} );


		//affichage des pois
		ArrayList<OverlayItem> pItems = new ArrayList<OverlayItem>();

		for(Poi poi : listePois){

			Location locationT = new Location("poi");
			locationT.setLatitude(Double.valueOf(poi.getLat()));
			locationT.setLongitude(Double.valueOf(poi.getLon()));
			float distance = debutLigne.distanceTo(locationT);
			poi.setPointKilometrique((distance/1000));

			OverlayItem poInt = new OverlayItem("Point d'interet", poi.getNom(), new GeoPoint(Double.valueOf(poi.getLat()),Double.valueOf(poi.getLon())));
			Drawable newMarker = this.getResources().getDrawable(R.drawable.poi);
			poInt.setMarker(newMarker);
			pItems.add(poInt);
		}
		Collections.sort(listePois, new PoisComparator());
		affichagePois = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), pItems, new OnItemGestureListener<OverlayItem>() {
			@Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
				Toast.makeText(CarteActivity.this, item.getTitle()+" "+item.getSnippet(), Toast.LENGTH_SHORT).show();
				return true;
			}
			@Override public boolean onItemLongPress(final int index, final OverlayItem item) {
				return true;
			}
		} );



		// Ajout de la ligne de train
		this.mMapView.getOverlays().add(myPath);

		// Ajout des gares
		this.mMapView.getOverlays().add(affichageGares);

		// Ajout des pois
		this.mMapView.getOverlays().add(affichagePois);


		// Centrage de la carte sur le premier point de la ligne
		this.mMapController.setCenter(new GeoPoint(Double.valueOf(nodeList.get(0).getLat()), Double.valueOf(nodeList.get(0).getLon())));

		this.mMapView.invalidate();

		if(MainActivity.DEBUG){
			new FakeLocation(this).start();
		} else {
            //TODO
            /*
			lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 100, this);*/
		}
	    /*lManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

		if(fLoc == null || fLoc.getStatus().equals(AsyncTask.Status.FINISHED))
			new FakeLocation().execute(this);*/

		if(MainActivity.userManager.isAuthentified()){
			poisCreator.setVisibility(View.VISIBLE);
		} else {
			poisCreator.setVisibility(View.GONE);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carte, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
		dernierLocation = location;
		float kilometreCourant = (debutLigne.distanceTo(location)/1000);
		DecimalFormat df = new DecimalFormat("0.0");
		distanceTerminusTextView.setText("Terminus: "+df.format(location.distanceTo(finLigne)/1000)+"km");
		kmCourantTextView.setText(df.format(kilometreCourant));


		//////////////////////////////////////////////////////////
		// set up currentPoi
		Poi currentPoiTmp = listePois.getNextPoi(kilometreCourant);
		if(currentPoiTmp != null) {
			currentPoisName.setText(currentPoiTmp.getNom());
			currentPoisDistance.setText(""+df.format(currentPoiTmp.getPointKilometrique() - kilometreCourant)+"km");
			currentPoisDistance.setText(getDistanceLabel(currentPoiTmp.getPointKilometrique() - kilometreCourant));
			currentPoisPk.setText(""+currentPoiTmp.getPointKilometrique());

			// set up old poi
			indexOfCurrentPoi = listePois.indexOf(currentPoiTmp);
			if (indexOfCurrentPoi > 0) {
				Poi oldPoi = listePois.get(indexOfCurrentPoi - 1);
				oldPoisName.setText(oldPoi.getNom());
				oldPoisDistance.setText(getDistanceLabel(oldPoi.getPointKilometrique() - kilometreCourant));
				oldPoisPk.setText(""+oldPoi.getPointKilometrique());
			}

			// set up next pois : nxtPois1
			if (indexOfCurrentPoi < listePois.size() - 2) {
				Poi nextPoi1 = listePois.get(indexOfCurrentPoi + 2);
				nextPois2Name.setText(nextPoi1.getNom());
				nextPois2Distance.setText(getDistanceLabel(nextPoi1.getPointKilometrique() - kilometreCourant));
				nextPois2Pk.setText(""+nextPoi1.getPointKilometrique());
			}
			// nextpoi 2
			if (indexOfCurrentPoi < listePois.size() - 1) {
				Poi nextPoi2 = listePois.get(indexOfCurrentPoi + 1);
				nextPois1Name.setText(nextPoi2.getNom());
				nextPois1Distance.setText(getDistanceLabel(nextPoi2.getPointKilometrique() - kilometreCourant));
				nextPois1Pk.setText(""+nextPoi2.getPointKilometrique());
			}
		}

		for(int i=0;i<listeVuePoi.size();i++){
			TextView textview = listeVuePoi.get(i);
			if(listePois.size() > i){
				Poi poi = listePois.get(i);
				textview.setText(df.format(poi.getDistance())+"km :"+poi.getNom());
				setOnClickListenerPoiTextView(textview, i);
			}else{
				setOnClickListenerPoiTextView(textview, -1);
			}
		}

		//CurentPoisVisibility
		if(checkCurrentPoisVisibility(kilometreCourant, currentPoiTmp))
		{
			//show the informations

            Log.d("DEBUG POI INFORMATIONS", currentPoiTmp.getNom() + " is near to you");
		}

		currentPoi = currentPoiTmp;

		this.mMapView.getOverlays().clear();
		this.mMapView.getOverlays().add(myPath);
		this.mMapView.getOverlays().add(affichageGares);
		current_position = new OverlayItem("Here", "votre position", new GeoPoint(location.getLatitude(),location.getLongitude()));
		Drawable newMarker = this.getResources().getDrawable(R.drawable.position);
		current_position.setMarker(newMarker);
		mItems.add(current_position);

		ItemizedIconOverlay<OverlayItem> myLocation = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), mItems, new OnItemGestureListener<OverlayItem>() {
			@Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
				Toast.makeText(CarteActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();

				return true;
			}
			@Override public boolean onItemLongPress(final int index, final OverlayItem item) {
				return true;
			}
		} );

		// Ajout de la localisation
		this.mMapView.getOverlays().add(myLocation);
		this.mMapView.getOverlays().add(affichagePois);
		this.mMapView.invalidate();
		centrerEcran();

	}

	public void setLocation(Integer index){

		final Noeud currentNode = nodeList.get(index);
		final ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();

		final GeoPoint newCurrent = new GeoPoint(Double.valueOf(currentNode.getLat()), Double.valueOf(currentNode.getLon()));
		Location current = new Location("reverseGeocoded");
		current.setLatitude(newCurrent.getLatitude());
		current.setLongitude(newCurrent.getLongitude());
		current.setAccuracy(3333);
		current.setBearing(333);


		final Location location = current;
		/*GeoPoint newCurrent = new GeoPoint(59529200, 18071400);
		Location current = new Location("reverseGeocoded");
		current.setLatitude(newCurrent.getLatitudeE6() / 1e6);
		current.setLongitude(newCurrent.getLongitudeE6() / 1e6);
		current.setAccuracy(3333);
		current.setBearing(333);*/

		dernierLocation = location;
		final float kilometreCourant = (debutLigne.distanceTo(location)/1000);

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				/*Toast.makeText(CarteActivity.this, "setLocation : Node '" + currentNode.getId() + "' => (lat:" + currentNode.getLat() + ", lon:" + currentNode.getLon() + ") => (" + location.getLatitude() + "," + location.getLongitude() + ")", Toast.LENGTH_SHORT).show();*/
				DecimalFormat df = new DecimalFormat("0.0");
				distanceTerminusTextView.setText("Terminus: " + df.format(location.distanceTo(finLigne) / 1000) + "km");
				kmCourantTextView.setText(df.format(kilometreCourant));


				//////////////////////////////////////////////////////////
				// set up currentPoi
				Poi currentPoiTmp = listePois.getNextPoi(kilometreCourant);

                if(currentPoi != currentPoiTmp){
                    currentPoiShowed = false;
                }
                currentPoi = currentPoiTmp;

				if(currentPoiTmp != null) {
					currentPoisName.setText(currentPoiTmp.getNom());
					currentPoisDistance.setText(getDistanceLabel(currentPoiTmp.getPointKilometrique() - kilometreCourant));
					Log.d("DEBUG", "" + (currentPoiTmp.getPointKilometrique() - kilometreCourant));
					currentPoisPk.setText(df.format(currentPoiTmp.getPointKilometrique()));

					// set up old poi
					indexOfCurrentPoi = listePois.indexOf(currentPoiTmp);
					if (indexOfCurrentPoi > 0) {
						Poi oldPoi = listePois.get(indexOfCurrentPoi - 1);
						oldPoisName.setText(oldPoi.getNom());
						oldPoisDistance.setText(getDistanceLabel(oldPoi.getPointKilometrique() - kilometreCourant));
						oldPoisPk.setText(df.format(oldPoi.getPointKilometrique()));
					}

					// set up next pois : nxtPois1
					if (indexOfCurrentPoi < listePois.size() - 2) {
						Poi nextPoi1 = listePois.get(indexOfCurrentPoi + 2);
						nextPois2Name.setText(nextPoi1.getNom());
						nextPois2Distance.setText(getDistanceLabel(nextPoi1.getPointKilometrique() - kilometreCourant));
						nextPois2Pk.setText(df.format(nextPoi1.getPointKilometrique()));
					} else {
						nextPois2Name.setText(getResources().getString(R.string.map_name));
						nextPois2Distance.setText(getResources().getString(R.string.map_distance));
						nextPois2Pk.setText(getResources().getString(R.string.map_pk));
					}
					// nextpoi 2
					if (indexOfCurrentPoi < listePois.size() - 1) {
						Poi nextPoi2 = listePois.get(indexOfCurrentPoi + 1);
						nextPois1Name.setText(nextPoi2.getNom());
						nextPois1Distance.setText(getDistanceLabel(nextPoi2.getPointKilometrique() - kilometreCourant));
						nextPois1Pk.setText(df.format(nextPoi2.getPointKilometrique()));
					}
					else {
						nextPois1Name.setText(getResources().getString(R.string.map_name));
						nextPois1Distance.setText(getResources().getString(R.string.map_distance));
						nextPois1Pk.setText(getResources().getString(R.string.map_pk));
					}
				} else {
                    /*currentPoisName.setText(getResources().getString(R.string.map_name));
                    currentPoisDistance.setText(getResources().getString(R.string.map_distance));
                    currentPoisPk.setText(getResources().getString(R.string.map_pk));

                    nextPois1Name.setText(getResources().getString(R.string.map_name));
                    nextPois1Distance.setText(getResources().getString(R.string.map_distance));
                    nextPois1Pk.setText(getResources().getString(R.string.map_pk));

                    nextPois2Name.setText(getResources().getString(R.string.map_name));
                    nextPois2Distance.setText(getResources().getString(R.string.map_distance));
                    nextPois2Pk.setText(getResources().getString(R.string.map_pk));

                    if (indexOfCurrentPoi != -1){
                        Poi oldPoi = listePois.get(indexOfCurrentPoi - 1);
                    oldPoisName.setText(oldPoi.getNom());
                    oldPoisDistance.setText(getDistanceLabel(oldPoi.getPointKilometrique() - kilometreCourant));
                    oldPoisPk.setText(df.format(oldPoi.getPointKilometrique()));
                }*/
				}

				for(int i=0;i<listeVuePoi.size();i++){
					TextView textview = listeVuePoi.get(i);
					if(listePois.size() > i){
						Poi poi = listePois.get(i);
						textview.setText(df.format(poi.getDistance())+"km :"+poi.getNom());
						setOnClickListenerPoiTextView(textview, i);
					}else{
						setOnClickListenerPoiTextView(textview, -1);
					}
				}

                //CurentPoisVisibility
                if(currentPoiTmp != null && checkCurrentPoisVisibility(kilometreCourant, currentPoiTmp) && currentPoiShowed == false)
                {
                    setPoiInfo(currentPoiTmp);
                    //show the informations
                    if(poisInformationsOpen == false){
                        viewSwitcher.showNext();
                        poisInformationsOpen = true;
                        currentPoiShowed = true;
                    }

                    Log.d("DEBUG POI INFORMATIONS", currentPoiTmp.getNom() + " is near to you");
                }



				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myPath);
				mMapView.getOverlays().add(affichageGares);
				current_position = new OverlayItem("Here", "votre position", new GeoPoint(location.getLatitude(), location.getLongitude()));
				Drawable newMarker = getResources().getDrawable(R.drawable.position);
				current_position.setMarker(newMarker);
				mItems.add(current_position);

				ItemizedIconOverlay<OverlayItem> myLocation = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), mItems, new OnItemGestureListener<OverlayItem>() {
					@Override
					public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
						Toast.makeText(CarteActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();

						return true;
					}

					@Override
					public boolean onItemLongPress(final int index, final OverlayItem item) {
						return true;
					}
				});

				// Ajout de la localisation
				mMapView.getOverlays().add(myLocation);
				mMapView.getOverlays().add(affichagePois);
				mMapView.invalidate();
				centrerEcran();
			}
		});
	}

	public void setOnClickListenerPoiTextView(TextView view, final int position){
		if(position == -1){
			view.setOnClickListener(null);
			view.setText("");
			return;
		}
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				PoiDialog poiDialog = new PoiDialog(listePois.get(position), CarteActivity.this);
				poiDialog.afficher();

			}
		});
	}

	public void centrerEcran(){
		if(dernierLocation != null) {
            this.mMapController.setCenter(new GeoPoint(dernierLocation));
        }

    }

    public String getDistanceLabel(float distance){
		DecimalFormat dfmetre= new DecimalFormat("0");
		DecimalFormat dfkm= new DecimalFormat("0.0");
		float f_distance = distance;
		if(distance<0) f_distance = distance * (-1);
		if(f_distance < 1) {
			return dfmetre.format(distance * 1000) + "m";
		}
		else {
			return dfkm.format(distance)+"km";
		}
	}

	public boolean checkCurrentPoisVisibility(float kilometreCourant, Poi currentPoi){
		if(currentPoi.getDebutVisible() <= kilometreCourant && currentPoi.getFinVisible() >= kilometreCourant){
			// the poi is visible, we can show the informations to the user
			return true;
		}
		return false;
	}

	public void createPoi(View view){
		Intent intent = new Intent(this, EditPoiActivity.class);
		intent.putExtra("debutLigne", debutLigne);
		intent.putExtra("currentLocation", (Parcelable)currentLocation);
		intent.putExtra("lineName", LineName.getText().toString());
		intent.putExtra("idLigne", idLigne);
		startActivity(intent);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public ListeNoeuds getNodeList() {
		return nodeList;
	}

    void setPoiInfo(Poi currentPoi){
        nomPoi.setText(currentPoi.getNom());
        poiDescription.setText(currentPoi.getCommentaire());
        poiWeb.setText(currentPoi.getLienWeb());
    }

    public void toMap(View view){
        viewSwitcher.showPrevious();
        poisInformationsOpen = false;
    }

	public void showInfo(View view){
		Log.d("CLICK", "Click !!!!!!!!!!");
		TextView  poiInfo = (TextView)view.findViewById(view.getId());
		for (Poi poi :listePois){
			if(poi.getNom().toString().equals(poiInfo.getText().toString())) {
				setPoiInfo(poi);
				if(poisInformationsOpen == false){
					viewSwitcher.showNext();
					poisInformationsOpen = true;
				}
				return;
			}
		}
	}
}
