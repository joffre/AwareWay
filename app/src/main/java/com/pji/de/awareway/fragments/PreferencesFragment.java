package com.pji.de.awareway.fragments;

import com.pji.de.awareway.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PreferencesFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_preferences, null);

		return view;
	}

	public static PreferencesFragment newInstance() {
		return new PreferencesFragment();
	}

}
