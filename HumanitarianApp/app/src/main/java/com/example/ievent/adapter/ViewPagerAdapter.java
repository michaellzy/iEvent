package com.example.ievent.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mfragmentList;
    public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.mfragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentList == null ? null: mfragmentList.get(position);
    }

    @Override
    public int getCount() {
        return  mfragmentList == null ? null: mfragmentList.size();
    }
}





