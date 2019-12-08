package com.weimu.universalib.origin.view.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;


/**
 * 适合少Fragment
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragment;


    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }


    public void setmFragment(List<Fragment> mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragment.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragment == null ? 0 : mFragment.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((Fragment) obj).getView();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
