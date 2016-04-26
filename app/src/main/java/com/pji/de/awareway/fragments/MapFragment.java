package com.pji.de.awareway.fragments;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.surfaceview.LigneMetroSurfaceView;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

	private MapView mMapView;
	private ResourceProxy mResourceProxy;
	private IMapController mMapController;

	protected List<String> points;

	private ScaleBarOverlay mScaleBarOverlay;
	private SimpleLocationOverlay mMyLocationOverlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View nullView = null;

		if (MainActivity.preferenceVue == "Carte") {
			// Creation de la carte
			this.mMapView = new MapView(getActivity(), 256);

			this.mMapView.setBuiltInZoomControls(true);
			this.mMapView.setMultiTouchControls(true);

			this.mMapController = this.mMapView.getController();

			this.mMapController = this.mMapView.getController();

			// Dunkerque
			GeoPoint gPt0 = new GeoPoint(51.03036, 2.3684999);
			// Coudekerque-Branche
			GeoPoint gPt1 = new GeoPoint(51.0177621, 2.3751331);
			// Bergues
			GeoPoint gPt2 = new GeoPoint(50.9688922, 2.4255408);
			// Escquelbecq
			GeoPoint gPt3 = new GeoPoint(50.889785, 2.4121583);
			// Arn�ke
			GeoPoint gPt4 = new GeoPoint(50.8315184, 2.4083297);
			// Cassel
			GeoPoint gPt5 = new GeoPoint(50.7876597, 2.4598073);
			// Hazebrouck
			GeoPoint gPt6 = new GeoPoint(50.7250487, 2.5423207);
			// Bailleul
			GeoPoint gPt7 = new GeoPoint(50.7289396, 2.734806);
			// Nieppe
			GeoPoint gPt8 = new GeoPoint(50.6967112, 2.8289803);
			// Armenti�res
			GeoPoint gPt9 = new GeoPoint(50.6805854, 2.8775278);
			// P�renchies
			GeoPoint gPt10 = new GeoPoint(50.6672331, 2.9770987);
			// Lille Flandres
			GeoPoint gPt11 = new GeoPoint(50.6359029, 3.0706419);

			// Ligne de Train
			PathOverlay myPath = new PathOverlay(Color.RED, getActivity());

			myPath.addPoint(gPt0);
			myPath.addPoint(gPt1);
			myPath.addPoint(gPt2);
			myPath.addPoint(gPt3);
			myPath.addPoint(gPt4);
			myPath.addPoint(gPt5);
			myPath.addPoint(gPt6);
			myPath.addPoint(gPt7);
			myPath.addPoint(gPt8);
			myPath.addPoint(gPt9);
			myPath.addPoint(gPt10);
			myPath.addPoint(gPt11);
			myPath.getPaint().setStrokeWidth(10.0f);

			// Ajout de la ligne de train
			this.mMapView.getOverlays().add(myPath);
			// Configuration du zoom
			this.mMapController.setZoom(14);
			// Centrage de la carte sur le premier point de la ligne
			this.mMapController.setCenter(gPt0);

			this.mMapView.invalidate();

			return this.mMapView;
		} else if (MainActivity.preferenceVue == "M�tro") {
			LigneMetroSurfaceView ligneMetro = new LigneMetroSurfaceView(
					getActivity());

			return ligneMetro;
		}

		return nullView;
	}

	public static com.pji.de.awareway.fragments.MapFragment newInstance() {
		return new com.pji.de.awareway.fragments.MapFragment();
	}

}
