package com.pji.de.awareway.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.activity.OnFragmentInteractionListener;
import com.pji.de.awareway.webbridge.AABridge;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAccountDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAccountDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserAccountDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserAccountDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserAccountDetailsFragment newInstance(String param1, String param2) {
        UserAccountDetailsFragment fragment = new UserAccountDetailsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_user_account_details, container, false);
        TextView textView = (TextView) v.findViewById(R.id.user_account_text_title);
        textView.setText("Détails de votre compte");

        CircleImageView imageView = (CircleImageView) v.findViewById(R.id.user_profile_image);

        if (MainActivity.userManager.isImaged()) {
            BitmapLoaderTask bitmapLoaderTask = new BitmapLoaderTask(MainActivity.userManager.getProfilImageURL(), imageView);
            bitmapLoaderTask.execute((Void) null);
        }

        TextView modeCoView = (TextView) v.findViewById(R.id.user_account_connect_mode);
        modeCoView.setText(String.format(getResources().getString(R.string.user_account_connect_detail), (MainActivity.userManager.googleAuthentified())?"Google":"vos identifiants"));

        TextView infosUserView = (TextView) v.findViewById(R.id.user_account_name_details);
        infosUserView.setText(MainActivity.userManager.getFormatedUserNameDetails());
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

    public void setHeaderImage(Bitmap image, CircleImageView view){
        view.setImageBitmap(image);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class BitmapLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        private final CircleImageView imageView;
        private final String urlImage;

        BitmapLoaderTask(Uri urlImage, CircleImageView imageView) {
            this.imageView = imageView;
            this.urlImage = urlImage.toString();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap profilPic = null;
            try {
                URL imageURL = new URL(urlImage);
                profilPic = AABridge.getBitmapFromURL(imageURL);
            } catch (MalformedURLException e) {
                Log.d("Parse image url",e.getMessage());
            }
            return profilPic;
        }

        @Override
        protected void onPostExecute(final Bitmap success) {
            setHeaderImage(success, imageView);
        }

    }
}
