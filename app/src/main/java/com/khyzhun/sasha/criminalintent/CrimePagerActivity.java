package com.khyzhun.sasha.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private List<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = createViewPager();
        setContentView(viewPager);

        crimes = CrimeLab.getInstance(this).getCrimes();
        wireViewPager();
        setCurrentCrime();
    }

    private ViewPager createViewPager() {
        ViewPager pager = new ViewPager(this);
        pager.setId(R.id.viewPager);

        return pager;
    }

    private void setCurrentCrime() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i=0; i<crimes.size(); i++) {
            if (crimes.get(i).getId().equals((crimeId))) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    private void wireViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int i) {
                Crime crime =  crimes.get(i);

                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onPageSelected(int i) {
                Crime crime = crimes.get(i);
                if (crime.getTitle() != null) {
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}