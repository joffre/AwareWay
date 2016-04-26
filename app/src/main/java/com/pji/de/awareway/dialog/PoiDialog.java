package com.pji.de.awareway.dialog;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.pji.de.awareway.R;
import com.pji.de.awareway.bean.Poi;

public class PoiDialog {
	private Poi poi;
	private Activity activity;
	public PoiDialog(Poi poi, Activity context){
		this.poi = poi;
		this.activity = context;
	}
	
	public void afficher(){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		// Add the buttons
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		
		builder.setTitle(poi.getNom()+"(km "+poi.getDebutVisible()+"-"+poi.getFinVisible()+")");
		View dialogVue = activity.getLayoutInflater().inflate(R.layout.dialog_poi, null);
		
		TextView descriptionPoi = (TextView)dialogVue.findViewById(R.id.descriptionPoi);
		descriptionPoi.setText(poi.getCommentaire());
		
		TextView lienWeb = (TextView)dialogVue.findViewById(R.id.lienPoi);
		lienWeb.setText(poi.getLienWeb());
		
		ImageView imagePoi = (ImageView)dialogVue.findViewById(R.id.imagePoi);
		
		DownloadImageTask imageTask = new DownloadImageTask(imagePoi);
		imageTask.execute(poi.getLienImage());
		
		
		builder.setView(dialogVue);

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		
		dialog.show();
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if(result != null){
	    		bmImage.setImageBitmap(result);
	    	}else{
	    		bmImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.image_poi));
	    	}
	    }
	}

}
