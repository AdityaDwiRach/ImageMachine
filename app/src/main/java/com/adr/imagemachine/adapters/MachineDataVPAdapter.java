package com.adr.imagemachine.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.adr.imagemachine.fragments.CodeReaderFragment;
import com.adr.imagemachine.fragments.MachineDataFragment;

import org.jetbrains.annotations.NotNull;

public class MachineDataVPAdapter extends FragmentPagerAdapter {

    public MachineDataVPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return CodeReaderFragment.newInstance();
        }
        return MachineDataFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1){
            return "Code Reader";
        } else {
            return "Machine Data";
        }
    }
}
