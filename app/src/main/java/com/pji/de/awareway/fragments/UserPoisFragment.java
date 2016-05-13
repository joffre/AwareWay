package com.pji.de.awareway.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.activity.OnFragmentInteractionListener;
import com.pji.de.awareway.liste.ListePois;
import com.pji.de.awareway.webbridge.AABridge;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserPoisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPoisFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PoiAdapter adapter;

    public UserPoisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPoisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPoisFragment newInstance(String param1, String param2) {
        UserPoisFragment fragment = new UserPoisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        UserPoiLoaderTask userPoiLoaderTask = new UserPoiLoaderTask();
        userPoiLoaderTask.execute((Void) null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_pois, container, false);
        ListView listPois = (ListView) v.findViewById(R.id.list_pois);

        adapter = new PoiAdapter(this, R.layout.list_view_poi_item, null);
        listPois.setAdapter(adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateUserPoiTextView(){
        TextView textView = (TextView) getActivity().findViewById(R.id.user_pois_text_title);
        textView.setText("Voici vos " +adapter.getCount()+" POI(s) :");
    }
    public void updateUserPois(ListePois pois){
        adapter.setPoisList(pois);
        adapter.notifyDataSetChanged();
        Log.d("UpdateUserPois", ""+adapter.getCount());
        updateUserPoiTextView();
    }

    public class UserPoiLoaderTask extends AsyncTask<Void, Void, ListePois> {

        UserPoiLoaderTask() {
        }

        @Override
        protected ListePois doInBackground(Void... params) {
            ListePois pois = null;
                if(MainActivity.userManager.isAuthentified()){
                    Integer idUser = MainActivity.userManager.getUser().getIdUser();
                    pois = AABridge.getUserPois(idUser);
                }
            return pois;
        }

        @Override
        protected void onPostExecute(final ListePois success) {
            updateUserPois(success);
        }

    }
}
