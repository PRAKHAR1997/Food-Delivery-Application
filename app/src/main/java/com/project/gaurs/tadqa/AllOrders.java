package com.project.gaurs.tadqa;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrders extends android.support.v4.app.Fragment {


    public AllOrders() {
        // Required empty public constructor
    }

    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.containerorder);
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabsorder);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));

        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        AllOrders.ViewPagerAdapter adapter = new AllOrders.ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new CurrentOrder(), "Current Order");
        adapter.addFrag(new PreviousOrder(), "Previous Order");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
