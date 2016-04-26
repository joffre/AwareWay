package com.pji.de.awareway.fragments;

import com.pji.de.awareway.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
	
	private View view;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_apropos, null);

        return view;
    }
	
	public static com.pji.de.awareway.fragments.AboutFragment newInstance() {
        return new com.pji.de.awareway.fragments.AboutFragment();
    }

}
