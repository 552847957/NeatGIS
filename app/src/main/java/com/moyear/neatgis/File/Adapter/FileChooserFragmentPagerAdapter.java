package com.moyear.neatgis.File.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FileChooserFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    public FileChooserFragmentPagerAdapter(Context context, FragmentManager fm, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);

        mContext = context;
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        if (titleList == null)
            return -1;

        return titleList.size();
    }
}
