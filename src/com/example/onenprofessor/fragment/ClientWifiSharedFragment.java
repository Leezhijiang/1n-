package com.example.onenprofessor.fragment;

import com.example.onenprofessor.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ClientWifiSharedFragment extends Fragment {
	private Context context;
	public ClientWifiSharedFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_client_wifishared,container,false);
		return v;
	}
}
