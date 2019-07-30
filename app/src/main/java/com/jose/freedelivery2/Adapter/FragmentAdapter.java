package com.jose.freedelivery2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jose.freedelivery2.Fragment.PassadosFragment;
import com.jose.freedelivery2.Fragment.PendentesFragment;


public class FragmentAdapter extends FragmentPagerAdapter {

    int numberTabs;


    public FragmentAdapter(FragmentManager fm, int numberTabs) {
        super(fm);
        this.numberTabs = numberTabs;
    }

    @Override
    public Fragment getItem(int i) {
       switch (i){
           case 0:
               PendentesFragment tab1 = new PendentesFragment();
               return tab1;
           case 1:
               PassadosFragment tab2 = new PassadosFragment();
               return tab2;
           default:

           return null;
       }
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
