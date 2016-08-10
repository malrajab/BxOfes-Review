package com.example.m_alrajab.popularmovies.ux;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.m_alrajab.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //getActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (viewPager != null) {
            setupViewPager(viewPager, savedInstanceState==null);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    //setup viewpager
    private void setupViewPager(ViewPager viewPager, Boolean isNewActivity) {
        //fragment pager adapter handles configuration changes for us, no need to worry about
        // retaining fragments in a bundle. **maybe add a now playing tab as well?
        PagerAdapter viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        //maybe find existing fragments here? how does viewpager handle rotation?
        //if (isNewActivity) {
        DetailsActivityFragment mDetailsFragment = new DetailsActivityFragment();
        Bundle detailsArgs = new Bundle();
        detailsArgs.putString(DetailsActivityFragment.ARG_TYPE, "Details");
        mDetailsFragment.setArguments(detailsArgs);

        DetailsActivityFragment mReviewFragment = new DetailsActivityFragment();
        Bundle reviewArgs = new Bundle();
        reviewArgs.putString(DetailsActivityFragment.ARG_TYPE, "Reviews");
        mReviewFragment.setArguments(reviewArgs);

        viewPagerAdapter.addFragment(mDetailsFragment, "Movie Details");
        viewPagerAdapter.addFragment(mReviewFragment, "Movie Review");
        //}

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
