package com.example.rusbellgutierrez.SRT.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rusbellgutierrez.SRT.R;

/**
 * Created by Rusbell Gutierrez on 14/03/2017.
 */

public class Fragment_Inicio extends Fragment {

    public static final String TAG="Inicio";

    public Fragment_Inicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }
}