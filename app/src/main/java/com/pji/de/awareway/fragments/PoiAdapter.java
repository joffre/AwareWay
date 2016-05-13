package com.pji.de.awareway.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.bean.Poi;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.webbridge.AABridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geoffrey on 03/05/2016.
 */
public class PoiAdapter extends ArrayAdapter<Poi> {

    List<Poi> poisList;
    UserPoisFragment fragment;

    public PoiAdapter(UserPoisFragment fragment, int resource, List<Poi> pois) {
        super(fragment.getContext(), resource, (pois != null)?pois:new ArrayList<Poi>());
        this.poisList = (pois != null)?pois:new ArrayList<Poi>();
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return this.poisList.size();
    }

    @Override
    public Poi getItem(int position) {
        return this.poisList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(this.poisList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Poi poi = this.poisList.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_poi_item, parent, false);
        }

        TextView tvPoiName = (TextView) convertView.findViewById(R.id.tvPoiName);
        tvPoiName.setText(poi.getNom());

        TextView tvPoiDesc = (TextView) convertView.findViewById(R.id.tvPoiDesc);
        tvPoiDesc.setText(poi.getCommentaire());

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Suppression ?")
                        .setMessage("Etes-vous sûr de vouloir supprimer le poi '"+poi.getNom()+"' ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                DeleteUserPoiTask deleteTask = new DeleteUserPoiTask(poi);
                                deleteTask.execute((Void) null);
                            }
                        }).create().show();
                return false;
            }
        });

        return convertView;
    }

    public void setPoisList(ListePois pois) {
        if(poisList == null) poisList = new ArrayList<Poi>();

        for(Poi poi : poisList){
            if(!pois.contains(poi)){
                poisList.remove(poi);
                remove(poi);
            }
        }

        for(Poi poi : pois){
            if(!poisList.contains(poi)){
                poisList.add(poi);
                add(poi);
            }
            Log.d("PoiAdapter", "add poi");
        }


    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DeleteUserPoiTask extends AsyncTask<Void, Void, Boolean> {

        Poi poi;
        DeleteUserPoiTask(Poi poi) {
            this.poi = poi;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(MainActivity.userManager.isAuthentified()){
                Integer idUser = MainActivity.userManager.getUser().getIdUser();
                return AABridge.deleteUserPoi(idUser, Long.parseLong(poi.getId()));
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                poisList.remove(poi);
                remove(poi);
                fragment.updateUserPoiTextView();
            }
        }
    }
}
