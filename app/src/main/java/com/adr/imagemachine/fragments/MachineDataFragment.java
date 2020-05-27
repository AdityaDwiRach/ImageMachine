package com.adr.imagemachine.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adr.imagemachine.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineDataFragment extends Fragment {

    public static MachineDataFragment newInstance() {
        return new MachineDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_machine_data, container, false);
    }
}
