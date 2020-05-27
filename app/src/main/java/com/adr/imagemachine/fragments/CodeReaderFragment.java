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
public class CodeReaderFragment extends Fragment {

    public static CodeReaderFragment newInstance() {
        return new CodeReaderFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_reader, container, false);
    }
}
