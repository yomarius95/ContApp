package com.tma.android.contapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tma.android.contapp.R;
import com.tma.android.contapp.fragments.FurnizorFragment;
import com.tma.android.contapp.fragments.NirFragment;
import com.tma.android.contapp.fragments.ProdusFragment;
import com.tma.android.contapp.fragments.StocFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[] {
                new FurnizorFragment(),
                new ProdusFragment(),
                new NirFragment(),
                new StocFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Furnizori";
        } else if (position == 1){
            return "Produse";
        } else if (position == 2){
            return "Nir-uri";
        } else {
            return "Stoc";
        }
    }
}
