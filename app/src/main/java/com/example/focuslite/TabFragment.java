package com.example.focuslite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focuslite.Database.DataHelp;
import com.example.focuslite.Database.MyOpenHelper;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;
    private String Speed;
    DataHelp dh;
    MyOpenHelper db;

    public TabFragment(String speed) {
        this.Speed = speed;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_tab, null);

        DeclareVariables(x);

//        Set an Apater for the View Pager
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

//        Now , this is a workaround ,
//        The setupWithViewPager dose't works without the runnable .
//        Maybe a Support Library Bug .
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;
    }


    private void DeclareVariables(View x) {
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        getActivity().setTitle("F\t\tTRAINING");
        dh = new DataHelp(getActivity());
        db = new MyOpenHelper(getActivity());
        int UserId = db.getUserId();

        dh.InputsSubmit(Speed,UserId);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

//        Return fragment with respect to Position .
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Speed();
                case 1:
                    return new SeamAngle();
                case 2:
                    return new BackSpin();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

//        This method returns the title of the tab according to the position.
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Speed";
                case 1:
                    return "Seam Angle";
                case 2:
                    return "CSV Data";
            }
            return null;
        }
    }
}